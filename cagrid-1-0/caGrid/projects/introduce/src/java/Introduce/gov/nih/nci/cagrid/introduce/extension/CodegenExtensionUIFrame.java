package gov.nih.nci.cagrid.introduce.extension;

import gov.nih.nci.cagrid.introduce.ServiceInformation;

import javax.swing.JFrame;

public abstract class CodegenExtensionUIFrame extends JFrame {
	private ServiceInformation serviceInfo;
	
	public ServiceInformation getServiceInfo() {
		return serviceInfo;
	}

	protected void setServiceInfo(ServiceInformation serviceInfo) {
		this.serviceInfo = serviceInfo;
	}

	public CodegenExtensionUIFrame(ServiceInformation info){
		this.serviceInfo = info;
	}

}
