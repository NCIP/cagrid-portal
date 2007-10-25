package org.cagrid.gaards.cds.client;

import org.globus.gsi.GlobusCredential;

public class DelegationClient {
	
	private String uri;
	private GlobusCredential cred;
	
	public DelegationClient(String uri, GlobusCredential cred){
		this.uri = uri;
		this.cred = cred;
	}

}
