package gov.nci.nih.cagrid.validator.steps.cadsr;

import gov.nci.nih.cagrid.validator.steps.AbstractBaseServiceTestStep;
import gov.nih.nci.cagrid.cadsr.client.CaDSRServiceClient;

import java.io.File;
import java.rmi.RemoteException;

import org.apache.axis.types.URI.MalformedURIException;

/** 
 *  BaseCadsrTestStep
 *  Base class for test steps for a caDSR grid service
 * 
 * @author David Ervin
 * 
 * @created Sep 10, 2007 10:53:52 AM
 * @version $Id: BaseCadsrTestStep.java,v 1.1 2007-09-10 14:57:05 dervin Exp $ 
 */
public abstract class BaseCadsrTestStep extends AbstractBaseServiceTestStep {
    
    private CaDSRServiceClient cadsrClient = null;

    public BaseCadsrTestStep(String serviceUrl, File tempDir) {
        super(serviceUrl, tempDir);
    }


    public abstract void runStep() throws Throwable;
    
    
    protected CaDSRServiceClient getCadsrClient() {
        if (cadsrClient == null) {
            try {
                cadsrClient = new CaDSRServiceClient(getServiceUrl());
            } catch (RemoteException ex) {
                ex.printStackTrace();
                fail("Error creating caDSR Service Client: " + ex.getMessage());
            } catch (MalformedURIException ex) {
                ex.printStackTrace();
                fail("The service URL was not valid: " + ex.getMessage());
            }
        }
        return cadsrClient;
    }
}
