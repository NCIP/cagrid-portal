package gov.nih.nci.cagrid.introduce.upgrade.introduce;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.upgrade.common.IntroduceUpgradeStatus;
import gov.nih.nci.cagrid.introduce.upgrade.common.StatusBase;
import gov.nih.nci.cagrid.introduce.upgrade.one.x.IntroduceUpgraderBase;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;


public class Introduce_1_2__1_3_Upgrader extends IntroduceUpgraderBase {

    public Introduce_1_2__1_3_Upgrader(IntroduceUpgradeStatus status, ServiceInformation serviceInformation,
        String servicePath) throws Exception {
        super(status, serviceInformation, servicePath, "1.2", "1.3");
    }

    private final class OldJarsFilter implements FileFilter {
        boolean hadGridGrouperJars = false;
        boolean hadCSMJars = false;


        public boolean accept(File name) {
            String filename = name.getName();
            boolean core = filename.startsWith("caGrid-core") && filename.endsWith(".jar");
            boolean advertisement = filename.startsWith("caGrid-advertisement") && filename.endsWith(".jar");
            boolean introduce = filename.startsWith("caGrid-Introduce") && filename.endsWith(".jar");
            boolean security = (filename.startsWith("caGrid-ServiceSecurityProvider") || filename
                .startsWith("caGrid-metadata-security"))
                && filename.endsWith(".jar");
           
            boolean gridGrouper = (filename.startsWith("caGrid-gridgrouper")) && filename.endsWith(".jar");
            if (gridGrouper) {
                hadGridGrouperJars = true;
            }
            boolean csm = (filename.startsWith("caGrid-authz-common")) && filename.endsWith(".jar");
            if (csm) {
                hadCSMJars = true;
            }

            boolean otherSecurityJarsNotNeeded = (filename.startsWith("caGrid-gridca"))
                && filename.endsWith(".jar");

            boolean wsrf = (filename.startsWith("globus_wsrf_mds") || filename.startsWith("globus_wsrf_servicegroup"))
                && filename.endsWith(".jar");
            boolean mobius = filename.startsWith("mobius") && filename.endsWith(".jar");

            return core || advertisement || introduce || security || gridGrouper || csm || wsrf || mobius
                || otherSecurityJarsNotNeeded;
        }

    };


    protected void upgrade() throws Exception {

        upgradeJars();
        
        getStatus().setStatus(StatusBase.UPGRADE_OK);
    }


    
    private void upgradeJars() throws Exception {

        OldJarsFilter oldDkeletonLibFilter = new OldJarsFilter();

        // locate the old libs in the service
        File serviceLibDir = new File(getServicePath() + File.separator + "lib");
        File[] serviceLibs = serviceLibDir.listFiles(oldDkeletonLibFilter);
        // delete the old libraries
        for (int i = 0; i < serviceLibs.length; i++) {
            boolean deleted = serviceLibs[i].delete();
            if (deleted) {
                getStatus().addDescriptionLine(serviceLibs[i].getName() + " removed");
            } else {
                getStatus().addDescriptionLine(serviceLibs[i].getName() + " could not be removed");
            }
        }

        FileFilter srcSkeletonLibFilter = new FileFilter() {
            public boolean accept(File name) {
                String filename = name.getName();
                return filename.endsWith(".jar");
            }
        };

        File skeletonLibDir = new File("skeleton" + File.separator + "lib");
        // File extLibDir = new File("ext" + File.separator + "lib");
        File csmLibDir = new File("ext" + File.separator + "skeleton" + File.separator + "csm" + File.separator + "lib");
        File grouperLibDir = new File("ext" + File.separator + "skeleton" + File.separator + "gridgrouper"
            + File.separator + "lib");

        // copy new libraries in (every thing in skeleton/lib)
        File[] skeletonLibs = skeletonLibDir.listFiles(srcSkeletonLibFilter);
        for (int i = 0; i < skeletonLibs.length; i++) {
            File out = new File(serviceLibDir.getAbsolutePath() + File.separator + skeletonLibs[i].getName());
            try {
                Utils.copyFile(skeletonLibs[i], out);
                getStatus().addDescriptionLine(skeletonLibs[i].getName() + " added");
            } catch (IOException ex) {
                throw new Exception("Error copying library (" + skeletonLibs[i] + ") to service: " + ex.getMessage(),
                    ex);
            }
        }

        if (oldDkeletonLibFilter.hadGridGrouperJars) {
            //TODO
        }

        if (oldDkeletonLibFilter.hadCSMJars) {
            //TODO
        }

    }

}
