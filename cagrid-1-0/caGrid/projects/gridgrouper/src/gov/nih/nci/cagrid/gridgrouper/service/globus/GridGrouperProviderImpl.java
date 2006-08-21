package gov.nih.nci.cagrid.gridgrouper.service.globus;

import gov.nih.nci.cagrid.gridgrouper.service.GridGrouperImpl;

import java.rmi.RemoteException;

/** 
 *  DO NOT EDIT:  This class is autogenerated!
 * 
 * @created by Introduce Toolkit version 1.0
 * 
 */
public class GridGrouperProviderImpl{
	
	GridGrouperImpl impl;
	
	public GridGrouperProviderImpl() throws RemoteException {
		impl = new GridGrouperImpl();
	}
	

	public gov.nih.nci.cagrid.gridgrouper.stubs.RevokeGroupPrivilegeResponse revokeGroupPrivilege(gov.nih.nci.cagrid.gridgrouper.stubs.RevokeGroupPrivilegeRequest params) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault, gov.nih.nci.cagrid.gridgrouper.stubs.RevokePrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.SchemaFault {
		gov.nih.nci.cagrid.gridgrouper.stubs.RevokeGroupPrivilegeResponse boxedResult = new gov.nih.nci.cagrid.gridgrouper.stubs.RevokeGroupPrivilegeResponse();
		impl.revokeGroupPrivilege(params.getGroup().getGroupIdentifier(),params.getSubject().getSubjectIdentifier(),params.getPrivilege().getGroupPrivilegeType());
		return boxedResult;
	}

	public gov.nih.nci.cagrid.gridgrouper.stubs.GetStemResponse getStem(gov.nih.nci.cagrid.gridgrouper.stubs.GetStemRequest params) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
		gov.nih.nci.cagrid.gridgrouper.stubs.GetStemResponse boxedResult = new gov.nih.nci.cagrid.gridgrouper.stubs.GetStemResponse();
		boxedResult.setStemDescriptor(impl.getStem(params.getStem().getStemIdentifier()));
		return boxedResult;
	}

	public gov.nih.nci.cagrid.gridgrouper.stubs.GetChildStemsResponse getChildStems(gov.nih.nci.cagrid.gridgrouper.stubs.GetChildStemsRequest params) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
		gov.nih.nci.cagrid.gridgrouper.stubs.GetChildStemsResponse boxedResult = new gov.nih.nci.cagrid.gridgrouper.stubs.GetChildStemsResponse();
		boxedResult.setStemDescriptor(impl.getChildStems(params.getParentStem().getStemIdentifier()));
		return boxedResult;
	}

	public gov.nih.nci.cagrid.gridgrouper.stubs.GetParentStemResponse getParentStem(gov.nih.nci.cagrid.gridgrouper.stubs.GetParentStemRequest params) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
		gov.nih.nci.cagrid.gridgrouper.stubs.GetParentStemResponse boxedResult = new gov.nih.nci.cagrid.gridgrouper.stubs.GetParentStemResponse();
		boxedResult.setStemDescriptor(impl.getParentStem(params.getChildStem().getStemIdentifier()));
		return boxedResult;
	}

	public gov.nih.nci.cagrid.gridgrouper.stubs.UpdateStemResponse updateStem(gov.nih.nci.cagrid.gridgrouper.stubs.UpdateStemRequest params) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemModifyFault {
		gov.nih.nci.cagrid.gridgrouper.stubs.UpdateStemResponse boxedResult = new gov.nih.nci.cagrid.gridgrouper.stubs.UpdateStemResponse();
		boxedResult.setStemDescriptor(impl.updateStem(params.getStem().getStemIdentifier(),params.getUpdate().getStemUpdate()));
		return boxedResult;
	}

	public gov.nih.nci.cagrid.gridgrouper.stubs.GetSubjectsWithStemPrivilegeResponse getSubjectsWithStemPrivilege(gov.nih.nci.cagrid.gridgrouper.stubs.GetSubjectsWithStemPrivilegeRequest params) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
		gov.nih.nci.cagrid.gridgrouper.stubs.GetSubjectsWithStemPrivilegeResponse boxedResult = new gov.nih.nci.cagrid.gridgrouper.stubs.GetSubjectsWithStemPrivilegeResponse();
		boxedResult.setSubjectIdentifier(impl.getSubjectsWithStemPrivilege(params.getStem().getStemIdentifier(),params.getPrivilege().getStemPrivilegeType()));
		return boxedResult;
	}

	public gov.nih.nci.cagrid.gridgrouper.stubs.GetStemPrivilegesResponse getStemPrivileges(gov.nih.nci.cagrid.gridgrouper.stubs.GetStemPrivilegesRequest params) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
		gov.nih.nci.cagrid.gridgrouper.stubs.GetStemPrivilegesResponse boxedResult = new gov.nih.nci.cagrid.gridgrouper.stubs.GetStemPrivilegesResponse();
		boxedResult.setStemPrivilege(impl.getStemPrivileges(params.getStem().getStemIdentifier(),params.getSubject().getSubjectIdentifier()));
		return boxedResult;
	}

	public gov.nih.nci.cagrid.gridgrouper.stubs.HasStemPrivilegeResponse hasStemPrivilege(gov.nih.nci.cagrid.gridgrouper.stubs.HasStemPrivilegeRequest params) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
		gov.nih.nci.cagrid.gridgrouper.stubs.HasStemPrivilegeResponse boxedResult = new gov.nih.nci.cagrid.gridgrouper.stubs.HasStemPrivilegeResponse();
		boxedResult.setResponse(impl.hasStemPrivilege(params.getStem().getStemIdentifier(),params.getSubject().getSubjectIdentifier(),params.getPrivilege().getStemPrivilegeType()));
		return boxedResult;
	}

	public gov.nih.nci.cagrid.gridgrouper.stubs.GrantStemPrivilegeResponse grantStemPrivilege(gov.nih.nci.cagrid.gridgrouper.stubs.GrantStemPrivilegeRequest params) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault, gov.nih.nci.cagrid.gridgrouper.stubs.GrantPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.SchemaFault {
		gov.nih.nci.cagrid.gridgrouper.stubs.GrantStemPrivilegeResponse boxedResult = new gov.nih.nci.cagrid.gridgrouper.stubs.GrantStemPrivilegeResponse();
		impl.grantStemPrivilege(params.getStem().getStemIdentifier(),params.getSubject().getSubjectIdentifier(),params.getPrivilege().getStemPrivilegeType());
		return boxedResult;
	}

	public gov.nih.nci.cagrid.gridgrouper.stubs.RevokeStemPrivilegeResponse revokeStemPrivilege(gov.nih.nci.cagrid.gridgrouper.stubs.RevokeStemPrivilegeRequest params) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.RevokePrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.SchemaFault, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault {
		gov.nih.nci.cagrid.gridgrouper.stubs.RevokeStemPrivilegeResponse boxedResult = new gov.nih.nci.cagrid.gridgrouper.stubs.RevokeStemPrivilegeResponse();
		impl.revokeStemPrivilege(params.getStem().getStemIdentifier(),params.getSubject().getSubjectIdentifier(),params.getPrivilege().getStemPrivilegeType());
		return boxedResult;
	}

	public gov.nih.nci.cagrid.gridgrouper.stubs.AddChildStemResponse addChildStem(gov.nih.nci.cagrid.gridgrouper.stubs.AddChildStemRequest params) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemAddFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
		gov.nih.nci.cagrid.gridgrouper.stubs.AddChildStemResponse boxedResult = new gov.nih.nci.cagrid.gridgrouper.stubs.AddChildStemResponse();
		boxedResult.setStemDescriptor(impl.addChildStem(params.getStem().getStemIdentifier(),params.getExtension(),params.getDisplayExtension()));
		return boxedResult;
	}

	public gov.nih.nci.cagrid.gridgrouper.stubs.DeleteStemResponse deleteStem(gov.nih.nci.cagrid.gridgrouper.stubs.DeleteStemRequest params) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemDeleteFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
		gov.nih.nci.cagrid.gridgrouper.stubs.DeleteStemResponse boxedResult = new gov.nih.nci.cagrid.gridgrouper.stubs.DeleteStemResponse();
		impl.deleteStem(params.getStem().getStemIdentifier());
		return boxedResult;
	}

	public gov.nih.nci.cagrid.gridgrouper.stubs.GetGroupResponse getGroup(gov.nih.nci.cagrid.gridgrouper.stubs.GetGroupRequest params) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault {
		gov.nih.nci.cagrid.gridgrouper.stubs.GetGroupResponse boxedResult = new gov.nih.nci.cagrid.gridgrouper.stubs.GetGroupResponse();
		boxedResult.setGroupDescriptor(impl.getGroup(params.getGroup().getGroupIdentifier()));
		return boxedResult;
	}

	public gov.nih.nci.cagrid.gridgrouper.stubs.GetChildGroupsResponse getChildGroups(gov.nih.nci.cagrid.gridgrouper.stubs.GetChildGroupsRequest params) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.StemNotFoundFault {
		gov.nih.nci.cagrid.gridgrouper.stubs.GetChildGroupsResponse boxedResult = new gov.nih.nci.cagrid.gridgrouper.stubs.GetChildGroupsResponse();
		boxedResult.setGroupDescriptor(impl.getChildGroups(params.getStem().getStemIdentifier()));
		return boxedResult;
	}

	public gov.nih.nci.cagrid.gridgrouper.stubs.AddChildGroupResponse addChildGroup(gov.nih.nci.cagrid.gridgrouper.stubs.AddChildGroupRequest params) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupAddFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault {
		gov.nih.nci.cagrid.gridgrouper.stubs.AddChildGroupResponse boxedResult = new gov.nih.nci.cagrid.gridgrouper.stubs.AddChildGroupResponse();
		boxedResult.setGroupDescriptor(impl.addChildGroup(params.getStem().getStemIdentifier(),params.getExtension(),params.getDisplayExtension()));
		return boxedResult;
	}

	public gov.nih.nci.cagrid.gridgrouper.stubs.DeleteGroupResponse deleteGroup(gov.nih.nci.cagrid.gridgrouper.stubs.DeleteGroupRequest params) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupDeleteFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault {
		gov.nih.nci.cagrid.gridgrouper.stubs.DeleteGroupResponse boxedResult = new gov.nih.nci.cagrid.gridgrouper.stubs.DeleteGroupResponse();
		impl.deleteGroup(params.getGroup().getGroupIdentifier());
		return boxedResult;
	}

	public gov.nih.nci.cagrid.gridgrouper.stubs.UpdateGroupResponse updateGroup(gov.nih.nci.cagrid.gridgrouper.stubs.UpdateGroupRequest params) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupModifyFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault {
		gov.nih.nci.cagrid.gridgrouper.stubs.UpdateGroupResponse boxedResult = new gov.nih.nci.cagrid.gridgrouper.stubs.UpdateGroupResponse();
		boxedResult.setGroupDescriptor(impl.updateGroup(params.getGroup().getGroupIdentifier(),params.getUpdate().getGroupUpdate()));
		return boxedResult;
	}

	public gov.nih.nci.cagrid.gridgrouper.stubs.AddMemberResponse addMember(gov.nih.nci.cagrid.gridgrouper.stubs.AddMemberRequest params) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.MemberAddFault {
		gov.nih.nci.cagrid.gridgrouper.stubs.AddMemberResponse boxedResult = new gov.nih.nci.cagrid.gridgrouper.stubs.AddMemberResponse();
		impl.addMember(params.getGroup().getGroupIdentifier(),params.getSubject().getSubjectIdentifier());
		return boxedResult;
	}

	public gov.nih.nci.cagrid.gridgrouper.stubs.GetMembersResponse getMembers(gov.nih.nci.cagrid.gridgrouper.stubs.GetMembersRequest params) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault {
		gov.nih.nci.cagrid.gridgrouper.stubs.GetMembersResponse boxedResult = new gov.nih.nci.cagrid.gridgrouper.stubs.GetMembersResponse();
		boxedResult.setMemberDescriptor(impl.getMembers(params.getGroup().getGroupIdentifier(),params.getFilter().getMemberFilter()));
		return boxedResult;
	}

	public gov.nih.nci.cagrid.gridgrouper.stubs.IsMemberOfResponse isMemberOf(gov.nih.nci.cagrid.gridgrouper.stubs.IsMemberOfRequest params) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault {
		gov.nih.nci.cagrid.gridgrouper.stubs.IsMemberOfResponse boxedResult = new gov.nih.nci.cagrid.gridgrouper.stubs.IsMemberOfResponse();
		boxedResult.setResponse(impl.isMemberOf(params.getGroup().getGroupIdentifier(),params.getMember().getSubjectIdentifier(),params.getFilter().getMemberFilter()));
		return boxedResult;
	}

	public gov.nih.nci.cagrid.gridgrouper.stubs.GetMembershipsResponse getMemberships(gov.nih.nci.cagrid.gridgrouper.stubs.GetMembershipsRequest params) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault {
		gov.nih.nci.cagrid.gridgrouper.stubs.GetMembershipsResponse boxedResult = new gov.nih.nci.cagrid.gridgrouper.stubs.GetMembershipsResponse();
		boxedResult.setMembershipDescriptor(impl.getMemberships(params.getGroup().getGroupIdentifier(),params.getFilter().getMemberFilter()));
		return boxedResult;
	}

	public gov.nih.nci.cagrid.gridgrouper.stubs.DeleteMemberResponse deleteMember(gov.nih.nci.cagrid.gridgrouper.stubs.DeleteMemberRequest params) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault, gov.nih.nci.cagrid.gridgrouper.stubs.MemberDeleteFault {
		gov.nih.nci.cagrid.gridgrouper.stubs.DeleteMemberResponse boxedResult = new gov.nih.nci.cagrid.gridgrouper.stubs.DeleteMemberResponse();
		impl.deleteMember(params.getGroup().getGroupIdentifier(),params.getMember().getSubjectIdentifier());
		return boxedResult;
	}

	public gov.nih.nci.cagrid.gridgrouper.stubs.AddCompositeMemberResponse addCompositeMember(gov.nih.nci.cagrid.gridgrouper.stubs.AddCompositeMemberRequest params) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault, gov.nih.nci.cagrid.gridgrouper.stubs.MemberAddFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault {
		gov.nih.nci.cagrid.gridgrouper.stubs.AddCompositeMemberResponse boxedResult = new gov.nih.nci.cagrid.gridgrouper.stubs.AddCompositeMemberResponse();
		boxedResult.setGroupDescriptor(impl.addCompositeMember(params.getType().getGroupCompositeType(),params.getComposite().getGroupIdentifier(),params.getLeft().getGroupIdentifier(),params.getRight().getGroupIdentifier()));
		return boxedResult;
	}

	public gov.nih.nci.cagrid.gridgrouper.stubs.DeleteCompositeMemberResponse deleteCompositeMember(gov.nih.nci.cagrid.gridgrouper.stubs.DeleteCompositeMemberRequest params) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.MemberDeleteFault {
		gov.nih.nci.cagrid.gridgrouper.stubs.DeleteCompositeMemberResponse boxedResult = new gov.nih.nci.cagrid.gridgrouper.stubs.DeleteCompositeMemberResponse();
		boxedResult.setGroupDescriptor(impl.deleteCompositeMember(params.getGroup().getGroupIdentifier()));
		return boxedResult;
	}

	public gov.nih.nci.cagrid.gridgrouper.stubs.GrantGroupPrivilegeResponse grantGroupPrivilege(gov.nih.nci.cagrid.gridgrouper.stubs.GrantGroupPrivilegeRequest params) throws RemoteException, gov.nih.nci.cagrid.gridgrouper.stubs.GridGrouperRuntimeFault, gov.nih.nci.cagrid.gridgrouper.stubs.GroupNotFoundFault, gov.nih.nci.cagrid.gridgrouper.stubs.GrantPrivilegeFault, gov.nih.nci.cagrid.gridgrouper.stubs.InsufficientPrivilegeFault {
		gov.nih.nci.cagrid.gridgrouper.stubs.GrantGroupPrivilegeResponse boxedResult = new gov.nih.nci.cagrid.gridgrouper.stubs.GrantGroupPrivilegeResponse();
		impl.grantGroupPrivilege(params.getGroup().getGroupIdentifier(),params.getSubject().getSubjectIdentifier(),params.getPrivilege().getGroupPrivilegeType());
		return boxedResult;
	}

}
