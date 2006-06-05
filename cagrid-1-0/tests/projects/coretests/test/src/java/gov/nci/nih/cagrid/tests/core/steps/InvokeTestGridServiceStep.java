/*
 * Created on Apr 12, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nih.nci.cagrid.tests.client.BasicAnalyticalServiceClient;

import com.atomicobject.haste.framework.Step;

public class InvokeTestGridServiceStep
	extends Step
{
	private int port;
	
	public InvokeTestGridServiceStep(int port)
	{
		super();
		
		this.port = port;
	}
	
	public void runStep() throws Throwable
	{
		BasicAnalyticalServiceClient client = new BasicAnalyticalServiceClient(
			"http://localhost:" + port + "/wsrf/services/cagrid/BasicAnalyticalService"
		);
		
		String dna = client.reverseTranslate("ATCGATCG");
		assertEquals("CGATCGAT", dna);
	}
}