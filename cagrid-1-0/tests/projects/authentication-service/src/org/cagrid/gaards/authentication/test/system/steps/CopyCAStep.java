package org.cagrid.gaards.authentication.test.system.steps;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.testing.system.deployment.SecureContainer;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.io.File;
import java.util.Date;


public class CopyCAStep extends Step {
    private SecureContainer container;
    private File caCert;


    public CopyCAStep(SecureContainer container) throws Exception {
        this.container = container;
    }


    public void runStep() throws Throwable {
        System.out.println("Copying user proxys to services dir");
        File inFileClient = new File(container.getCertificatesDirectory().getAbsolutePath() + File.separator + "ca" + File.separator + "testing_ca_cert.0");
       this.caCert = new File(Utils.getTrustedCerificatesDirectory().getAbsolutePath()+File.separator + new Date().getTime()+".0");
        Utils.copyFile(inFileClient, this.caCert);
    }


	public File getCACert() {
		return caCert;
	}
    
    

}
