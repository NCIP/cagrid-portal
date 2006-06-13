package gov.nih.nci.cagrid.introduce.extension.example;

import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionDescription;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsPreferencesConfigurationPanel;

import javax.swing.JLabel;

public class ExampleExtensionPreferencesConfigurationPanel extends
		ExtensionsPreferencesConfigurationPanel {
	
	private JLabel jLabel = null;

	public ExampleExtensionPreferencesConfigurationPanel(ExtensionDescription ext){
		super(ext);
		initialize();
	}

	public void initialize() {
		//i will build up my gui here which lets me set whatever i need to...
        jLabel = new JLabel();
        jLabel.setText("Example Extension");
        this.setSize(new java.awt.Dimension(345,200));
        this.add(jLabel, null);
		// TODO Auto-generated method stub
	}

	public void apply() {
		//this is where i would take the changed information, set it on my extension object
		//from the extensions loader
		//getExtensionDescription()
		//then i would write back out my extension xml

	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
