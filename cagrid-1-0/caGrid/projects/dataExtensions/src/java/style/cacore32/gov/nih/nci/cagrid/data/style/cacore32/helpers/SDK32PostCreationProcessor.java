package gov.nih.nci.cagrid.data.style.cacore32.helpers;

import java.io.File;

import gov.nih.nci.cagrid.data.style.StyleCreationPostProcessor;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;

/** 
 *  SDK32PostCreationProcessor
 *  Deletes the old castor jar from the service's lib dir
 * 
 * @author David Ervin
 * 
 * @created Aug 31, 2007 11:17:40 AM
 * @version $Id: SDK32PostCreationProcessor.java,v 1.1 2007-08-31 15:50:47 dervin Exp $ 
 */
public class SDK32PostCreationProcessor implements StyleCreationPostProcessor {

    public void creationPostProcessStyle(ServiceExtensionDescriptionType desc, 
        ServiceInformation serviceInfo) throws Exception {
        File castorLib = new File(serviceInfo.getBaseDirectory().getAbsolutePath() 
            + File.separator + "lib" + File.separator + "castor-0.9.9.jar");
        castorLib.delete();
    }
}
