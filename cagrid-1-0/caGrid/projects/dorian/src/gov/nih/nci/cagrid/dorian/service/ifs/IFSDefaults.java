package gov.nih.nci.cagrid.dorian.service.ifs;

import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP;


public class IFSDefaults {
	private IFSUser defaultUser;
	private TrustedIdP defaultIdP;


	public IFSDefaults(TrustedIdP idp, IFSUser user) {
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
