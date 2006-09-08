package gov.nih.nci.cagrid.gridca.ui;

import org.globus.gsi.GlobusCredential;

public class ProxyCaddy {
	private GlobusCredential proxy;

	private String identity;

	public ProxyCaddy(GlobusCredential cred) {
		this.identity = cred.getIdentity();
		this.proxy = cred;
	}

	public ProxyCaddy(String identity, GlobusCredential cred) {
		this.identity = identity;
		this.proxy = cred;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public void setProxy(GlobusCredential proxy) {
		this.proxy = proxy;
	}

	public String getIdentity() {
		return identity;
	}

	public GlobusCredential getProxy() {
		return proxy;
	}

	public String toString() {
		return identity;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		ProxyCaddy caddy = (ProxyCaddy) obj;
		if (this.identity.equals(caddy.getIdentity())) {
			return true;
		} else {
			return false;
		}
	}

}