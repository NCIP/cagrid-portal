package gov.nih.nci.cagrid.data.ui;

import gov.nih.nci.cagrid.introduce.portal.modification.types.SchemaElementTypeConfigurePanel;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JRadioButton;
import java.awt.GridBagConstraints;
import javax.swing.JButton;

/** 
 *  TypeSerializationConfigDialog
 *  Dialog to configure a schema element type's [de]serialization
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 7, 2006 
 * @version $Id$ 
 */
public class TypeSerializationConfigDialog extends JDialog {
	
	private JPanel serializationTypePanel = null;
	private JRadioButton defaultSerializationRadioButton = null;
	private JRadioButton customSerializationRadioButton = null;
	private JRadioButton sdkSerializationRadioButton = null;
	private SchemaElementTypeConfigurePanel typeConfigPanel = null;
	private JButton okButton = null;
	private JButton cancelButton = null;
	private JPanel buttonPanel = null;
	private JPanel mainPanel = null;
	
	public TypeSerializationConfigDialog() {
		super();
		initialize();
	}
	
	
	private void initialize() {
		this.setContentPane(getMainPanel());
		this.setTitle("Serialization Configuration");
		pack();
		this.setSize(new java.awt.Dimension(466,283));
		show();
	}
	
	
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getSerializationTypePanel() {
		if (serializationTypePanel == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints2.gridy = 2;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints1.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints.gridy = 0;
			serializationTypePanel = new JPanel();
			serializationTypePanel.setLayout(new GridBagLayout());
			serializationTypePanel.setPreferredSize(new java.awt.Dimension(100,100));
			serializationTypePanel.add(getDefaultSerializationRadioButton(), gridBagConstraints);
			serializationTypePanel.add(getCustomSerializationRadioButton(), gridBagConstraints1);
			serializationTypePanel.add(getSdkSerializationRadioButton(), gridBagConstraints2);
			ButtonGroup group = new ButtonGroup();
			group.add(getDefaultSerializationRadioButton());
			group.add(getCustomSerializationRadioButton());
			group.add(getSdkSerializationRadioButton());
			group.setSelected(getDefaultSerializationRadioButton().getModel(), true);
		}
		return serializationTypePanel;
	}
	
	
	/**
	 * This method initializes jRadioButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getDefaultSerializationRadioButton() {
		if (defaultSerializationRadioButton == null) {
			defaultSerializationRadioButton = new JRadioButton();
			defaultSerializationRadioButton.setText("Default Serialization");
		}
		return defaultSerializationRadioButton;
	}
	
	
	/**
	 * This method initializes jRadioButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getCustomSerializationRadioButton() {
		if (customSerializationRadioButton == null) {
			customSerializationRadioButton = new JRadioButton();
			customSerializationRadioButton.setText("Custom Serialization");
		}
		return customSerializationRadioButton;
	}
	
	
	/**
	 * This method initializes jRadioButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getSdkSerializationRadioButton() {
		if (sdkSerializationRadioButton == null) {
			sdkSerializationRadioButton = new JRadioButton();
			sdkSerializationRadioButton.setText("SDK Serialization");
		}
		return sdkSerializationRadioButton;
	}
	
	
	private SchemaElementTypeConfigurePanel getTypeConfigPanel() {
		if (typeConfigPanel == null) {
			typeConfigPanel = new SchemaElementTypeConfigurePanel();
			typeConfigPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Type Serialization", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
		}
		return typeConfigPanel;
	}
	
	
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setText("OK");
			okButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
				}
			});
		}
		return okButton;
	}
	
	
	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText("Cancel");
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.out.println("actionPerformed()"); // TODO Auto-generated Event stub actionPerformed()
				}
			});
		}
		return cancelButton;
	}
	
	
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 1;
			gridBagConstraints4.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints4.gridy = 0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints3.gridy = 0;
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			buttonPanel.add(getOkButton(), gridBagConstraints3);
			buttonPanel.add(getCancelButton(), gridBagConstraints4);
		}
		return buttonPanel;
	}
	
	
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 1;
			gridBagConstraints7.gridy = 1;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 1;
			gridBagConstraints6.gridy = 0;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.NORTH;
			gridBagConstraints5.gridheight = 2;
			gridBagConstraints5.gridy = 0;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getSerializationTypePanel(), gridBagConstraints5);
			mainPanel.add(getTypeConfigPanel(), gridBagConstraints6);
			mainPanel.add(getButtonPanel(), gridBagConstraints7);
		}
		return mainPanel;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
