package gov.nih.nci.cagrid.introduce.portal.modification.services.resourceproperties;

import gov.nih.nci.cagrid.introduce.portal.extension.ResourcePropertyEditorComponent;
import gov.nih.nci.cagrid.introduce.portal.modification.services.resourceproperties.editor.XMLEditorViewer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jdom.Document;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.XMLUtilities;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JButton;


public class ResourcePropertyEditorDialog extends JDialog {
	private ResourcePropertyEditorComponent component;
	private File resourcePropertyFile;
	private JPanel mainPanel = null;
	private JPanel editorPanel = null;
	private JPanel buttonPanel = null;
	private JButton doneButton = null;


	public ResourcePropertyEditorDialog(ResourcePropertyEditorComponent component, File resourcePropertyFile) {
		this.component = component;
		this.resourcePropertyFile = resourcePropertyFile;
		initialize();
	}


	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setTitle("Resource Property Editor Component");
		this.setContentPane(getMainPanel());
		this.setSize(new java.awt.Dimension(371, 270));
	}


	/**
	 * This method initializes mainPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints1.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.gridy = 0;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getEditorPanel(), gridBagConstraints);
			mainPanel.add(getButtonPanel(), gridBagConstraints1);
		}
		return mainPanel;
	}


	/**
	 * This method initializes editorPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getEditorPanel() {
		if (editorPanel == null) {
			editorPanel = new JPanel(new GridBagLayout());
			editorPanel.add(component, new GridBagConstraints());
		}
		return editorPanel;
	}


	/**
	 * This method initializes buttonPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.add(getDoneButton(), null);
		}
		return buttonPanel;
	}


	/**
	 * This method initializes doneButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getDoneButton() {
		if (doneButton == null) {
			doneButton = new JButton();
			doneButton.setText("Done");
			doneButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (component.save()) {
						Document doc = component.getDoc();

						// write the xml file back out for this
						// properties
						FileWriter fw;
						try {
							fw = new FileWriter(resourcePropertyFile);
							fw.write(XMLUtilities.formatXML(XMLUtilities.documentToString(doc)));
							fw.close();
						} catch (Exception e1) {
							e1.printStackTrace();
						}

						dispose();

					}
				}
			});
		}
		return doneButton;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
