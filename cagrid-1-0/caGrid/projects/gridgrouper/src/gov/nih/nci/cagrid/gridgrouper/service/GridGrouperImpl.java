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
		// System.out.println("Caller: " + caller);
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

		String jndiName = Constants.JNDI_SERVICES_BASE_NAME + servicePath
				+ "/serviceconfiguration";
		try {
			javax.naming.Context initialContext = new InitialContext();
			this.configuration = (ServiceConfiguration) initialContext
					.lookup(jndiName);
		} catch (Exception e) {
			throw new Exception("Unable to instantiate service configuration.",
					e);
		}

		return this.configuration;
	}

	public gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor getStem(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
		return gridGrouper.getStem(getCallerIdentity(), stem);
	}

	public gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor[] getChildStems(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier parentStem) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
		return gridGrouper.getChildStems(getCallerIdentity(), parentStem);
	}

	public gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor getParentStem(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier childStem) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
		return gridGrouper.getParentStem(getCallerIdentity(), childStem);
	}

	public synchronized gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor updateStemDescription(
			gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem,
			java.lang.String description) throws RemoteException,
			gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.StemModifyFault {
		return gridGrouper.updateStemDescription(getCallerIdentity(), stem,
				description);
	}

	public synchronized gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor updateStemDisplayExtension(
			gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem,
			java.lang.String displayExtension) throws RemoteException,
			gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.StemModifyFault {
		return gridGrouper.updateStemDisplayExtension(getCallerIdentity(),
				stem, displayExtension);
	}

	public java.lang.String[] getSubjectsWithStemPrivilege(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem,gov.nih.nci.cagrid.gridgrouper.bean.StemPrivilegeType privilege) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
		return gridGrouper.getSubjectsWithStemPrivilege(getCallerIdentity(),
				stem, privilege);
	}

	public gov.nih.nci.cagrid.gridgrouper.bean.StemPrivilege[] getStemPrivileges(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem,java.lang.String subject) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
		return gridGrouper
				.getStemPrivileges(getCallerIdentity(), stem, subject);
	}

	public boolean hasStemPrivilege(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem,java.lang.String subject,gov.nih.nci.cagrid.gridgrouper.bean.StemPrivilegeType privilege) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
		return gridGrouper.hasStemPrivilege(getCallerIdentity(), stem, subject,
				privilege);
	}

	public boolean grantStemPrivilege(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem,java.lang.String subject,gov.nih.nci.cagrid.gridgrouper.bean.StemPrivilegeType privilege) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault, gov.nih.nci.cagrid.gridgrouper.stubs.GrantPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.SchemaFault {
		return gridGrouper.grantStemPrivilege(getCallerIdentity(),stem, subject, privilege);
	}

	public boolean revokeStemPrivilege(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem,java.lang.String subject,gov.nih.nci.cagrid.gridgrouper.bean.StemPrivilegeType privilege) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.RevokePrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.SchemaFault, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault {
		return gridGrouper.revokeStemPrivilege(getCallerIdentity(),stem, subject, privilege);
	}

}
