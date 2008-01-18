package gov.nih.nci.cagrid.sdkquery4.style.helper;

import java.io.File;

import org.apache.log4j.Logger;

import gov.nih.nci.cagrid.data.style.StyleCreationPostProcessor;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;

/** 
 *  SDK4PostCreationHelper
 *  Post-creation helper for caCORE SDK 4.0 data service style
 * 
 * @author David Ervin
 * 
 * @created Jan 18, 2008 12:38:01 PM
 * @version $Id: SDK4PostCreationHelper.java,v 1.1 2008-01-18 18:16:27 dervin Exp $ 
 */
public class SDK4PostCreationHelper implements StyleCreationPostProcessor {
    
    public static final String OLD_CASTOR_JAR_NAME = "castor-0.9.9.jar";
    
    private static final Logger LOG = Logger.getLogger(SDK4PostCreationHelper.class);

    public void creationPostProcessStyle(ServiceExtensionDescriptionType desc, 
        ServiceInformation serviceInfo) throws Exception {
        deleteOldCastorJar(serviceInfo);
    }
    
    
    private void deleteOldCastorJar(ServiceInformation info) {
        File castorLib = new File(info.getBaseDirectory().getAbsolutePath() 
            + File.separator + "lib" + File.separator + OLD_CASTOR_JAR_NAME);
        LOG.debug("Deleting old castor jar (" + castorLib.getAbsolutePath() + ")");
        castorLib.delete();
    }
}
