package gov.nih.nci.cagrid.data.ui.types;

import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;

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

/** 
 *  TypeSerializationConfigDialog
 *  Dialog to configure a schema element type's [de]serialization
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 7, 2006 
 * @version $Id$ 
 */
public class TypeSerializationConfigPanel extends JPanel {
	public static final String SDK_SERIALIZER = "gov.nih.nci.cagrid.encoding.SDKSerializerFactory";
	public static final String SDK_DESERIALIZER = "gov.nih.nci.cagrid.encoding.SDKDeserializerFactory";
	
	private JPanel serializationTypePanel = null;
	private JRadioButton defaultSerializationRadioButton = null;
	private JRadioButton customSerializationRadioButton = null;
	private JRadioButton sdkSerializationRadioButton = null;
	private ButtonGroup radioGroup = null;
	private JButton setButton = null;
	private JButton undoButton = null;
	private JPanel buttonPanel = null;
	private JPanel serializationConfigPanel = null;
	private JTextField deserializerTextField = null;
	private JTextField serializerTextField = null;
	private JLabel serializerLabel = null;
	private JLabel deserializerLabel = null;
	
	private DataServiceTypesTable table;
	private SchemaElementType schemaType;
	
	public TypeSerializationConfigPanel(DataServiceTypesTable table) {
		super();
		this.table = table;
		clear();
		initialize();
	}
	
	
	public void clear() {
		getSerializerTextField().setText("");
		getDeserializerTextField().setText("");
		getSetButton().setEnabled(false);
		getUndoButton().setEnabled(false);
	}
	
	
	public void setSchemaElementType(SchemaElementType type) {
		schemaType = type;
		if (schemaType.getSerializer() != null) {
			getSerializerTextField().setText(schemaType.getSerializer());
		} else {
			getSerializerTextField().setText("");
		}
		if (schemaType.getDeserializer() != null) {
			getDeserializerTextField().setText(schemaType.getDeserializer());	
		} else {
			getDeserializerTextField().setText("");
		}		
		getSetButton().setEnabled(true);
		getUndoButton().setEnabled(true);
		if (isDefaultSerialization()) {
			getRadioGroup().setSelected(getDefaultSerializationRadioButton().getModel(), true);
		} else if (isSdkSerialization()) {
			getRadioGroup().setSelected(getSdkSerializationRadioButton().getModel(), true);
		} else {
			getRadioGroup().setSelected(getCustomSerializationRadioButton().getModel(), true);
		}
	}
	
	
	private void initialize() {
		enableAll(getSerializationConfigPanel(), false);
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
		setLayout(new GridBagLayout());
		setSize(new java.awt.Dimension(396,139));
		add(getSerializationTypePanel(), gridBagConstraints11);
		add(getSerializationConfigPanel(), gridBagConstraints12);
		add(getButtonPanel(), gridBagConstraints13);
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
						getSerializerTextField().setText(SDK_SERIALIZER);
						getDeserializerTextField().setText(SDK_DESERIALIZER);
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
	private JButton getSetButton() {
		if (setButton == null) {
			setButton = new JButton();
			setButton.setText("Set");
			setButton.setIcon(PortalLookAndFeel.getSaveIcon());
			setButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					// set values into the mapping
					schemaType.setSerializer(getSerializerTextField().getText());
					schemaType.setDeserializer(getDeserializerTextField().getText());
					if (!isDefaultSerialization()) {
						schemaType.setClassName(schemaType.getType());
					} else {
						schemaType.setClassName("");
					}
					// get the table to refresh these fields
					table.refreshSerialization(schemaType);
				}
			});
		}
		return setButton;
	}
	
	
	/**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getUndoButton() {
		if (undoButton == null) {
			undoButton = new JButton();
			undoButton.setText("Undo Changes");
			undoButton.setIcon(IntroduceLookAndFeel.getUndoIcon());
			undoButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setSchemaElementType(schemaType);
				}
			});
		}
		return undoButton;
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
			buttonPanel.add(getSetButton(), gridBagConstraints3);
			buttonPanel.add(getUndoButton(), gridBagConstraints4);
		}
		return buttonPanel;
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
		return schemaType.getSerializer() != null && schemaType.getSerializer().length() == 0 &&
			schemaType.getDeserializer() != null && schemaType.getDeserializer().length() == 0;
	}
	
	
	private boolean isSdkSerialization() {
		return schemaType.getSerializer() != null && schemaType.getSerializer().equals(SDK_SERIALIZER) &&
			schemaType.getDeserializer() != null && schemaType.getDeserializer().equals(SDK_DESERIALIZER);
	}
	
	
	private ButtonGroup getRadioGroup() {
		if (radioGroup == null) {
			radioGroup = new ButtonGroup();
			radioGroup.add(getDefaultSerializationRadioButton());
			radioGroup.add(getCustomSerializationRadioButton());
			radioGroup.add(getSdkSerializationRadioButton());
		}
		return radioGroup;
	}
}
