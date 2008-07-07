package org.cagrid.gaards.ui.dorian.ifs;

import gov.nih.nci.cagrid.common.Runner;
import gov.nih.nci.cagrid.common.Utils;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.cagrid.gaards.dorian.client.GridAdministrationClient;
import org.cagrid.gaards.dorian.federation.HostCertificateRecord;
import org.cagrid.gaards.dorian.federation.HostCertificateStatus;
import org.cagrid.gaards.dorian.federation.HostCertificateUpdate;
import org.cagrid.gaards.pki.CertUtil;
import org.cagrid.gaards.pki.KeyUtil;
import org.cagrid.gaards.ui.common.CertificatePanel;
import org.cagrid.gaards.ui.common.CredentialCaddy;
import org.cagrid.gaards.ui.common.CredentialComboBox;
import org.cagrid.gaards.ui.dorian.DorianLookAndFeel;
import org.cagrid.grape.ApplicationComponent;
import org.cagrid.grape.GridApplication;
import org.cagrid.grape.LookAndFeel;
import org.cagrid.grape.utils.ErrorDialog;
import org.globus.gsi.GlobusCredential;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: HostCertificateWindow.java,v 1.1 2007/06/06 20:55:45 langella
 *          Exp $
 */
public class HostCertificateWindow extends ApplicationComponent {

    private final static String INFO_PANEL = "Summary";

    private final static String CERTIFICATE_PANEL = "Certificate";

    private javax.swing.JPanel jContentPane = null;

    private JPanel mainPanel = null;

    private JPanel buttonPanel = null;

    private JButton cancel = null;

    private JTabbedPane jTabbedPane = null;

    private JPanel jPanel1 = null;

    private JPanel sessionPanel = null;

    private JLabel jLabel14 = null;

    private String serviceId;

    private JTextField service = null;

    private JPanel infoPanel = null;

    private GlobusCredential cred;

    private JLabel credentialLabel = null;

    private JComboBox proxy = null;

    private JPanel certificatePanel = null;

    private CertificatePanel credPanel = null;

    private HostCertificateRecord record;

    private JLabel jLabel = null;

    private JTextField recordId = null;

    private boolean admin;

    private JLabel jLabel1 = null;

    private JTextField host = null;

    private JLabel jLabel2 = null;

    private JPanel ownerPanel = null;

    private JTextField owner = null;

    private JButton findUser = null;

    private JLabel jLabel3 = null;

    private HostCertificateStatusComboBox status = null;

    private JLabel jLabel6 = null;

    private JTextField strength = null;

    private JButton approve = null;

    private JButton renew = null;

    private JButton save = null;

    private JButton update = null;

    private JLabel jLabel4 = null;

    private JTextField hostGridIdentity = null;


    /**
     * This is the default constructor
     */
    public HostCertificateWindow(String serviceId, GlobusCredential proxy, HostCertificateRecord record, boolean admin)
        throws Exception {
        super();
        this.serviceId = serviceId;
        this.cred = proxy;
        this.record = record;
        this.admin = admin;
        initialize();
        this.setFrameIcon(DorianLookAndFeel.getHostIcon());
        loadRecord();
    }


    private void loadRecord() {
        getRecordId().setText(String.valueOf(record.getId()));
        getHost().setText(record.getHost());
        getOwner().setText(record.getOwner());
        getStatus().setSelectedItem(record.getStatus());
        if (record.getStatus().equals(HostCertificateStatus.Compromised)) {
            getStatus().setEnabled(false);
            getOwner().setEditable(false);
            getFindUser().setVisible(false);
            getFindUser().setEnabled(false);
        }
        try {
            PublicKey key = KeyUtil.loadPublicKey(record.getPublicKey().getKeyAsString());
            strength.setText(String.valueOf(((RSAPublicKey) key).getModulus().bitLength()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if ((record.getCertificate() != null)
                && (Utils.clean(record.getCertificate().getCertificateAsString()) != null)) {
                X509Certificate cert = CertUtil.loadCertificate(record.getCertificate().getCertificateAsString());
                getCredPanel().setCertificate(cert);
                hostGridIdentity.setText(CertUtil.subjectToIdentity(cert.getSubjectDN().getName()));
                getSave().setEnabled(true);
                getSave().setVisible(true);
            } else {
                getSave().setEnabled(false);
                getSave().setVisible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (record.getStatus().equals(HostCertificateStatus.Pending) && admin) {
            getApprove().setEnabled(true);
            getApprove().setVisible(true);
        } else {
            getApprove().setEnabled(false);
            getApprove().setVisible(false);
        }

        if (record.getStatus().equals(HostCertificateStatus.Active) && admin) {
            getRenew().setEnabled(true);
            getRenew().setVisible(true);
        } else {
            getRenew().setEnabled(false);
            getRenew().setVisible(false);
        }
        detectUpdate();
    }


    /**
     * This method initializes this
     */
    private void initialize() {
        this.setContentPane(getJContentPane());
        this.setTitle("Host Certificate [" + record.getHost() + "]");
    }


    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new javax.swing.JPanel();
            jContentPane.setLayout(new java.awt.BorderLayout());
            jContentPane.add(getMainPanel(), java.awt.BorderLayout.CENTER);
        }
        return jContentPane;
    }


    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getMainPanel() {
        if (mainPanel == null) {
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.fill = GridBagConstraints.BOTH;
            gridBagConstraints4.gridy = 1;
            gridBagConstraints4.weightx = 1.0;
            gridBagConstraints4.weighty = 1.0D;
            gridBagConstraints4.gridx = 0;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints1.gridy = 0;
            gridBagConstraints1.weightx = 1.0D;
            gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTH;
            gridBagConstraints1.gridx = 0;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            mainPanel = new JPanel();
            mainPanel.setLayout(new GridBagLayout());
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.gridy = 2;
            gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints2.anchor = java.awt.GridBagConstraints.SOUTH;
            gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
            mainPanel.add(getButtonPanel(), gridBagConstraints2);
            mainPanel.add(getSessionPanel(), gridBagConstraints1);
            mainPanel.add(getJTabbedPane(), gridBagConstraints4);
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
            buttonPanel.add(getApprove(), null);
            buttonPanel.add(getUpdate(), null);
            buttonPanel.add(getRenew(), null);
            buttonPanel.add(getSave(), null);
            buttonPanel.add(getCancel(), null);
        }
        return buttonPanel;
    }


    /**
     * This method initializes jButton1
     * 
     * @return javax.swing.JButton
     */
    private JButton getCancel() {
        if (cancel == null) {
            cancel = new JButton();
            cancel.setText("Close");
            cancel.setIcon(LookAndFeel.getCloseIcon());
            cancel.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    dispose();
                }
            });
        }
        return cancel;
    }


    private void approveHostCertificate() {
        try {
            getApprove().setEnabled(false);
            String serviceUrl = getService().getText();
            GlobusCredential c = ((CredentialCaddy) getProxy().getSelectedItem()).getProxy();
            GridAdministrationClient client = new GridAdministrationClient(serviceUrl, c);
            record = client.approveHostCertificate(record.getId());
            loadRecord();
            JOptionPane.showMessageDialog(this, "The host certificate has been succesfully approved.");
        } catch (Exception e) {
            ErrorDialog.showError(e);
            getApprove().setEnabled(true);
        }

    }


    private void renewHostCertificate() {
        try {
            getRenew().setEnabled(false);
            String serviceUrl = getService().getText();
            GlobusCredential c = ((CredentialCaddy) getProxy().getSelectedItem()).getProxy();
            GridAdministrationClient client = new GridAdministrationClient(serviceUrl, c);
            record = client.renewHostCertificate(record.getId());
            loadRecord();
            JOptionPane.showMessageDialog(this, "The host certificate has been succesfully renewed.");
        } catch (Exception e) {
            ErrorDialog.showError(e);
            getRenew().setEnabled(true);
        }

    }


    private void detectUpdate() {
        if (admin) {
            if (!record.getStatus().equals(getStatus().getSelectedItem())) {
                getUpdate().setEnabled(true);
            } else if (!record.getOwner().equals(getOwner().getText())) {
                getUpdate().setEnabled(true);
            } else {
                getUpdate().setEnabled(false);
            }
        }
    }


    private void updateHostCertificate() {
        try {
            getUpdate().setEnabled(false);

            boolean performUpdate = false;
            HostCertificateUpdate certUpdate = new HostCertificateUpdate();
            certUpdate.setId(record.getId());
            if (!record.getStatus().equals(getStatus().getSelectedItem())) {
                certUpdate.setStatus((HostCertificateStatus) getStatus().getSelectedItem());
                performUpdate = true;
            }

            if (!record.getOwner().equals(getOwner().getText())) {
                certUpdate.setOwner(getOwner().getText());
                performUpdate = true;
            }

            if (performUpdate) {
                String serviceUrl = getService().getText();
                GlobusCredential c = ((CredentialCaddy) getProxy().getSelectedItem()).getProxy();
                GridAdministrationClient client = new GridAdministrationClient(serviceUrl, c);
                client.updateHostCertificateRecord(certUpdate);
                if (!record.getStatus().equals(getStatus().getSelectedItem())) {
                    record.setStatus((HostCertificateStatus) getStatus().getSelectedItem());
                }

                if (!record.getOwner().equals(getOwner().getText())) {
                    record.setOwner(getOwner().getText());
                }
                loadRecord();
                JOptionPane.showMessageDialog(this, "The host certificate has been succesfully updated.");
            } else {
                JOptionPane.showMessageDialog(this, "There are no changes to update.");
            }

        } catch (Exception e) {
            ErrorDialog.showError(e);
            getUpdate().setEnabled(true);
        }

    }


    /**
     * This method initializes jTabbedPane
     * 
     * @return javax.swing.JTabbedPane
     */
    private JTabbedPane getJTabbedPane() {
        if (jTabbedPane == null) {
            jTabbedPane = new JTabbedPane();
            jTabbedPane.setBorder(BorderFactory.createTitledBorder(null, "Host Certificate",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, LookAndFeel
                    .getPanelLabelColor()));
            jTabbedPane.addTab(INFO_PANEL, DorianLookAndFeel.getHostIcon(), getInfoPanel(), null);
            jTabbedPane.addTab(CERTIFICATE_PANEL, DorianLookAndFeel.getCertificateIcon(), getCertificatePanel(), null);
        }
        return jTabbedPane;
    }


    /**
     * This method initializes jPanel1
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
            gridBagConstraints18.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints18.gridy = 5;
            gridBagConstraints18.weightx = 1.0;
            gridBagConstraints18.anchor = GridBagConstraints.WEST;
            gridBagConstraints18.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints18.gridx = 1;
            GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
            gridBagConstraints15.gridx = 0;
            gridBagConstraints15.anchor = GridBagConstraints.WEST;
            gridBagConstraints15.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints15.gridy = 5;
            jLabel4 = new JLabel();
            jLabel4.setText("Host Grid Identity");
            GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
            gridBagConstraints22.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints22.gridy = 4;
            gridBagConstraints22.weightx = 1.0;
            gridBagConstraints22.anchor = GridBagConstraints.WEST;
            gridBagConstraints22.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints22.gridx = 1;
            GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
            gridBagConstraints21.gridx = 0;
            gridBagConstraints21.anchor = GridBagConstraints.WEST;
            gridBagConstraints21.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints21.gridy = 4;
            jLabel6 = new JLabel();
            jLabel6.setText("Strength");
            GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
            gridBagConstraints14.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints14.gridy = 3;
            gridBagConstraints14.weightx = 1.0;
            gridBagConstraints14.anchor = GridBagConstraints.WEST;
            gridBagConstraints14.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints14.gridx = 1;
            GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
            gridBagConstraints13.gridx = 0;
            gridBagConstraints13.anchor = GridBagConstraints.WEST;
            gridBagConstraints13.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints13.gridy = 3;
            jLabel3 = new JLabel();
            jLabel3.setText("Status");
            GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
            gridBagConstraints10.gridx = 1;
            gridBagConstraints10.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints10.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints10.weightx = 1.0D;
            gridBagConstraints10.gridy = 2;
            GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
            gridBagConstraints9.anchor = GridBagConstraints.WEST;
            gridBagConstraints9.gridx = 0;
            gridBagConstraints9.gridy = 2;
            gridBagConstraints9.insets = new Insets(2, 2, 2, 2);
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints8.gridy = 1;
            gridBagConstraints8.weightx = 1.0;
            gridBagConstraints8.anchor = GridBagConstraints.WEST;
            gridBagConstraints8.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints8.gridx = 1;
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.gridx = 0;
            gridBagConstraints7.anchor = GridBagConstraints.WEST;
            gridBagConstraints7.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints7.gridy = 1;
            jLabel1 = new JLabel();
            jLabel1.setText("Host");
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.gridx = 0;
            gridBagConstraints6.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints6.anchor = GridBagConstraints.WEST;
            gridBagConstraints6.gridy = 0;
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints5.anchor = GridBagConstraints.WEST;
            gridBagConstraints5.gridx = 1;
            gridBagConstraints5.gridy = 0;
            gridBagConstraints5.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints5.weightx = 1.0;
            jLabel = new JLabel();
            jLabel.setText("Record Id");
            GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
            gridBagConstraints16.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints16.gridy = 7;
            gridBagConstraints16.weightx = 1.0;
            gridBagConstraints16.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints16.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints16.gridx = 1;
            jPanel1 = new JPanel();
            jPanel1.setName(INFO_PANEL);
            jPanel1.setLayout(new GridBagLayout());
            jPanel1.add(jLabel, gridBagConstraints6);
            jPanel1.add(getRecordId(), gridBagConstraints5);
            jPanel1.add(jLabel1, gridBagConstraints7);
            jPanel1.add(getHost(), gridBagConstraints8);
            jPanel1.add(jLabel2, gridBagConstraints9);
            jPanel1.add(getOwnerPanel(), gridBagConstraints10);
            jPanel1.add(jLabel3, gridBagConstraints13);
            jPanel1.add(getStatus(), gridBagConstraints14);
            jPanel1.add(jLabel6, gridBagConstraints21);
            jPanel1.add(getStrength(), gridBagConstraints22);
            jPanel1.add(jLabel4, gridBagConstraints15);
            jPanel1.add(getHostGridIdentity(), gridBagConstraints18);
        }
        return jPanel1;
    }


    /**
     * This method initializes sessionPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getSessionPanel() {
        if (sessionPanel == null) {
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints3.gridy = 1;
            gridBagConstraints3.weightx = 1.0;
            gridBagConstraints3.gridx = 1;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints.gridy = 1;
            credentialLabel = new JLabel();
            credentialLabel.setText("Proxy");
            GridBagConstraints gridBagConstraints27 = new GridBagConstraints();
            gridBagConstraints27.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints27.weightx = 1.0;
            GridBagConstraints gridBagConstraints28 = new GridBagConstraints();
            gridBagConstraints28.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints28.gridx = 1;
            gridBagConstraints28.gridy = 0;
            gridBagConstraints28.insets = new java.awt.Insets(2, 2, 2, 2);
            gridBagConstraints28.anchor = java.awt.GridBagConstraints.WEST;
            gridBagConstraints28.weightx = 1.0;
            GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
            gridBagConstraints31.anchor = GridBagConstraints.WEST;
            gridBagConstraints31.gridwidth = 1;
            gridBagConstraints31.gridx = 0;
            gridBagConstraints31.gridy = 0;
            gridBagConstraints31.insets = new Insets(2, 2, 2, 2);
            jLabel14 = new JLabel();
            jLabel14.setText("Service");
            sessionPanel = new JPanel();
            sessionPanel.setLayout(new GridBagLayout());
            sessionPanel.setBorder(BorderFactory.createTitledBorder(null, "Login Information",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, LookAndFeel
                    .getPanelLabelColor()));
            sessionPanel.add(jLabel14, gridBagConstraints31);
            sessionPanel.add(getService(), gridBagConstraints27);
            sessionPanel.add(credentialLabel, gridBagConstraints);
            sessionPanel.add(getProxy(), gridBagConstraints3);
            if (!admin) {
                sessionPanel.setVisible(false);
            }
        }
        return sessionPanel;
    }


    /**
     * This method initializes service1
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getService() {
        if (service == null) {
            service = new JTextField();
            service.setText(serviceId);
            service.setEditable(false);
        }
        return service;
    }


    /**
     * This method initializes infoPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getInfoPanel() {
        if (infoPanel == null) {
            jLabel2 = new JLabel();
            jLabel2.setText("Owner");
            infoPanel = new JPanel();
            infoPanel.setLayout(new BorderLayout());
            infoPanel.add(getJPanel1(), java.awt.BorderLayout.NORTH);
        }
        return infoPanel;
    }


    /**
     * This method initializes proxy1
     * 
     * @return javax.swing.JComboBox
     */
    private JComboBox getProxy() {
        if (proxy == null) {
            proxy = new CredentialComboBox(cred);
            if (!admin) {
                proxy.setEditable(false);
            }
        }
        return proxy;
    }


    /**
     * This method initializes certificatePanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getCertificatePanel() {
        if (certificatePanel == null) {
            GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
            gridBagConstraints17.anchor = java.awt.GridBagConstraints.NORTH;
            gridBagConstraints17.gridy = 0;
            gridBagConstraints17.weightx = 1.0D;
            gridBagConstraints17.weighty = 1.0D;
            gridBagConstraints17.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints17.gridx = 0;
            certificatePanel = new JPanel();
            certificatePanel.setLayout(new GridBagLayout());
            certificatePanel.add(getCredPanel(), gridBagConstraints17);
        }
        return certificatePanel;
    }


    /**
     * This method initializes credPanel
     * 
     * @return javax.swing.JPanel
     */
    private CertificatePanel getCredPanel() {
        if (credPanel == null) {
            try {
                credPanel = new CertificatePanel();
                credPanel.setAllowImport(false);
                credPanel.setAllowExport(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return credPanel;
    }


    /**
     * This method initializes recordId
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getRecordId() {
        if (recordId == null) {
            recordId = new JTextField();
            recordId.setEditable(false);
        }
        return recordId;
    }


    /**
     * This method initializes host
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getHost() {
        if (host == null) {
            host = new JTextField();
            host.setEditable(false);
        }
        return host;
    }


    /**
     * This method initializes ownerPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getOwnerPanel() {
        if (ownerPanel == null) {
            GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
            gridBagConstraints12.gridx = 1;
            gridBagConstraints12.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints12.anchor = GridBagConstraints.WEST;
            gridBagConstraints12.gridy = 0;
            GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
            gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints11.anchor = GridBagConstraints.WEST;
            gridBagConstraints11.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints11.gridx = 0;
            gridBagConstraints11.gridy = 0;
            gridBagConstraints11.weightx = 1.0;
            ownerPanel = new JPanel();
            ownerPanel.setLayout(new GridBagLayout());
            ownerPanel.add(getOwner(), gridBagConstraints11);
            ownerPanel.add(getFindUser(), gridBagConstraints12);
        }
        return ownerPanel;
    }


    /**
     * This method initializes owner
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getOwner() {
        if (owner == null) {
            owner = new JTextField();
            owner.addCaretListener(new javax.swing.event.CaretListener() {
                public void caretUpdate(javax.swing.event.CaretEvent e) {
                    detectUpdate();
                }
            });
            if (!admin) {
                owner.setEditable(false);

            }
        }
        return owner;
    }


    /**
     * This method initializes findUser
     * 
     * @return javax.swing.JButton
     */
    private JButton getFindUser() {
        if (findUser == null) {
            findUser = new JButton();
            findUser.setText("Find...");
            if (!admin) {
                findUser.setVisible(false);
            } else {
                findUser.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        FindUserDialog dialog = new FindUserDialog();
                        dialog.setModal(true);
                        GridApplication.getContext().showDialog(dialog);
                        if (dialog.getSelectedUser() != null) {
                            owner.setText(dialog.getSelectedUser());
                        }
                    }
                });
            }
        }
        return findUser;
    }


    /**
     * This method initializes status
     * 
     * @return gov.nih.nci.cagrid.dorian.ui.ifs.HostCertificateStatusComboBox
     */
    private HostCertificateStatusComboBox getStatus() {
        if (status == null) {
            status = new HostCertificateStatusComboBox(false);
            status.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    detectUpdate();
                }
            });
            if (!admin) {
                status.setEnabled(false);

            }
        }
        return status;
    }


    /**
     * This method initializes strength
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getStrength() {
        if (strength == null) {
            strength = new JTextField();
            strength.setEditable(false);
        }
        return strength;
    }


    /**
     * This method initializes approve
     * 
     * @return javax.swing.JButton
     */
    private JButton getApprove() {
        if (approve == null) {
            approve = new JButton();
            approve.setText("Approve Certificate");
            approve.setIcon(LookAndFeel.getSelectIcon());
            approve.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Runner runner = new Runner() {
                        public void execute() {
                            approveHostCertificate();
                        }
                    };
                    try {
                        GridApplication.getContext().executeInBackground(runner);
                    } catch (Exception t) {
                        t.getMessage();
                    }

                }
            });
        }
        return approve;
    }


    /**
     * This method initializes renew
     * 
     * @return javax.swing.JButton
     */
    private JButton getRenew() {
        if (renew == null) {
            renew = new JButton();
            renew.setText("Renew Certificate");
            renew.setIcon(DorianLookAndFeel.getRenewIcon());
            renew.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Runner runner = new Runner() {
                        public void execute() {
                            renewHostCertificate();
                        }
                    };
                    try {
                        GridApplication.getContext().executeInBackground(runner);
                    } catch (Exception t) {
                        t.getMessage();
                    }

                }
            });
        }
        return renew;
    }


    /**
     * This method initializes save
     * 
     * @return javax.swing.JButton
     */
    private JButton getSave() {
        if (save == null) {
            save = new JButton();
            save.setText("Save Certificate");
            save.setIcon(LookAndFeel.getSaveIcon());
            save.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    exportCertificate();
                }
            });
        }
        return save;
    }


    private void exportCertificate() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                X509Certificate cert = CertUtil.loadCertificate(record.getCertificate().getCertificateAsString());
                CertUtil.writeCertificate(cert, new File(fc.getSelectedFile().getAbsolutePath()));
            } catch (Exception ex) {
                ErrorDialog.showError(ex);
            }
        }

    }


    /**
     * This method initializes update
     * 
     * @return javax.swing.JButton
     */
    private JButton getUpdate() {
        if (update == null) {
            update = new JButton();
            update.setText("Update Certificate");
            update.setIcon(DorianLookAndFeel.getHostIcon());
            update.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Runner runner = new Runner() {
                        public void execute() {
                            updateHostCertificate();
                        }
                    };
                    try {
                        GridApplication.getContext().executeInBackground(runner);
                    } catch (Exception t) {
                        t.getMessage();
                    }

                }
            });
            if (!admin) {
                update.setEnabled(false);
                update.setVisible(false);

            }
        }
        return update;
    }


    /**
     * This method initializes hostGridIdentity
     * 
     * @return javax.swing.JTextField
     */
    private JTextField getHostGridIdentity() {
        if (hostGridIdentity == null) {
            hostGridIdentity = new JTextField();
            hostGridIdentity.setEnabled(true);
            hostGridIdentity.setEditable(false);
        }
        return hostGridIdentity;
    }

}
