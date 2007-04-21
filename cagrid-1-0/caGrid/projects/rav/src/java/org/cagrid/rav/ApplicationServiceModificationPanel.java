package org.cagrid.rav;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import org.ggf.schemas.jsdl._2005._11.jsdl.Application_Type;

import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.portal.extension.ServiceModificationUIPanel;

/**
 * @author madduri
 *
 */
public class ApplicationServiceModificationPanel extends
		ServiceModificationUIPanel {

	private JTextArea appTextArea = null;
	private String applicationData = null;
	private Application_Type appTypeData = null;
	/**
	 * @param arg0
	 * @param arg1
	 */
	public ApplicationServiceModificationPanel(ServiceExtensionDescriptionType desc, ServiceInformation info) {
		super(desc, info);
		ExtensionTypeExtensionData data = ExtensionTools.getExtensionData(desc, info);
		initialize();
		try {
			this.appTypeData = ExtensionDataUtils.getExtensionData(data);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(ApplicationServiceModificationPanel.this, "Error: setting the extensions ");
			e.printStackTrace();
		}
		applicationData = "Application Name: " + this.appTypeData.getApplicationName() 
		+ "\n " + "Version: " + this.appTypeData.getApplicationVersion() + "\n" 
		+ this.appTypeData.getDescription();
		appTextArea.setText(applicationData);
	}
	
	private void initialize() {
		setLayout(new GridBagLayout());
		GridBagConstraints cons = new GridBagConstraints();
		cons.gridx = 0;
		cons.gridy = 0;
		cons.weightx = 1.0D;
		cons.weighty = 1.0D;
		cons.fill = GridBagConstraints.BOTH;
		appTextArea = new JTextArea(100,100);
		add(appTextArea, cons);
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.introduce.portal.extension.ServiceModificationUIPanel#resetGUI()
	 */
	protected void resetGUI() {
		// TODO Auto-generated method stub
		
	}
	

}
