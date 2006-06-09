package gov.nih.nci.cagrid.introduce.portal.preferences;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class IntroducePreferencesConfigurationPanel extends
		BasePreferenceConfigurationPanel {
	
	private JLabel globusLocationLabel = null;
	private JTextField globusLocationTextField = null;

	public IntroducePreferencesConfigurationPanel(){
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints1.gridy = 0;
        gridBagConstraints1.weightx = 1.0;
        gridBagConstraints1.insets = new java.awt.Insets(2,2,2,2);
        gridBagConstraints1.gridx = 1;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.insets = new java.awt.Insets(2,2,2,2);
        gridBagConstraints.gridy = 0;
        globusLocationLabel = new JLabel();
        globusLocationLabel.setText("Globus Location");
        this.setLayout(new GridBagLayout());
        this.setSize(new java.awt.Dimension(315,257));
        this.add(globusLocationLabel, gridBagConstraints);
        this.add(getGlobusLocationTextField(), gridBagConstraints1);
			
	}

	public void apply() {
	}

	/**
	 * This method initializes globusLocationTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getGlobusLocationTextField() {
		if (globusLocationTextField == null) {
			globusLocationTextField = new JTextField();
		}
		return globusLocationTextField;
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
