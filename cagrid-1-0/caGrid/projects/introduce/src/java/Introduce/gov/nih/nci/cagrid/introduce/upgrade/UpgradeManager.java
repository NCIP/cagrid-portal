package gov.nih.nci.cagrid.introduce.upgrade;

import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.common.CommonTools;


public class UpgradeManager {
	private ServiceDescription service;
	
	public UpgradeManager(ServiceDescription service){
		this.service = service;
	}
	
	public boolean canBeUpgraded(){
		if(service.getIntroduceVersion()!=CommonTools.getIntroduceVersion()){
			
			
		} else {
			return false;
		}
		return false;
	}
	
	public void upgrade() throws Exception {
		
	}
}
