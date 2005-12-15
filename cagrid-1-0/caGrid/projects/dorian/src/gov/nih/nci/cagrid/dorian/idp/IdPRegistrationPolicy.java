package gov.nih.nci.cagrid.dorian.idp;

import gov.nih.nci.cagrid.dorian.bean.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.idp.bean.Application;
import gov.nih.nci.cagrid.dorian.idp.bean.ApplicationReview;
import gov.nih.nci.cagrid.dorian.idp.bean.InvalidUserPropertyFault;


public interface IdPRegistrationPolicy {
	public ApplicationReview register(Application application) throws DorianInternalFault,InvalidUserPropertyFault;
	public String getDescription();
	public String getName();
}
