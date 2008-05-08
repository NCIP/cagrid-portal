package gov.nih.nci.cagrid.introduce.portal.modification.security;

import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.common.security.ProxyUtil;
import gov.nih.nci.cagrid.gridgrouper.bean.MembershipExpression;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.security.AnonymousCommunication;
import gov.nih.nci.cagrid.introduce.beans.security.GridMapAuthorization;
import gov.nih.nci.cagrid.introduce.beans.security.MethodSecurity;
import gov.nih.nci.cagrid.introduce.beans.security.NoAuthorization;
import gov.nih.nci.cagrid.introduce.beans.security.ProxyCredential;
import gov.nih.nci.cagrid.introduce.beans.security.RunAsMode;
import gov.nih.nci.cagrid.introduce.beans.security.SecureConversation;
import gov.nih.nci.cagrid.introduce.beans.security.SecureMessage;
import gov.nih.nci.cagrid.introduce.beans.security.SecuritySetting;
import gov.nih.nci.cagrid.introduce.beans.security.ServiceAuthorization;
import gov.nih.nci.cagrid.introduce.beans.security.ServiceCredential;
import gov.nih.nci.cagrid.introduce.beans.security.ServiceSecurity;
import gov.nih.nci.cagrid.introduce.beans.security.TransportLevelSecurity;
import gov.nih.nci.cagrid.introduce.beans.security.X509Credential;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;

import org.cagrid.gaards.pki.CertUtil;
import org.cagrid.gaards.ui.common.CertificatePanel;
import org.cagrid.gaards.ui.common.CredentialPanel;
import org.cagrid.gaards.ui.gridgrouper.expressioneditor.GridGrouperExpressionEditor;
import org.cagrid.grape.GridApplication;
import org.cagrid.grape.utils.CompositeErrorDialog;
import java.awt.Insets;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @created Jun 22, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class ServiceSecurityPanel extends JPanel implements PanelSynchronizer {

    private JPanel secureCommunicationPanel = null;

    private ButtonGroup buttonGroup = new ButtonGroup();

    private JRadioButton noneButton = null;

    private JRadioButton customButton = null;

    private TransportLayerSecurityPanel tlsPanel = null;

    private JCheckBox tlsButton = null;

    private JCheckBox secureConversationButton = null;

    private JPanel choicePanel = null;

    private JPanel commPanel = null;

    private JLabel jLabel1 = null;

    private JLabel Custom = null;

    private JLabel jLabel2 = null;

    private JLabel jLabel3 = null;

    private JCheckBox secureMessageButton = null;

    private JLabel jLabel4 = null;

    private SecureConversationPanel secureConversationPanel = null;

    private SecureMessagePanel secureMessagePanel = null;

    private JComboBox runAsMode = null;

    private JComboBox anonymousCommunication = null;

    private JTabbedPane transportPanel = null;

    private boolean isInited = false;

    private final static String NO_AUTHORIZATION = "No Authorization";

    private final static String GRID_MAP_AUTHORIZATION = "Gridmap";

    private final static String GRID_GROUPER_AUTHORIZATION = "Grid Grouper";

    private final static String CSM_AUTHORIZATION = "Common Security Module (CSM)";

    private final static String CUSTOM_AUTHORIZATION = "Custom PDP Chain Authorization";

    private final static String FILE_SYSTEM_PROXY = "Proxy from file system"; // @jve:decl-index=0:

    private final static String FILE_SYSTEM_CERT_KEY = "Certificate/Private Key from file system";

    private final static String PROXY_CRED_PANEL = "Proxy Cred Panel";

    private final static String PKI_CRED_PANEL = "PKI Cred Panel";

    private final static String N0_CRED_PANEL = "No Cred Panel";

    private JPanel communicationPanel = null;

    private JPanel generalSecurity = null;

    private JLabel jLabel = null;

    private JPanel serviceCredentials = null;

    private JPanel selectPanel = null;

    private JComboBox credentialLoadMethod = null;

    private JButton loadCredentialsButton = null;

    private String certificateLocation;

    private String privateKeyLocation;

    private String proxyLocation;

    private JPanel credentialsPanel = null;

    private CardLayout credentialPanelLayout;

    private JPanel nonePanel = null;

    private CertificatePanel certificatePanel = null;

    private CredentialPanel proxyPanel = null;

    private boolean isSyncingRunAs = false;

    private JPanel authorizationPanel = null;

    private JPanel authorizationTypePanel = null;

    private JComboBox authorizationMechanism = null;

    private CardLayout authLayout;

    private JPanel authPanel = null;

    private JPanel noAuthorizationPanel = null;

    private JPanel gridmapPanel = null;

    private JPanel jPanel = null;

    private JLabel jLabel6 = null;

    private JPanel jPanel1 = null;

    private JLabel credentialsRequired = null;

    private JLabel authLabel = null;

    private ServiceType service;

    private GridGrouperExpressionEditor gridGrouper = null;

    private CSMPanel csmPanel = null;

    private ServiceDescription description;

    private CustomPDPPanel pdpPanel = null;

    private String previousAuthSelection;


    public ServiceSecurityPanel(ServiceDescription description, ServiceType service) {
        super();
        this.description = description;
        this.service = service;
        initialize();
        try {
            if (this.service.getServiceSecurity() != null) {
                setServiceSecurity(this.service.getServiceSecurity());
            }
        } catch (Exception e) {
            // PortalUtils.showErrorDialogDialog(e);
            CompositeErrorDialog.showErrorDialog(e);
        }
    }


    private void initialize() {
        GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
        gridBagConstraints17.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints17.weighty = 1.0;
        gridBagConstraints17.gridx = 0;
        gridBagConstraints17.gridy = 2;
        gridBagConstraints17.insets = new java.awt.Insets(2, 2, 2, 2);
        gridBagConstraints17.weightx = 1.0;
        GridBagConstraints gridBagConstraints71 = new GridBagConstraints();
        gridBagConstraints71.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints71.weightx = 1.0D;
        this.setLayout(new GridBagLayout());
        this.add(getSecureCommunicationPanel(), gridBagConstraints71);
        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Security Configuration",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
            new Font("Dialog", Font.BOLD, 12), PortalLookAndFeel.getPanelLabelColor()));
        this.add(getTransportPanel(), gridBagConstraints17);
        synchronize();
        isInited = true;
    }


    /**
     * This method initializes secureCommunicationPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getSecureCommunicationPanel() {
        if (secureCommunicationPanel == null) {
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.insets = new Insets(0, 0, 0, 0);
            gridBagConstraints8.gridy = 0;
            gridBagConstraints8.gridx = 0;
            secureCommunicationPanel = new JPanel();
            secureCommunicationPanel.setLayout(new GridBagLayout());
            secureCommunicationPanel.add(getChoicePanel(), gridBagConstraints8);
        }
        return secureCommunicationPanel;
    }


    /**
     * This method initializes noneButton
     * 
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getNoneButton() {
        if (noneButton == null) {
            noneButton = new JRadioButton();
            noneButton.setSelected(true);
            noneButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    synchronize();
                }
            });
            buttonGroup.add(noneButton);
        }
        return noneButton;
    }


    /**
     * This method initializes customButton
     * 
     * @return javax.swing.JRadioButton
     */
    private JRadioButton getCustomButton() {
        if (customButton == null) {
            customButton = new JRadioButton();
            customButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    synchronize();
                }
            });
            buttonGroup.add(customButton);
        }
        return customButton;
    }


    private boolean isSecure() {
        if (usesTransportSecurity() || usesSecureConversation() || usesSecureMessage()) {
            return true;
        } else {
            return false;
        }

    }


    private boolean usesTransportSecurity() {
        if (tlsButton.isSelected()) {
            return true;
        } else {
            return false;
        }
    }


    private boolean usesSecureConversation() {
        if (secureConversationButton.isSelected()) {
            return true;
        } else {
            return false;
        }
    }


    private boolean usesSecureMessage() {
        if (secureMessageButton.isSelected()) {
            return true;
        } else {
            return false;
        }
    }


    public ServiceSecurity getServiceSecurity(boolean isSave) throws Exception {

        if (customButton.isSelected()) {
            ServiceSecurity ss = new ServiceSecurity();
            ss.setSecuritySetting(SecuritySetting.Custom);
            if (!isSecure()) {
                throw new Exception("You must select at least one transport mechanism!!!");
            }

            if (tlsButton.isSelected()) {
                ss.setTransportLevelSecurity(tlsPanel.getTransportLevelSecurity());

            }
            if (secureConversationButton.isSelected()) {
                ss.setSecureConversation(secureConversationPanel.getSecureConversation());

            }
            if (secureMessageButton.isSelected()) {
                ss.setSecureMessage(secureMessagePanel.getSecureMessage());
            }
            if (runAsMode.isEnabled()) {
                ss.setRunAsMode((RunAsMode) runAsMode.getSelectedItem());
            }

            if (anonymousCommunication.isEnabled()) {
                ss.setAnonymousClients((AnonymousCommunication) anonymousCommunication.getSelectedItem());
            } else {
                ss.setAnonymousClients(AnonymousCommunication.No);
            }

            if ((certificateLocation != null) && (privateKeyLocation != null)) {
                ServiceCredential cred = new ServiceCredential();
                X509Credential x509 = new X509Credential();
                x509.setCertificateLocation(certificateLocation);
                x509.setPrivateKeyLocation(privateKeyLocation);
                cred.setX509Credential(x509);
                ss.setServiceCredentials(cred);
            } else if (proxyLocation != null) {
                ServiceCredential cred = new ServiceCredential();
                ProxyCredential proxy = new ProxyCredential();
                proxy.setProxyLocation(proxyLocation);
                cred.setProxyCredential(proxy);
                ss.setServiceCredentials(cred);
            }

            String authType = (String) authorizationMechanism.getSelectedItem();
            ServiceAuthorization sa = new ServiceAuthorization();
            if (authType.equals(GRID_MAP_AUTHORIZATION)) {
                GridMapAuthorization gma = new GridMapAuthorization();
                gma.setGridMapFileLocation(((GridMapPanel) gridmapPanel).saveGridMapAndGetLocation());
                sa.setGridMapAuthorization(gma);
            } else if (authType.equals(GRID_GROUPER_AUTHORIZATION)) {
                // TODO: Validate the expression
                MembershipExpression exp = getGridGrouper().getMembershipExpression();
                sa.setGridGrouperAuthorization(exp);
            } else if (authType.equals(CSM_AUTHORIZATION)) {
                CommonTools.setServiceProperty(description, CSMPanel.CSM_CONFIGURATION_FILE, "", false);
                sa.setCSMAuthorization(getCsmPanel().getAuthorization());
            } else if (authType.equals(CUSTOM_AUTHORIZATION)) {
                sa.setCustomPDPChainAuthorization(getPdpPanel().getAuthorization());
            } else {
                sa.setNoAuthorization(new NoAuthorization());
            }
            ss.setServiceAuthorization(sa);
            return ss;
        } else {
            return null;
        }
    }


    public void setServiceSecurity(ServiceSecurity ss) throws Exception {
        if ((ss != null) && (ss.getSecuritySetting() != null)) {
            if (ss.getSecuritySetting().equals(SecuritySetting.None)) {
                noneButton.setSelected(true);
            } else if (ss.getSecuritySetting().equals(SecuritySetting.Custom)) {
                customButton.setSelected(true);

                TransportLevelSecurity tls = ss.getTransportLevelSecurity();
                if (tls != null) {
                    tlsButton.setSelected(true);
                    tlsPanel.setTransportLevelSecurity(tls);
                }
                SecureConversation sc = ss.getSecureConversation();
                if (sc != null) {
                    secureConversationButton.setSelected(true);
                    secureConversationPanel.setSecureConversation(sc);
                }
                SecureMessage sm = ss.getSecureMessage();
                if (sm != null) {
                    secureMessageButton.setSelected(true);
                    secureMessagePanel.setSecureMessage(sm);
                }

                RunAsMode runas = ss.getRunAsMode();
                if (runas != null) {
                    runAsMode.setSelectedItem(runas);
                }

                AnonymousCommunication anon = ss.getAnonymousClients();
                if (anon != null) {
                    anonymousCommunication.setSelectedItem(anon);
                }

                ServiceCredential scred = ss.getServiceCredentials();
                if (scred != null) {
                    this.setCredentials(scred.getX509Credential());
                    this.setProxy(scred.getProxyCredential());
                }

                ServiceAuthorization sa = ss.getServiceAuthorization();
                if (sa != null) {
                    if (sa.getGridMapAuthorization() != null) {
                        String location = sa.getGridMapAuthorization().getGridMapFileLocation();
                        if (location != null) {
                            ((GridMapPanel) gridmapPanel).setGridMapFile(location);
                            authorizationMechanism.setSelectedItem(GRID_MAP_AUTHORIZATION);
                        } else {
                            CompositeErrorDialog.showErrorDialog("No GridMap file specified!!!");
                            authorizationMechanism.setSelectedItem(NO_AUTHORIZATION);
                        }
                    } else if (sa.getGridGrouperAuthorization() != null) {
                        this.getGridGrouper().setExpression(sa.getGridGrouperAuthorization());
                        authorizationMechanism.setSelectedItem(GRID_GROUPER_AUTHORIZATION);
                    } else if (sa.getCSMAuthorization() != null) {
                        this.getCsmPanel().setAuthorization(sa.getCSMAuthorization());
                        authorizationMechanism.setSelectedItem(CSM_AUTHORIZATION);
                    } else if (sa.getCustomPDPChainAuthorization() != null) {
                        this.getPdpPanel().setAuthorization(sa.getCustomPDPChainAuthorization());
                        authorizationMechanism.setSelectedItem(CUSTOM_AUTHORIZATION);
                    } else {
                        authorizationMechanism.setSelectedItem(NO_AUTHORIZATION);
                    }
                    previousAuthSelection = (String) authorizationMechanism.getSelectedItem();
                }
                synchronize();
            }
        }

    }


    public void synchronize() {
        disableAll();
        if (customButton.isSelected()) {
            tlsButton.setEnabled(true);
            secureConversationButton.setEnabled(true);
            secureMessageButton.setEnabled(true);

            if (tlsButton.isSelected()) {
                tlsPanel.enablePanel();
            }
            if (secureConversationButton.isSelected()) {
                secureConversationPanel.enablePanel();
            }
            if (secureMessageButton.isSelected()) {
                secureMessagePanel.enablePanel();
            }

            synchRunAsMode();
            if (isSecure()) {
                runAsMode.setEnabled(true);
            }
            this.syncServiceCredentials();

            this.syncAuthorization();
            this.syncAnonymousCommunication();

        }
    }


    private void syncAnonymousCommunication() {

        if (usesTransportSecurity() || usesSecureConversation()) {
            if (getAuthorizationMechanism().getSelectedItem().equals(NO_AUTHORIZATION)) {
                anonymousCommunication.setEnabled(true);
            } else {
                anonymousCommunication.setEnabled(false);
            }
        } else {
            anonymousCommunication.setEnabled(false);
        }
    }


    public void disableAll() {
        tlsPanel.disablePanel();
        tlsButton.setEnabled(false);
        secureConversationButton.setEnabled(false);
        secureMessageButton.setEnabled(false);
        secureConversationPanel.disablePanel();
        secureMessagePanel.disablePanel();
        runAsMode.setEnabled(false);
        anonymousCommunication.setEnabled(false);
        credentialLoadMethod.setEnabled(false);
        loadCredentialsButton.setEnabled(false);
        credentialPanelLayout.show(credentialsPanel, N0_CRED_PANEL);
        authorizationMechanism.setEnabled(false);
        authLayout.show(authPanel, NO_AUTHORIZATION);
    }


    private void syncServiceCredentials() {
        if ((secureConversationButton.isSelected()) || (secureMessageButton.isSelected())) {
            credentialLoadMethod.setEnabled(true);
            loadCredentialsButton.setEnabled(true);
            if ((certificateLocation != null) && (privateKeyLocation != null)) {
                credentialPanelLayout.show(credentialsPanel, PKI_CRED_PANEL);
            } else if (proxyLocation != null) {
                credentialPanelLayout.show(credentialsPanel, PROXY_CRED_PANEL);
            } else {
                credentialPanelLayout.show(credentialsPanel, N0_CRED_PANEL);
            }
        } else {
            credentialLoadMethod.setEnabled(false);
            credentialPanelLayout.show(credentialsPanel, N0_CRED_PANEL);
        }
    }


    private void syncAuthorization() {
        if (isSecure()) {
            authorizationMechanism.setEnabled(true);
            String mech = (String) authorizationMechanism.getSelectedItem();

            authLayout.show(authPanel, mech);

        } else {
            authorizationMechanism.setEnabled(false);
            authLayout.show(authPanel, NO_AUTHORIZATION);
        }
    }


    private synchronized void synchRunAsMode() {
        if (!isSyncingRunAs) {
            isSyncingRunAs = true;
            runAsMode.removeAllItems();
            if (isSecure()) {
                runAsMode.setEnabled(true);
                runAsMode.addItem(RunAsMode.System);
                if (hasServiceCredentials()) {
                    runAsMode.addItem(RunAsMode.Service);
                }

                if (!getAnonymousCommunication().isEnabled()) {
                    runAsMode.addItem(RunAsMode.Caller);
                } else if (getAnonymousCommunication().getSelectedItem().equals(AnonymousCommunication.No)) {
                    runAsMode.addItem(RunAsMode.Caller);
                }

            }
            isSyncingRunAs = false;
        }

    }


    private boolean hasServiceCredentials() {
        if ((certificateLocation != null) && (privateKeyLocation != null)) {
            return true;
        } else if (proxyLocation != null) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * This method initializes tlsPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getTlsPanel() {
        if (tlsPanel == null) {
            tlsPanel = new TransportLayerSecurityPanel();
        }
        return tlsPanel;
    }


    /**
     * This method initializes tlsButton
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getTlsButton() {
        if (tlsButton == null) {
            tlsButton = new JCheckBox();
            tlsButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    synchronize();
                }
            });
        }
        return tlsButton;
    }


    /**
     * This method initializes secureConversationButton
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getSecureConversationButton() {
        if (secureConversationButton == null) {
            secureConversationButton = new JCheckBox();
            secureConversationButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    synchronize();
                }
            });

        }
        return secureConversationButton;
    }


    /**
     * This method initializes choicePanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getChoicePanel() {
        if (choicePanel == null) {
            GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
            gridBagConstraints15.insets = new Insets(0, 0, 0, 0);
            gridBagConstraints15.gridy = 0;
            gridBagConstraints15.gridx = 3;
            GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
            gridBagConstraints14.insets = new Insets(0, 5, 0, 0);
            gridBagConstraints14.gridy = 0;
            gridBagConstraints14.gridx = 2;
            GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
            gridBagConstraints13.insets = new Insets(0, 0, 0, 5);
            gridBagConstraints13.gridy = 0;
            gridBagConstraints13.gridx = 1;
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.insets = new Insets(0, 0, 0, 0);
            gridBagConstraints9.gridy = 0;
            gridBagConstraints9.gridx = 0;
            Custom = new JLabel();
            Custom.setText("Custom");
            jLabel1 = new JLabel();
            jLabel1.setText("None");
            choicePanel = new JPanel();
            choicePanel.setLayout(new GridBagLayout());
            choicePanel.add(getNoneButton(), gridBagConstraints9);
            choicePanel.add(jLabel1, gridBagConstraints13);
            choicePanel.add(getCustomButton(), gridBagConstraints14);
            choicePanel.add(Custom, gridBagConstraints15);
        }
        return choicePanel;
    }


    /**
     * This method initializes commPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getCommPanel() {
        if (commPanel == null) {
            jLabel4 = new JLabel();
            jLabel4.setText("Secure Message");
            jLabel3 = new JLabel();
            jLabel3.setText("Secure Conversation");
            jLabel2 = new JLabel();
            jLabel2.setText("Transport Layer Security");
            commPanel = new JPanel();
            commPanel.add(getTlsButton(), null);
            commPanel.add(jLabel2, null);
            commPanel.add(getSecureConversationButton(), null);
            commPanel.add(jLabel3, null);
            commPanel.add(getSecureMessageButton(), null);
            commPanel.add(jLabel4, null);
        }
        return commPanel;
    }


    /**
     * This method initializes secureMessageButton
     * 
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getSecureMessageButton() {
        if (secureMessageButton == null) {
            secureMessageButton = new JCheckBox();
            secureMessageButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    synchronize();
                }
            });
        }
        return secureMessageButton;
    }


    /**
     * This method initializes secureConversationPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getSecureConversationPanel() {
        if (secureConversationPanel == null) {
            secureConversationPanel = new SecureConversationPanel();
        }
        return secureConversationPanel;
    }


    /**
     * This method initializes secureMessagePanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getSecureMessagePanel() {
        if (secureMessagePanel == null) {
            secureMessagePanel = new SecureMessagePanel();
        }
        return secureMessagePanel;
    }


    /**
     * This method initializes runAsMode
     * 
     * @return javax.swing.JComboBox
     */
    private JComboBox getRunAsMode() {
        if (runAsMode == null) {
            runAsMode = new JComboBox();
        }
        return runAsMode;
    }


    /**
     * This method initializes anonymousCommunication
     * 
     * @return javax.swing.JComboBox
     */
    private JComboBox getAnonymousCommunication() {
        if (anonymousCommunication == null) {
            anonymousCommunication = new JComboBox();
            anonymousCommunication.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (isInited) {
                        synchronize();
                    }
                }
            });
            anonymousCommunication.addItem(AnonymousCommunication.No);
            anonymousCommunication.addItem(AnonymousCommunication.Yes);
        }
        return anonymousCommunication;
    }


    /**
     * This method initializes transportPanel
     * 
     * @return javax.swing.JTabbedPane
     */
    private JTabbedPane getTransportPanel() {
        if (transportPanel == null) {
            transportPanel = new JTabbedPane();
            transportPanel.addTab("Secure Communication", null, getCommunicationPanel(), null);
            transportPanel.addTab("Authorization", null, getAuthorizationPanel(), null);
            transportPanel.addTab("Service Credentials", null, getServiceCredentials(), null);
        }
        return transportPanel;
    }


    /**
     * This method initializes communicationPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getCommunicationPanel() {
        if (communicationPanel == null) {
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints11.gridy = 3;
            gridBagConstraints11.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints11.weightx = 1.0D;
            gridBagConstraints11.weighty = 1.0D;
            gridBagConstraints11.gridx = 0;
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints7.gridy = 2;
            gridBagConstraints7.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints7.weightx = 1.0D;
            gridBagConstraints7.weighty = 1.0D;
            gridBagConstraints7.gridx = 0;
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints6.gridy = 1;
            gridBagConstraints6.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints6.weightx = 1.0D;
            gridBagConstraints6.weighty = 1.0D;
            gridBagConstraints6.gridx = 0;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints4.gridy = 0;
            gridBagConstraints4.weightx = 1.0D;
            gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints4.gridx = 0;
            communicationPanel = new JPanel();
            communicationPanel.setLayout(new GridBagLayout());
            communicationPanel.add(getCommPanel(), gridBagConstraints4);
            communicationPanel.add(getTlsPanel(), gridBagConstraints6);
            communicationPanel.add(getSecureConversationPanel(), gridBagConstraints7);
            communicationPanel.add(getSecureMessagePanel(), gridBagConstraints11);
        }
        return communicationPanel;
    }


    /**
     * This method initializes generalSecurity
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getGeneralSecurity() {
        if (generalSecurity == null) {
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.anchor = java.awt.GridBagConstraints.NORTHWEST;
            gridBagConstraints12.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints12.gridx = 1;
            gridBagConstraints12.gridy = 0;
            gridBagConstraints12.weightx = 1.0;
            gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
            GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
            gridBagConstraints21.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints21.gridy = 0;
            gridBagConstraints21.gridx = 0;
            jLabel = new JLabel();
            jLabel.setText("Run As");
            generalSecurity = new JPanel();
            generalSecurity.setLayout(new GridBagLayout());
            generalSecurity.add(jLabel, gridBagConstraints21);
            generalSecurity.add(getRunAsMode(), gridBagConstraints12);
        }
        return generalSecurity;
    }


    /**
     * This method initializes serviceCredentials
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getServiceCredentials() {
        if (serviceCredentials == null) {
            serviceCredentials = new JPanel();
            serviceCredentials.setLayout(new BorderLayout());
            serviceCredentials.add(getCredentialsPanel(), java.awt.BorderLayout.CENTER);
            serviceCredentials.add(getJPanel(), java.awt.BorderLayout.NORTH);
        }
        return serviceCredentials;
    }


    /**
     * This method initializes selectPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getSelectPanel() {
        if (selectPanel == null) {
            GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
            gridBagConstraints24.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints24.gridy = 0;
            gridBagConstraints24.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints24.gridx = 2;
            GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
            gridBagConstraints23.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints23.gridx = 1;
            gridBagConstraints23.gridy = 0;
            gridBagConstraints23.weightx = 1.0;
            gridBagConstraints23.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints23.insets = new Insets(2, 2, 2, 2);
            GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
            gridBagConstraints22.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints22.gridy = 0;
            gridBagConstraints22.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints22.gridx = 0;
            jLabel6 = new JLabel();
            jLabel6.setText("Import Credentials");
            selectPanel = new JPanel();

            selectPanel.setLayout(new GridBagLayout());
            selectPanel.add(jLabel6, gridBagConstraints22);
            selectPanel.add(getCredentialLoadMethod(), gridBagConstraints23);
            selectPanel.add(getLoadCredentialsButton(), gridBagConstraints24);
        }
        return selectPanel;
    }


    /**
     * This method initializes credentialLoadMethod
     * 
     * @return javax.swing.JComboBox
     */
    private JComboBox getCredentialLoadMethod() {
        if (credentialLoadMethod == null) {
            credentialLoadMethod = new JComboBox();
            credentialLoadMethod.addItem(FILE_SYSTEM_CERT_KEY);
            credentialLoadMethod.addItem(FILE_SYSTEM_PROXY);
        }
        return credentialLoadMethod;
    }


    /**
     * This method initializes loadCredentialsButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getLoadCredentialsButton() {
        if (loadCredentialsButton == null) {
            loadCredentialsButton = new JButton();
            loadCredentialsButton.setText("Import");
            loadCredentialsButton.setIcon(IntroduceLookAndFeel.getLoadCredentialsIcon());
            loadCredentialsButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    loadCredentials();
                }
            });
        }
        return loadCredentialsButton;
    }


    private void loadCredentials() {
        String method = (String) credentialLoadMethod.getSelectedItem();
        if (method.equals(FILE_SYSTEM_CERT_KEY)) {
            GridApplication.getContext().getApplication().addApplicationComponent(
                new LoadCredentialsFromFileSystemWindow(this), 500, 200);
        }
        if (method.equals(FILE_SYSTEM_PROXY)) {
            GridApplication.getContext().getApplication().addApplicationComponent(
                new LoadProxyFromFileSystemWindow(this), 500, 200);
        }

    }


    public void setProxy(ProxyCredential proxy) throws Exception {
        if (proxy != null) {
            certificateLocation = null;
            privateKeyLocation = null;
            proxyLocation = proxy.getProxyLocation();
            try {
                proxyPanel.clearProxy();
                proxyPanel.showProxy(ProxyUtil.loadProxy(proxyLocation));
            } catch (Exception e) {
                CompositeErrorDialog.showErrorDialog("Invalid proxy specified!!!");
            }
            syncServiceCredentials();
            synchRunAsMode();
        }

    }


    public void setCredentials(X509Credential cred) throws Exception {
        if (cred != null) {
            certificateLocation = cred.getCertificateLocation();
            privateKeyLocation = cred.getPrivateKeyLocation();
            proxyLocation = null;
            try {
                certificatePanel.setCertificate(CertUtil.loadCertificate(new File(cred.getCertificateLocation())));
            } catch (Exception e) {
                // PortalUtils.showErrorDialogDialog("Invalid certificate
                // specified!!!");
                CompositeErrorDialog.showErrorDialog("Invalid certificate specified!!!");
            }
            syncServiceCredentials();
            synchRunAsMode();
        }
    }


    /**
     * This method initializes credentialsPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getCredentialsPanel() {
        if (credentialsPanel == null) {
            credentialsPanel = new JPanel();
            credentialPanelLayout = new CardLayout();
            credentialsPanel.setLayout(credentialPanelLayout);
            credentialsPanel.add(getNonePanel(), N0_CRED_PANEL);
            credentialsPanel.add(getCertificatePanel(), PKI_CRED_PANEL);
            credentialsPanel.add(getProxyPanel(), PROXY_CRED_PANEL);
        }
        return credentialsPanel;
    }


    /**
     * This method initializes nonePanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getNonePanel() {
        if (nonePanel == null) {
            nonePanel = new JPanel();
            nonePanel.setLayout(new BorderLayout());
            nonePanel.setName(N0_CRED_PANEL);
        }
        return nonePanel;
    }


    /**
     * This method initializes certificatePanel
     * 
     * @return javax.swing.JPanel
     */
    private CertificatePanel getCertificatePanel() {
        if (certificatePanel == null) {
            certificatePanel = new CertificatePanel();
            certificatePanel.setName(PKI_CRED_PANEL);
            certificatePanel.setAllowExport(false);
            certificatePanel.setAllowImport(false);

        }
        return certificatePanel;
    }


    /**
     * This method initializes proxyPanel
     * 
     * @return javax.swing.JPanel
     */
    private CredentialPanel getProxyPanel() {
        if (proxyPanel == null) {
            proxyPanel = new CredentialPanel();
        }
        return proxyPanel;
    }


    /**
     * This method initializes authorizationPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getAuthorizationPanel() {
        if (authorizationPanel == null) {
            authorizationPanel = new JPanel();
            authorizationPanel.setLayout(new BorderLayout());
            authorizationPanel.add(getAuthorizationTypePanel(), java.awt.BorderLayout.NORTH);
            authorizationPanel.add(getAuthPanel(), java.awt.BorderLayout.CENTER);
        }
        return authorizationPanel;
    }


    /**
     * This method initializes authorizationTypePanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getAuthorizationTypePanel() {
        if (authorizationTypePanel == null) {
            authorizationTypePanel = new JPanel();
            authorizationTypePanel.setLayout(new FlowLayout());
            authorizationTypePanel.add(getJPanel1(), null);
        }
        return authorizationTypePanel;
    }


    /**
     * This method initializes authorizationMechanism
     * 
     * @return javax.swing.JComboBox
     */
    private JComboBox getAuthorizationMechanism() {
        if (authorizationMechanism == null) {
            authorizationMechanism = new JComboBox();
            authorizationMechanism.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (isInited) {
                        if (authorizationMechanism.getSelectedItem().equals(GRID_MAP_AUTHORIZATION)
                            || authorizationMechanism.getSelectedItem().equals(CUSTOM_AUTHORIZATION)) {
                            int result = JOptionPane
                                .showConfirmDialog(
                                    ServiceSecurityPanel.this,
                                    "Are you sure you want to change to "
                                        + (String) authorizationMechanism.getSelectedItem()
                                        + ".\nThis will remove all authorization and anonymous settings on all operations in the service and service contexts.");
                            if (result == JOptionPane.OK_OPTION) {
                                // need to look through the introduce
                                // model for the methods and
                                // reset the security for anon and auth
                                // to null

                                if (service.getMethods() != null && service.getMethods().getMethod() != null) {
                                    for (int methodI = 0; methodI < service.getMethods().getMethod().length; methodI++) {
                                        MethodType method = service.getMethods().getMethod(methodI);
                                        if (!method.getName()
                                            .equals(IntroduceConstants.SERVICE_SECURITY_METADATA_METHOD)) {
                                            MethodSecurity methodSec = method.getMethodSecurity();
                                            if (methodSec != null) {
                                                methodSec.setAnonymousClients(null);
                                                methodSec.setMethodAuthorization(null);
                                            }
                                        }

                                    }
                                }

                                synchronize();
                                previousAuthSelection = (String) authorizationMechanism.getSelectedItem();
                            } else {
                                authorizationMechanism.setSelectedItem(previousAuthSelection);
                            }
                        } else {
                            synchronize();
                            previousAuthSelection = (String) authorizationMechanism.getSelectedItem();
                        }
                    }
                }
            });
            authorizationMechanism.addItem(NO_AUTHORIZATION);
            authorizationMechanism.addItem(GRID_MAP_AUTHORIZATION);
            authorizationMechanism.addItem(GRID_GROUPER_AUTHORIZATION);
            authorizationMechanism.addItem(CSM_AUTHORIZATION);
            authorizationMechanism.addItem(CUSTOM_AUTHORIZATION);
        }
        return authorizationMechanism;
    }


    /**
     * This method initializes authPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getAuthPanel() {
        if (authPanel == null) {
            authPanel = new JPanel();
            authLayout = new CardLayout();
            authPanel.setLayout(authLayout);
            authPanel.add(getNoAuthorizationPanel(), getNoAuthorizationPanel().getName());
            authPanel.add(getGridmapPanel(), getGridmapPanel().getName());
            authPanel.add(getGridGrouper(), getGridGrouper().getName());
            authPanel.add(getCsmPanel(), getCsmPanel().getName());
            authPanel.add(getPdpPanel(), getPdpPanel().getName());
        }
        return authPanel;
    }


    /**
     * This method initializes noAuthorizationPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getNoAuthorizationPanel() {
        if (noAuthorizationPanel == null) {
            noAuthorizationPanel = new JPanel();
            noAuthorizationPanel.setName(NO_AUTHORIZATION);
        }
        return noAuthorizationPanel;
    }


    /**
     * This method initializes gridmapPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getGridmapPanel() {
        if (gridmapPanel == null) {
            gridmapPanel = new GridMapPanel();
            gridmapPanel.setName(GRID_MAP_AUTHORIZATION);
        }
        return gridmapPanel;
    }


    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel() {
        if (jPanel == null) {
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.gridx = 0;
            gridBagConstraints10.insets = new java.awt.Insets(2, 5, 2, 5);
            gridBagConstraints10.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints10.weightx = 1.0D;
            gridBagConstraints10.gridy = 0;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints.weightx = 1.0D;
            gridBagConstraints.gridy = 1;
            jPanel = new JPanel();
            jPanel.setLayout(new GridBagLayout());
            jPanel.add(getSelectPanel(), gridBagConstraints10);
            jPanel.add(getGeneralSecurity(), gridBagConstraints);
        }
        return jPanel;
    }


    /**
     * This method initializes jPanel1
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            credentialsRequired = new JLabel();
            credentialsRequired.setText("Anonymous Clients");
            authLabel = new JLabel();
            authLabel.setText("Authorization Mechanism");

            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints5.gridx = 3;
            gridBagConstraints5.gridy = 0;
            gridBagConstraints5.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints5.weightx = 1.0;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 2;
            gridBagConstraints3.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints3.gridy = 0;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints2.gridx = 1;
            gridBagConstraints2.gridy = 0;
            gridBagConstraints2.insets = new Insets(2, 2, 2, 10);
            gridBagConstraints2.weightx = 1.0;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints1.gridy = 0;
            jPanel1 = new JPanel();
            jPanel1.setLayout(new GridBagLayout());
            jPanel1.add(credentialsRequired, gridBagConstraints1);
            jPanel1.add(getAnonymousCommunication(), gridBagConstraints2);
            jPanel1.add(authLabel, gridBagConstraints3);
            jPanel1.add(getAuthorizationMechanism(), gridBagConstraints5);
        }
        return jPanel1;
    }


    /**
     * This method initializes gridGrouper
     * 
     * @return javax.swing.JPanel
     */
    private GridGrouperExpressionEditor getGridGrouper() {
        if (gridGrouper == null) {
            gridGrouper = new GridGrouperExpressionEditor(GridGrouperURLManager.getGridGroupers(),
                GridGrouperURLManager.getLoadOnStartup());
            gridGrouper.setName(GRID_GROUPER_AUTHORIZATION);
        }
        return gridGrouper;
    }


    /**
     * This method initializes csmPanel
     * 
     * @return javax.swing.JPanel
     */
    private CSMPanel getCsmPanel() {
        if (csmPanel == null) {
            csmPanel = new CSMPanel(service.getName());
            csmPanel.setName(CSM_AUTHORIZATION);
        }
        return csmPanel;
    }


    /**
     * This method initializes pdpPanel
     * 
     * @return javax.swing.JPanel
     */
    private CustomPDPPanel getPdpPanel() {
        if (pdpPanel == null) {
            pdpPanel = new CustomPDPPanel();
            pdpPanel.setName(CUSTOM_AUTHORIZATION);
        }
        return pdpPanel;
    }
}
