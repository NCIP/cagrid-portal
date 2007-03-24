/*
 * Created on Jun 11, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nci.nih.cagrid.tests.core.util.GlobusHelper;

import java.io.IOException;

import com.atomicobject.haste.framework.Step;


/**
 * This step configures the EVS grid service
 * 
 * @author Avinash Shanbhag
 */
public class EvsServiceConfigStep extends Step {
    private GlobusHelper globusHelper;


    public EvsServiceConfigStep(GlobusHelper globusHelper) {
        super();

        this.globusHelper = globusHelper;
    }


    @Override
    public void runStep() throws IOException {
        // does nothing - perform any configuration (like setting the EVS url)
        // currently the EVS url is hard coded in
        // gov.nih.nci.cagrid.evsgridservice.service.CACORE_31_URL
    }
}
