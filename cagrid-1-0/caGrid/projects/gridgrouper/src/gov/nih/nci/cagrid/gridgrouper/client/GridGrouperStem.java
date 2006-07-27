package gov.nih.nci.cagrid.gridgrouper.client;

import edu.internet2.middleware.grouper.GrouperRuntimeException;
import edu.internet2.middleware.grouper.InsufficientPrivilegeException;
import edu.internet2.middleware.grouper.StemModifyException;
import edu.internet2.middleware.grouper.StemNotFoundException;
import edu.internet2.middleware.subject.Subject;
import edu.internet2.middleware.subject.SubjectNotFoundException;
import gov.nih.nci.cagrid.gridgrouper.beans.StemDescriptor;
import gov.nih.nci.cagrid.gridgrouper.grouper.Stem;
import gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.StemModifyFault;

import java.util.Date;
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
			this.des = gridGrouper.getClient().updateStemDescription(getName(),
					value);
		} catch (InsufficientPrivilegeFault f) {
			throw new InsufficientPrivilegeException(f.getFaultString());
		} catch (StemModifyFault f) {
			throw new StemModifyException(f.getFaultString());
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}

	}

	public void setDisplayExtension(String value)
			throws InsufficientPrivilegeException, StemModifyException {
		try {
			this.des = gridGrouper.getClient().updateStemDisplayExtension(
					getName(), value);
		} catch (InsufficientPrivilegeFault f) {
			throw new InsufficientPrivilegeException(f.getFaultString());
		} catch (StemModifyFault f) {
			throw new StemModifyException(f.getFaultString());
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new GrouperRuntimeException(e.getMessage());
		}

	}

}
