package gov.nih.nci.cagrid.gridgrouper.grouper;

import java.util.Date;

import edu.internet2.middleware.grouper.GroupDeleteException;
import edu.internet2.middleware.grouper.GroupModifyException;
import edu.internet2.middleware.grouper.GrouperRuntimeException;
import edu.internet2.middleware.grouper.InsufficientPrivilegeException;
import edu.internet2.middleware.subject.Subject;
import edu.internet2.middleware.subject.SubjectNotFoundException;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public interface GroupI {

	// Property Methods
	
	public String getUuid();
	
	public String getName() throws GrouperRuntimeException;
	
	public String getDisplayName() throws GrouperRuntimeException;
	
	public String getExtension();
	
	public String getDisplayExtension() throws GrouperRuntimeException;
	
	public String getDescription();
	
	public String getCreateSource();

	public Subject getCreateSubject() throws SubjectNotFoundException;

	public Date getCreateTime();

	public String getModifySource();

	public Subject getModifySubject() throws SubjectNotFoundException;

	public Date getModifyTime();

	
	
	

	// Action Methods
	
	public void delete() throws GroupDeleteException,
	InsufficientPrivilegeException;

	public void setDescription(String value) throws GroupModifyException,
			InsufficientPrivilegeException;

	public void setExtension(String value) throws GroupModifyException,
			InsufficientPrivilegeException;

	public void setDisplayExtension(String value) throws GroupModifyException,
			InsufficientPrivilegeException;
/*
	public Set getOptins() throws GrouperRuntimeException;

	public Set getOptouts() throws GrouperRuntimeException;

	public Stem getParentStem();

	public Set getPrivs(Subject subj);

	public Set getReaders() throws GrouperRuntimeException;

	public void addCompositeMember(CompositeType type, Group left, Group right)
			throws InsufficientPrivilegeException, MemberAddException;

	public void addMember(Subject subj) throws InsufficientPrivilegeException,
			MemberAddException;

	public void addMember(Subject subj, Field f)
			throws InsufficientPrivilegeException, MemberAddException,
			SchemaException;

	public boolean canReadField(Field f) throws IllegalArgumentException,
			SchemaException;

	public boolean canReadField(Subject subj, Field f)
			throws IllegalArgumentException, SchemaException;

	public boolean canWriteField(Field f) throws IllegalArgumentException,
			SchemaException;

	public boolean canWriteField(Subject subj, Field f)
			throws IllegalArgumentException, SchemaException;

	

	public void deleteCompositeMember() throws InsufficientPrivilegeException,
			MemberDeleteException;

	public void deleteMember(Subject subj)
			throws InsufficientPrivilegeException, MemberDeleteException;

	public void deleteMember(Subject subj, Field f)
			throws InsufficientPrivilegeException, MemberDeleteException,
			SchemaException;

	public Set getAdmins() throws GrouperRuntimeException;

	public Set getCompositeMembers();

	public Set getCompositeMemberships();

	public Set getEffectiveMembers() throws GrouperRuntimeException;

	public Set getEffectiveMembers(Field f) throws SchemaException;

	public Set getEffectiveMemberships() throws GrouperRuntimeException;

	public Set getEffectiveMemberships(Field f) throws SchemaException;

	public Set getImmediateMembers() throws GrouperRuntimeException;

	public Set getImmediateMembers(Field f) throws SchemaException;

	public Set getImmediateMemberships() throws GrouperRuntimeException;

	public Set getImmediateMemberships(Field f) throws SchemaException;

	public Set getMembers() throws GrouperRuntimeException;

	public Set getMembers(Field f) throws SchemaException;

	public Set getMemberships() throws GrouperRuntimeException;

	public Set getMemberships(Field f) throws SchemaException;

	public Set getUpdaters() throws GrouperRuntimeException;

	public Set getViewers() throws GrouperRuntimeException;

	public void grantPriv(Subject subj, Privilege priv)
			throws GrantPrivilegeException, InsufficientPrivilegeException,
			SchemaException;

	public boolean hasAdmin(Subject subj);

	public boolean hasComposite();

	public boolean hasEffectiveMember(Subject subj)
			throws GrouperRuntimeException;

	public boolean hasEffectiveMember(Subject subj, Field f)
			throws SchemaException;

	public boolean hasImmediateMember(Subject subj)
			throws GrouperRuntimeException;

	public boolean hasImmediateMember(Subject subj, Field f)
			throws SchemaException;

	public boolean hasMember(Subject subj) throws GrouperRuntimeException;

	public boolean hasMember(Subject subj, Field f) throws SchemaException;

	public boolean hasOptin(Subject subj);

	public boolean hasOptout(Subject subj);

	public boolean hasRead(Subject subj);

	public boolean hasUpdate(Subject subj);

	public boolean hasView(Subject subj);

	public boolean isComposite();

	public void revokePriv(Privilege priv)
			throws InsufficientPrivilegeException, RevokePrivilegeException,
			SchemaException;

	public void revokePriv(Subject subj, Privilege priv)
			throws InsufficientPrivilegeException, RevokePrivilegeException,
			SchemaException;

	// Not sure if we will support
	public void addType(GroupType type) throws GroupModifyException,
			InsufficientPrivilegeException, SchemaException;

	public void deleteType(GroupType type) throws GroupModifyException,
			InsufficientPrivilegeException, SchemaException;

	public Set getRemovableTypes();

	public Set getTypes();

	public boolean hasType(GroupType type);

	public void deleteAttribute(String attr) throws AttributeNotFoundException,
			GroupModifyException, InsufficientPrivilegeException;

	public String getAttribute(String attr) throws AttributeNotFoundException;

	public Map getAttributes();

	public void setAttribute(String attr, String value)
			throws AttributeNotFoundException, GroupModifyException,
			InsufficientPrivilegeException;

	public Subject toSubject() throws GrouperRuntimeException;*/
}
