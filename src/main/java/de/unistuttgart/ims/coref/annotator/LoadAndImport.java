package de.unistuttgart.ims.coref.annotator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.impl.XmiCasDeserializer;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.JCasIterator;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.xml.sax.SAXException;

import de.unistuttgart.ims.coref.annotator.plugins.IOPlugin;
import de.unistuttgart.ims.coref.annotator.uima.EnsureMeta;

class LoadAndImport extends SwingWorker<JCas, Object> {

	private DocumentWindow documentWindow;
	InputStream inputStream = null;
	TypeSystemDescription typeSystemDescription;
	IOPlugin flavor;
	File file = null;

	public LoadAndImport(DocumentWindow documentWindow, InputStream inputStream,
			TypeSystemDescription typeSystemDescription, IOPlugin flavor) {
		this.documentWindow = documentWindow;
		this.inputStream = inputStream;
		this.typeSystemDescription = typeSystemDescription;
		this.flavor = flavor;
	}

	public LoadAndImport(DocumentWindow documentWindow, File file, TypeSystemDescription typeSystemDescription,
			IOPlugin flavor) {
		this.documentWindow = documentWindow;
		this.typeSystemDescription = typeSystemDescription;
		this.flavor = flavor;
		this.file = file;
	}

	private JCas readStream() {
		JCas jcas = null;

		try {
			jcas = JCasFactory.createJCas(typeSystemDescription);
		} catch (UIMAException e1) {
			DocumentWindow.logger.catching(e1);
			return null;
		}
		try {
			DocumentWindow.logger.info("Deserialising input stream.");
			XmiCasDeserializer.deserialize(inputStream, jcas.getCas(), true);
			this.documentWindow.progressBar.setValue(25);
			DocumentWindow.logger.debug("Setting loading progress to {}", 50);

		} catch (SAXException | IOException e1) {
			DocumentWindow.logger.catching(e1);
			System.exit(1);
		}
		try {
			DocumentWindow.logger.info("Applying importer from {}", flavor.getClass().getName());
			SimplePipeline.runPipeline(jcas, flavor.getImporter(),
					AnalysisEngineFactory.createEngineDescription(EnsureMeta.class));
			this.documentWindow.progressBar.setValue(50);
			DocumentWindow.logger.debug("Setting loading progress to {}", 80);

		} catch (AnalysisEngineProcessException | ResourceInitializationException e1) {
			DocumentWindow.logger.catching(e1);
		}

		return jcas;
	}

	private JCas readFile() throws ResourceInitializationException {
		JCasIterator iter;
		iter = SimplePipeline.iteratePipeline(flavor.getReader(file), flavor.getImporter()).iterator();
		if (iter.hasNext()) {
			return iter.next();
		}
		return null;

	}

	@Override
	protected JCas doInBackground() throws Exception {

		if (inputStream != null)
			return readStream();
		else if (file != null)
			return readFile();
		return null;
	}

	@Override
	protected void done() {
		try {
			this.documentWindow.jcas = this.get();
			this.documentWindow.fireJCasLoadedEvent();
		} catch (InterruptedException | ExecutionException e) {
			DocumentWindow.logger.catching(e);
		}
	}

}