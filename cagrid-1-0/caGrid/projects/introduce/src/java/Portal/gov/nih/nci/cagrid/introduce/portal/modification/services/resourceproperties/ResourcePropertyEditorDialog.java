package gov.nih.nci.cagrid.introduce.portal.modification.services.resourceproperties;

import gov.nih.nci.cagrid.introduce.portal.extension.ResourcePropertyEditorPanel;

import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.projectmobius.common.XMLUtilities;


public class ResourcePropertyEditorDialog extends JDialog {
	private ResourcePropertyEditorPanel component;
	private File resourcePropertyFile;
	private JPanel mainPanel = null;
	private JPanel editorPanel = null;
	private JPanel buttonPanel = null;
	private JButton doneButton = null;
	private JButton cancelButton = null;


	public ResourcePropertyEditorDialog(Frame owner, ResourcePropertyEditorPanel component, File resourcePropertyFile) {
		super(owner);
		this.component = component;
		this.resourcePropertyFile = resourcePropertyFile;
		initialize();
	}


	/**
	 * This method initializes this
	 */
	private void initialize() {
		this.setTitle("Resource Property Editor");
		this.setContentPane(getMainPanel());
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
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.gridy = 0;
			editorPanel.add(component, gridBagConstraints);
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
			buttonPanel.add(getCancelButton(), null);
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
						InputStream doc = component.getResultRPInputStream();

						// write the xml file back out for this
						// properties
						FileWriter fw;
						try {
							fw = new FileWriter(resourcePropertyFile);
							// TODO: validate here?
							fw.write(XMLUtilities.formatXML(XMLUtilities.streamToString(doc)));
							fw.close();
						} catch (Exception e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(ResourcePropertyEditorDialog.this,
								"ERROR: Invalid XML Document");
							return;
						}

						dispose();

					}
				}
			});
		}
		return doneButton;
	}


	/**
	 * This method initializes cancelButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText("Cancel");
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return cancelButton;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
