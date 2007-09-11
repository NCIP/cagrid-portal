package gov.nci.nih.cagrid.validator.steps.dorian;

import gov.nci.nih.cagrid.validator.steps.AbstractBaseServiceTestStep;
import gov.nih.nci.cagrid.dorian.client.DorianClient;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Properties;

import org.apache.axis.types.URI.MalformedURIException;


/**
 * BaseDorianTestStep 
 * Base step for testing dorian
 * 
 * @author David Ervin
 * 
 * @created Aug 27, 2007 4:04:29 PM
 * @version $Id: BaseDorianTestStep.java,v 1.2 2007-09-11 14:53:56 dervin Exp $
 */
public abstract class BaseDorianTestStep extends AbstractBaseServiceTestStep {
    
    private DorianClient dorianClient = null;

    
    public BaseDorianTestStep(String serviceURL, File tempDir, Properties configuration) {
        super(serviceURL, tempDir, configuration);
    }


    public abstract void runStep() throws Throwable;
    
    
    protected DorianClient getDorianClient() {
        if (dorianClient == null) {
            try {
                dorianClient = new DorianClient(getServiceUrl());
            } catch (RemoteException ex) {
                ex.printStackTrace();
                fail("Error creating Dorian Service Client: " + ex.getMessage());
            } catch (MalformedURIException ex) {
                ex.printStackTrace();
                fail("The service URL was not valid: " + ex.getMessage());
            }
        }
        return dorianClient;
    }
}