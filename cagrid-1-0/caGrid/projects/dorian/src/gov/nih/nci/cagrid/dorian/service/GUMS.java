package gov.nih.nci.cagrid.gums.service;

import gov.nih.nci.cagrid.gums.Registration;
import gov.nih.nci.cagrid.gums.bean.AttributeDescriptor;
import gov.nih.nci.cagrid.gums.common.GUMSInternalException;

public class GUMS implements Registration{
	
	private GUMSManager jm;
	private String identity;
	
	public GUMS(GUMSManager jm, String identity){
		this.jm = jm;
		this.identity = identity;
	}

	public AttributeDescriptor[] getRequiredUserAttributes() throws GUMSInternalException {
		return jm.getUserAttributeManager().getRequiredAttributes();
	}

	public String getIdentity() {
		return identity;
	}

}
