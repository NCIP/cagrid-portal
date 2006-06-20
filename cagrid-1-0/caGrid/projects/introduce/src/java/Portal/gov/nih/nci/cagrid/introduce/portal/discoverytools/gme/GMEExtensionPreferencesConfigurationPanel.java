package gov.nih.nci.cagrid.introduce.portal.discoverytools.gme;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionDescription;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.portal.extension.ExtensionsPreferencesConfigurationPanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.xml.namespace.QName;


public class GMEExtensionPreferencesConfigurationPanel extends ExtensionsPreferencesConfigurationPanel {

	private JLabel gmeLabel = null;
	private JTextField gmeLocationTextField = null;


	public GMEExtensionPreferencesConfigurationPanel(ExtensionDescription ext) {
		super(ext);
		initialize();
	}


	public void initialize() {
		// i will build up my gui here which lets me set whatever i need to...
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.insets = new java.awt.Insets(2,2,2,2);
		gridBagConstraints1.weighty = 0.0D;
		gridBagConstraints1.gridx = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridx = 0;
		gmeLabel = new JLabel();
		gmeLabel.setText("GME URL");
		this.setLayout(new GridBagLayout());
		this.add(gmeLabel, gridBagConstraints);
		this.add(getGmeLocationTextField(), gridBagConstraints1);
		
	}


	public void apply() {
		// this is where i would take the changed information, set it on my
		// extension object
		// from the extensions loader
		// getExtensionDescription()
		// then i would write back out my extension xml
		ExtensionTools.getPropertyObject(getExtensionDescription().getDiscoveryExtensionDescription().getProperties(),
			GMESchemaLocatorPanel.GME_URL).setValue(getGmeLocationTextField().getText());
		
		try {
			Utils.serializeDocument(ExtensionsLoader.getInstance().getExtensionsDir().getAbsolutePath() + File.separator + GMESchemaLocatorPanel.TYPE + File.separator + "extension.xml",getExtensionDescription(),new QName("gme://gov.nih.nci.cagrid.introduce/1/Extension","ExtensionDescription"));
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(GMEExtensionPreferencesConfigurationPanel.this,"Error: could not apply changes");
		}
	}


	/**
	 * This method initializes gmeLocationTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getGmeLocationTextField() {
		if (gmeLocationTextField == null) {
			gmeLocationTextField = new JTextField();
			String gmeLocation = ExtensionTools.getProperty(getExtensionDescription().getDiscoveryExtensionDescription().getProperties(),
				GMESchemaLocatorPanel.GME_URL);
			gmeLocationTextField.setText(gmeLocation);
		}
		return gmeLocationTextField;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
