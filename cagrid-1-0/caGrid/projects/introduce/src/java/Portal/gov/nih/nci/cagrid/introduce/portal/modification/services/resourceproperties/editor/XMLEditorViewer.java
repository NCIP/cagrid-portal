package gov.nih.nci.cagrid.introduce.portal.modification.services.resourceproperties.editor;

import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.introduce.portal.common.jedit.JEditTextArea;
import gov.nih.nci.cagrid.introduce.portal.common.jedit.XMLTokenMarker;
import gov.nih.nci.cagrid.introduce.portal.extension.ResourcePropertyEditorPanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jdom.Document;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.XMLUtilities;

import java.awt.Insets;
import java.io.File;

import javax.swing.JButton;


/**
 * CreationViewer
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created Jun 22, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class XMLEditorViewer extends ResourcePropertyEditorPanel {

	private JPanel schemaViewer = null;
	private JEditTextArea schemaTextPane = null;
	private JButton doneButton = null;


	/**
	 * This method initializes
	 */
	public XMLEditorViewer(Document doc, File schemaFile, File schemaDir) {
		super(doc, schemaFile, schemaDir);
		initialize();
		System.out.println("SCHEMA FILE: " + schemaFile.getAbsolutePath());
		System.out.println("SCHEMA DIR: " + schemaDir.getAbsolutePath());

	}


	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridx = -1;
		gridBagConstraints.gridy = -1;
		gridBagConstraints.weightx = 1.0D;
		gridBagConstraints.weighty = 1.0D;
		gridBagConstraints.insets = new Insets(2, 2, 2, 2);
		this.setSize(new java.awt.Dimension(615,426));
		this.setPreferredSize(new java.awt.Dimension(615,426));
		this.setLayout(new GridBagLayout());
		this.add(getSchemaViewer(), gridBagConstraints);
	}


	/**
	 * This method initializes schemaViewer
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getSchemaViewer() {
		if (schemaViewer == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.weighty = 1.0;
			gridBagConstraints3.gridheight = 1;
			gridBagConstraints3.gridx = 0;
			schemaViewer = new JPanel();
			schemaViewer.setLayout(new GridBagLayout());
			schemaViewer.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Resource Property XML",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			schemaViewer.add(getSchemaTextPane(), gridBagConstraints3);
		}
		return schemaViewer;
	}


	/**
	 * This method initializes schemaTextPane
	 * 
	 * @return javax.swing.JTextArea
	 */
	private JEditTextArea getSchemaTextPane() {
		if (schemaTextPane == null) {
			schemaTextPane = new JEditTextArea(XMLEditorTextAreaDefaults.createDefaults());
			schemaTextPane.setTokenMarker(new XMLTokenMarker());
			if (getDoc() != null) {
				schemaTextPane.setText(XMLUtilities.documentToString(getDoc()));
				schemaTextPane.setCaretPosition(0);
			}
		}
		return schemaTextPane;
	}


	/**
	 * This method initializes doneButton
	 * 
	 * @return javax.swing.JButton
	 */
	public boolean save() {

		Document doc = null;
		try {
			doc = XMLUtilities.stringToDocument(getSchemaTextPane().getText());
		} catch (MobiusException e1) {
			e1.printStackTrace();
		}
		if (doc == null) {
			JOptionPane.showMessageDialog(XMLEditorViewer.this, "ERROR: Invalid XML Document");
		} else {
			setDoc(doc);
			return true;
		}
		return false;
	}

} // @jve:decl-index=0:visual-constraint="10,4"
