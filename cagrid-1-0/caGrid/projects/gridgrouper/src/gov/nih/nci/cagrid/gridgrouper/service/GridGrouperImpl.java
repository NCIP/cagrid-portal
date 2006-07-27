package gov.nih.nci.cagrid.gridgrouper.service;

import gov.nih.nci.cagrid.gridgrouper.subject.AnonymousGridUserSubject;

import java.rmi.RemoteException;

import javax.naming.InitialContext;

import org.apache.axis.MessageContext;
import org.globus.wsrf.Constants;
import org.globus.wsrf.security.SecurityManager;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class GridGrouperImpl {
	private ServiceConfiguration configuration;
	private GridGrouper gridGrouper;


	public GridGrouperImpl() throws RemoteException {
		this.gridGrouper = new GridGrouper();
	}


	private String getCallerIdentity() {
		String caller = SecurityManager.getManager().getCaller();
		//System.out.println("Caller: " + caller);
		if ((caller == null) || (caller.equals("<anonymous>"))) {
			return AnonymousGridUserSubject.ANONYMOUS_GRID_USER_ID;
		} else {
			return caller;
		}
	}


	public ServiceConfiguration getConfiguration() throws Exception {
		if (this.configuration != null) {
			return this.configuration;
		}
		MessageContext ctx = MessageContext.getCurrentContext();

		String servicePath = ctx.getTargetService();

		String jndiName = Constants.JNDI_SERVICES_BASE_NAME + servicePath + "/serviceconfiguration";
		try {
			javax.naming.Context initialContext = new InitialContext();
			this.configuration = (ServiceConfiguration) initialContext.lookup(jndiName);
		} catch (Exception e) {
			throw new Exception("Unable to instantiate service configuration.", e);
		}

		return this.configuration;
	}


	public gov.nih.nci.cagrid.gridgrouper.beans.StemDescriptor getStem(java.lang.String stemName)
		throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault,
		gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
		return gridGrouper.getStem(getCallerIdentity(), stemName);
	}


	public gov.nih.nci.cagrid.gridgrouper.beans.StemDescriptor[] getChildStems(java.lang.String parentStemName)
		throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault,
		gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
		return gridGrouper.getChildStems(getCallerIdentity(), parentStemName);
	}

}
