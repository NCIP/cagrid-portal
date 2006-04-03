package gov.nih.nci.cagrid.introduce.extension;

import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import javax.swing.JDialog;

public abstract class CreationExtensionUIDialog extends JDialog {
	private ServiceInformation serviceInfo;
	
	public ServiceInformation getServiceInfo() {
		return serviceInfo;
	}

	protected void setServiceInfo(ServiceInformation serviceInfo) {
		this.serviceInfo = serviceInfo;
	}

	public CreationExtensionUIDialog(ServiceInformation info){
		this.serviceInfo = info;
		this.setModal(true);
	}

}
