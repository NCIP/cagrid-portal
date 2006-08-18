package gov.nih.nci.cagrid.gridgrouper.client;

import edu.internet2.middleware.grouper.CompositeType;
import edu.internet2.middleware.grouper.GroupDeleteException;
import edu.internet2.middleware.grouper.GroupModifyException;
import edu.internet2.middleware.grouper.GrouperRuntimeException;
import edu.internet2.middleware.grouper.InsufficientPrivilegeException;
import edu.internet2.middleware.grouper.MemberAddException;
import edu.internet2.middleware.grouper.MemberDeleteException;
import edu.internet2.middleware.subject.Subject;
import edu.internet2.middleware.subject.SubjectNotFoundException;
import gov.nih.nci.cagrid.gridgrouper.bean.GroupCompositeType;
import gov.nih.nci.cagrid.gridgrouper.bean.GroupDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier;
import gov.nih.nci.cagrid.gridgrouper.bean.GroupUpdate;
import gov.nih.nci.cagrid.gridgrouper.bean.MemberDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.MemberFilter;
import gov.nih.nci.cagrid.gridgrouper.bean.MembershipDescriptor;
import gov.nih.nci.cagrid.gridgrouper.common.SubjectUtils;
import gov.nih.nci.cagrid.gridgrouper.grouper.GroupI;
import gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.GroupDeleteFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.GroupModifyFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.MemberAddFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.MemberDeleteFault;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella</A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @version $Id: GridGrouperBaseTreeNode.java,v 1.1 2006/08/04 03:49:26 langella
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
			this.des = gridGrouper.getClient().updateGroup(
					this.getGroupIdentifier(), update);
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
			this.des = gridGrouper.getClient().updateGroup(
					this.getGroupIdentifier(), update);
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
			this.des = gridGrouper.getClient().updateGroup(
					this.getGroupIdentifier(), update);
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

	public void addMember(Subject subj) throws InsufficientPrivilegeException,
			MemberAddException {

		try {
			gridGrouper.getClient().addMember(getGroupIdentifier(),
					subj.getId());
		} catch (InsufficientPrivilegeFault f) {
			throw new InsufficientPrivilegeException(f.getFaultString());
		} catch (MemberAddFault f) {
			throw new MemberAddException(f.getFaultString());
		} catch (GridGrouperRuntimeFault e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getFaultString());
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}

	}

	public Set getEffectiveMembers() throws GrouperRuntimeException {
		return getMembers(MemberFilter.EffectiveMembers);
	}

	public Set getImmediateMembers() throws GrouperRuntimeException {
		return getMembers(MemberFilter.ImmediateMembers);
	}

	public Set getMembers() throws GrouperRuntimeException {
		return getMembers(MemberFilter.All);
	}

	public Set getCompositeMembers() {
		return this.getMembers(MemberFilter.CompositeMembers);
	}

	private Set getMembers(MemberFilter filter) throws GrouperRuntimeException {
		try {
			MemberDescriptor[] list = gridGrouper.getClient().getMembers(
					getGroupIdentifier(), filter);
			Set members = new LinkedHashSet();
			if (list != null) {
				for (int i = 0; i < list.length; i++) {
					members.add(new Member(list[i]));
				}
			}
			return members;
		} catch (GridGrouperRuntimeFault e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getFaultString());
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}
	}

	public boolean hasEffectiveMember(Subject subj)
			throws GrouperRuntimeException {
		return hasMember(subj, MemberFilter.EffectiveMembers);
	}

	public boolean hasImmediateMember(Subject subj)
			throws GrouperRuntimeException {
		return hasMember(subj, MemberFilter.ImmediateMembers);
	}

	public boolean hasMember(Subject subj) throws GrouperRuntimeException {
		return hasMember(subj, MemberFilter.All);
	}

	private boolean hasMember(Subject member, MemberFilter filter)
			throws GrouperRuntimeException {
		try {

			return getGridGrouper().getClient().isMemberOf(
					getGroupIdentifier(), member.getId(), filter);
		} catch (GridGrouperRuntimeFault e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getFaultString());
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}
	}

	private Set getMemberships(MemberFilter filter)
			throws GrouperRuntimeException {
		try {
			MembershipDescriptor[] list = gridGrouper.getClient()
					.getMemberships(getGroupIdentifier(), filter);
			Set members = new LinkedHashSet();
			if (list != null) {
				for (int i = 0; i < list.length; i++) {
					members.add(new Membership(this.gridGrouper, list[i]));
				}
			}
			return members;
		} catch (GridGrouperRuntimeFault e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getFaultString());
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}
	}

	public Set getEffectiveMemberships() throws GrouperRuntimeException {
		return this.getMemberships(MemberFilter.EffectiveMembers);
	}

	public Set getImmediateMemberships() throws GrouperRuntimeException {
		return this.getMemberships(MemberFilter.ImmediateMembers);
	}

	public Set getMemberships() throws GrouperRuntimeException {
		return this.getMemberships(MemberFilter.All);
	}

	public Set getCompositeMemberships() {
		return this.getMemberships(MemberFilter.CompositeMembers);
	}

	public void deleteMember(Subject subj)
			throws InsufficientPrivilegeException, MemberDeleteException {
		try {
			gridGrouper.getClient().deleteMember(getGroupIdentifier(),
					subj.getId());
		} catch (InsufficientPrivilegeFault f) {
			throw new InsufficientPrivilegeException(f.getFaultString());
		} catch (MemberDeleteFault f) {
			throw new MemberDeleteException(f.getFaultString());
		} catch (GridGrouperRuntimeFault e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getFaultString());
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}

	}

	public boolean hasComposite() {
		return des.isHasComposite();
	}

	public void addCompositeMember(CompositeType type, GroupI left, GroupI right)
			throws InsufficientPrivilegeException, MemberAddException {
		try {
			GroupCompositeType ct = null;
			if (type.equals(CompositeType.UNION)) {
				ct = GroupCompositeType.Union;
			} else if (type.equals(CompositeType.INTERSECTION)) {
				ct = GroupCompositeType.Intersection;
			} else if (type.equals(CompositeType.COMPLEMENT)) {
				ct = GroupCompositeType.Complement;
			} else {
				throw new Exception("The composite type " + type.toString()
						+ " is not supported!!!");
			}
			this.des = gridGrouper.getClient().addCompositeMember(ct,
					this.getGroupIdentifier(),
					((Group) left).getGroupIdentifier(),
					((Group) right).getGroupIdentifier());
		} catch (InsufficientPrivilegeFault f) {
			throw new InsufficientPrivilegeException(f.getFaultString());
		} catch (MemberAddFault f) {
			throw new MemberAddException(f.getFaultString());
		} catch (GridGrouperRuntimeFault e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getFaultString());
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}

	}

	public boolean isComposite() {
		return des.isIsComposite();
	}

	public void deleteCompositeMember() throws InsufficientPrivilegeException,
			MemberDeleteException {
		try {
			gridGrouper.getClient().deleteCompositeMember(getGroupIdentifier());
		} catch (InsufficientPrivilegeFault f) {
			throw new InsufficientPrivilegeException(f.getFaultString());
		} catch (MemberDeleteFault f) {
			throw new MemberDeleteException(f.getFaultString());
		} catch (GridGrouperRuntimeFault e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getFaultString());
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}

	}

	public Subject toSubject() throws GrouperRuntimeException {
		try {
			return SubjectUtils.getSubject(des);
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}
	}
	
	

}
