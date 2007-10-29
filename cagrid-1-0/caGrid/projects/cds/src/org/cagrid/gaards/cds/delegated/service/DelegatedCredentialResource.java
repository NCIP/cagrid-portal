package org.cagrid.gaards.cds.delegated.service;

import org.cagrid.gaards.cds.common.CertificateChain;
import org.cagrid.gaards.cds.common.DelegationIdentifier;
import org.cagrid.gaards.cds.common.PublicKey;
import org.cagrid.gaards.cds.service.CDS;
import org.cagrid.gaards.cds.stubs.types.CDSInternalFault;
import org.cagrid.gaards.cds.stubs.types.DelegationFault;
import org.cagrid.gaards.cds.stubs.types.PermissionDeniedFault;
import org.globus.wsrf.Resource;
import org.globus.wsrf.security.SecurityManager;

public class DelegatedCredentialResource implements Resource {

	private CDS cds;
	private DelegationIdentifier id;

	public DelegatedCredentialResource(CDS cds, DelegationIdentifier id) {
		this.cds = cds;
		this.id = id;
	}

	public CertificateChain getDelegatedCredential(PublicKey publicKey)
			throws CDSInternalFault, DelegationFault, PermissionDeniedFault {
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out
				.println("*******************************************************");
		System.out.println("");
		System.out.println("RESOURCE INVOKED - getDelegatedCredential() FOR "
				+ id.getDelegationId());
		System.out.println("");
		System.out
				.println("*******************************************************");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		return this.cds.getDelegatedCredential(getCallerIdentity(), id,
				publicKey);
	}

	private String getCallerIdentity() throws PermissionDeniedFault {
		String caller = SecurityManager.getManager().getCaller();
		if ((caller == null) || (caller.equals("<anonymous>"))) {
			PermissionDeniedFault fault = new PermissionDeniedFault();
			fault.setFaultString("No Grid Credentials Provided.");
			throw fault;
		}
		return caller;
	}
}
