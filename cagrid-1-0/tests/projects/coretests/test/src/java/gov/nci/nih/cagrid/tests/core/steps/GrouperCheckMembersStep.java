/*
 * Created on Sep 21, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier;
import gov.nih.nci.cagrid.gridgrouper.bean.MemberDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.MemberFilter;
import gov.nih.nci.cagrid.gridgrouper.client.GridGrouperClient;
import gov.nih.nci.cagrid.gridgrouper.stubs.types.GridGrouperRuntimeFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.types.StemNotFoundFault;

import java.rmi.RemoteException;
import java.util.HashSet;

import org.apache.axis.types.URI.MalformedURIException;

import com.atomicobject.haste.framework.Step;

public class GrouperCheckMembersStep
	extends Step
{
	private String endpoint;
	private String path;
	private String filter;
	private String[] members;
	
	public GrouperCheckMembersStep(String path, String filter, String[] members)
	{
		this(path, filter, members, "https://localhost:9443/wsrf/services/cagrid/GridGrouper");
	}
	
	public GrouperCheckMembersStep(String path, String filter, String[] members, String endpoint)
	{
		super();
		
		this.endpoint = endpoint;
		this.path = path;
		this.filter = filter;
		this.members = members;
	}
	
	public void runStep() 
		throws GridGrouperRuntimeFault, StemNotFoundFault, RemoteException, MalformedURIException
	{
		GridGrouperClient grouper = new GridGrouperClient(endpoint);
		
		// get child stems
		MemberDescriptor[] members = grouper.getMembers(new GroupIdentifier(null, path), MemberFilter.fromString(filter));
		if (members == null && this.members.length == 0) return;
		assertEquals(this.members.length, members.length);
		HashSet<String> memberSet = new HashSet<String>(members.length);
		for (MemberDescriptor member : members) {
			memberSet.add(member.getSubjectId());
		}
		
		// check members
		for (String member : this.members) {
			assertTrue(memberSet.contains(member));
		}
	}
	
	public static void main(String[] args) 
		throws GridGrouperRuntimeFault, StemNotFoundFault, RemoteException, MalformedURIException
	{
		new GrouperCheckMembersStep(
			"test:mygroup",
			"All",
			new String[] { "/O=OSU/OU=BMI/OU=caGrid/OU=Dorian/OU=localhost/OU=IdP [1]/CN=subject1" }
		).runStep();
	}
}
