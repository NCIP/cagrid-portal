package org.cagrid.gaards.cds.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.gaards.cds.common.DelegationPolicy;
import org.cagrid.gaards.cds.common.IdentityDelegationPolicy;
import org.cagrid.gaards.cds.stubs.types.CDSInternalFault;
import org.cagrid.tools.database.Database;

public class IdentityPolicyHandler implements PolicyHandler {

	private Database db;
	private Log log;

	public IdentityPolicyHandler(Database db) {
		this.log = LogFactory.getLog(this.getClass().getName());
		this.db = db;
	}

	public void removeAllStoredPolicies() throws CDSInternalFault {
		// TODO Auto-generated method stub

	}

	public boolean isSupported(DelegationPolicy policy) {
		if (policy instanceof IdentityDelegationPolicy) {
			return true;
		} else {
			return false;
		}
	}

}
