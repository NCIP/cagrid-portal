package gov.nih.nci.cagrid.introduce.extensions.metadata.editors.servicemetdata;

import gov.nih.nci.cagrid.metadata.common.Address;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;


/**
 * @author oster
 * 
 */
public class AddressEditorPanel extends JPanel {
	private Address address;
	private JLabel street1Label = null;
	private JTextField street1TextField = null;
	private JLabel street2Label = null;
	private JTextField street2TextField = null;
	private JLabel localityLabel = null;
	private JTextField localityTextField = null;
	private JLabel stateLabel = null;
	private JTextField stateTextField = null;
	private JLabel zipLabel = null;
	private JTextField zipTextField = null;
	private JLabel countryLabel = null;
	private JTextField countryTextField = null;
	
	public AddressEditorPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
        gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints11.gridy = 3;
        gridBagConstraints11.weightx = 1.0;
        gridBagConstraints11.insets = new java.awt.Insets(5,5,5,5);
        gridBagConstraints11.weighty = 0.0;
        gridBagConstraints11.gridx = 3;
        GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
        gridBagConstraints10.gridx = 2;
        gridBagConstraints10.insets = new java.awt.Insets(5,5,5,5);
        gridBagConstraints10.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints10.weightx = 1.0;
        gridBagConstraints10.weighty = 0.0;
        gridBagConstraints10.gridy = 3;
        countryLabel = new JLabel();
        countryLabel.setText("Country");
        GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
        gridBagConstraints9.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints9.gridy = 3;
        gridBagConstraints9.weightx = 1.0;
        gridBagConstraints9.insets = new java.awt.Insets(5,5,5,5);
        gridBagConstraints9.weighty = 0.0;
        gridBagConstraints9.gridx = 1;
        GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
        gridBagConstraints8.gridx = 0;
        gridBagConstraints8.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints8.weightx = 1.0;
        gridBagConstraints8.weighty = 0.0;
        gridBagConstraints8.gridy = 3;
        zipLabel = new JLabel();
        zipLabel.setText("Zip Code");
        GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
        gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints7.gridy = 2;
        gridBagConstraints7.weightx = 1.0;
        gridBagConstraints7.insets = new java.awt.Insets(5,5,5,5);
        gridBagConstraints7.weighty = 0.0;
        gridBagConstraints7.gridx = 3;
        GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
        gridBagConstraints6.gridx = 2;
        gridBagConstraints6.insets = new java.awt.Insets(5,5,5,5);
        gridBagConstraints6.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints6.weightx = 1.0;
        gridBagConstraints6.weighty = 0.0;
        gridBagConstraints6.gridy = 2;
        stateLabel = new JLabel();
        stateLabel.setText("State");
        GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
        gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints5.gridy = 2;
        gridBagConstraints5.weightx = 1.0;
        gridBagConstraints5.insets = new java.awt.Insets(5,5,5,5);
        gridBagConstraints5.weighty = 0.0;
        gridBagConstraints5.gridx = 1;
        GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
        gridBagConstraints4.gridx = 0;
        gridBagConstraints4.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints4.insets = new java.awt.Insets(5,5,5,5);
        gridBagConstraints4.weightx = 1.0;
        gridBagConstraints4.weighty = 0.0;
        gridBagConstraints4.gridy = 2;
        localityLabel = new JLabel();
        localityLabel.setText("Locality (city)");
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints3.gridy = 0;
        gridBagConstraints3.weightx = 1.0;
        gridBagConstraints3.insets = new java.awt.Insets(5,5,5,5);
        gridBagConstraints3.weighty = 0.0;
        gridBagConstraints3.gridx = 3;
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 2;
        gridBagConstraints2.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints2.insets = new java.awt.Insets(5,5,5,5);
        gridBagConstraints2.weightx = 1.0;
        gridBagConstraints2.weighty = 0.0;
        gridBagConstraints2.gridy = 0;
        street2Label = new JLabel();
        street2Label.setText("Street 2");
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.weightx = 1.0;
        gridBagConstraints1.insets = new java.awt.Insets(5,5,5,5);
        gridBagConstraints1.weighty = 0.0;
        gridBagConstraints1.gridx = 1;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.insets = new java.awt.Insets(5,5,5,5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.gridy = 0;
        street1Label = new JLabel();
        street1Label.setText("Street 1");
        street1Label.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 12));
        this.setLayout(new GridBagLayout());
        this.setSize(new java.awt.Dimension(515,332));
        this.add(street1Label, gridBagConstraints);
        this.add(getStreet1TextField(), gridBagConstraints1);
        this.add(street2Label, gridBagConstraints2);
        this.add(getStreet2TextField(), gridBagConstraints3);
        this.add(localityLabel, gridBagConstraints4);
        this.add(getLocalityTextField(), gridBagConstraints5);
        this.add(stateLabel, gridBagConstraints6);
        this.add(getStateTextField(), gridBagConstraints7);
        this.add(zipLabel, gridBagConstraints8);
        this.add(getZipTextField(), gridBagConstraints9);
        this.add(countryLabel, gridBagConstraints10);
        this.add(getCountryTextField(), gridBagConstraints11);
			
	}

	public Address getAddress() {
		return this.address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	/**
	 * This method initializes street1TextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getStreet1TextField() {
		if (street1TextField == null) {
			street1TextField = new JTextField();
		}
		return street1TextField;
	}

	/**
	 * This method initializes street2TextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getStreet2TextField() {
		if (street2TextField == null) {
			street2TextField = new JTextField();
		}
		return street2TextField;
	}

	/**
	 * This method initializes localityTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getLocalityTextField() {
		if (localityTextField == null) {
			localityTextField = new JTextField();
		}
		return localityTextField;
	}

	/**
	 * This method initializes stateTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getStateTextField() {
		if (stateTextField == null) {
			stateTextField = new JTextField();
		}
		return stateTextField;
	}

	/**
	 * This method initializes zipTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getZipTextField() {
		if (zipTextField == null) {
			zipTextField = new JTextField();
		}
		return zipTextField;
	}

	/**
	 * This method initializes countryTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getCountryTextField() {
		if (countryTextField == null) {
			countryTextField = new JTextField();
		}
		return countryTextField;
	}	
}  //  @jve:decl-index=0:visual-constraint="10,10"
