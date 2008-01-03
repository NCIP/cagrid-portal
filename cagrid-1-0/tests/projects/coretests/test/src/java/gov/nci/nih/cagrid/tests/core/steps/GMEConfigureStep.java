/*
 * Created on Jul 31, 2006
 */
package gov.nci.nih.cagrid.tests.core.steps;

import gov.nci.nih.cagrid.tests.core.util.GlobusHelper;
import gov.nci.nih.cagrid.tests.core.util.SimpleXmlReplacer;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.io.File;


/**
 * This step sets the mysql username and password fields in
 * gme-globus-config.xml of a deployed GME service
 * 
 * @author Patrick McConnell
 */
public class GMEConfigureStep extends Step {
    private GlobusHelper globus;
    private String user;
    private String password;


    public GMEConfigureStep(GlobusHelper globus) {
        this(globus, System.getProperty("mysql.user", "root"), System.getProperty("mysql.password", ""));
    }


    public GMEConfigureStep(GlobusHelper globus, String user, String password) {
        super();

        this.globus = globus;
        this.user = user;
        this.password = password;
    }


    @Override
    public void runStep() throws Throwable {
        File configFile = new File(this.globus.getTempGlobusLocation(), "etc" + File.separator
            + "cagrid_GlobalModelExchange" + File.separator + "gme-globus-config.xml");
        SimpleXmlReplacer replacer = new SimpleXmlReplacer();
        replacer.addReplacement("username", this.user);
        replacer.addReplacement("password", this.password);
        replacer.performReplacement(configFile);
    }
}