package gov.nih.nci.cagrid.gme.service.globus.resource;

public class MetadataConfiguration {
	private String registrationTemplateFile;
	private boolean performRegistration;
	private String commonServiceMetadataFile;



	public boolean shouldPerformRegistration() {
		return performRegistration;
	}


	public void setPerformRegistration(boolean performRegistration) {
		this.performRegistration = performRegistration;
	}


	public String getRegistrationTemplateFile() {
		return registrationTemplateFile;
	}


	public void setRegistrationTemplateFile(String registrationTemplateFile) {
		this.registrationTemplateFile = registrationTemplateFile;
	}
	
	
	
	public String getCommonServiceMetadataFile() {
		return commonServiceMetadataFile;
	}
	
	
	public void setCommonServiceMetadataFile(String commonServiceMetadataFile) {
		this.commonServiceMetadataFile = commonServiceMetadataFile;
	}
		
}
