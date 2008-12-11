/**
 * 
 */
package org.cagrid.installer.tasks.installer;

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
    

}
