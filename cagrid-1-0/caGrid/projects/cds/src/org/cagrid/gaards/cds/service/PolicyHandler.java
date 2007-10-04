package org.cagrid.gaards.cds.service;

import org.cagrid.gaards.cds.common.DelegationPolicy;
import org.cagrid.gaards.cds.stubs.types.CDSInternalFault;

public interface PolicyHandler {
	public void removeAllStoredPolicies() throws CDSInternalFault;

	public boolean isSupported(DelegationPolicy policy);
}
