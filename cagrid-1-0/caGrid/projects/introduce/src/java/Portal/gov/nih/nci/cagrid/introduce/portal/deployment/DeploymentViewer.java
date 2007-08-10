package gov.nih.nci.cagrid.introduce.portal.deployment;

import gov.nih.nci.cagrid.common.portal.BusyDialogRunnable;
import gov.nih.nci.cagrid.common.portal.ErrorDialog;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.property.ServicePropertiesProperty;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ResourceManager;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;
import gov.nih.nci.cagrid.introduce.portal.extension.ServiceDeploymentUIPanel;
import gov.nih.nci.cagrid.introduce.statistics.StatisticsClient;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import org.projectmobius.portal.GridPortalBaseFrame;
import org.projectmobius.portal.PortalResourceManager;
import java.awt.Dimension;


/**
 * DeploymentViewer
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin </A>
 * @created Jun 22, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class DeploymentViewer extends GridPortalBaseFrame {

    private static final String GLOBUS = "GLOBUS_LOCATION";

    private static final String TOMCAT = "CATALINA_HOME";
    
    private static final String JBOSS = "JBOSS_HOME";

    private ServiceInformation info = null;

    private JPanel deployPropertiesPanel = null;

    private JTabbedPane mainPanel = null;

    private JPanel buttonPanel = null;

    private JButton deployButton = null;

    private File serviceDirectory; // @jve:decl-index=0:

    private JPanel deploymentTypePanel = null;

    private JComboBox deploymentTypeSelector = null;

    private JPanel servicePropertiesPanel = null;

    private JScrollPane servicePropertiesScrollPane = null;

    private JPanel defaultPanel = null;

    private JPanel holderPanel = null;


    /**
     * This method initializes
     */
    public DeploymentViewer() {
        super();
        promptAndInitialize();
    }


    private void promptAndInitialize() {
        Callable chooserCallable = new Callable() {
            public File call() throws Exception {
                String dir = ResourceManager.promptDir(null);
                if (dir != null) {
                    return new File(dir);
                }
                return null;
            }
        };
        FutureTask<File> choiceTask = new FutureTask(chooserCallable);
        ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.submit(choiceTask);

        try {
            serviceDirectory = choiceTask.get();
        } catch (Exception ex) {
            ex.printStackTrace();
            ErrorDialog.showErrorDialog("Error selecting service directory", ex.getMessage(), ex);
        }

        if (serviceDirectory == null) {
            DeploymentViewer.this.dispose();
            return;
        }
        if (serviceDirectory.exists() && serviceDirectory.canRead()) {
            // init must be done on the Swing worker thread
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    try {
                        initialize();
                    } catch (Exception e) {
                        ErrorDialog.showErrorDialog("Error initializing the deployment: " + e.getMessage(), e);
                        DeploymentViewer.this.dispose();
                    }
                }
            });
        } else {
            ErrorDialog.showErrorDialog("Error deleting directory", "Directory " + serviceDirectory.getAbsolutePath()
                + " does not seem to be an introduce service");
            DeploymentViewer.this.dispose();
        }
    }


    /**
     * This method initializes this
     * 
     */
    private void initialize() throws Exception {
        this.info = new ServiceInformation(serviceDirectory);

        if (info != null) {
            // load up the deploy properties;
            Enumeration keys = info.getDeploymentProperties().keys();
            int i = 0;
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                this.addTextField(this.getDeployPropertiesPanel(), key,
                    info.getDeploymentProperties().getProperty(key), i++, true);
            }

            // load up the service properties
            if (info != null && info.getServiceDescriptor().getServiceProperties() != null
                && info.getServiceDescriptor().getServiceProperties().getProperty() != null) {
                for (i = 0; i < info.getServiceDescriptor().getServiceProperties().getProperty().length; i++) {
                    ServicePropertiesProperty prop = info.getServiceProperties().getProperty(i);
                    this.addTextField(this.getServicePropertiesPanel(), prop.getKey(), prop.getValue(), i, true);
                    this.getTextField(prop.getKey()).setForeground(Color.BLUE);

                }
            }
        }

        this.setFrameIcon(IntroduceLookAndFeel.getDeployIcon());
        this.setSize(new Dimension(301, 365));
        this.setContentPane(getHolderPanel());
        this.setTitle("Deploy Grid Service");
        pack();
    }


    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getDeployPropertiesPanel() {
        if (deployPropertiesPanel == null) {
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints10.gridy = 3;
            gridBagConstraints10.weightx = 1.0;
            gridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints10.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints10.gridwidth = 2;
            gridBagConstraints10.weighty = 1.0D;
            gridBagConstraints10.gridx = 1;
            deployPropertiesPanel = new JPanel();
            deployPropertiesPanel.setLayout(new GridBagLayout());
            deployPropertiesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
                null, "Deployment Properties", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.gridx = 0;
            gridBagConstraints9.gridy = 3;
            gridBagConstraints9.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints9.anchor = java.awt.GridBagConstraints.WEST;
        }
        return deployPropertiesPanel;
    }


    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JTabbedPane getMainPanel() {
        if (mainPanel == null) {
            mainPanel = new JTabbedPane();
            mainPanel.addTab("General Deployment", getDefaultPanel());
            // run any extensions that need to be ran

            if ((info != null && info.getServiceDescriptor().getExtensions() != null)
                && (info.getServiceDescriptor().getExtensions().getExtension() != null)) {
                ExtensionType[] extensions = info.getServiceDescriptor().getExtensions().getExtension();
                for (ExtensionType element : extensions) {
                    ServiceExtensionDescriptionType edesc = ExtensionsLoader.getInstance()
                        .getServiceExtension(element.getName());
                    if (edesc != null) {
                        try {
                            ServiceDeploymentUIPanel depPanel = gov.nih.nci.cagrid.introduce.portal.extension.ExtensionTools
                                .getServiceDeploymentUIPanel(element.getName(), info);
                            if (depPanel != null) {
                                mainPanel.addTab(edesc.getDisplayName(), depPanel);
                            }
                        } catch (Exception ex) {
                            ErrorDialog.showErrorDialog("Error loading deployment UI for extension "
                                + element.getName(), ex.getMessage(), ex);
                        }
                    }
                }
            }
            mainPanel.addChangeListener(new javax.swing.event.ChangeListener() {
                public void stateChanged(javax.swing.event.ChangeEvent e) {
                    for (int i = 0; i < getMainPanel().getTabCount(); i++) {
                        Component tab = getMainPanel().getComponentAt(i);
                        if (tab instanceof ServiceDeploymentUIPanel) {
                            ((ServiceDeploymentUIPanel) tab).resetGUI();
                        }
                    }
                }
            });
        }
        return mainPanel;
    }


    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getButtonPanel() {
        if (buttonPanel == null) {
            buttonPanel = new JPanel();
            buttonPanel.add(getDeployButton(), null);
        }
        return buttonPanel;
    }


    /**
     * This method initializes jButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getDeployButton() {
        if (deployButton == null) {
            deployButton = new JButton();
            deployButton.setText("Deploy");
            deployButton.setIcon(IntroduceLookAndFeel.getDeployIcon());
            deployButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    BusyDialogRunnable r = new BusyDialogRunnable(
                        PortalResourceManager.getInstance().getGridPortal(), "Deployment") {

                        public void process() {
                            StatisticsClient.sendDeployedServiceStat(CommonTools.getIntroduceVersion(), 
                                info.getServiceDescriptor().getServices().getService(0).getName(), 
                                info.getServices().getService(0).getNamespace(), 
                                (String) getDeploymentTypeSelector().getSelectedItem());

                            setProgressText("setting introduce resource properties...");

                            try {
                                ResourceManager.setStateProperty(ResourceManager.LAST_DEPLOYMENT,
                                    (String) deploymentTypeSelector.getSelectedItem());
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }

                            setProgressText("writing deployment property file");

                            Enumeration keys = info.getDeploymentProperties().keys();
                            while (keys.hasMoreElements()) {
                                String key = (String) keys.nextElement();
                                String value = getTextFieldValue(key);
                                info.getDeploymentProperties().setProperty(key, value);
                            }

                            try {
                                info.getDeploymentProperties().store(
                                    new FileOutputStream(new File(serviceDirectory.getAbsolutePath() 
                                        + File.separator + "deploy.properties")), 
                                        "introduce service deployment properties");
                            } catch (FileNotFoundException ex) {
                                ex.printStackTrace();
                                setErrorMessage("Error: " + ex.getMessage());
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                setErrorMessage("Error: " + ex.getMessage());
                            }

                            Properties serviceProps = new Properties();
                            // load up the service properties
                            if (info.getServiceDescriptor().getServiceProperties() != null
                                && info.getServiceDescriptor().getServiceProperties().getProperty() != null) {
                                for (int i = 0; i < info.getServiceProperties().getProperty().length; i++) {
                                    ServicePropertiesProperty prop = info.getServiceProperties().getProperty(i);
                                    serviceProps.put(prop.getKey(), getTextFieldValue(prop.getKey()));
                                }
                            }

                            try {
                                serviceProps.store(new FileOutputStream(new File(serviceDirectory.getAbsolutePath()
                                    + File.separator + IntroduceConstants.INTRODUCE_SERVICE_PROPERTIES)),
                                    "service deployment properties");
                            } catch (FileNotFoundException ex) {
                                ex.printStackTrace();
                                setErrorMessage("Error: " + ex.getMessage());
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                setErrorMessage("Error: " + ex.getMessage());
                            }

                            setProgressText("deploying");

                            try {
                                String cmd = "";
                                if (((String) getDeploymentTypeSelector().getSelectedItem()).equals(GLOBUS)) {
                                    cmd = CommonTools.getAntDeployGlobusCommand(serviceDirectory.getAbsolutePath());
                                } else if (((String) getDeploymentTypeSelector().getSelectedItem()).equals(TOMCAT)) {
                                    cmd = CommonTools.getAntDeployTomcatCommand(serviceDirectory.getAbsolutePath());
                                } else {
                                    cmd = CommonTools.getAntDeployJBossCommand(serviceDirectory.getAbsolutePath());
                                }
                                Process p = CommonTools.createAndOutputProcess(cmd);
                                p.waitFor();
                                if (p.exitValue() != 0) {
                                    setErrorMessage("Error deploying service!");
                                }
                            } catch (Exception ex) {
                                setErrorMessage("Error deploying service! " + ex.getMessage());
                                ex.printStackTrace();
                            }
                            dispose();
                        }
                    };
                    Thread th = new Thread(r);
                    th.start();
                }
            });
        }

        return deployButton;
    }


    /**
     * This method initializes deploymetnTypePanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getDeploymentTypePanel() {
        if (deploymentTypePanel == null) {
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints2.weightx = 1.0;
            deploymentTypePanel = new JPanel();
            deploymentTypePanel.setLayout(new GridBagLayout());
            deploymentTypePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
                null, "Deployment Location", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
            deploymentTypePanel.add(getDeploymentTypeSelector(), gridBagConstraints2);
        }
        return deploymentTypePanel;
    }


    /**
     * This method initializes deploymentTypeSelector
     * 
     * @return javax.swing.JComboBox
     */
    private JComboBox getDeploymentTypeSelector() {
        if (deploymentTypeSelector == null) {
            deploymentTypeSelector = new JComboBox();
            deploymentTypeSelector.addItem(TOMCAT);
            deploymentTypeSelector.addItem(GLOBUS);
            deploymentTypeSelector.addItem(JBOSS);
            try {
                if (ResourceManager.getStateProperty(ResourceManager.LAST_DEPLOYMENT) != null) {
                    deploymentTypeSelector.setSelectedItem(
                        ResourceManager.getStateProperty(ResourceManager.LAST_DEPLOYMENT));
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return deploymentTypeSelector;
    }


    /**
     * This method initializes servicePropertiesPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getServicePropertiesPanel() {
        if (servicePropertiesPanel == null) {
            servicePropertiesPanel = new JPanel();
            servicePropertiesPanel.setBackground(Color.WHITE);
            servicePropertiesPanel.setLayout(new GridBagLayout());
        }
        return servicePropertiesPanel;
    }


    /**
     * This method initializes servicePropertiesScrollPane
     * 
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getServicePropertiesScrollPane() {
        if (servicePropertiesScrollPane == null) {
            servicePropertiesScrollPane = new JScrollPane();
            servicePropertiesScrollPane.setSize(new Dimension(400,200));
            servicePropertiesScrollPane.setPreferredSize(new Dimension(400,200));
            servicePropertiesScrollPane.setViewportView(getServicePropertiesPanel());
            servicePropertiesScrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder(
                null, "Service Properties", 
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION, null, 
                PortalLookAndFeel.getPanelLabelColor()));
        }
        return servicePropertiesScrollPane;
    }


    /**
     * This method initializes defaultPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getDefaultPanel() {
        if (defaultPanel == null) {
            GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
            gridBagConstraints13.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints13.weighty = 1.0;
            gridBagConstraints13.gridx = 0;
            gridBagConstraints13.gridy = 2;
            gridBagConstraints13.weightx = 1.0;
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.gridx = 0;
            gridBagConstraints11.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints11.gridy = 0;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.gridy = 3;
            gridBagConstraints1.anchor = java.awt.GridBagConstraints.SOUTH;
            gridBagConstraints1.weighty = 0.0D;
            gridBagConstraints1.weightx = 1.0D;
            gridBagConstraints1.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.gridheight = 1;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridheight = 1;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
            gridBagConstraints.weightx = 0.0D;
            gridBagConstraints.weighty = 0.0D;
            gridBagConstraints.gridwidth = 1;
            defaultPanel = new JPanel();
            defaultPanel.setLayout(new GridBagLayout());
            defaultPanel.add(getDeployPropertiesPanel(), gridBagConstraints);
            defaultPanel.add(getButtonPanel(), gridBagConstraints1);
            defaultPanel.add(getDeploymentTypePanel(), gridBagConstraints11);
            defaultPanel.add(getServicePropertiesScrollPane(), gridBagConstraints13);
        }
        return defaultPanel;
    }


    /**
     * This method initializes holderPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getHolderPanel() {
        if (holderPanel == null) {
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.fill = GridBagConstraints.BOTH;
            gridBagConstraints3.weighty = 1.0;
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.gridy = 0;
            gridBagConstraints3.weightx = 1.0;
            holderPanel = new JPanel();
            holderPanel.setLayout(new GridBagLayout());
            holderPanel.add(getMainPanel(), gridBagConstraints3);
        }
        return holderPanel;
    }
}  //  @jve:decl-index=0:visual-constraint="10,10"
