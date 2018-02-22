package de.unistuttgart.ims.coref.annotator;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.text.JTextComponent;

import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class PotentialAnnotationTransfer implements Transferable {

	public static DataFlavor dataFlavor = new DataFlavor(PotentialAnnotationTransfer.class, "PotentialAnnotation");

	@Deprecated
	int begin;
	@Deprecated
	int end;
	JTextComponent textView;
	ImmutableList<Span> list;

	public PotentialAnnotationTransfer(JTextComponent tv, int begin, int end) {
		this.textView = tv;
		this.list = Lists.immutable.of(new Span(begin, end));
		this.begin = begin;
		this.end = end;
	}

	public PotentialAnnotationTransfer(JTextComponent tv, Iterable<Span> iterable) {
		this.textView = tv;
		this.list = Lists.immutable.withAll(iterable);
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { dataFlavor };
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor == dataFlavor;
	}

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		return list.collect(span -> new PotentialAnnotation(textView, span.begin, span.end));
		// return new PotentialAnnotation(textView, begin, end);
	}

}
