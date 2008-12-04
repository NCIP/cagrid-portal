/**
 * 
 */
package org.cagrid.installer.tasks;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;


/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public abstract class CaGridAntTask extends BasicTask {

    private String targetName;


    /**
     * @param name
     * @param description
     */
    public CaGridAntTask(String name, String description, String targetName) {
        super(name, description);
        this.targetName = targetName;
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.cagrid.installer.tasks.BasicTask#internalExecute(java.util.Map)
     */
    @Override
    protected Object internalExecute(CaGridInstallerModel model) throws Exception {
        Map<String, String> env = new HashMap<String, String>();
        env.put("GLOBUS_LOCATION", model.getProperty(Constants.GLOBUS_HOME));
        env.put("CATALINA_HOME", model.getProperty(Constants.TOMCAT_HOME));
        Properties sysProps = new Properties();
        sysProps.setProperty(Constants.SERVICE_DEST_DIR, model.getServiceDestDir());
        sysProps.setProperty("env.GLOBUS_LOCATION", model.getProperty(Constants.GLOBUS_HOME));

        model.setProperty(Constants.BUILD_FILE_PATH, getBuildFilePath(model));

        return runAntTask(model, this.targetName, env, sysProps);

    }


    protected abstract Object runAntTask(CaGridInstallerModel model, String target, Map<String, String> env,
        Properties sysProps) throws Exception;


    protected abstract String getBuildFilePath(CaGridInstallerModel model);

}
