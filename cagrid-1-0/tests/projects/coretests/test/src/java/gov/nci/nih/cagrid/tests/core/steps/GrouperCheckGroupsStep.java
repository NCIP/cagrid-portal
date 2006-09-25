/*
 * Created on Sep 21, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nih.nci.cagrid.gridgrouper.bean.GroupDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier;
import gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier;
import gov.nih.nci.cagrid.gridgrouper.client.GridGrouperClient;
import gov.nih.nci.cagrid.gridgrouper.stubs.types.GridGrouperRuntimeFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.types.StemNotFoundFault;

import java.rmi.RemoteException;
import java.util.HashSet;

import org.apache.axis.types.URI.MalformedURIException;

import com.atomicobject.haste.framework.Step;

public class GrouperCheckGroupsStep
	extends Step
{
	private String endpoint;
	private String path;
	private String[] children;
	
	public GrouperCheckGroupsStep(String path, String[] children)
	{
		this(path, children, "https://localhost:9443/wsrf/services/cagrid/GridGrouper");
	}
	
	public GrouperCheckGroupsStep(String path, String[] children, String endpoint)
	{
		super();
		
		this.endpoint = endpoint;
		this.path = path;
		this.children = children;
	}
	
	public void runStep() 
		throws GridGrouperRuntimeFault, StemNotFoundFault, RemoteException, MalformedURIException
	{
		GridGrouperClient grouper = new GridGrouperClient(endpoint);
		
		// get child stems
		GroupDescriptor[] groups = grouper.getChildGroups(new StemIdentifier(null, path));
		if (groups == null && children.length == 0) return;
		assertEquals(children.length, groups.length);
		HashSet<String> groupSet = new HashSet<String>(groups.length);
		for (GroupDescriptor group : groups) {
			groupSet.add(group.getName());
		}
		
		// check child groups
		String path = this.path;
		if (! path.equals("")) path = path + ":";
		for (String child : children) {
			assertTrue(groupSet.contains(path + child));
		}
	}
	
	public static void main(String[] args) 
		throws GridGrouperRuntimeFault, StemNotFoundFault, RemoteException, MalformedURIException
	{
		new GrouperCheckGroupsStep(
			"test",
			new String[] { "mygroup" }
		).runStep();
	}
}
