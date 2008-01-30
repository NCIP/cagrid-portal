package org.cagrid.transfer.test.system.steps;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.test.TestCaseInfo;
import gov.nih.nci.cagrid.testing.system.deployment.SecureContainer;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.io.File;


public class CopyProxyStep extends Step {
    private TestCaseInfo tci;
    private SecureContainer container;


    public CopyProxyStep(SecureContainer container, TestCaseInfo tci) throws Exception {
        this.tci = tci;
        this.container = container;

    }


    public void runStep() throws Throwable {
        System.out.println("Adding a simple methods implementation.");

        File inFileClient = container.getProxyFile();
        File outFileClient = new File(tci.getDir() + File.separator + "localhost.proxy");

        Utils.copyFile(inFileClient, outFileClient);
    }

}
