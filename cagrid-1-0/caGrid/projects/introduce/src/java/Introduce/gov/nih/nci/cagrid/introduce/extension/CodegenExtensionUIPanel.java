package gov.nih.nci.cagrid.introduce.extension;

import gov.nih.nci.cagrid.introduce.ServiceInformation;

import javax.swing.JPanel;

public abstract class CodegenExtensionUIPanel extends JPanel {
	private ServiceInformation serviceInfo;
	
	public ServiceInformation getServiceInfo() {
		return serviceInfo;
	}

	protected void setServiceInfo(ServiceInformation serviceInfo) {
		this.serviceInfo = serviceInfo;
	}

	public CodegenExtensionUIPanel(ServiceInformation info){
		this.serviceInfo = info;
	}

}
