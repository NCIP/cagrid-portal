package gov.nih.nci.cagrid.introduce.portal.preferences;

import javax.swing.JLabel;

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
