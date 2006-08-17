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

	public gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor getStem(
			gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem)
			throws RemoteException,
			gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
		return gridGrouper.getStem(getCallerIdentity(), stem);
	}

	public gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor[] getChildStems(
			gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier parentStem)
			throws RemoteException,
			gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
		return gridGrouper.getChildStems(getCallerIdentity(), parentStem);
	}

	public gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor getParentStem(
			gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier childStem)
			throws RemoteException,
			gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
		return gridGrouper.getParentStem(getCallerIdentity(), childStem);
	}

	public java.lang.String[] getSubjectsWithStemPrivilege(
			gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem,
			gov.nih.nci.cagrid.gridgrouper.bean.StemPrivilegeType privilege)
			throws RemoteException,
			gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
		return gridGrouper.getSubjectsWithStemPrivilege(getCallerIdentity(),
				stem, privilege);
	}

	public gov.nih.nci.cagrid.gridgrouper.bean.StemPrivilege[] getStemPrivileges(
			gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem,
			java.lang.String subject) throws RemoteException,
			gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
		return gridGrouper
				.getStemPrivileges(getCallerIdentity(), stem, subject);
	}

	public boolean hasStemPrivilege(
			gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem,
			java.lang.String subject,
			gov.nih.nci.cagrid.gridgrouper.bean.StemPrivilegeType privilege)
			throws RemoteException,
			gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
		return gridGrouper.hasStemPrivilege(getCallerIdentity(), stem, subject,
				privilege);
	}

	public void grantStemPrivilege(
			gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem,
			java.lang.String subject,
			gov.nih.nci.cagrid.gridgrouper.bean.StemPrivilegeType privilege)
			throws RemoteException,
			gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.GrantPrivilegeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.SchemaFault {
		gridGrouper.grantStemPrivilege(getCallerIdentity(), stem, subject,
				privilege);
	}

	public void revokeStemPrivilege(
			gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem,
			java.lang.String subject,
			gov.nih.nci.cagrid.gridgrouper.bean.StemPrivilegeType privilege)
			throws RemoteException,
			gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.RevokePrivilegeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.SchemaFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault {
		gridGrouper.revokeStemPrivilege(getCallerIdentity(), stem, subject,
				privilege);
	}

	public gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor addChildStem(
			gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem,
			java.lang.String extension, java.lang.String displayExtension)
			throws RemoteException,
			gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.StemAddFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
		return gridGrouper.addChildStem(getCallerIdentity(), stem, extension,
				displayExtension);
	}

	public void deleteStem(
			gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem)
			throws RemoteException,
			gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.StemDeleteFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
		gridGrouper.deleteStem(getCallerIdentity(), stem);
	}

	public gov.nih.nci.cagrid.gridgrouper.bean.GroupDescriptor getGroup(
			gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier group)
			throws RemoteException,
			gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault {
		return gridGrouper.getGroup(getCallerIdentity(), group);
	}

	public gov.nih.nci.cagrid.gridgrouper.bean.GroupDescriptor[] getChildGroups(
			gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem)
			throws RemoteException,
			gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
		return gridGrouper.getChildGroups(getCallerIdentity(), stem);
	}

	public gov.nih.nci.cagrid.gridgrouper.bean.GroupDescriptor addChildGroup(
			gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem,
			java.lang.String extension, java.lang.String displayExtension)
			throws RemoteException,
			gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.GroupAddFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault {
		return gridGrouper.addChildGroup(getCallerIdentity(), stem, extension,
				displayExtension);
	}

	public void deleteGroup(
			gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier group)
			throws RemoteException,
			gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.GroupDeleteFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault {
		gridGrouper.deleteGroup(getCallerIdentity(), group);
	}

	public gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor updateStem(
			gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem,
			gov.nih.nci.cagrid.gridgrouper.bean.StemUpdate update)
			throws RemoteException,
			gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.StemModifyFault {
		return gridGrouper.updateStem(getCallerIdentity(), stem, update);
	}

	public gov.nih.nci.cagrid.gridgrouper.bean.GroupDescriptor updateGroup(
			gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier group,
			gov.nih.nci.cagrid.gridgrouper.bean.GroupUpdate update)
			throws RemoteException,
			gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.GroupModifyFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault {
		return gridGrouper.updateGroup(getCallerIdentity(), group, update);
	}

	public void addMember(
			gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier group,
			java.lang.String subject) throws RemoteException,
			gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.MemberAddFault {
		gridGrouper.addMember(getCallerIdentity(), group, subject);
	}

	public gov.nih.nci.cagrid.gridgrouper.bean.MemberDescriptor[] getMembers(
			gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier group,
			gov.nih.nci.cagrid.gridgrouper.bean.MemberFilter filter)
			throws RemoteException,
			gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault {
		return gridGrouper.getMembers(getCallerIdentity(), group, filter);
	}

	public boolean isMemberOf(
			gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier group,
			java.lang.String member,
			gov.nih.nci.cagrid.gridgrouper.bean.MemberFilter filter)
			throws RemoteException,
			gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault {
		return gridGrouper.isMemberOf(getCallerIdentity(), group, member,
				filter);
	}

	public gov.nih.nci.cagrid.gridgrouper.bean.MembershipDescriptor[] getMemberships(
			gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier group,
			gov.nih.nci.cagrid.gridgrouper.bean.MemberFilter filter)
			throws RemoteException,
			gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault {
		return this.gridGrouper.getMemberships(getCallerIdentity(), group,
				filter);

	}

	public void deleteMember(
			gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier group,
			java.lang.String member) throws RemoteException,
			gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.MemberDeleteFault {
		gridGrouper.deleteMember(getCallerIdentity(), group, member);
	}

	public gov.nih.nci.cagrid.gridgrouper.bean.GroupDescriptor addCompositeMember(
			gov.nih.nci.cagrid.gridgrouper.bean.GroupCompositeType type,
			gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier composite,
			gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier left,
			gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier right)
			throws RemoteException,
			gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.MemberAddFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault {
		return gridGrouper.addCompositeMember(getCallerIdentity(), type,
				composite, left, right);
	}

	public gov.nih.nci.cagrid.gridgrouper.bean.GroupDescriptor deleteCompositeMember(
			gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier group)
			throws RemoteException,
			gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault,
			gov.nih.nci.cagrid.gridgrouper.stubs.MemberDeleteFault {
		return gridGrouper.deleteCompositeMember(getCallerIdentity(), group);
	}

}
