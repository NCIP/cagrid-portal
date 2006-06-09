package gov.nih.nci.cagrid.introduce.portal.preferences;

import gov.nih.nci.cagrid.introduce.portal.IntroducePortalConf;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JTextField;

import org.jdom.Document;
import org.jdom.Element;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.XMLUtilities;
import org.projectmobius.portal.PortalResourceManager;

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
		File configFile = PortalResourceManager
		.getInstance().getGridPortal().getConfigFile();
		try {
			Document doc = XMLUtilities.fileNameToDocument(configFile.getAbsolutePath());
			Element resource = (Element)doc.getRootElement().getChildren("resource").get(1);
			Element globusConfig = resource.getChild("introduce-portal-config").getChild("globusLocation");
			globusConfig.setText(getGlobusLocationTextField().getText());
			FileWriter fw;
			try {
				fw = new FileWriter(configFile);
				fw.write(XMLUtilities.formatXML(XMLUtilities.documentToString(doc)));
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		} catch (MobiusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		IntroducePortalConf conf = (IntroducePortalConf) PortalResourceManager
		.getInstance().getResource(IntroducePortalConf.RESOURCE);
		conf.setGlobusLocation(getGlobusLocationTextField().getText());
	}

	/**
	 * This method initializes globusLocationTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getGlobusLocationTextField() {
		if (globusLocationTextField == null) {
			globusLocationTextField = new JTextField();
			IntroducePortalConf conf = (IntroducePortalConf) PortalResourceManager
			.getInstance().getResource(IntroducePortalConf.RESOURCE);
			globusLocationTextField.setText(conf.getGlobusLocation());
		}
		return globusLocationTextField;
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
