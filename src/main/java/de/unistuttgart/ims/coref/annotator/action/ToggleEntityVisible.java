package de.unistuttgart.ims.coref.annotator.action;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import org.eclipse.collections.impl.factory.Lists;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import de.unistuttgart.ims.coref.annotator.CATreeNode;
import de.unistuttgart.ims.coref.annotator.CATreeSelectionEvent;
import de.unistuttgart.ims.coref.annotator.Constants;
import de.unistuttgart.ims.coref.annotator.DocumentWindow;
import de.unistuttgart.ims.coref.annotator.Util;
import de.unistuttgart.ims.coref.annotator.api.v1.Entity;
import de.unistuttgart.ims.coref.annotator.document.op.ToggleEntityFlag;

@Deprecated
public class ToggleEntityVisible extends TargetedIkonAction<DocumentWindow> implements CAAction {
	private static final long serialVersionUID = 1L;

	public ToggleEntityVisible(DocumentWindow dw) {
		super(dw, Constants.Strings.ACTION_TOGGLE_ENTITY_VISIBILITY, MaterialDesign.MDI_ACCOUNT_OUTLINE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		getTarget().getDocumentModel()
				.edit(new ToggleEntityFlag(Constants.ENTITY_FLAG_HIDDEN,
						Lists.immutable.of(getTarget().getTree().getSelectionPaths())
								.collect(tp -> ((CATreeNode) tp.getLastPathComponent()).getFeatureStructure())));
	}

	@Override
	public void setEnabled(CATreeSelectionEvent l) {
		boolean en = l.isEntity();
		setEnabled(en);
		putValue(Action.SELECTED_KEY, en && l.getFeatureStructures().allSatisfy(fs -> ((Entity) fs).getHidden()));
		putValue(Action.SELECTED_KEY,
				en && l.getFeatureStructures().allSatisfy(fs -> Util.isX(fs, Constants.ENTITY_FLAG_HIDDEN)));

	}
}