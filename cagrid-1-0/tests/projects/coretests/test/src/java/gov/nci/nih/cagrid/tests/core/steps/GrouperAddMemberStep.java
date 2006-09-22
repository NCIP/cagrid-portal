/*
 * Created on Sep 21, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier;
import gov.nih.nci.cagrid.gridgrouper.client.GridGrouperClient;
import gov.nih.nci.cagrid.gridgrouper.stubs.types.GridGrouperRuntimeFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.types.StemNotFoundFault;

import java.rmi.RemoteException;

import org.apache.axis.types.URI.MalformedURIException;

import com.atomicobject.haste.framework.Step;

public class GrouperAddMemberStep
	extends Step
{
	private String endpoint;
	private String group;
	private String subject;
	
	public GrouperAddMemberStep(String group, String subject)
	{
		this(group, subject, "https://localhost:9443/wsrf/services/cagrid/GridGrouper");
	}
	
	public GrouperAddMemberStep(String group, String subject, String endpoint)
	{
		super();
		
		this.endpoint = endpoint;
		this.group = group;
		this.subject = subject;
	}
	
	public void runStep() 
		throws GridGrouperRuntimeFault, StemNotFoundFault, RemoteException, MalformedURIException
	{
		GridGrouperClient grouper = new GridGrouperClient(endpoint);
		
		// add member
		grouper.addMember(new GroupIdentifier(null, group), subject);
	}
	
	public static void main(String[] args) 
		throws GridGrouperRuntimeFault, StemNotFoundFault, RemoteException, MalformedURIException
	{
		new GrouperAddMemberStep(
			"test:mygroup",
			"/O=OSU/OU=BMI/OU=caGrid/OU=Dorian/OU=localhost/OU=IdP [1]/CN=subject1"
		).runStep();
	}
}
