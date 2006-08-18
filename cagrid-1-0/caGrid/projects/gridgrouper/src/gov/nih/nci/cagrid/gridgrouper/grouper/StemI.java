package gov.nih.nci.cagrid.gridgrouper.grouper;

import java.util.Date;
import java.util.Set;

import edu.internet2.middleware.grouper.GrantPrivilegeException;
import edu.internet2.middleware.grouper.GroupAddException;
import edu.internet2.middleware.grouper.InsufficientPrivilegeException;
import edu.internet2.middleware.grouper.Privilege;
import edu.internet2.middleware.grouper.RevokePrivilegeException;
import edu.internet2.middleware.grouper.SchemaException;
import edu.internet2.middleware.grouper.StemAddException;
import edu.internet2.middleware.grouper.StemDeleteException;
import edu.internet2.middleware.grouper.StemModifyException;
import edu.internet2.middleware.grouper.StemNotFoundException;
import edu.internet2.middleware.subject.Subject;
import edu.internet2.middleware.subject.SubjectNotFoundException;
import gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A href="mailto:ervin@bmi.osu.edu">David Ervin </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public interface StemI {

	// Stem Information

	public String getCreateSource();

	public Subject getCreateSubject() throws SubjectNotFoundException;

	public Date getCreateTime();

	public String getDescription();

	public String getDisplayExtension();

	public String getDisplayName();

	public String getExtension();

	public String getModifySource();

	public Subject getModifySubject() throws SubjectNotFoundException;

	public Date getModifyTime();

	public String getName();

	public String getUuid();

	public StemIdentifier getStemIdentifier();

	// Stem Actions
	public Set getChildStems();

	public StemI getParentStem() throws StemNotFoundException;

	public void setDescription(String value)
			throws InsufficientPrivilegeException, StemModifyException;

	public void setDisplayExtension(String value)
			throws InsufficientPrivilegeException, StemModifyException;

	public Set getCreators();

	public Set getPrivs(Subject subj);

	public Set getStemmers();

	public boolean hasCreate(Subject subj);

	public boolean hasStem(Subject subj);

	public void grantPriv(Subject subj, Privilege priv)
			throws GrantPrivilegeException, InsufficientPrivilegeException,
			SchemaException;

	public void revokePriv(Subject subj, Privilege priv)
			throws InsufficientPrivilegeException, RevokePrivilegeException,
			SchemaException;

	public StemI addChildStem(String extension, String displayExtension)
			throws InsufficientPrivilegeException, StemAddException;

	public void delete() throws InsufficientPrivilegeException,
			StemDeleteException;

	public GroupI addChildGroup(String extension, String displayExtension)
			throws GroupAddException, InsufficientPrivilegeException;

	public Set getChildGroups();
}
