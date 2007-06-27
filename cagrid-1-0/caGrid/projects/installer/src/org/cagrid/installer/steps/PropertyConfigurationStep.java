/**
 * 
 */
package org.cagrid.installer.steps;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.options.BooleanPropertyConfigurationOption;
import org.cagrid.installer.steps.options.ListPropertyConfigurationOption;
import org.cagrid.installer.steps.options.PropertyConfigurationOption;
import org.cagrid.installer.steps.options.TextPropertyConfigurationOption;
import org.cagrid.installer.validator.Validator;
import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class PropertyConfigurationStep extends PanelWizardStep {

	private static final Log logger = LogFactory
			.getLog(PropertyConfigurationStep.class);

	private List<PropertyConfigurationOption> options = new ArrayList<PropertyConfigurationOption>();

	private JPanel optionsPanel = null;

	private List<String> optionKeys = new ArrayList<String>();

	private List<Component> optionValueFields = new ArrayList<Component>();

	protected Map<String, Boolean> requiredFields = new HashMap<String, Boolean>();

	protected CaGridInstallerModel model;

	private List<Validator> validators = new ArrayList<Validator>();

	public List<Validator> getValidators() {
		return validators;
	}

	public void setValidators(List<Validator> validators) {
		this.validators = validators;
	}

	/**
	 * 
	 */
	public PropertyConfigurationStep() {

	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public PropertyConfigurationStep(String name, String description) {
		super(name, description);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	public PropertyConfigurationStep(String name, String description, Icon icon) {
		super(name, description, icon);
	}

	public void init(WizardModel m) {
		if (!(m instanceof CaGridInstallerModel)) {
			throw new IllegalStateException(
					"This step requires a StatefulWizardModel instance.");
		}
		this.model = (CaGridInstallerModel) m;

		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.fill = GridBagConstraints.BOTH;
		gridBagConstraints1.weightx = 1.0D;
		gridBagConstraints1.weighty = 1.0D;
		gridBagConstraints1.gridy = 1;
		this.setLayout(new GridBagLayout());
		this.add(getOptionsPanel(), gridBagConstraints1);

		for (PropertyConfigurationOption option : getOptions()) {
			if (option instanceof TextPropertyConfigurationOption) {
				addTextOption((TextPropertyConfigurationOption) option);
			} else if (option instanceof ListPropertyConfigurationOption) {
				addListOption((ListPropertyConfigurationOption) option);
			} else if (option instanceof BooleanPropertyConfigurationOption) {
				addBooleanOption((BooleanPropertyConfigurationOption) option);
			} else {
				throw new IllegalStateException(
						"Unknown PropertyConfigurationOption type: "
								+ option.getClass().getName());
			}
		}
		checkComplete();
	}
	
	public void prepare(){
		for(String key : this.requiredFields.keySet()){
			if(this.model.getState().containsKey(key)){
				this.requiredFields.put(key, true);
			}
		}
		checkComplete();
	}
	
	protected void checkComplete() {
		boolean allRequiredFieldsSpecified = true;
		for (String key : this.requiredFields.keySet()) {
			boolean isSet = this.requiredFields.get(key);
//			logger.debug("Checking " + key + ": " + isSet);
			if (!isSet) {
				allRequiredFieldsSpecified = false;
				break;
			}
		}
		setComplete(allRequiredFieldsSpecified);
	}

	protected void addBooleanOption(BooleanPropertyConfigurationOption option) {
		String defaultValue = (String) this.model.getState().get(
				option.getName());
		try {
			Boolean.valueOf(defaultValue);
		} catch (Exception ex) {
			defaultValue = String.valueOf(option.getDefaultValue());
		}
		JCheckBox valueField = new JCheckBox();
		if (option.isRequired()) {
			addRequiredListener(option.getName(), valueField);
		}
		valueField.setSelected(option.getDefaultValue());
		addOption(option.getName(), option.getDescription(), valueField);
	}

	protected void addRequiredListener(final String requiredField,
			ItemSelectable valueField) {
//		logger.debug("Added required field: " + requiredField);
		this.requiredFields.put(requiredField, false);
		valueField.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (PropertyConfigurationStep.this.requiredFields
						.containsKey(requiredField)) {
					boolean selected = e.getStateChange() == ItemEvent.SELECTED;
					PropertyConfigurationStep.this.requiredFields.put(
							requiredField, selected);
					PropertyConfigurationStep.this.checkComplete();
				}
			}

		});
	}

	protected void addListOption(ListPropertyConfigurationOption option) {
		String defaultValue = (String) this.model.getState().get(
				option.getName());
		if (defaultValue != null) {
			boolean foundIt = false;
			for (String value : option.getChoices()) {
				if (value.equals(defaultValue)) {
					foundIt = true;
					break;
				}
			}
			if (!foundIt) {
				defaultValue = option.getChoices()[0];
				logger.warn("Pre-existing value of '" + defaultValue
						+ "' is not valid. Using default value of '"
						+ defaultValue + "'");
			}
		} else {
			defaultValue = option.getChoices()[0];
		}
		JComboBox valueField = new JComboBox(option.getChoices());
		valueField.setSelectedItem(defaultValue);
		if (option.isRequired()) {
			addRequiredListener(option.getName(), valueField);
			this.requiredFields.put(option.getName(), true);
		}
		addOption(option.getName(), option.getDescription(), valueField);
	}

	protected void addTextOption(TextPropertyConfigurationOption option) {
		String defaultValue = (String) this.model.getState().get(
				option.getName());
		if (defaultValue == null) {
			defaultValue = option.getDefaultValue();
		}
		final String requiredField = option.getName();
		final JTextField valueField = new JTextField(defaultValue);
		if (option.isRequired()) {
			boolean isSet = defaultValue != null && defaultValue.trim().length() > 0;
			this.requiredFields.put(option.getName(), isSet);
			valueField.addCaretListener(new CaretListener() {
				public void caretUpdate(CaretEvent evt) {
					if (PropertyConfigurationStep.this.requiredFields
							.containsKey(requiredField)) {
						boolean selected = valueField.getText() != null
								&& valueField.getText().trim().length() > 0;
						PropertyConfigurationStep.this.requiredFields.put(
								requiredField, selected);
						PropertyConfigurationStep.this.checkComplete();
					}
				}

			});
		}
		addOption(option.getName(), option.getDescription(), valueField);
	}

	protected void addOption(String key, String description,
			Component valueField) {
		this.optionKeys.add(key);
		JLabel label = new JLabel(description);
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.fill = GridBagConstraints.BOTH;
		gridBagConstraints1.gridy = optionKeys.size() - 1;
		this.optionValueFields.add(valueField);
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 1;
		gridBagConstraints2.fill = GridBagConstraints.BOTH;
		gridBagConstraints2.gridy = optionKeys.size() - 1;
		gridBagConstraints2.weightx = 1;
		gridBagConstraints2.insets = new Insets(2, 5, 2, 2);
		this.getOptionsPanel().add(label, gridBagConstraints1);
		this.getOptionsPanel().add(valueField, gridBagConstraints2);
	}

	public void applyState() throws InvalidStateException {

		Map<String, String> tempState = new HashMap<String, String>();
		try {
			for (int i = 0; i < optionKeys.size(); i++) {
				String key = optionKeys.get(i);
				String value = null;
				if (optionValueFields.get(i) instanceof JTextField) {
					value = ((JTextField) optionValueFields.get(i)).getText();
				} else if (optionValueFields.get(i) instanceof JCheckBox) {
					value = String.valueOf(((JCheckBox) optionValueFields
							.get(i)).isSelected());
				} else if (optionValueFields.get(i) instanceof JComboBox) {
					value = String.valueOf(((JComboBox) optionValueFields
							.get(i)).getSelectedItem());
				}
//				logger.debug("Setting " + key + " = " + value);
				tempState.put(key, value);
			}
		} catch (Exception ex) {
			throw new InvalidStateException("Error occurred: "
					+ ex.getMessage(), ex);
		}

		
		Map m = new HashMap();
		m.putAll(this.model.getState());
		m.putAll(tempState);
		validate(m);

		this.model.getState().putAll(tempState);

	}

	protected void validate(Map<String, String> state)
			throws InvalidStateException {
		for (Validator v : getValidators()) {
			v.validate(state);
		}
	}

	private JPanel getOptionsPanel() {
		if (optionsPanel == null) {
			optionsPanel = new JPanel();
			optionsPanel.setLayout(new GridBagLayout());
		}
		return optionsPanel;
	}

	public List<PropertyConfigurationOption> getOptions() {
		return options;
	}

	public void setOptions(List<PropertyConfigurationOption> options) {
		this.options = options;
	}

}
