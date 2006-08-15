package gov.nih.nci.cagrid.gridgrouper.client;

import edu.internet2.middleware.grouper.GroupDeleteException;
import edu.internet2.middleware.grouper.GroupModifyException;
import edu.internet2.middleware.grouper.GrouperRuntimeException;
import edu.internet2.middleware.grouper.InsufficientPrivilegeException;
import edu.internet2.middleware.subject.Subject;
import edu.internet2.middleware.subject.SubjectNotFoundException;
import gov.nih.nci.cagrid.gridgrouper.bean.GroupDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier;
import gov.nih.nci.cagrid.gridgrouper.bean.GroupUpdate;
import gov.nih.nci.cagrid.gridgrouper.common.SubjectUtils;
import gov.nih.nci.cagrid.gridgrouper.grouper.GroupI;
import gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.GroupDeleteFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.GroupModifyFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault;

import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class Group extends GridGrouperObject implements GroupI {

	private GroupDescriptor des;

	private GridGrouper gridGrouper;

	protected Group(GridGrouper gridGrouper, GroupDescriptor des) {
		this.gridGrouper = gridGrouper;
		this.des = des;
	}

	public String getCreateSource() {
		return des.getCreateSource();
	}

	public Subject getCreateSubject() throws SubjectNotFoundException {
		return SubjectUtils.getSubject(des.getCreateSubject(), true);
	}

	public Date getCreateTime() {
		return new Date(des.getCreateTime());
	}

	public String getDescription() {
		return des.getDescription();
	}

	public String getDisplayExtension() {
		return des.getDisplayExtension();
	}

	public String getDisplayName() {
		return des.getDisplayName();
	}

	public String getExtension() {
		return des.getExtension();
	}

	public String getModifySource() {
		return des.getModifySource();
	}

	public Subject getModifySubject() throws SubjectNotFoundException {
		return SubjectUtils.getSubject(des.getModifySubject(), true);
	}

	public Date getModifyTime() {
		if (des.getModifyTime() == 0) {
			return getCreateTime();
		} else {
			return new Date(des.getModifyTime());
		}
	}

	public GroupIdentifier getGroupIdentifier() {
		return gridGrouper.getGroupIdentifier(getName());
	}

	public String getName() {
		return des.getName();
	}

	public String getUuid() {
		return des.getUUID();
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append("displayName", getDisplayName()).append("name",
						getName()).append("uuid", getUuid()).append("created",
						getCreateTime()).append("modified", getModifyTime())
				.toString();
	}

	public GridGrouper getGridGrouper() {
		return gridGrouper;
	}

	public void delete() throws GroupDeleteException,
			InsufficientPrivilegeException {
		try {
			gridGrouper.getClient().deleteGroup(getGroupIdentifier());
		} catch (InsufficientPrivilegeFault f) {
			throw new InsufficientPrivilegeException(f.getFaultString());
		} catch (GroupDeleteFault f) {
			throw new GroupDeleteException(f.getFaultString());
		} catch (GridGrouperRuntimeFault e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getFaultString());
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}
	}

	public void setDescription(String value) throws GroupModifyException,
			InsufficientPrivilegeException {
		try {
			GroupUpdate update = new GroupUpdate();
			update.setDescription(value);
			this.des=gridGrouper.getClient().updateGroup(this.getGroupIdentifier(),
					update);
		} catch (InsufficientPrivilegeFault f) {
			throw new InsufficientPrivilegeException(f.getFaultString());
		} catch (GroupModifyFault f) {
			throw new GroupModifyException(f.getFaultString());
		} catch (GridGrouperRuntimeFault e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getFaultString());
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}

	}

	public void setDisplayExtension(String value) throws GroupModifyException,
			InsufficientPrivilegeException {
		try {
			GroupUpdate update = new GroupUpdate();
			update.setDisplayExtension(value);
			this.des=gridGrouper.getClient().updateGroup(this.getGroupIdentifier(),
					update);
		} catch (InsufficientPrivilegeFault f) {
			throw new InsufficientPrivilegeException(f.getFaultString());
		} catch (GroupModifyFault f) {
			throw new GroupModifyException(f.getFaultString());
		} catch (GridGrouperRuntimeFault e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getFaultString());
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}
	}

	public void setExtension(String value) throws GroupModifyException,
			InsufficientPrivilegeException {
		try {
			GroupUpdate update = new GroupUpdate();
			update.setExtension(value);
			this.des=gridGrouper.getClient().updateGroup(this.getGroupIdentifier(),
					update);
		} catch (InsufficientPrivilegeFault f) {
			throw new InsufficientPrivilegeException(f.getFaultString());
		} catch (GroupModifyFault f) {
			throw new GroupModifyException(f.getFaultString());
		} catch (GridGrouperRuntimeFault e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getFaultString());
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}
	}

}
