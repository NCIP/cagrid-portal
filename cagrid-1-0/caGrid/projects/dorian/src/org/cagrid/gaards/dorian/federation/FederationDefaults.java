package org.cagrid.gaards.dorian.federation;



public class FederationDefaults {
	private IFSUser defaultUser;
	private TrustedIdP defaultIdP;


	public FederationDefaults(TrustedIdP idp, IFSUser user) {
		this.defaultIdP = idp;
		this.defaultUser = user;
	}


	public IFSUser getDefaultUser() {
		return defaultUser;
	}


	public TrustedIdP getDefaultIdP() {
		return defaultIdP;
	}


	public void setDefaultUser(IFSUser defaultUser) {
		this.defaultUser = defaultUser;
	}


	public void setDefaultIdP(TrustedIdP defaultIdP) {
		this.defaultIdP = defaultIdP;
	}

}
