package gov.nih.nci.cagrid.gums.service;

import gov.nih.nci.cagrid.gums.Registration;
import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.common.FaultUtil;
import gov.nih.nci.cagrid.gums.ifs.bean.AttributeDescriptor;
import gov.nih.nci.cagrid.gums.ifs.bean.InvalidApplicationFault;
import gov.nih.nci.cagrid.gums.ifs.bean.UserApplication;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class GUMS implements Registration{
	
	private GUMSManager jm;
	private String identity;
	
	public GUMS(GUMSManager jm, String identity){
		this.jm = jm;
		this.identity = identity;
	}

	public AttributeDescriptor[] getRequiredUserAttributes() throws GUMSInternalFault {
		try{
		return jm.getUserAttributeManager().getRequiredAttributes();
		}catch(GUMSInternalFault e){
			FaultUtil.printFault(e);
			throw e;
		}
	}
	
	

	public String registerUser(UserApplication application) throws InvalidApplicationFault, GUMSInternalFault {
		// TODO Auto-generated method stub
		return null;
	}

	public String getIdentity() {
		return identity;
	}

}
