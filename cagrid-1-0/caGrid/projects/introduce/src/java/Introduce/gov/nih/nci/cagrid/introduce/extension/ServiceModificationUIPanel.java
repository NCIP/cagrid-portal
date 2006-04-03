package gov.nih.nci.cagrid.introduce.extension;

import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import javax.swing.JPanel;

public abstract class ServiceModificationUIPanel extends JPanel {
	private ServiceInformation serviceInfo;
	
	public ServiceInformation getServiceInfo() {
		return serviceInfo;
	}

	protected void setServiceInfo(ServiceInformation serviceInfo) {
		this.serviceInfo = serviceInfo;
	}

	public ServiceModificationUIPanel(ServiceInformation info){
		this.serviceInfo = info;
	}

}
