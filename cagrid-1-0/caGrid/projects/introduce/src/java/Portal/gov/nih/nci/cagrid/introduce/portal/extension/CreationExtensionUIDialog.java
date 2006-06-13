package gov.nih.nci.cagrid.introduce.portal.extension;

import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import javax.swing.JDialog;

public abstract class CreationExtensionUIDialog extends JDialog {
	private ServiceExtensionDescriptionType description;
	private ServiceInformation serviceInfo;
	
	public CreationExtensionUIDialog(ServiceExtensionDescriptionType desc, ServiceInformation info) {
		this.description = desc;
		this.serviceInfo = info;
		this.setModal(true);
	}
	
	
	public ServiceExtensionDescriptionType getExtensionDescription() {
		return description;
	}
	
	
	protected void setExtensionDescription(ServiceExtensionDescriptionType desc) {
		this.description = desc;
	}
	
	
	public ServiceInformation getServiceInfo() {
		return serviceInfo;
	}
	

	protected void setServiceInfo(ServiceInformation serviceInfo) {
		this.serviceInfo = serviceInfo;
	}
	
	
	public ExtensionTypeExtensionData getExtensionTypeExtensionData() {
		return ExtensionTools.getExtensionData(getExtensionDescription(), getServiceInfo());
	}
}
