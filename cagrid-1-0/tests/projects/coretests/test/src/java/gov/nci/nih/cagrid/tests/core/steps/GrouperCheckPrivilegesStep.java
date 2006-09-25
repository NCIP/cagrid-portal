/*
 * Created on Sep 21, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier;
import gov.nih.nci.cagrid.gridgrouper.bean.GroupPrivilege;
import gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier;
import gov.nih.nci.cagrid.gridgrouper.bean.StemPrivilege;
import gov.nih.nci.cagrid.gridgrouper.client.GridGrouperClient;
import gov.nih.nci.cagrid.gridgrouper.stubs.types.GridGrouperRuntimeFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.types.StemNotFoundFault;

import java.rmi.RemoteException;
import java.util.HashSet;

import org.apache.axis.types.URI.MalformedURIException;

import com.atomicobject.haste.framework.Step;

public class GrouperCheckPrivilegesStep
	extends Step
{
	private String endpoint;
	private String path;
	private String subject;
	private String[] privs;
	
	public GrouperCheckPrivilegesStep(String path, String subject, String[] privs)
	{
		this(path, subject, privs, "https://localhost:9443/wsrf/services/cagrid/GridGrouper");
	}
	
	public GrouperCheckPrivilegesStep(String path, String subject, String[] privs, String endpoint)
	{
		super();
		
		this.endpoint = endpoint;
		this.path = path;
		this.subject = subject;
		this.privs = privs;
	}
	
	public void runStep() 
		throws GridGrouperRuntimeFault, StemNotFoundFault, RemoteException, MalformedURIException
	{
		GridGrouperClient grouper = new GridGrouperClient(endpoint);

		// group or stem?
		boolean isGroup = false;
		try {
			grouper.getGroup(new GroupIdentifier(null, path));
			isGroup = true;
		} catch (Exception e) {
			isGroup = false;
		}
		
		// get privs
		String[] privs = new String[0];
		String[] commonPrivs = new String[0];
		if (isGroup) {
			GroupPrivilege[] commonGroupPrivs = grouper.getGroupPrivileges(new GroupIdentifier(null, path), "GrouperAll");
			GroupPrivilege[] groupPrivs = grouper.getGroupPrivileges(new GroupIdentifier(null, path), subject);
			privs = new String[groupPrivs.length];
			commonPrivs = new String[commonGroupPrivs.length];
			for (int i = 0; i < groupPrivs.length; i++) privs[i] = groupPrivs[i].getPrivilegeType().getValue();
			for (int i = 0; i < commonGroupPrivs.length; i++) commonPrivs[i] = commonGroupPrivs[i].getPrivilegeType().getValue();
		} else {
			StemPrivilege[] stemPrivs = grouper.getStemPrivileges(new StemIdentifier(null, path), subject);
			privs = new String[stemPrivs.length];
			for (int i = 0; i < stemPrivs.length; i++) privs[i] = stemPrivs[i].getPrivilegeType().getValue();
		}
		
		// put check privs in a set
		HashSet<String> checkPrivSet = new HashSet<String>(this.privs.length);
		for (String priv : this.privs) checkPrivSet.add(priv);
		for (String priv : commonPrivs) checkPrivSet.add(priv);
		
		// put privs in a set
		HashSet<String> privSet = new HashSet<String>(privs.length);
		for (String priv : privs) privSet.add(priv);
		
		// check privs
		assertEquals(checkPrivSet.size(), privSet.size());
		for (String priv : checkPrivSet) assertTrue(privSet.contains(priv));
	}
	
	public static void main(String[] args) 
		throws GridGrouperRuntimeFault, StemNotFoundFault, RemoteException, MalformedURIException
	{
		new GrouperCheckPrivilegesStep(
			"test:mygroup",
			"/O=OSU/OU=BMI/OU=caGrid/OU=Dorian/OU=localhost/OU=IdP [1]/CN=subject1",
			new String[] { "admin" }
		).runStep();
	}
}
