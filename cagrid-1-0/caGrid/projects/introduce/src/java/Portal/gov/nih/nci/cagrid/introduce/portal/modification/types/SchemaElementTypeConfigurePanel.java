package gov.nih.nci.cagrid.introduce.portal.modification.types;

import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.portal.IntroduceLookAndFeel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;
import javax.swing.JToggleButton;


public class SchemaElementTypeConfigurePanel extends JPanel {

	private JTextField typeText = null;
	private JTextField classNameText = null;

	private SchemaElementType type;
	private JPanel beanPanel = null;
	private JPanel customBeanPanel = null;
	private JLabel typeLabel = null;
	private JLabel classNameLabell = null;
	private JLabel serializerLabel = null;
	private JTextField serializerText = null;
	private JLabel deserializerLabel = null;
	private JTextField deserializerText = null;
	private JTextArea helpArea = null;
	private JToggleButton customizeButton = null;
	private JLabel customizeLabel = null;


	/**
	 * This method initializes
	 */
	public SchemaElementTypeConfigurePanel() {
		super();
		initialize();
	}


	public void setSchemaElementType(SchemaElementType type, boolean classEditable) {
		this.type = type;
		getTypeText().setText(type.getType());
		getClassNameText().setText(type.getClassName());
		getDeserializerText().setText(type.getDeserializer());
		getSerializerText().setText(type.getDeserializer());
		getClassNameText().setEditable(classEditable);
		getDeserializerText().setEditable(classEditable);
		getSerializerText().setEditable(classEditable);
	}


	public void clear() {
		type = null;
		getTypeText().setText("");
		getClassNameText().setText("");
		getDeserializerText().setText("");
		getSerializerText().setText("");
	}


	/**
	 * This method initializes this
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 0;
		gridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints3.gridwidth = 2;
		gridBagConstraints3.gridheight = 2;
		gridBagConstraints3.gridy = 1;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.gridy = 0;
		gridBagConstraints2.weighty = 0.0D;
		gridBagConstraints2.weightx = 1.0D;
		gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
		this.setLayout(new GridBagLayout());
		this.setSize(new java.awt.Dimension(312, 240));
		this.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Element Type Configuration",
			javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
			null, IntroduceLookAndFeel.getPanelLabelColor()));
		this.add(getBeanPanel(), gridBagConstraints2);
		this.add(getCustomBeanPanel(), gridBagConstraints3);
	}


	/**
	 * This method initializes typeText
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTypeText() {
		if (typeText == null) {
			typeText = new JTextField();
			typeText.setEditable(false);
			typeText.getDocument().addDocumentListener(new DocumentListener() {
				public void changedUpdate(DocumentEvent e) {
					if (type != null) {
						type.setType(getTypeText().getText());
					}
				}


				public void removeUpdate(DocumentEvent e) {
					if (type != null) {
						type.setType(getTypeText().getText());
					}
				}


				public void insertUpdate(DocumentEvent e) {
					if (type != null) {
						type.setType(getTypeText().getText());
					}
				}
			});
		}
		return typeText;
	}


	/**
	 * This method initializes classNameText
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getClassNameText() {
		if (classNameText == null) {
			classNameText = new JTextField();
			classNameText.getDocument().addDocumentListener(new DocumentListener() {
				public void changedUpdate(DocumentEvent e) {
					if (type != null) {
						type.setClassName(getClassNameText().getText());
					}
				}


				public void removeUpdate(DocumentEvent e) {
					if (type != null) {
						type.setClassName(getClassNameText().getText());
					}
				}


				public void insertUpdate(DocumentEvent e) {
					if (type != null) {
						type.setClassName(getClassNameText().getText());
					}
				}
			});
		}
		return classNameText;
	}


	/**
	 * This method initializes beanPanel
	 * 
	 * @return javax. gridBagConstraints6.gridwidth = 2;
	 *         gridBagConstraints6.anchor =
	 *         java.awt.GridBagConstraints.NORTHWEST;
	 *         beanPanel.add(getCustomizeButton(), gridBagConstraints6);
	 *         swing.JPanel
	 */
	private JPanel getBeanPanel() {
		if (beanPanel == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 2;
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints6.gridy = 1;
			customizeLabel = new JLabel();
			customizeLabel.setText("Customize Bean");
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.gridwidth = 2;
			gridBagConstraints7.gridy = 1;
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.gridx = 1;
			gridBagConstraints31.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints31.gridy = 0;
			typeLabel = new JLabel();
			typeLabel.setText("Type");
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints1.gridx = 2;
			beanPanel = new JPanel();
			beanPanel.setLayout(new GridBagLayout());
			beanPanel.add(getCustomizeButton(), gridBagConstraints7);
			beanPanel.add(customizeLabel, gridBagConstraints6);
			beanPanel.add(getTypeText(), gridBagConstraints1);
			beanPanel.add(typeLabel, gridBagConstraints31);
		}
		return beanPanel;
	}


	/**
	 * This method initializes customBeanPanel
	 * 
	 * @return gridBagConstraints51.insets = new java.awt.Insets(2,2,2,2);
	 *         customBeanPanel.add(getSerializerText(), gridBagConstraints51);
	 *         customBeanPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
	 *         "Customize Bean",
	 *         javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
	 *         javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
	 *         javax.swing.JPanel
	 */
	private JPanel getCustomBeanPanel() {
		if (customBeanPanel == null) {
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints15.gridy = 3;
			gridBagConstraints15.weightx = 1.0;
			gridBagConstraints15.weighty = 1.0;
			gridBagConstraints15.gridwidth = 2;
			gridBagConstraints15.gridheight = 2;
			gridBagConstraints15.gridx = 0;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 1;
			gridBagConstraints12.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints12.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints12.gridy = 2;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.gridy = 1;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints11.gridx = 1;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints10.gridy = 2;
			deserializerLabel = new JLabel();
			deserializerLabel.setText("Deserializer*");
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.gridy = 1;
			serializerLabel = new JLabel();
			serializerLabel.setText("Serializer*");
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.gridy = 0;
			classNameLabell = new JLabel();
			classNameLabell.setText("Classname");
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
			customBeanPanel = new JPanel();
			customBeanPanel.setLayout(new GridBagLayout());
			customBeanPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Customize Bean",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, IntroduceLookAndFeel.getPanelLabelColor()));
			customBeanPanel.setEnabled(false);
			customBeanPanel.add(getClassNameText(), gridBagConstraints);
			customBeanPanel.add(classNameLabell, gridBagConstraints4);
			customBeanPanel.add(serializerLabel, gridBagConstraints5);
			customBeanPanel.add(deserializerLabel, gridBagConstraints10);
			customBeanPanel.add(getDeserializerText(), gridBagConstraints12);
			customBeanPanel.add(getSerializerText(), gridBagConstraints11);
			customBeanPanel.add(getHelpArea(), gridBagConstraints15);
		}
		return customBeanPanel;
	}


	/**
	 * This method initializes serializerText
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getSerializerText() {
		if (serializerText == null) {
			serializerText = new JTextField();
			serializerText.getDocument().addDocumentListener(new DocumentListener() {
				public void changedUpdate(DocumentEvent e) {
					if (type != null) {
						type.setSerializer(getSerializerText().getText());
					}
				}


				public void removeUpdate(DocumentEvent e) {
					if (type != null) {
						type.setSerializer(getSerializerText().getText());
					}
				}


				public void insertUpdate(DocumentEvent e) {
					if (type != null) {
						type.setSerializer(getSerializerText().getText());
					}
				}
			});
		}
		return serializerText;
	}


	/**
	 * This method initializes deserializerText
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getDeserializerText() {
		if (deserializerText == null) {
			deserializerText = new JTextField();
			deserializerText.getDocument().addDocumentListener(new DocumentListener() {
				public void changedUpdate(DocumentEvent e) {
					if (type != null) {
						type.setDeserializer(getDeserializerText().getText());
					}
				}


				public void removeUpdate(DocumentEvent e) {
					if (type != null) {
						type.setDeserializer(getDeserializerText().getText());
					}
				}


				public void insertUpdate(DocumentEvent e) {
					if (type != null) {
						type.setDeserializer(getDeserializerText().getText());
					}
				}
			});
		}
		return deserializerText;
	}


	/**
	 * This method initializes helpArea
	 * 
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getHelpArea() {
		if (helpArea == null) {
			helpArea = new JTextArea();
			helpArea.setEditable(false);
			helpArea.setFont(new java.awt.Font("Dialog", java.awt.Font.ITALIC, 10));
			helpArea.setLineWrap(true);
			helpArea.setBackground(new java.awt.Color(204, 204, 204));
			helpArea
				.setText("* optional, if the deserializer and serializer are left blank axis defaults will be used for this class");
		}
		return helpArea;
	}


	/**
	 * This method initializes customizeButton
	 * 
	 * @return javax.swing.JToggleButton
	 */
	private JToggleButton getCustomizeButton() {
		if (customizeButton == null) {
			customizeButton = new JToggleButton();
			customizeButton.setSelectedIcon(IntroduceLookAndFeel.getAddIcon());
			customizeButton.setIcon(IntroduceLookAndFeel.getRemoveIcon());
			customizeButton.setText("");
			customizeButton.setSelected(false);
			getCustomBeanPanel().setVisible(false);
			customizeButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (getCustomizeButton().isSelected()) {
						getCustomBeanPanel().setVisible(true);
					} else {
						getCustomBeanPanel().setVisible(false);
					}
				}
			});
		}
		return customizeButton;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

} // @jve:decl-index=0:visual-constraint="10,10"
