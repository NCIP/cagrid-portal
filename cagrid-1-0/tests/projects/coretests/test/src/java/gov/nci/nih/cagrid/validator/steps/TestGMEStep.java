/*
 * Created on Jul 14, 2006
 */
package gov.nci.nih.cagrid.validator.steps;

import com.atomicobject.haste.framework.Step;


/**
 * This step authenticates a user with dorian and saves the proxy as the globus
 * default proxy.
 * 
 * @author Patrick McConnell
 */
public class TestGMEStep extends Step {

    private String serviceURL;
    

    public TestGMEStep(String serviceURL) {
    	System.out.println("Checking Globus Service");
       this.serviceURL = serviceURL;
       //connect to the url and validate a return
    }


    @Override
    public void runStep() throws Throwable {
      
    }

}