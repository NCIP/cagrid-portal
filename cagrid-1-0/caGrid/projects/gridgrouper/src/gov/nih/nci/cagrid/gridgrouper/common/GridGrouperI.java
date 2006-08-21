package gov.nih.nci.cagrid.gridgrouper.common;

import java.rmi.RemoteException;

/**
 * This class is autogenerated, DO NOT EDIT.
 * 
 * @created by Introduce Toolkit version 1.0
 * 
 */
public interface GridGrouperI {

	public java.lang.String[] getSubjectsWithGroupPrivilege(gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier group,gov.nih.nci.cagrid.gridgrouper.bean.GroupPrivilegeType privilege) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault ;
public gov.nih.nci.cagrid.metadata.security.ServiceSecurityMetadata getServiceSecurityMetadata() throws RemoteException ;
public gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor getStem(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault ;
public gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor[] getChildStems(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier parentStem) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault ;
public gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor getParentStem(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier childStem) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault ;
public gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor updateStem(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem,gov.nih.nci.cagrid.gridgrouper.bean.StemUpdate update) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemModifyFault ;
public java.lang.String[] getSubjectsWithStemPrivilege(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem,gov.nih.nci.cagrid.gridgrouper.bean.StemPrivilegeType privilege) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault ;
public gov.nih.nci.cagrid.gridgrouper.bean.StemPrivilege[] getStemPrivileges(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem,java.lang.String subject) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault ;
public boolean hasStemPrivilege(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem,java.lang.String subject,gov.nih.nci.cagrid.gridgrouper.bean.StemPrivilegeType privilege) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault ;
public void grantStemPrivilege(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem,java.lang.String subject,gov.nih.nci.cagrid.gridgrouper.bean.StemPrivilegeType privilege) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault, gov.nih.nci.cagrid.gridgrouper.stubs.GrantPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.SchemaFault ;
public void revokeStemPrivilege(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem,java.lang.String subject,gov.nih.nci.cagrid.gridgrouper.bean.StemPrivilegeType privilege) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.RevokePrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.SchemaFault, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault ;
public gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor addChildStem(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem,java.lang.String extension,java.lang.String displayExtension) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemAddFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault ;
public void deleteStem(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemDeleteFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault ;
public gov.nih.nci.cagrid.gridgrouper.bean.GroupDescriptor getGroup(gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier group) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault ;
public gov.nih.nci.cagrid.gridgrouper.bean.GroupDescriptor[] getChildGroups(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault ;
public gov.nih.nci.cagrid.gridgrouper.bean.GroupDescriptor addChildGroup(gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier stem,java.lang.String extension,java.lang.String displayExtension) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupAddFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault ;
public void deleteGroup(gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier group) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupDeleteFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault ;
public gov.nih.nci.cagrid.gridgrouper.bean.GroupDescriptor updateGroup(gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier group,gov.nih.nci.cagrid.gridgrouper.bean.GroupUpdate update) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupModifyFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault ;
public void addMember(gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier group,java.lang.String subject) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.MemberAddFault ;
public gov.nih.nci.cagrid.gridgrouper.bean.MemberDescriptor[] getMembers(gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier group,gov.nih.nci.cagrid.gridgrouper.bean.MemberFilter filter) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault ;
public boolean isMemberOf(gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier group,java.lang.String member,gov.nih.nci.cagrid.gridgrouper.bean.MemberFilter filter) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault ;
public gov.nih.nci.cagrid.gridgrouper.bean.MembershipDescriptor[] getMemberships(gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier group,gov.nih.nci.cagrid.gridgrouper.bean.MemberFilter filter) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault ;
public void deleteMember(gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier group,java.lang.String member) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault, gov.nih.nci.cagrid.gridgrouper.stubs.MemberDeleteFault ;
public gov.nih.nci.cagrid.gridgrouper.bean.GroupDescriptor addCompositeMember(gov.nih.nci.cagrid.gridgrouper.bean.GroupCompositeType type,gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier composite,gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier left,gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier right) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault, gov.nih.nci.cagrid.gridgrouper.stubs.MemberAddFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault ;
public gov.nih.nci.cagrid.gridgrouper.bean.GroupDescriptor deleteCompositeMember(gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier group) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.MemberDeleteFault ;
public void grantGroupPrivilege(gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier group,java.lang.String subject,gov.nih.nci.cagrid.gridgrouper.bean.GroupPrivilegeType privilege) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault, gov.nih.nci.cagrid.gridgrouper.stubs.GrantPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault ;
public void revokeGroupPrivilege(gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier group,java.lang.String subject,gov.nih.nci.cagrid.gridgrouper.bean.GroupPrivilegeType privilege) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault, gov.nih.nci.cagrid.gridgrouper.stubs.RevokePrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.SchemaFault ;

}
