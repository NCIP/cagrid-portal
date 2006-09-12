package gov.nih.nci.cagrid.dorian.service.idp;

import gov.nih.nci.cagrid.dorian.idp.bean.Application;
import gov.nih.nci.cagrid.dorian.idp.bean.ApplicationReview;
import gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.stubs.types.InvalidUserPropertyFault;


public interface IdPRegistrationPolicy {
	public ApplicationReview register(Application application) throws DorianInternalFault,InvalidUserPropertyFault;
	public String getDescription();
	public String getName();
}
