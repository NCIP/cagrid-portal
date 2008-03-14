package gov.nih.nci.cagrid.introduce.extensions.wsenum.upgrade;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.extension.utils.ExtensionUtilities;
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
 * WsEnumUpgradeTo1pt2
 * Upgrades ws enumeration to 1.2
 * 
 * @author ervin
 * @created Apr 9, 2007 11:21:24 AM
 * @version $Id: multiscaleEclipseCodeTemplates.xml,v 1.1 2007/03/02 14:35:01
 *          dervin Exp $
 */
public class WsEnumUpgradeTo1pt2 extends ExtensionUpgraderBase {

    private static final String CAGRID_1_1_WS_ENUM_JAR_PREFIX = "caGrid-1.1-wsEnum";
    private static final String CAGRID_1_0_WS_ENUM_JAR_PREFIX = "caGrid-1.0-wsEnum";
    
    protected static Log LOG = LogFactory.getLog(WsEnumUpgradeTo1pt2.class.getName());


    public WsEnumUpgradeTo1pt2(ExtensionType extensionType, ServiceInformation serviceInfo, String servicePath,
        String fromVersion, String toVersion) {
        super("WsEnumUpgradeTo1pt2", extensionType, serviceInfo, servicePath, fromVersion, toVersion);
    }


    @Override
    protected void upgrade() throws Exception {
        upgradeJars();
        getStatus().setStatus(StatusBase.UPGRADE_OK);
    }


    /**
     * Upgrade the jars which are required for metadata
     */
    private void upgradeJars() {
        FileFilter enumLibFilter = new FileFilter() {
            public boolean accept(File pathname) {
                String name = pathname.getName();
                return name.endsWith(".jar") 
                && (name.startsWith(CAGRID_1_1_WS_ENUM_JAR_PREFIX) || name.startsWith(CAGRID_1_0_WS_ENUM_JAR_PREFIX));
            }
        };
        FileFilter newEnumLibFilter = new FileFilter() {
            public boolean accept(File pathname) {
                String name = pathname.getName();
                return name.endsWith(".jar") 
                && name.startsWith("caGrid-wsEnum-");
            }
        };
        // locate the old data service libs in the service
        File serviceLibDir = new File(getServicePath() + File.separator + "lib");
        File[] serviceEnumLibs = serviceLibDir.listFiles(enumLibFilter);
        // delete the old libraries
        for (File oldLib : serviceEnumLibs) {
            oldLib.delete();
            getStatus().addDescriptionLine("old caGrid library " + oldLib.getName() + " removed");
            System.out.println("old caGrid library " + oldLib.getName() + " removed");
        }
        // copy new libraries in
        File extLibDir = new File(ExtensionsLoader.EXTENSIONS_DIRECTORY + File.separator + "lib");
        File[] enumLibs = extLibDir.listFiles(newEnumLibFilter);
        List<File> outLibs = new ArrayList<File>(enumLibs.length);
        for (File newLib : enumLibs) {
            File out = new File(serviceLibDir.getAbsolutePath() + File.separator + newLib.getName());
            try {
                Utils.copyFile(newLib, out);
                getStatus().addDescriptionLine("caGrid 1.2 library " + newLib.getName() + " added");
                System.out.println("caGrid 1.2 library " + newLib.getName() + " added");
            } catch (IOException ex) {
                // TODO: change this to use a better exception
                throw new RuntimeException("Error copying new metadata library: " + ex.getMessage(), ex);
            }
            outLibs.add(out);
        }
        // remove unused wsrf_core_[stubs_]enum.jar files
        File deleteJar1 = new File(serviceLibDir, "wsrf_core_enum.jar");
        File deleteJar2 = new File(serviceLibDir, "wsrf_core_stubs_enum.jar");
        if (deleteJar1.exists()) {
        	deleteJar1.delete();
        	getStatus().addDescriptionLine("old caGrid library " + deleteJar1.getName() + " removed");
            System.out.println("old caGrid library " + deleteJar1.getName() + " removed");
        }
        if (deleteJar2.exists()) {
        	deleteJar2.delete();
        	getStatus().addDescriptionLine("old caGrid library " + deleteJar2.getName() + " removed");
            System.out.println("old caGrid library " + deleteJar2.getName() + " removed");
        }
        
        // update the Eclipse .classpath file
        File classpathFile = new File(getServicePath() + File.separator + ".classpath");
        File[] outLibArray = new File[enumLibs.length];
        outLibs.toArray(outLibArray);
        try {
        	ExtensionUtilities.syncEclipseClasspath(classpathFile, outLibArray);
        	ExtensionUtilities.removeLibrariesFromClasspath(classpathFile, 
        			new File[] {deleteJar1, deleteJar2});
            getStatus().addDescriptionLine("Eclipse .classpath file updated");
        } catch (Exception ex) {
            // TODO: change this to use a better exception
            throw new RuntimeException("Error updating Eclipse .classpath file: " + ex.getMessage(), ex);
        }
    }
}
