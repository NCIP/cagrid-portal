/**
 * 
 */
package org.cagrid.installer.syncgts;

import java.io.File;

import javax.swing.Icon;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.PropertyConfigurationStep;
import org.cagrid.installer.util.InstallerUtils;
import org.pietschy.wizard.InvalidStateException;


/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class ReplaceDefaultGTSCAStep extends PropertyConfigurationStep {

    private static final Log logger = LogFactory.getLog(ReplaceDefaultGTSCAStep.class);


    /**
     * 
     */
    public ReplaceDefaultGTSCAStep() {
    }


    /**
     * @param name
     * @param description
     */
    public ReplaceDefaultGTSCAStep(String name, String description) {
        super(name, description);
    }


    /**
     * @param name
     * @param description
     * @param icon
     */
    public ReplaceDefaultGTSCAStep(String name, String description, Icon icon) {
        super(name, description, icon);
    }


    public void applyState() throws InvalidStateException {

        super.applyState();
        try {
            File certsDir = new File(this.model.getServiceDestDir() + "/syncgts/ext/target_grid/certificates");
            File[] files = certsDir.listFiles();
            for (File file : files) {
                logger.info("Deleting '" + file.getAbsolutePath() + "'");
                file.delete();
            }

            File f = new File(this.model.getProperty(Constants.REPLACEMENT_GTS_CA_CERT_PATH));
            String fileName = f.getName();
            if (!fileName.matches(".+\\.[0-9]$")) {
                fileName += ".0";
            }

            InstallerUtils.copyFile(f.getAbsolutePath(), certsDir.getAbsolutePath() + "/" + fileName);
        } catch (Exception ex) {
            String msg = "Error replacing certificate: " + ex.getMessage();
            logger.error(msg, ex);
            throw new InvalidStateException(msg, ex);
        }
    }

}
