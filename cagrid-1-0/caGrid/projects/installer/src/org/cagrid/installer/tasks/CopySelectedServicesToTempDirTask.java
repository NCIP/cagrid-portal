/**
 * 
 */
package org.cagrid.installer.tasks;

import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;

import java.util.*;


/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class CopySelectedServicesToTempDirTask extends CaGridInstallerAntTask {

    /**
     * @param name
     * @param description
     * @param targetName
     */
    public CopySelectedServicesToTempDirTask(String name, String description) {
        super(name, description, "copy-selected-services");
    }


    protected Object runAntTask(CaGridInstallerModel model, String target, Map<String, String> env, Properties sysProps)
        throws Exception {
        List<String> selectedServices = new ArrayList<String>();

        if (model.isTrue(Constants.INSTALL_SYNC_GTS)) {
            selectedServices.add("syncgts");
        }

        StringBuilder sb = new StringBuilder();
        for (Iterator i = selectedServices.iterator(); i.hasNext();) {
            sb.append((String) i.next());
            if (i.hasNext()) {
                sb.append(",");
            }
        }
        sysProps.setProperty("selected.services", sb.toString());

        new AntTask("", "", getBuildFilePath(), target, env, sysProps).execute(model);

        return null;
    }

}
