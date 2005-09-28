package gov.nih.nci.cagrid.gums.service;

import gov.nih.nci.cagrid.gums.Registration;
import gov.nih.nci.cagrid.gums.bean.AttributeDescriptor;
import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;

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
		}catch(Exception e){
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString(e.getMessage());
			throw fault;
		}
	}

	public String getIdentity() {
		return identity;
	}

}
