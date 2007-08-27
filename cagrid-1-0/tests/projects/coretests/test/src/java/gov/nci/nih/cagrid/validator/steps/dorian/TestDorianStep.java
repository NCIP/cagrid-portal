/*
 * Created on Jul 14, 2006
 */
package gov.nci.nih.cagrid.validator.steps.dorian;

import com.atomicobject.haste.framework.Step;


/**
 * TestDorianStep 
 * Step tests Dorian
 * 
 * @author David Ervin
 * 
 * @created Aug 27, 2007 4:04:29 PM
 * @version $Id: TestDorianStep.java,v 1.1 2007-08-27 20:06:02 dervin Exp $
 */
public class TestDorianStep extends Step {

    private String serviceURL;


    public TestDorianStep(String serviceURL) {
        System.out.println("Checking Globus Service");
        this.serviceURL = serviceURL;
    }


    @Override
    public void runStep() throws Throwable {
        // TODO: connect to the url and validate a return
    }
}