package gov.nih.nci.cagrid.introduce.portal.extension;

import gov.nih.nci.cagrid.introduce.beans.extension.AuthorizationExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import javax.swing.JPanel;

public abstract class AbstractServiceAuthorizationPanel extends JPanel {

	private AuthorizationExtensionDescriptionType authDesc;

	private ServiceInformation serviceInfo;

	private ServiceType service;

	public AbstractServiceAuthorizationPanel(
			AuthorizationExtensionDescriptionType authDesc,
			ServiceInformation serviceInfo, ServiceType service) {
		this.authDesc = authDesc;
		this.serviceInfo = serviceInfo;
		this.service = service;
	}

	public AuthorizationExtensionDescriptionType getAuthorizationExtensionDescriptionType() {
		return authDesc;
	}

	public ServiceInformation getServiceInformation() {
		return serviceInfo;
	}

	public ServiceType getService() {
		return service;
	}

	public abstract void save() throws Exception;
}
