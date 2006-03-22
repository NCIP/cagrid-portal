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


public class SchemaElementTypeConfigurePanel extends JPanel {

	private JLabel typeLabel = null;
	private JLabel classNameLabel = null;
	private JTextField typeText = null;
	private JTextField classNameText = null;

	private SchemaElementType type;


	/**
	 * This method initializes
	 */
	public SchemaElementTypeConfigurePanel() {
		super();
		initialize();
	}


	public void setSchemaElementType(SchemaElementType type) {
		this.type = type;
		getTypeText().setText(type.getType());
		getClassNameText().setText(type.getClassName());
	}


	public void clear() {
		type = null;
		getTypeText().setText("");
		getClassNameText().setText("");
	}


	/**
	 * This method initializes this
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints4.gridy = 1;
		gridBagConstraints4.weightx = 1.0;
		gridBagConstraints4.gridx = 1;
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints3.gridy = 0;
		gridBagConstraints3.weightx = 1.0;
		gridBagConstraints3.gridx = 1;
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints2.gridy = 2;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints1.gridy = 1;
		classNameLabel = new JLabel();
		classNameLabel.setText("Class");
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 0;
		typeLabel = new JLabel();
		typeLabel.setText("Type");
		this.setLayout(new GridBagLayout());
		this.setSize(new java.awt.Dimension(312, 240));
		this.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Element Type Configuration",
			javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
			null, IntroduceLookAndFeel.getPanelLabelColor()));
		this.add(typeLabel, gridBagConstraints);
		this.add(classNameLabel, gridBagConstraints1);
		this.add(getTypeText(), gridBagConstraints3);
		this.add(getClassNameText(), gridBagConstraints4);

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
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

} // @jve:decl-index=0:visual-constraint="10,10"
