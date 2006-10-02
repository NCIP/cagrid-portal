package gov.nih.nci.cagrid.introduce.portal.preferences;

import gov.nih.nci.cagrid.introduce.common.BasePreferenceConfigurationPanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public abstract class DynamicPreferencesConfigurationPanel extends BasePreferenceConfigurationPanel {
	private Map textFields;
	private Map labels;
	private Map passwordFields;


	public DynamicPreferencesConfigurationPanel() {
		super();
		this.setLayout(new GridBagLayout());
		this.textFields = new HashMap();
		this.labels = new HashMap();
		this.passwordFields = new HashMap();
	}


	protected String getTextFieldValue(String label) {
		JTextField field = (JTextField) textFields.get(label);
		if (field != null) {
			return field.getText();
		} else {
			return null;
		}
	}


	protected JTextField getTextField(String label) {
		JTextField field = (JTextField) textFields.get(label);
		if (field != null) {
			return field;
		} else {
			return null;
		}
	}


	protected JLabel getLabel(String label) {
		JLabel jlabel = (JLabel) labels.get(label);
		if (jlabel != null) {
			return jlabel;
		} else {
			return null;
		}
	}


	protected void setTextFieldValue(String label, String value) {
		JTextField field = (JTextField) textFields.get(label);
		if (field != null) {
			field.setText(value);
		}
	}


	protected void setTextFieldValueAndDisable(String label, String value) {
		JTextField field = (JTextField) textFields.get(label);
		if (field != null) {
			field.setText(value);
			field.setEditable(false);
		}
	}


	protected void setJPasswordFieldValueAndDisable(String label, String value) {
		JPasswordField field = (JPasswordField) textFields.get(label);
		if (field != null) {
			field.setText(value);
			field.setEditable(false);
		}
	}


	protected String getPasswordFieldValue(String label) {
		JPasswordField field = (JPasswordField) passwordFields.get(label);
		if (field != null) {
			return new String(field.getPassword());
		} else {
			return null;
		}
	}


	protected void addTextField(JPanel panel, String label, String value, int y, boolean enabled) {
		JLabel label1 = new JLabel();
		label1.setText(label);
		JComponent component = null;
		JTextField text = new JTextField();
		text.setText(value);
		text.setEditable(enabled);
		component = text;
		this.textFields.put(label, text);
		this.labels.put(label, label1);

		GridBagConstraints const1 = new GridBagConstraints();
		const1.gridx = 0;
		const1.gridy = y;
		const1.insets = new java.awt.Insets(5, 5, 5, 5);
		const1.anchor = java.awt.GridBagConstraints.WEST;

		GridBagConstraints const2 = new GridBagConstraints();
		const2.gridx = 1;
		const2.gridy = y;
		const2.weightx = 1;
		const2.weighty = 1;
		const2.fill = GridBagConstraints.HORIZONTAL;
		const2.insets = new java.awt.Insets(5, 5, 5, 5);
		const2.anchor = java.awt.GridBagConstraints.WEST;

		panel.add(label1, const1);
		panel.add(component, const2);
	}


	protected void addPasswordField(JPanel panel, String label, String value, int y, boolean enabled) {
		JLabel label1 = new JLabel();
		label1.setText(label);
		JComponent component = null;
		JPasswordField text = new JPasswordField();
		text.setText(value);
		text.setEditable(enabled);
		component = text;
		this.passwordFields.put(label, text);
		this.labels.put(label, label1);

		GridBagConstraints const1 = new GridBagConstraints();
		const1.gridx = 0;
		const1.gridy = y;
		const1.insets = new java.awt.Insets(5, 5, 5, 5);
		const1.anchor = java.awt.GridBagConstraints.WEST;

		GridBagConstraints const2 = new GridBagConstraints();
		const2.gridx = 1;
		const2.gridy = y;
		const2.weightx = 1;
		const2.weighty = 1;
		const2.fill = GridBagConstraints.HORIZONTAL;
		const2.insets = new java.awt.Insets(5, 5, 5, 5);
		const2.anchor = java.awt.GridBagConstraints.WEST;

		panel.add(label1, const1);
		panel.add(component, const2);
	}

} // @jve:decl-index=0:visual-constraint="10,10"
