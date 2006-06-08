package gov.nih.nci.cagrid.introduce.portal.modification.types;

import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


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
	private JPanel customBeanWrapperPanel = null;
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
		getSerializerText().setText(type.getSerializer());
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
		GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
		gridBagConstraints8.gridx = 0;
		gridBagConstraints8.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints8.gridy = 1;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.gridy = 0;
		gridBagConstraints2.weighty = 0.0D;
		gridBagConstraints2.weightx = 1.0D;
		gridBagConstraints2.insets = new java.awt.Insets(0, 0, 0, 0);
		gridBagConstraints2.ipady = 2;
		gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
		this.setLayout(new GridBagLayout());
		this.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Element Type Configuration",
			javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
			null, IntroduceLookAndFeel.getPanelLabelColor()));
		this.add(getBeanPanel(), gridBagConstraints2);
		this.add(getCustomBeanWrapperPanel(), gridBagConstraints8);
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
			classNameLabell.setText("Classname*");
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
			customBeanPanel = new JPanel();
			customBeanPanel.setLayout(new GridBagLayout());
			customBeanPanel.setVisible(false);
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
				.setToolTipText("<html>For every Schema that omits these fields for all of its data types, "
					+ "default Classes, Serializers, and Deserializers will be generated and used.<br>"
					+ "If you specify a customization of any data types in a given "
					+ "Schema, no Classes, Serializers, or Deserializers will be generated for any other data types"
					+ " in that Schema.<br><b>Therefore, if you customize a data type in a Schema,"
					+ " you need to also customize all other data types in that Schema that you are using in your service.");
			helpArea
				.setText("* Optional.  You must specify all these fields if you specify any. [See tooltip, and documentation, for more details]");
		}
		return helpArea;
	}


	/**
	 * This method initializes customBeanWrapperPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getCustomBeanWrapperPanel() {
		if (customBeanWrapperPanel == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints6.fill = java.awt.GridBagConstraints.NONE;
			gridBagConstraints6.weightx = 1.0D;
			gridBagConstraints6.gridy = 0;
			customizeLabel = new JLabel();
			customizeLabel.setIcon(IntroduceLookAndFeel.getAddIcon());
			customizeLabel.setFont(new java.awt.Font("Dialog", java.awt.Font.ITALIC, 10));
			customizeLabel.setText("Customize Beans");
			customizeLabel.addMouseListener(new MouseAdapter() {

				public void mouseClicked(MouseEvent e) {
					super.mouseClicked(e);
					if (getCustomBeanPanel().isVisible()) {
						getCustomBeanPanel().setVisible(false);
						customizeLabel.setIcon(IntroduceLookAndFeel.getAddIcon());
					} else {
						getCustomBeanPanel().setVisible(true);
						customizeLabel.setIcon(IntroduceLookAndFeel.getRemoveIcon());
					}
				}

			});
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.BOTH;
			gridBagConstraints3.gridwidth = 2;
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 1.0D;
			gridBagConstraints3.weighty = 1.0D;
			gridBagConstraints3.gridheight = 2;
			customBeanWrapperPanel = new JPanel();
			customBeanWrapperPanel.setLayout(new GridBagLayout());
			customBeanWrapperPanel.add(getCustomBeanPanel(), gridBagConstraints3);
			customBeanWrapperPanel.add(customizeLabel, gridBagConstraints6);
		}
		return customBeanWrapperPanel;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
