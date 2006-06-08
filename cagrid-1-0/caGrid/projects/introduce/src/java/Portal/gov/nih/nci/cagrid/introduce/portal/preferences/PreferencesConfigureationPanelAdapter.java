package gov.nih.nci.cagrid.introduce.portal.preferences;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;

public class PreferencesConfigureationPanelAdapter extends
		BasePreferenceConfigurationPanel {
	
	private JLabel prefsLabel = null;

	public PreferencesConfigureationPanelAdapter(){
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        prefsLabel = new JLabel();
        prefsLabel.setText("Introduce Preferences");
        this.setSize(new java.awt.Dimension(261,171));
        this.add(prefsLabel, null);
			
	}

	public void apply() {
		// TODO Auto-generated method stub
		
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
