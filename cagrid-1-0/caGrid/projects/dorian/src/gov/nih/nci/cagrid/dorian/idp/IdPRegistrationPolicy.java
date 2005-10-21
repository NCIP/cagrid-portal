package gov.nih.nci.cagrid.gums.idp;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.idp.bean.Application;
import gov.nih.nci.cagrid.gums.idp.bean.ApplicationReview;
import gov.nih.nci.cagrid.gums.idp.bean.InvalidUserPropertyFault;

public interface IdPRegistrationPolicy {
	public ApplicationReview register(Application application) throws GUMSInternalFault,InvalidUserPropertyFault;
	public String getDescription();
	public String getName();
}
