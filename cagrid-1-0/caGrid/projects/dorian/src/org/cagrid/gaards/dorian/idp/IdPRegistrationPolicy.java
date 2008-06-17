package org.cagrid.gaards.dorian.idp;

import org.cagrid.gaards.dorian.idp.Application;
import org.cagrid.gaards.dorian.idp.ApplicationReview;
import org.cagrid.gaards.dorian.stubs.types.DorianInternalFault;
import org.cagrid.gaards.dorian.stubs.types.InvalidUserPropertyFault;


public interface IdPRegistrationPolicy {
	public ApplicationReview register(Application application) throws DorianInternalFault,InvalidUserPropertyFault;
	public String getDescription();
	public String getName();
}
