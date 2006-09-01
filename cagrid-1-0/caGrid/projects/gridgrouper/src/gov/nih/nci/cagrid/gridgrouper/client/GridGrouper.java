package gov.nih.nci.cagrid.gridgrouper.client;

import edu.internet2.middleware.grouper.GroupNotFoundException;
import edu.internet2.middleware.grouper.GrouperRuntimeException;
import edu.internet2.middleware.grouper.StemNotFoundException;
import edu.internet2.middleware.subject.Subject;
import gov.nih.nci.cagrid.gridgrouper.bean.GroupDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier;
import gov.nih.nci.cagrid.gridgrouper.bean.MemberFilter;
import gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier;
import gov.nih.nci.cagrid.gridgrouper.grouper.GroupI;
import gov.nih.nci.cagrid.gridgrouper.grouper.GrouperI;
import gov.nih.nci.cagrid.gridgrouper.grouper.StemI;
import gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault;

import java.util.HashSet;
import java.util.Set;

import org.globus.gsi.GlobusCredential;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella</A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @version $Id: GridGrouperBaseTreeNode.java,v 1.1 2006/08/04 03:49:26 langella
 *          Exp $
 */
public class GridGrouper extends GridGrouperObject implements GrouperI {

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

	public StemI getRootStem() throws StemNotFoundException {
		return findStem(ROOT_STEM);
	}

	public StemI findStem(String name) throws StemNotFoundException {
		try {
			StemDescriptor des = getClient().getStem(getStemIdentifier(name));
			return new Stem(this, des);
		} catch (StemNotFoundFault f) {
			throw new StemNotFoundException(f.getFaultString());
		} catch (GridGrouperRuntimeFault e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getFaultString());
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
					set.add(new Stem(this, children[i]));
				}
			}
			return set;
		} catch (GridGrouperRuntimeFault e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getFaultString());
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}
	}

	public StemI getParentStem(String childStemName)
			throws StemNotFoundException {
		try {
			StemDescriptor des = getClient().getParentStem(
					getStemIdentifier(childStemName));
			return new Stem(this, des);
		} catch (StemNotFoundFault f) {
			throw new StemNotFoundException(f.getFaultString());
		} catch (GridGrouperRuntimeFault e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getFaultString());
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}
	}

	
	public GroupI findGroup(String name) throws GroupNotFoundException {
		try {
			GroupDescriptor des = getClient()
					.getGroup(getGroupIdentifier(name));
			return new Group(this, des);
		} catch (GroupNotFoundFault f) {
			throw new GroupNotFoundException(f.getFaultString());
		} catch (GridGrouperRuntimeFault e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getFaultString());
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

	protected GroupIdentifier getGroupIdentifier(String groupName) {
		GroupIdentifier id = new GroupIdentifier();
		id.setGridGrouperURL(getClient().getEndpointReference().toString());
		id.setGroupName(groupName);
		return id;
	}

	public String getName() {
		return getClient().getEndpointReference().getAddress().toString();
	}

	public String getProxyIdentity() {
		return getClient().getProxyIdentity();
	}

	public boolean isMemberOf(String subjectId, String groupName) {
		try {
			return getClient().isMemberOf(getGroupIdentifier(groupName),
					subjectId, MemberFilter.All);
		} catch (GridGrouperRuntimeFault e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getFaultString());
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}
	}

	public boolean isMemberOf(Subject subject, String groupName) {
		try {
			return getClient().isMemberOf(getGroupIdentifier(groupName),
					subject.getId(), MemberFilter.All);
		} catch (GridGrouperRuntimeFault e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getFaultString());
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}
	}

}
