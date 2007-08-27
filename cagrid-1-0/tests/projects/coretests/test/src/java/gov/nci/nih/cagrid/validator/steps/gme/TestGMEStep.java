package gov.nci.nih.cagrid.validator.steps.gme;

import com.atomicobject.haste.framework.Step;


/**
 * TestGMEStep 
 * Step invokes the GME service
 * 
 * @author David Ervin
 * 
 * @created Aug 27, 2007 4:05:07 PM
 * @version $Id: TestGMEStep.java,v 1.1 2007-08-27 20:06:02 dervin Exp $
 */
public class TestGMEStep extends Step {

    private String serviceURL;


    public TestGMEStep(String serviceURL) {
        System.out.println("Checking Globus Service");
        this.serviceURL = serviceURL;
    }


    @Override
    public void runStep() throws Throwable {
        // TODO: connect to the url and validate a return
    }
}