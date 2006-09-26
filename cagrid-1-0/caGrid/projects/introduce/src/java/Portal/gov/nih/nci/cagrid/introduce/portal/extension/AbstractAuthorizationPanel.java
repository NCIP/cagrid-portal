package gov.nih.nci.cagrid.introduce.portal.extension;

import gov.nih.nci.cagrid.introduce.beans.extension.AuthorizationExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import javax.swing.JPanel;


public abstract class AbstractAuthorizationPanel extends JPanel {

	private AuthorizationExtensionDescriptionType authDesc;
	private ServiceInformation serviceInfo;
	
	public AbstractAuthorizationPanel(AuthorizationExtensionDescriptionType authDesc, ServiceInformation serviceInfo) {
		this.authDesc = authDesc;
		this.serviceInfo = serviceInfo;
	}


	public AuthorizationExtensionDescriptionType getAuthorizationExtensionDescriptionType() {
		return authDesc;
	}


	public ServiceInformation getServiceInformation() {
		return serviceInfo;
	}
	
	
	
	public abstract void save(boolean isService, String methodName) throws Exception;
}
