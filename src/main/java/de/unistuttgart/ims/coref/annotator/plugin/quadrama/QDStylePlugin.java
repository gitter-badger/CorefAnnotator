package de.unistuttgart.ims.coref.annotator.plugin.quadrama;

import java.util.HashMap;
import java.util.Map;

import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;

import de.unistuttgart.ims.coref.annotator.StyleManager;

public class QDStylePlugin implements de.unistuttgart.ims.coref.annotator.plugins.StylePlugin {

	@Override
	public Map<AttributeSet, Type> getSpanStyles(TypeSystem ts, StyleContext styleContext, Style defaultStyle) {
		Map<AttributeSet, Type> map = new HashMap<AttributeSet, Type>();

		SimpleAttributeSet sas;

		Style style = styleContext.addStyle("Speaker", defaultStyle);
		sas = new SimpleAttributeSet();
		sas.addAttribute(StyleConstants.Bold, true);
		map.put(style, ts.getType(Constants.TYPE_SPEAKER));

		style = styleContext.addStyle("Stage direction", defaultStyle);
		sas = new SimpleAttributeSet();
		sas.addAttribute(StyleConstants.Italic, true);
		map.put(style, ts.getType(Constants.TYPE_STAGEDIRECTION));

		style = styleContext.addStyle("Header", defaultStyle);
		sas = new SimpleAttributeSet();
		sas.addAttribute(StyleConstants.FontSize, (Integer) defaultStyle.getAttribute(StyleConstants.FontSize) + 6);
		sas.addAttribute(StyleConstants.Bold, true);
		map.put(style, ts.getType(Constants.TYPE_HEADING));

		return map;
	}

	@Override
	public Style getBaseStyle() {
		return StyleManager.getDefaultCharacterStyle();
	}

	@Override
	public String getDescription() {
		return "Plugin to format speakers bold and stage directions in italic.";
	}

	@Override
	public String getName() {
		return "QuaDramA";
	}

}
