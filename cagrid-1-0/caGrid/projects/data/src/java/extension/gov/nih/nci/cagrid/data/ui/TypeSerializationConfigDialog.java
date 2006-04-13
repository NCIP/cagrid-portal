package gov.nih.nci.cagrid.data.ui;

import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.introduce.portal.IntroduceLookAndFeel;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.projectmobius.portal.GridPortalComponent;

/** 
 *  TypeSerializationConfigDialog
 *  Dialog to configure a schema element type's [de]serialization
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 7, 2006 
 * @version $Id$ 
 */
public class TypeSerializationConfigDialog extends GridPortalComponent {
	
	private JPanel serializationTypePanel = null;
	private JRadioButton defaultSerializationRadioButton = null;
	private JRadioButton customSerializationRadioButton = null;
	private JRadioButton sdkSerializationRadioButton = null;
	private JButton okButton = null;
	private JButton cancelButton = null;
	private JPanel buttonPanel = null;
	private JPanel mainPanel = null;
	private JPanel serializationConfigPanel = null;
	private JTextField deserializerTextField = null;
	private JTextField serializerTextField = null;
	private JTextField encodingTextField = null;
	private JLabel serializerLabel = null;
	private JLabel deserializerLabel = null;
	private JLabel encodingLabel = null;
	
	public TypeSerializationConfigDialog() {
		super();
		initialize();
	}
	
	
	private void initialize() {
		setTitle("Serialization Configuration");
		setFrameIcon(IntroduceLookAndFeel.getSchemaTypeIcon());
		setContentPane(getMainPanel());
		// for now, we'll use this
		enableAll(getSerializationConfigPanel(), false);
		pack();
		setSize(new java.awt.Dimension(400,153));
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
			gridBagConstraints2.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.gridy = 2;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints1.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridy = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.gridy = 0;
			serializationTypePanel = new JPanel();
			serializationTypePanel.setLayout(new GridBagLayout());
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
			customSerializationRadioButton.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					enableAll(getSerializationConfigPanel(), getCustomSerializationRadioButton().isSelected());
				}
			});
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
	
	
	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setText("OK");
			okButton.setIcon(PortalLookAndFeel.getSaveIcon());
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
			cancelButton.setIcon(PortalLookAndFeel.getCloseIcon());
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
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
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.anchor = java.awt.GridBagConstraints.EAST;
			gridBagConstraints13.gridwidth = 2;
			gridBagConstraints13.gridy = 1;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 1;
			gridBagConstraints12.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints12.anchor = java.awt.GridBagConstraints.NORTH;
			gridBagConstraints12.weightx = 1.0D;
			gridBagConstraints12.weighty = 1.0D;
			gridBagConstraints12.gridy = 0;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints11.weightx = 0.0D;
			gridBagConstraints11.weighty = 0.0D;
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints11.gridy = 0;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.setSize(new java.awt.Dimension(396,139));
			mainPanel.add(getSerializationTypePanel(), gridBagConstraints11);
			mainPanel.add(getSerializationConfigPanel(), gridBagConstraints12);
			mainPanel.add(getButtonPanel(), gridBagConstraints13);
		}
		return mainPanel;
	}
	
	
	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getSerializationConfigPanel() {
		if (serializationConfigPanel == null) {
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints10.gridy = 2;
			gridBagConstraints10.weightx = 1.0;
			gridBagConstraints10.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints10.gridx = 1;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.gridy = 1;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints9.gridx = 1;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints8.gridy = 0;
			gridBagConstraints8.weightx = 1.0;
			gridBagConstraints8.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints8.gridx = 1;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints7.gridy = 2;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints6.gridy = 1;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.insets = new java.awt.Insets(2,2,2,2);
			gridBagConstraints5.gridy = 0;
			serializationConfigPanel = new JPanel();
			serializationConfigPanel.setLayout(new GridBagLayout());
			serializationConfigPanel.add(getSerializerLabel(), gridBagConstraints5);
			serializationConfigPanel.add(getDeserializerLabel(), gridBagConstraints6);
			serializationConfigPanel.add(getEncodingLabel(), gridBagConstraints7);
			serializationConfigPanel.add(getSerializerTextField(), gridBagConstraints8);
			serializationConfigPanel.add(getDeserializerTextField(), gridBagConstraints9);
			serializationConfigPanel.add(getEncodingTextField(), gridBagConstraints10);
		}
		return serializationConfigPanel;
	}


	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getDeserializerTextField() {
		if (deserializerTextField == null) {
			deserializerTextField = new JTextField();
		}
		return deserializerTextField;
	}


	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getSerializerTextField() {
		if (serializerTextField == null) {
			serializerTextField = new JTextField();
		}
		return serializerTextField;
	}


	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getEncodingTextField() {
		if (encodingTextField == null) {
			encodingTextField = new JTextField();
		}
		return encodingTextField;
	}


	/**
	 * This method initializes jLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getSerializerLabel() {
		if (serializerLabel == null) {
			serializerLabel = new JLabel();
			serializerLabel.setText("Serializer:");
		}
		return serializerLabel;
	}


	/**
	 * This method initializes jLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getDeserializerLabel() {
		if (deserializerLabel == null) {
			deserializerLabel = new JLabel();
			deserializerLabel.setText("Deserializer:");
		}
		return deserializerLabel;
	}


	/**
	 * This method initializes jLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getEncodingLabel() {
		if (encodingLabel == null) {
			encodingLabel = new JLabel();
			encodingLabel.setText("Encoding:");
		}
		return encodingLabel;
	}
	
	
	private void enableAll(Container c, boolean enable) {
		c.setEnabled(enable);
		Component[] children = c.getComponents();
		for (int i = 0; i < children.length; i++) {
			if (children[i] instanceof Container) {
				enableAll((Container) children[i], enable);
			} else {
				children[i].setEnabled(enable);
			}
		}
	}


	public static void main(String[] args) {
		new TypeSerializationConfigDialog();
	}
}
