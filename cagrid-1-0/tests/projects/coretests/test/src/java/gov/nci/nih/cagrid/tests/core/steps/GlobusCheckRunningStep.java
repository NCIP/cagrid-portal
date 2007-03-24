/*
 * Created on Jun 11, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nci.nih.cagrid.tests.core.util.GlobusHelper;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import com.atomicobject.haste.framework.Step;


/**
 * This step checks to see if globus is running by hitting the CounterService
 * endpoint. Note: this step will not work on a secure globus globus container.
 * Instead, you should deploy the echo service and then invoke it.
 * 
 * @author Patrick McConnell
 */
public class GlobusCheckRunningStep extends Step {
    private GlobusHelper globus;


    public GlobusCheckRunningStep(GlobusHelper globus) {
        super();

        this.globus = globus;
    }


    @Override
    public void runStep() throws ServiceException, RemoteException {
        assertNotNull(this.globus);
        assertTrue("Globus was not running.", this.globus.isGlobusRunning());
    }
}
