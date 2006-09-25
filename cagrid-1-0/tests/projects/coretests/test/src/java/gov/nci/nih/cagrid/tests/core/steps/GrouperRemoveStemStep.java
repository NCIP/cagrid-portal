/*
 * Created on Sep 21, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier;
import gov.nih.nci.cagrid.gridgrouper.client.GridGrouperClient;

import com.atomicobject.haste.framework.Step;

public class GrouperRemoveStemStep
	extends Step
{
	private String endpoint;
	private String stem;
	private boolean shouldFail;
	
	public GrouperRemoveStemStep(String stem)
	{
		this(stem, false, "https://localhost:9443/wsrf/services/cagrid/GridGrouper");
	}
	
	public GrouperRemoveStemStep(String stem, boolean shouldFail)
	{
		this(stem, shouldFail, "https://localhost:9443/wsrf/services/cagrid/GridGrouper");
	}
	
	public GrouperRemoveStemStep(String stem, boolean shouldFail, String endpoint)
	{
		super();
		
		this.endpoint = endpoint;
		this.stem = stem;
		this.shouldFail = shouldFail;
	}
	
	public void runStep() 
		throws Exception
	{
		GridGrouperClient grouper = new GridGrouperClient(endpoint);
		
		// remove stem
		try {
			grouper.deleteStem(new StemIdentifier(null, stem));
			if (shouldFail) fail("deleteMember should fail");
		} catch (Exception e) {
			if (! shouldFail) throw e;
		}
	}
	
	public static void main(String[] args) 
		throws Exception
	{
		new GrouperRemoveStemStep(
			"test:stem4"
		).runStep();
	}
}
