package gov.nih.nci.cagrid.gridgrouper.client;

import edu.internet2.middleware.grouper.GrouperRuntimeException;
import edu.internet2.middleware.grouper.Privilege;
import edu.internet2.middleware.grouper.StemNotFoundException;
import edu.internet2.middleware.subject.Subject;
import gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier;
import gov.nih.nci.cagrid.gridgrouper.bean.StemPrivilege;
import gov.nih.nci.cagrid.gridgrouper.bean.StemPrivilegeType;
import gov.nih.nci.cagrid.gridgrouper.common.SubjectUtils;
import gov.nih.nci.cagrid.gridgrouper.grouper.Grouper;
import gov.nih.nci.cagrid.gridgrouper.grouper.NamingPrivilege;
import gov.nih.nci.cagrid.gridgrouper.grouper.Stem;
import gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault;

import java.util.HashSet;
import java.util.Set;

import org.globus.gsi.GlobusCredential;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class GridGrouper extends GridGrouperObject implements Grouper {

	public static final String ROOT_STEM = "";

	private GridGrouperClient client;

	public GridGrouper(String serviceURI) {
		this(serviceURI, null);
	}

	public GridGrouper(String serviceURI, GlobusCredential cred) {
		try {
			this.setClient(new GridGrouperClient(serviceURI, cred));
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}
	}

	public Stem getRootStem() throws StemNotFoundException {
		return findStem(ROOT_STEM);
	}

	public Stem findStem(String name) throws StemNotFoundException {
		try {
			StemDescriptor des = getClient().getStem(getStemIdentifier(name));
			return new GridGrouperStem(this, des);
		} catch (StemNotFoundFault f) {
			throw new StemNotFoundException(f.getFaultString());
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}
	}

	public Set getChildStems(String stemName) {
		try {
			StemDescriptor[] children = getClient().getChildStems(
					getStemIdentifier(stemName));
			Set set = new HashSet();
			if (children != null) {
				for (int i = 0; i < children.length; i++) {
					set.add(new GridGrouperStem(this, children[i]));
				}
			}
			return set;
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}
	}

	public Stem getParentStem(String childStemName)
			throws StemNotFoundException {
		try {
			StemDescriptor des = getClient().getParentStem(
					getStemIdentifier(childStemName));
			return new GridGrouperStem(this, des);
		} catch (StemNotFoundFault f) {
			throw new StemNotFoundException(f.getFaultString());
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}
	}

	public Set getStemPrivileges(String stemName, Subject subject)
			throws StemNotFoundException {
		try {
			StemPrivilege[] privs = getClient().getStemPrivileges(
					getStemIdentifier(stemName), subject.getId());
			Set set = new HashSet();
			if (privs != null) {
				for (int i = 0; i < privs.length; i++) {
					NamingPrivilege priv = new GridGrouperNamingPrivilege(
							privs[i].getStemName(), SubjectUtils
									.getSubject(privs[i].getSubject()),
							SubjectUtils.getSubject(privs[i].getOwner()),
							Privilege.getInstance(privs[i].getPrivilegeType()
									.getValue()), privs[i]
									.getImplementationClass(), privs[i]
									.isIsRevokable());
					set.add(priv);
				}
			}
			return set;
		} catch (StemNotFoundFault f) {
			throw new StemNotFoundException(f.getFaultString());
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}
	}

	public Set getSubjectsWithStemPrivilege(String stemName, Privilege privilege)
			throws StemNotFoundException {
		try {
			String[] subs = getClient().getSubjectsWithStemPrivilege(
					getStemIdentifier(stemName),
					StemPrivilegeType.fromValue(privilege.getName()));
			Set set = new HashSet();
			if (subs != null) {
				for (int i = 0; i < subs.length; i++) {
					set.add(SubjectUtils.getSubject(subs[i], true));
				}
			}
			return set;
		} catch (StemNotFoundFault f) {
			throw new StemNotFoundException(f.getFaultString());
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}
	}

	public boolean hasStemPrivilege(String stemName, Subject subject,
			Privilege privilege) throws StemNotFoundException {
		try {
			StemPrivilegeType type = StemPrivilegeType.fromValue(privilege
					.getName());
			return getClient().hasStemPrivilege(getStemIdentifier(stemName),
					subject.getId(), type);
		} catch (StemNotFoundFault f) {
			throw new StemNotFoundException(f.getFaultString());
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}
	}

	protected GridGrouperClient getClient() {
		return client;
	}

	protected void setClient(GridGrouperClient client) {
		this.client = client;
	}

	protected StemIdentifier getStemIdentifier(String stemName) {
		StemIdentifier id = new StemIdentifier();
		id.setGridGrouperURL(getClient().getEndpointReference().toString());
		id.setStemName(stemName);
		return id;
	}

}
