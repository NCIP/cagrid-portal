/*
 * Created on Jul 14, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nih.nci.cagrid.dorian.client.IdPRegistrationClient;
import gov.nih.nci.cagrid.dorian.idp.bean.Application;
import gov.nih.nci.cagrid.testing.system.haste.Step;


/**
 * This step submits to dorian an application for user account registration.
 * 
 * @author Patrick McConnell
 */
public class DorianSubmitRegistrationStep extends Step {
    private String serviceURL;
    private Application application;


    public DorianSubmitRegistrationStep(Application application, String serviceURL) {
        super();

        this.application = application;
        this.serviceURL = serviceURL;
    }


    @Override
    public void runStep() throws Throwable {
        IdPRegistrationClient client = new IdPRegistrationClient(this.serviceURL);
        client.register(this.application);
    }
}
