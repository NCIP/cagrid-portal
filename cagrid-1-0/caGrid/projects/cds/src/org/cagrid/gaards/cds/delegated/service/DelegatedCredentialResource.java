package org.cagrid.gaards.cds.delegated.service;

import org.cagrid.gaards.cds.common.DelegationIdentifier;
import org.cagrid.gaards.cds.service.CDS;
import org.globus.wsrf.Resource;

public class DelegatedCredentialResource implements Resource {

	private CDS cds;
	private DelegationIdentifier id;

	public DelegatedCredentialResource(CDS cds, DelegationIdentifier id) {
		this.cds = cds;
		this.id = id;
	}
	
	 public DelegationIdentifier getDelegatedCredential(){
		 System.out.println("");
		 System.out.println("");
		 System.out.println("");
		 System.out.println("*******************************************************");
		 System.out.println("");
		 System.out.println("RESOURCE INVOKED - getDelegatedCredential() FOR "+id.getDelegationId());
		 System.out.println("");
		 System.out.println("*******************************************************");
		 System.out.println("");
		 System.out.println("");
		 System.out.println("");
		 return id;
	 }
}
