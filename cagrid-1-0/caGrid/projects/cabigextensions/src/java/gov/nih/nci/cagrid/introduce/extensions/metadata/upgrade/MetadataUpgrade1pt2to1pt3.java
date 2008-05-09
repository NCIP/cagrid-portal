package gov.nih.nci.cagrid.introduce.extensions.metadata.upgrade;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.extension.utils.ExtensionUtilities;
import gov.nih.nci.cagrid.introduce.extensions.metadata.common.MetadataExtensionHelper;
import gov.nih.nci.cagrid.introduce.extensions.metadata.constants.MetadataConstants;
import gov.nih.nci.cagrid.introduce.upgrade.common.StatusBase;
import gov.nih.nci.cagrid.introduce.upgrade.one.x.ExtensionUpgraderBase;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * MetadataUpgrade1pt0to1pt1 copies metadata descriptions
 * 
 * @author oster
 * @created Apr 9, 2007 11:21:24 AM
 * @version $Id: multiscaleEclipseCodeTemplates.xml,v 1.1 2007/03/02 14:35:01
 *          dervin Exp $
 */
public class MetadataUpgrade1pt2to1pt3 extends ExtensionUpgraderBase {
    protected static Log LOG = LogFactory.getLog(MetadataUpgrade1pt2to1pt3.class.getName());


    /**
     * @param extensionType
     * @param serviceInfo
     * @param servicePath
     * @param fromVersion
     * @param toVersion
     */
    public MetadataUpgrade1pt2to1pt3(ExtensionType extensionType, ServiceInformation serviceInfo, String servicePath,
        String fromVersion, String toVersion) {
        super("MetadataUpgrade1pt1to1pt2", extensionType, serviceInfo, servicePath, fromVersion, toVersion);
    }


    @Override
    protected void upgrade() throws Exception {  
        //TODO: need to make sure to upgrade the namespace types of hte service so that it now
        //includes all three metadata schemas and they they are set to generate stubs false
        
        getStatus().setStatus(StatusBase.UPGRADE_OK);
    }

}
