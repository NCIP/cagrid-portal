package gov.nih.nci.cagrid.gums.service;

import gov.nih.nci.cagrid.gums.Registration;
import gov.nih.nci.cagrid.gums.bean.AttributeDescriptor;
import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.bean.InvalidApplicationFault;
import gov.nih.nci.cagrid.gums.bean.UserApplication;
import gov.nih.nci.cagrid.gums.common.FaultUtil;

public class GUMS implements Registration{
	
	private GUMSManager jm;
	private String identity;
	
	public GUMS(GUMSManager jm, String identity){
		this.jm = jm;
		this.identity = identity;
	}

	public AttributeDescriptor[] getRequiredUserAttributes() throws GUMSInternalFault {
		try{
		return jm.getUserAttributeManager().getRequiredAttributes();
		}catch(GUMSInternalFault e){
			FaultUtil.printFault(e);
			throw e;
		}
	}
	
	

	public String registerUser(UserApplication application) throws InvalidApplicationFault, GUMSInternalFault {
		// TODO Auto-generated method stub
		return null;
	}

	public String getIdentity() {
		return identity;
	}

}
