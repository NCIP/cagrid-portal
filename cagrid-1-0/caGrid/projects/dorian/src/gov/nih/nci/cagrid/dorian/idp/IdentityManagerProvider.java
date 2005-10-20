package gov.nih.nci.cagrid.gums.idp;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.GUMSObject;
import gov.nih.nci.cagrid.gums.idp.bean.Application;
import gov.nih.nci.cagrid.gums.idp.bean.InvalidUserPropertyFault;
import gov.nih.nci.cagrid.gums.idp.bean.User;

import org.globus.wsrf.utils.FaultHelper;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */

public class IdentityManagerProvider extends GUMSObject{
	
	private IdPProperties properties;
	private UserManager userManager;
	
	public IdentityManagerProvider(Database db) throws GUMSInternalFault {
		try {
			this.properties = new IdPProperties(db);
			this.userManager = new UserManager(db,this.properties);
		} catch (Exception e) {
			logError(e.getMessage(), e);
			GUMSInternalFault fault = new GUMSInternalFault();
			fault
					.setFaultString("Error initializing the Identity Manager Provider.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		}
	}
	
	public String register(Application application)throws GUMSInternalFault, InvalidUserPropertyFault{
		IdPRegistrationPolicy policy = properties.getRegistrationPolicy();
		User u = policy.register(application);
		userManager.addUser(u);
		if(u.getStatus().equals(UserManager.PENDING)){
			return "Your application will be reviewed by an administrator and you will be contacted at "+u.getEmail()+" upon a decision.";
		}else{
			return "Your account was approved, your current account status is "+u.getStatus().getValue()+" you mail use "+u.getEmail()+" to access your account";
		}
	}
	
	

	public IdPProperties getProperties() {
		return properties;
	}
}
