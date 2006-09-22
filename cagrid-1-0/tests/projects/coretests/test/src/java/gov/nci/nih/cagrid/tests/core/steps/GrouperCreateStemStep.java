/*
 * Created on Sep 21, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier;
import gov.nih.nci.cagrid.gridgrouper.client.GridGrouperClient;
import gov.nih.nci.cagrid.gridgrouper.stubs.types.GridGrouperRuntimeFault;
import gov.nih.nci.cagrid.gridgrouper.stubs.types.StemNotFoundFault;
import gov.nih.nci.cagrid.gridgrouper.testutils.Utils;

import java.rmi.RemoteException;

import org.apache.axis.types.URI.MalformedURIException;

import com.atomicobject.haste.framework.Step;

public class GrouperCreateStemStep
	extends Step
{
	private String endpoint;
	private String path;
	
	public GrouperCreateStemStep(String path)
	{
		this(path, "https://localhost:9443/wsrf/services/cagrid/GridGrouper");
	}
	
	public GrouperCreateStemStep(String path, String endpoint)
	{
		super();
		
		this.endpoint = endpoint;
		this.path = path;
	}
	
	public void runStep() 
		throws GridGrouperRuntimeFault, StemNotFoundFault, RemoteException, MalformedURIException
	{
		GridGrouperClient grouper = new GridGrouperClient(endpoint);
		
		StemIdentifier stem = Utils.getRootStemIdentifier();
		for (String name : path.split(":")) {
			StemIdentifier nextStem = new StemIdentifier(null, (stem.getStemName() == "" ? "" : stem.getStemName() + ":") + name);

			boolean foundChild = false;
			StemDescriptor[] childStems = grouper.getChildStems(stem);
			if (childStems != null) {
				for (StemDescriptor childStem : childStems) {
					if (childStem.getName().equals(nextStem.getStemName())) {
						foundChild = true;
						break;
					}
				}
			}
			if (! foundChild) grouper.addChildStem(stem, name, name);
			stem = nextStem;
		}
	}
	
	public static void main(String[] args) 
		throws GridGrouperRuntimeFault, StemNotFoundFault, RemoteException, MalformedURIException
	{
		new GrouperCreateStemStep(
			"test:hi:there"
		).runStep();
	}
}
