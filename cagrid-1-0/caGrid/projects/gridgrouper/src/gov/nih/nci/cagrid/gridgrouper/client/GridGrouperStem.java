package gov.nih.nci.cagrid.gridgrouper.client;

import edu.internet2.middleware.grouper.GrantPrivilegeException;
import edu.internet2.middleware.grouper.GroupAddException;
import edu.internet2.middleware.grouper.GrouperRuntimeException;
import edu.internet2.middleware.grouper.InsufficientPrivilegeException;
import edu.internet2.middleware.grouper.NamingPrivilege;
import edu.internet2.middleware.grouper.Privilege;
import edu.internet2.middleware.grouper.RevokePrivilegeException;
import edu.internet2.middleware.grouper.SchemaException;
import edu.internet2.middleware.grouper.StemAddException;
import edu.internet2.middleware.grouper.StemDeleteException;
import edu.internet2.middleware.grouper.StemModifyException;
import edu.internet2.middleware.grouper.StemNotFoundException;
import edu.internet2.middleware.subject.Subject;
import edu.internet2.middleware.subject.SubjectNotFoundException;
import gov.nih.nci.cagrid.gridgrouper.bean.GroupDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier;
import gov.nih.nci.cagrid.gridgrouper.bean.StemPrivilegeType;
import gov.nih.nci.cagrid.gridgrouper.common.SubjectUtils;
import gov.nih.nci.cagrid.gridgrouper.grouper.Group;
import gov.nih.nci.cagrid.gridgrouper.grouper.Stem;
import gov.nih.nci.cagrid.gridgrouper.stubs.GrantPrivilegeFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.GroupAddFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.SchemaFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.StemAddFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.StemDeleteFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.StemModifyFault;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class GridGrouperStem extends GridGrouperObject implements Stem {

	private StemDescriptor des;

	private GridGrouper gridGrouper;

	protected GridGrouperStem(GridGrouper gridGrouper, StemDescriptor des) {
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

	public StemIdentifier getStemIdentifier() {
		return gridGrouper.getStemIdentifier(getName());
	}

	public String getName() {
		return des.getName();
	}

	public String getUuid() {
		return des.getUUID();
	}

	public Set getChildStems() {
		return gridGrouper.getChildStems(getName());
	}

	public Stem getParentStem() throws StemNotFoundException {
		return gridGrouper.getParentStem(getName());
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append("displayName", getDisplayName()).append("name",
						getName()).append("uuid", getUuid()).append("created",
						getCreateTime()).append("modified", getModifyTime())
				.toString();
	}

	public void setDescription(String value)
			throws InsufficientPrivilegeException, StemModifyException {
		try {
			this.des = gridGrouper.getClient().updateStemDescription(
					getStemIdentifier(), value);
		} catch (InsufficientPrivilegeFault f) {
			throw new InsufficientPrivilegeException(f.getFaultString());
		} catch (StemModifyFault f) {
			throw new StemModifyException(f.getFaultString());
		} catch (GridGrouperRuntimeFault e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getFaultString());
		}catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}

	}

	public Set getCreators() {
		try {
			return gridGrouper.getSubjectsWithStemPrivilege(getName(),
					Privilege.getInstance(StemPrivilegeType.create.getValue()));
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}
	}

	public Set getPrivs(Subject subj) {
		try {
			return gridGrouper.getStemPrivileges(getName(), subj);
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}
	}

	public Set getStemmers() {
		try {
			return gridGrouper.getSubjectsWithStemPrivilege(getName(),
					Privilege.getInstance(StemPrivilegeType.stem.getValue()));
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}
	}

	public void setDisplayExtension(String value)
			throws InsufficientPrivilegeException, StemModifyException {
		try {
			this.des = gridGrouper.getClient().updateStemDisplayExtension(
					getStemIdentifier(), value);
		} catch (InsufficientPrivilegeFault f) {
			throw new InsufficientPrivilegeException(f.getFaultString());
		} catch (StemModifyFault f) {
			throw new StemModifyException(f.getFaultString());
		} catch (GridGrouperRuntimeFault e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getFaultString());
		}catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}

	}

	public boolean hasCreate(Subject subj) {
		try {
			return gridGrouper.hasStemPrivilege(getName(), subj,
					NamingPrivilege.CREATE);
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}
	}

	public boolean hasStem(Subject subj) {
		try {
			return gridGrouper.hasStemPrivilege(getName(), subj,
					NamingPrivilege.STEM);
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}
	}

	public void grantPriv(Subject subj, Privilege priv)
			throws GrantPrivilegeException, InsufficientPrivilegeException,
			SchemaException {
		try {
			StemPrivilegeType type = StemPrivilegeType
					.fromValue(priv.getName());
			gridGrouper.getClient().grantStemPrivilege(getStemIdentifier(),
					subj.getId(), type);
		} catch (InsufficientPrivilegeFault f) {
			throw new InsufficientPrivilegeException(f.getFaultString());
		} catch (GrantPrivilegeFault f) {
			throw new GrantPrivilegeException(f.getFaultString());
		} catch (SchemaFault f) {
			throw new SchemaException(f.getFaultString());
		} catch (GridGrouperRuntimeFault e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getFaultString());
		}catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}

	}

	public void revokePriv(Subject subj, Privilege priv)
			throws InsufficientPrivilegeException, RevokePrivilegeException,
			SchemaException {
		try {
			StemPrivilegeType type = StemPrivilegeType
					.fromValue(priv.getName());
			gridGrouper.getClient().revokeStemPrivilege(getStemIdentifier(),
					subj.getId(), type);
		} catch (InsufficientPrivilegeFault f) {
			throw new InsufficientPrivilegeException(f.getFaultString());
		} catch (GrantPrivilegeFault f) {
			throw new RevokePrivilegeException(f.getFaultString());
		} catch (SchemaFault f) {
			throw new SchemaException(f.getFaultString());
		} catch (GridGrouperRuntimeFault e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getFaultString());
		}catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}
	}

	public Stem addChildStem(String extension, String displayExtension)
			throws InsufficientPrivilegeException, StemAddException {
		try {
			StemDescriptor stem = gridGrouper.getClient().addChildStem(
					this.getStemIdentifier(), extension, displayExtension);
			return new GridGrouperStem(this.gridGrouper, stem);
		} catch (InsufficientPrivilegeFault f) {
			throw new InsufficientPrivilegeException(f.getFaultString());
		} catch (StemAddFault f) {
			throw new StemAddException(f.getFaultString());
		}catch (GridGrouperRuntimeFault e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getFaultString());
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}
	}

	public void delete() throws InsufficientPrivilegeException,
			StemDeleteException {
		try {
			gridGrouper.getClient().deleteStem(this.getStemIdentifier());
		} catch (InsufficientPrivilegeFault f) {
			throw new InsufficientPrivilegeException(f.getFaultString());
		} catch (StemDeleteFault f) {
			throw new StemDeleteException(f.getFaultString());
		}catch (GridGrouperRuntimeFault e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getFaultString());
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}
	}

	public Group addChildGroup(String extension, String displayExtension)
			throws GroupAddException, InsufficientPrivilegeException {
		try {
			GroupDescriptor grp = gridGrouper.getClient().addChildGroup(
					this.getStemIdentifier(), extension, displayExtension);
			return new GridGrouperGroup(this.gridGrouper, grp);
		} catch (InsufficientPrivilegeFault f) {
			throw new InsufficientPrivilegeException(f.getFaultString());
		} catch (GroupAddFault f) {
			throw new GroupAddException(f.getFaultString());
		} catch (GridGrouperRuntimeFault e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getFaultString());
		}catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}
	}

	public Set getChildGroups() {
		try {
			GroupDescriptor[] children = gridGrouper.getClient()
					.getChildGroups(getStemIdentifier());
			Set set = new HashSet();
			if (children != null) {
				for (int i = 0; i < children.length; i++) {
					set.add(new GridGrouperGroup(gridGrouper, children[i]));
				}
			}
			return set;
		} catch (GridGrouperRuntimeFault e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getFaultString());
		}catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}
	}

	public GridGrouper getGridGrouper() {
		return gridGrouper;
	}

}
