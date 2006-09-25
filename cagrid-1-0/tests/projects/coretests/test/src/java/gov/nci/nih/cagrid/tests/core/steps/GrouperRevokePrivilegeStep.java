/*
 * Created on Sep 21, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier;
import gov.nih.nci.cagrid.gridgrouper.bean.GroupPrivilegeType;
import gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier;
import gov.nih.nci.cagrid.gridgrouper.bean.StemPrivilegeType;
import gov.nih.nci.cagrid.gridgrouper.client.GridGrouperClient;
import gov.nih.nci.cagrid.gridgrouper.stubs.types.GridGrouperRuntimeFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.types.StemNotFoundFault;

import java.rmi.RemoteException;

import org.apache.axis.types.URI.MalformedURIException;

import com.atomicobject.haste.framework.Step;

public class GrouperRevokePrivilegeStep
	extends Step
{
	private String endpoint;
	private String path;
	private String subject;
	private String priv;
	
	public GrouperRevokePrivilegeStep(String path, String subject, String priv)
	{
		this(path, subject, priv, "https://localhost:9443/wsrf/services/cagrid/GridGrouper");
	}
	
	public GrouperRevokePrivilegeStep(String path, String subject, String priv, String endpoint)
	{
		super();
		
		this.endpoint = endpoint;
		this.path = path;
		this.subject = subject;
		this.priv = priv;
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
		
		// grant
		if (isGroup) {
			grouper.revokeGroupPrivilege(new GroupIdentifier(null, path), subject, GroupPrivilegeType.fromString(priv));
		} else {
			grouper.revokeStemPrivilege(new StemIdentifier(null, path), subject, StemPrivilegeType.fromString(priv));
		}
	}
	
	public static void main(String[] args) 
		throws GridGrouperRuntimeFault, StemNotFoundFault, RemoteException, MalformedURIException
	{
		new GrouperRevokePrivilegeStep(
			"test:mygroup",
			"/O=OSU/OU=BMI/OU=caGrid/OU=Dorian/OU=localhost/OU=IdP [1]/CN=subject1",
			"admin"
		).runStep();
	}
}
