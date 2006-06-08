package gov.nih.nci.cagrid.data.ui;

import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.projectmobius.portal.GridPortalComponent;
import org.projectmobius.portal.MDIDesktopPane;

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
	private JLabel serializerLabel = null;
	private JLabel deserializerLabel = null;
	
	private DataServiceTypesTable table;
	private SerializationMapping mapping;
	
	public TypeSerializationConfigDialog(DataServiceTypesTable table, SerializationMapping mapping) {
		super();
		this.table = table;
		this.mapping = mapping;
		initialize();
	}
	
	
	private void initialize() {
		setTitle("Serialization Configuration");
		setFrameIcon(IntroduceLookAndFeel.getSchemaTypeIcon());
		enableAll(getSerializationConfigPanel(), false);
		setContentPane(getMainPanel());		
		populateMappingTextFields();
		// for now, we'll use this
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
			if (isDefaultSerialization()) {
				group.setSelected(getDefaultSerializationRadioButton().getModel(), true);
			} else if (isSdkSerialization()) {
				group.setSelected(getSdkSerializationRadioButton().getModel(), true);
			} else {
				group.setSelected(getCustomSerializationRadioButton().getModel(), true);
			}
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
			defaultSerializationRadioButton.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if (getDefaultSerializationRadioButton().isSelected()) {
						getSerializerTextField().setText("");
						getDeserializerTextField().setText("");
					}
				}
			});
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
			sdkSerializationRadioButton.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if (getSdkSerializationRadioButton().isSelected()) {
						getSerializerTextField().setText(SerializationMapping.SDK_SERIALIZER);
						getDeserializerTextField().setText(SerializationMapping.SDK_DESERIALIZER);
					}
				}
			});
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
					// set values into the mapping
					mapping.getElemType().setSerializer(getSerializerTextField().getText());
					mapping.getElemType().setDeserializer(getDeserializerTextField().getText());
					// get the table to refresh these fields
					table.refreshSerializationMapping(mapping);
					dispose();
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
			serializationConfigPanel.add(getSerializerTextField(), gridBagConstraints8);
			serializationConfigPanel.add(getDeserializerTextField(), gridBagConstraints9);
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
	
	
	private boolean isDefaultSerialization() {
		return mapping.getElemType().getSerializer().length() == 0 &&
			mapping.getElemType().getDeserializer().length() == 0;
	}
	
	
	private boolean isSdkSerialization() {
		return mapping.getElemType().getSerializer().equals(SerializationMapping.SDK_SERIALIZER) &&
			mapping.getElemType().getDeserializer().equals(SerializationMapping.SDK_DESERIALIZER);
	}
	
	
	private void populateMappingTextFields() {
		getSerializerTextField().setText(mapping.getElemType().getSerializer());
		getDeserializerTextField().setText(mapping.getElemType().getDeserializer());
	}


	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MDIDesktopPane desk = new MDIDesktopPane();
		frame.setContentPane(desk);
		desk.add(new TypeSerializationConfigDialog(null, null));
		frame.setSize(600,600);
		frame.setVisible(true);
	}
}
