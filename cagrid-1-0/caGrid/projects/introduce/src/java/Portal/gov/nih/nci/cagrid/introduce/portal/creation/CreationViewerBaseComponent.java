package gov.nih.nci.cagrid.introduce.portal.creation;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.BusyDialogRunnable;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ResourceManager;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.portal.modification.ModificationViewer;
import gov.nih.nci.cagrid.introduce.statistics.StatisticsClient;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.projectmobius.portal.GridPortalComponent;
import org.projectmobius.portal.PortalResourceManager;


/**
 * CreationViewerBaseComponent This is the base component for developing and
 * creation viewer. A base implementation is porvided, however, to create
 * specific look-feel or additions one should extend this panel and add this
 * componenet to the introduce portal configuration.
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella</A>
 * @created Jul 7, 2006
 */
public abstract class CreationViewerBaseComponent extends GridPortalComponent {

    /**
     * Will call the create service engine component to create the base
     * framework for the grid service
     * 
     * @param dir
     *            the path to the location to create the service
     * @param service
     *            the name of the service
     * @param servicePackage
     *            the package name to use for the created service
     * @param serviceNamespace
     *            the namespace to be used for this services wsdl and stubs
     * @param extensions
     *            a list of strings with the display names of the extensions to
     *            be added
     */
    public void createService(final String dir, final String service, final String servicePackage,
        final String serviceNamespace, final List extensions) {
        int doIdeleteResult = JOptionPane.OK_OPTION;
        final File dirFile = new File(dir);
        if (dirFile.exists() && dirFile.list().length != 0) {
            doIdeleteResult = JOptionPane.NO_OPTION;
            File duceXML = new File(dirFile.getAbsolutePath() + File.separator + IntroduceConstants.INTRODUCE_XML_FILE);
            if (duceXML.exists()) {
                doIdeleteResult = JOptionPane.showConfirmDialog(this, "The creation directory ("
                    + dirFile.getAbsolutePath() + ") is not empty.  All information in the directory will be lost.",
                    "Confirm Overwrite", JOptionPane.YES_NO_OPTION);
            } else {
                JOptionPane.showMessageDialog(this, "The creation directory (" + dirFile.getAbsolutePath()
                    + ") is not empty, and does not appear to be an Introduce-created service."
                    + "  You must manually delete the directory, or specify a different directory.");
            }
        }

        if (doIdeleteResult == JOptionPane.OK_OPTION) {
            
            CreationViewerBaseComponent.this.setVisible(false);
            dispose();
            
            
            BusyDialogRunnable r = new BusyDialogRunnable(PortalResourceManager.getInstance().getGridPortal(),
                "Creating") {
                @Override
                public void process() {
                    try {
                        setProgressText("Validating service name...");
                        String serviceName = service;
                        String dirName = dir;
                        String packageName = servicePackage;
                        String serviceNsDomain = serviceNamespace;
              

                        if (dirFile.exists()) {
                            setProgressText("Deleting existing directory...");
                            boolean deleted = Utils.deleteDir(dirFile);
                            if (!deleted) {
                                setErrorMessage("Unable to delete creation directory");
                                return;
                            }
                        }

                        setProgressText("Purging old archives...");
                        ResourceManager.purgeArchives(serviceName);

                        String serviceExtensions = "";
                        for (int i = 0; i < extensions.size(); i++) {
                            ServiceExtensionDescriptionType edt = ExtensionsLoader.getInstance()
                                .getServiceExtensionByDisplayName((String) extensions.get(i));
                            serviceExtensions += edt.getName();
                            if (i < extensions.size() - 1) {
                                serviceExtensions += ",";
                            }
                        }

                        setProgressText("Creating service...");

                        StatisticsClient.sendCreatedServiceStat(CommonTools.getIntroduceVersion(), serviceName,
                            serviceNsDomain, serviceExtensions);

                        String cmd = CommonTools.getAntSkeletonCreationCommand(".", serviceName, dirName, packageName,
                            serviceNsDomain, serviceExtensions);
                        Process p = CommonTools.createAndOutputProcess(cmd);
                        p.waitFor();
                        if (p.exitValue() != 0) {
                            setErrorMessage("Error creating new service!  Please check the console output for more details.");
                            return;
                        }

                        setProgressText("Invoking extension viewers...");
                        Properties properties = new Properties();
                        properties.load(new FileInputStream(dir + File.separator
                            + IntroduceConstants.INTRODUCE_PROPERTIES_FILE));
                        ServiceDescription introService = (ServiceDescription) Utils.deserializeDocument(dir
                            + File.separator + IntroduceConstants.INTRODUCE_XML_FILE, ServiceDescription.class);
                        ServiceInformation info = new ServiceInformation(introService, properties, new File(dir));

                        for (int i = 0; i < extensions.size(); i++) {
                            ServiceExtensionDescriptionType edt = ExtensionsLoader.getInstance()
                                .getServiceExtensionByDisplayName((String) extensions.get(i));
                            JDialog extDialog = gov.nih.nci.cagrid.introduce.portal.extension.ExtensionTools
                                .getCreationUIDialog(PortalResourceManager.getInstance().getGridPortal(),
                                    edt.getName(), info);
                            if (extDialog != null) {
                                PortalUtils.centerComponent(extDialog);
                                extDialog.setVisible(true);
                            }
                        }

                        Utils.serializeDocument(dir + File.separator + IntroduceConstants.INTRODUCE_XML_FILE,
                            introService, IntroduceConstants.INTRODUCE_SKELETON_QNAME);

                        setProgressText("Invoking post creation processes...");
                        cmd = CommonTools.getAntSkeletonPostCreationCommand(".", serviceName, dirName, packageName,
                            serviceNsDomain, serviceExtensions);
                        p = CommonTools.createAndOutputProcess(cmd);
                        p.waitFor();
                        if (p.exitValue() != 0) {
                            setErrorMessage("Error during service post creations!");
                            return;
                        }

                        setProgressText("Building created service...");
                        cmd = CommonTools.getAntAllCommand(dirName);
                        p = CommonTools.createAndOutputProcess(cmd);
                        p.waitFor();
                        if (p.exitValue() == 0) {
                            setProgressText("Launching modification viewer...");
                            ModificationViewer modViewer = new ModificationViewer(new File(dirName));
                            PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(modViewer);
                            modViewer.setMaximum(true);
                        } else {
                            setErrorMessage("Error creating new service!");
                            return;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        setErrorMessage("Error: " + ex.getMessage());
                        return;
                    }
                }
            };

            Thread th = new Thread(r);
            th.start();
            

        }
    }

}
