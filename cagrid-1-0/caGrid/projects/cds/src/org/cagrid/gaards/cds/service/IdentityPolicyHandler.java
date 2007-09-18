package org.cagrid.gaards.cds.service;

import org.cagrid.gaards.cds.conf.PolicyHandlerConfiguration;
import org.cagrid.gaards.cds.stubs.types.CDSInternalFault;
import org.cagrid.tools.database.Database;


public class IdentityPolicyHandler extends PolicyHandler {

	public IdentityPolicyHandler(PolicyHandlerConfiguration conf, Database db) {
		super(conf, db);
	}


	@Override
	public void removeAllStoredPolicies() throws CDSInternalFault {
		// TODO Auto-generated method stub

	}

}
