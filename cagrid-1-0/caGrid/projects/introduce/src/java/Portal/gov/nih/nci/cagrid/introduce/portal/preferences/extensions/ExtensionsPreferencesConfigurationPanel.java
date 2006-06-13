package gov.nih.nci.cagrid.introduce.portal.preferences.extensions;

import IntroducePortalConf;
import gov.nih.nci.cagrid.introduce.portal.preferences.BasePreferenceConfigurationPanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JTextField;

import org.jdom.Document;
import org.jdom.Element;
import org.omg.CORBA.INITIALIZE;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.XMLUtilities;
import org.projectmobius.portal.PortalResourceManager;

public abstract class ExtensionsPreferencesConfigurationPanel extends
		BasePreferenceConfigurationPanel {
	
	public ExtensionsPreferencesConfigurationPanel(){
		super();
		initialize();
	}
	
	public abstract void initialize();
}  //  @jve:decl-index=0:visual-constraint="10,10"
