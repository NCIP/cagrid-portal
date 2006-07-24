/*
 * Created on Jul 14, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nih.nci.cagrid.dorian.client.DorianCertifcateAuthorityClient;
import gov.nih.nci.cagrid.gridca.common.CertUtil;

import java.io.File;

import com.atomicobject.haste.framework.Step;

public class DorianAddTrustedCAStep
	extends Step
{
	private File caFile;
	private String serviceURL;
	
	public DorianAddTrustedCAStep(File caFile, int port) 
	{
		this(caFile, "https://localhost:" + port + "/wsrf/services/cagrid/Dorian");
	}
	
	public DorianAddTrustedCAStep(File caFile, String serviceURL)
	{
		super();
		
		this.caFile = caFile;
		this.serviceURL = serviceURL;
	}
	
	public void runStep() 
		throws Throwable
	{
		DorianCertifcateAuthorityClient client = new DorianCertifcateAuthorityClient(serviceURL);
		CertUtil.writeCertificate(client.getCACertificate(), caFile);
	}
}
