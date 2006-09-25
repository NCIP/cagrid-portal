/*
 * Created on Sep 21, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier;
import gov.nih.nci.cagrid.gridgrouper.client.GridGrouperClient;
import gov.nih.nci.cagrid.gridgrouper.testutils.Utils;

import com.atomicobject.haste.framework.Step;

public class GrouperCreateStemStep
	extends Step
{
	private String endpoint;
	private String path;
	private boolean shouldFail;
	
	public GrouperCreateStemStep(String path)
	{
		this(path, false, "https://localhost:9443/wsrf/services/cagrid/GridGrouper");
	}
	
	public GrouperCreateStemStep(String path, boolean shouldFail)
	{
		this(path, shouldFail, "https://localhost:9443/wsrf/services/cagrid/GridGrouper");
	}
	
	public GrouperCreateStemStep(String path, boolean shouldFail, String endpoint)
	{
		super();
		
		this.endpoint = endpoint;
		this.path = path;
		this.shouldFail = shouldFail;
	}
	
	public void runStep() 
		throws Exception
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
			if (! foundChild) {
				try { 
					grouper.addChildStem(stem, name, name);
					if (shouldFail) fail("addChildStem should fail");
				} catch (Exception e) {
					if (! shouldFail) throw e;
				}
			}
			stem = nextStem;
		}
	}
	
	public static void main(String[] args) 
		throws Exception
	{
		new GrouperCreateStemStep(
			"test:hi:there"
		).runStep();
	}
}
