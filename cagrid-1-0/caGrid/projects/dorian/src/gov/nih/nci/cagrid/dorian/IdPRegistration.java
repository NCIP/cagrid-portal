package gov.nih.nci.cagrid.gums;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.common.GUMSFault;
import gov.nih.nci.cagrid.gums.idp.bean.Application;
import gov.nih.nci.cagrid.gums.idp.bean.InvalidUserPropertyFault;

public interface IdPRegistration {
	public String register(Application a) throws GUMSFault,GUMSInternalFault,InvalidUserPropertyFault;
	
}
