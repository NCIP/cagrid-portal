/*
 * Created on Jul 14, 2006
 */
package gov.nci.nih.cagrid.validator.steps.dorian;

import gov.nci.nih.cagrid.validator.steps.AbstractBaseServiceTestStep;

import java.io.File;


/**
 * TestDorianStep 
 * Step tests Dorian
 * 
 * @author David Ervin
 * 
 * @created Aug 27, 2007 4:04:29 PM
 * @version $Id: TestDorianStep.java,v 1.2 2007-09-10 14:32:49 dervin Exp $
 */
public class TestDorianStep extends AbstractBaseServiceTestStep {

    
    public TestDorianStep(String serviceURL, File tempDir) {
        super(serviceURL, tempDir);
    }


    public void runStep() throws Throwable {
        // TODO: connect to the url and validate a return
    }
}