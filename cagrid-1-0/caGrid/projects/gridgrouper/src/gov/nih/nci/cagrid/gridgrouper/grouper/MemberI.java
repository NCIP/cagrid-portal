package gov.nih.nci.cagrid.gridgrouper.grouper;

import edu.internet2.middleware.grouper.GrouperRuntimeException;
import edu.internet2.middleware.subject.Source;
import edu.internet2.middleware.subject.SubjectType;

public interface MemberI {
	
	//Descriptors
	
	public String getSubjectId();

	public Source getSubjectSource() throws GrouperRuntimeException;

	public String getSubjectSourceId();

	public SubjectType getSubjectType();

	public String getSubjectTypeId();

	public String getUuid();
	
	//Actions

//	public boolean canAdmin(Group g) throws IllegalArgumentException;
//
//	public boolean canCreate(StemI ns) throws IllegalArgumentException;
//
//	public boolean canOptin(Group g) throws IllegalArgumentException;
//
//	public boolean canOptout(Group g) throws IllegalArgumentException;
//
//	public boolean canRead(Group g) throws IllegalArgumentException;
//
//	public boolean canStem(StemI ns) throws IllegalArgumentException;
//
//	public boolean canUpdate(Group g) throws IllegalArgumentException;
//
//	public boolean canView(Group g) throws IllegalArgumentException;
//
//	public Set getEffectiveGroups();
//
//	public Set getEffectiveMemberships() throws GrouperRuntimeException;
//
//	public Set getEffectiveMemberships(FieldI f) throws SchemaException;
//
//	public Set getGroups();
//
//	public Set getImmediateGroups();
//
//	public Set getImmediateMemberships() throws GrouperRuntimeException;
//
//	public Set getImmediateMemberships(FieldI f) throws SchemaException;
//
//	public Set getMemberships() throws GrouperRuntimeException;
//
//	public Set getMemberships(FieldI f) throws SchemaException;
//
//	public Set getPrivs(Group g);
//
//	public Set getPrivs(StemI ns);
//
//	public Subject getSubject() throws SubjectNotFoundException;
//
//	
//
//	public Set hasAdmin() throws GrouperRuntimeException;
//
//	public boolean hasAdmin(Group g);
//
//	public Set hasCreate() throws GrouperRuntimeException;
//
//	public boolean hasCreate(StemI ns);
//
//	public Set hasOptin() throws GrouperRuntimeException;
//
//	public boolean hasOptin(Group g);
//
//	public Set hasOptout() throws GrouperRuntimeException;
//
//	public boolean hasOptout(Group g);
//
//	public Set hasRead() throws GrouperRuntimeException;
//
//	public boolean hasRead(Group g);
//
//	public Set hasStem() throws GrouperRuntimeException;
//
//	public boolean hasStem(StemI ns);
//
//	public Set hasUpdate() throws GrouperRuntimeException;
//
//	public boolean hasUpdate(Group g);
//
//	public Set hasView() throws GrouperRuntimeException;
//
//	public boolean hasView(Group g);
//
//	public boolean isEffectiveMember(Group g) throws GrouperRuntimeException;
//
//	public boolean isEffectiveMember(Group g, FieldI f) throws SchemaException;
//
//	public boolean isImmediateMember(Group g) throws GrouperRuntimeException;
//
//	public boolean isImmediateMember(Group g, FieldI f) throws SchemaException;
//
//	public boolean isMember(Group g) throws GrouperRuntimeException;
//
//	public boolean isMember(Group g, FieldI f) throws SchemaException;
//
//	public void setSubjectId(String id) throws InsufficientPrivilegeException;
}
