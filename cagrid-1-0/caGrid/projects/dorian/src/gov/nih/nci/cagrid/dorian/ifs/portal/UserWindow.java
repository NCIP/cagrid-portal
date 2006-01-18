package gov.nih.nci.cagrid.dorian.ifs.portal;

import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.common.security.commstyle.SecureConversationWithEncryption;
import gov.nih.nci.cagrid.dorian.bean.PermissionDeniedFault;
import gov.nih.nci.cagrid.dorian.client.IFSAdministrationClient;
import gov.nih.nci.cagrid.dorian.common.ca.CertUtil;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP;
import gov.nih.nci.cagrid.dorian.portal.DorianLookAndFeel;
import gov.nih.nci.cagrid.dorian.portal.ProxyCaddy;
import gov.nih.nci.cagrid.dorian.portal.ProxyComboBox;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.security.cert.X509Certificate;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.globus.gsi.GlobusCredential;
import org.projectmobius.common.MobiusRunnable;
import org.projectmobius.portal.GridPortalBaseFrame;
import org.projectmobius.portal.PortalResourceManager;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: UserWindow.java,v 1.9 2006-01-18 03:10:21 langella Exp $
 */
public class UserWindow extends GridPortalBaseFrame {

	private final static String INFO_PANEL = "User Information";

	private final static String CERTIFICATE_PANEL = "Certificate";

	private javax.swing.JPanel jContentPane = null;

	private JPanel mainPanel = null;

	private JPanel buttonPanel = null;

	private JButton cancel = null;

	private JButton updateUser = null;

	private JTabbedPane jTabbedPane = null;

	private JPanel jPanel1 = null;

	private JPanel jPanel2 = null;

	private JLabel jLabel14 = null;

	private String serviceId;

	private IFSUser user;

	private JTextField service = null;

	private JPanel infoPanel = null;

	private GlobusCredential cred;

	private JLabel credentialLabel = null;

	private JComboBox proxy = null;

	private JLabel gridIdLabel = null;

	private JTextField gridIdentity = null;

	private TrustedIdP idp;

	private JLabel idpLabel = null;

	private JTextField trustedIdP = null;

	private JLabel uidLabel = null;

	private JTextField userId = null;

	private JLabel emailLabel = null;

	private JTextField email = null;

	private JLabel statusLabel = null;

	private JLabel roleStatus = null;

	private JComboBox userStatus = null;

	private JComboBox userRole = null;

	private JPanel certificatePanel = null;

	private JButton renewCredentials = null;

	private CertificatePanel credPanel = null;


	/**
	 * This is the default constructor
	 */
	public UserWindow(String serviceId, GlobusCredential proxy, IFSUser u, TrustedIdP idp) throws Exception {
		super();
		this.serviceId = serviceId;
		this.cred = proxy;
		this.user = u;
		this.idp = idp;
		initialize();
		this.setFrameIcon(DorianLookAndFeel.getUserMagnifyIcon());
	}


	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(500, 500);
		this.setContentPane(getJContentPane());
		this.setTitle("Manage User [" + user.getGridId() + "]");

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
			mainPanel.add(getJPanel2(), gridBagConstraints1);
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
			buttonPanel.add(getUpdateUser(), null);
			buttonPanel.add(getRenewCredentials(), null);
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
			cancel.setIcon(DorianLookAndFeel.getCloseIcon());
			cancel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return cancel;
	}


	/**
	 * This method initializes manageUser
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getUpdateUser() {
		if (updateUser == null) {
			updateUser = new JButton();
			updateUser.setText("Update User");
			updateUser.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MobiusRunnable runner = new MobiusRunnable() {
						public void execute() {
							updateUser();
						}
					};
					try {
						PortalResourceManager.getInstance().getThreadManager().executeInBackground(runner);
					} catch (Exception t) {
						t.getMessage();
					}

				}
			});
			updateUser.setIcon(DorianLookAndFeel.getUpdateUserIcon());
		}
		return updateUser;
	}


	private synchronized void updateUser() {

		user.setUserRole(((UserRolesComboBox) this.getUserRole()).getSelectedUserRole());
		user.setUserStatus(((UserStatusComboBox) this.getUserStatus()).getSelectedUserStatus());

		try {
			String service = getService().getText();
			GlobusCredential c = ((ProxyCaddy) getProxy().getSelectedItem()).getProxy();
			IFSAdministrationClient client = new IFSAdministrationClient(service, new SecureConversationWithEncryption(
				c));
			client.updateUser(user);

			PortalUtils.showMessage("User " + user.getGridId() + " update successfully.");

		} catch (PermissionDeniedFault pdf) {
			PortalUtils.showErrorMessage(pdf);
		} catch (Exception e) {
			e.printStackTrace();
			PortalUtils.showErrorMessage(e);
		}

	}


	private void renewCredentials() {
		try {
			String service = getService().getText();
			GlobusCredential c = ((ProxyCaddy) getProxy().getSelectedItem()).getProxy();
			IFSAdministrationClient client = new IFSAdministrationClient(service, new SecureConversationWithEncryption(
				c));
			user = client.renewUserCredentials(user);
			X509Certificate cert = CertUtil.loadCertificateFromString(user.getCertificate().getCertificateAsString());
			this.getCredPanel().setCertificate(cert);
			PortalUtils.showMessage("Successfully renewed the credentials for the user " + user.getGridId() + ".");
		} catch (PermissionDeniedFault pdf) {
			PortalUtils.showErrorMessage(pdf);
		} catch (Exception e) {
			e.printStackTrace();
			PortalUtils.showErrorMessage(e);
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
			jTabbedPane.setBorder(BorderFactory.createTitledBorder(null, "Grid User",
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, DorianLookAndFeel
					.getPanelLabelColor()));
			jTabbedPane.addTab(INFO_PANEL, DorianLookAndFeel.getUserIcon(), getInfoPanel(), null);
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
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints16.gridy = 5;
			gridBagConstraints16.weightx = 1.0;
			gridBagConstraints16.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints16.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints16.gridx = 1;
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints15.gridy = 4;
			gridBagConstraints15.weightx = 1.0;
			gridBagConstraints15.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints15.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints15.gridx = 1;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 0;
			gridBagConstraints14.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints14.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints14.gridy = 5;
			roleStatus = new JLabel();
			roleStatus.setText("User Role");
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints13.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints13.gridy = 4;
			statusLabel = new JLabel();
			statusLabel.setText("User Status");
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints12.gridy = 3;
			gridBagConstraints12.weightx = 1.0;
			gridBagConstraints12.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints12.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints12.gridx = 1;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints11.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints11.gridy = 3;
			emailLabel = new JLabel();
			emailLabel.setText("Email");
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints10.gridy = 2;
			gridBagConstraints10.weightx = 1.0;
			gridBagConstraints10.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints10.gridx = 1;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints9.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints9.gridy = 2;
			uidLabel = new JLabel();
			uidLabel.setText("User Id");
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints8.gridy = 1;
			gridBagConstraints8.weightx = 1.0;
			gridBagConstraints8.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints8.gridx = 1;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints7.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints7.gridy = 1;
			idpLabel = new JLabel();
			idpLabel.setText("Identity Provider");
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints6.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints6.gridx = 1;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.weightx = 1.0;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints5.gridx = 0;
			gridIdLabel = new JLabel();
			gridIdLabel.setText("Grid Identity");
			jPanel1 = new JPanel();
			jPanel1.setName(INFO_PANEL);
			jPanel1.setLayout(new GridBagLayout());
			jPanel1.add(gridIdLabel, gridBagConstraints5);
			jPanel1.add(getGridIdentity(), gridBagConstraints6);
			jPanel1.add(idpLabel, gridBagConstraints7);
			jPanel1.add(getTrustedIdP(), gridBagConstraints8);
			jPanel1.add(uidLabel, gridBagConstraints9);
			jPanel1.add(getUserId(), gridBagConstraints10);
			jPanel1.add(emailLabel, gridBagConstraints11);
			jPanel1.add(getEmail(), gridBagConstraints12);
			jPanel1.add(statusLabel, gridBagConstraints13);
			jPanel1.add(roleStatus, gridBagConstraints14);
			jPanel1.add(getUserStatus(), gridBagConstraints15);
			jPanel1.add(getUserRole(), gridBagConstraints16);
		}
		return jPanel1;
	}


	/**
	 * This method initializes jPanel2
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
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
			jPanel2 = new JPanel();
			jPanel2.setLayout(new GridBagLayout());
			jPanel2.setBorder(BorderFactory.createTitledBorder(null, "Login Information",
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, DorianLookAndFeel
					.getPanelLabelColor()));
			jPanel2.add(jLabel14, gridBagConstraints31);
			jPanel2.add(getService(), gridBagConstraints27);
			jPanel2.add(credentialLabel, gridBagConstraints);
			jPanel2.add(getProxy(), gridBagConstraints3);
		}
		return jPanel2;
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
			proxy = new ProxyComboBox(cred);
		}
		return proxy;
	}


	/**
	 * This method initializes gridIdentity
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getGridIdentity() {
		if (gridIdentity == null) {
			gridIdentity = new JTextField();
			gridIdentity.setText(user.getGridId());
			gridIdentity.setEditable(false);
		}
		return gridIdentity;
	}


	/**
	 * This method initializes trustedIdP
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTrustedIdP() {
		if (trustedIdP == null) {
			trustedIdP = new JTextField();
			trustedIdP.setEditable(false);
			trustedIdP.setText("[IdP Id:" + idp.getId() + "] " + idp.getName());
		}
		return trustedIdP;
	}


	/**
	 * This method initializes userId
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getUserId() {
		if (userId == null) {
			userId = new JTextField();
			userId.setEditable(false);
			userId.setText(user.getUID());
		}
		return userId;
	}


	/**
	 * This method initializes email
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getEmail() {
		if (email == null) {
			email = new JTextField();
			email.setEditable(false);
			email.setText(user.getEmail());
		}
		return email;
	}


	/**
	 * This method initializes userStatus
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getUserStatus() {
		if (userStatus == null) {
			userStatus = new UserStatusComboBox();
			userStatus.setSelectedItem(user.getUserStatus());
		}
		return userStatus;
	}


	/**
	 * This method initializes userRole
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getUserRole() {
		if (userRole == null) {
			userRole = new UserRolesComboBox();
			userRole.setSelectedItem(user.getUserRole());
		}
		return userRole;
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
	 * This method initializes renewCredentials
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getRenewCredentials() {
		if (renewCredentials == null) {
			renewCredentials = new JButton();
			renewCredentials.setText("Renew Credentials");
			renewCredentials.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					renewCredentials();
				}
			});
			renewCredentials.setIcon(DorianLookAndFeel.getCertificateActionIcon());
		}
		return renewCredentials;
	}


	/**
	 * This method initializes credPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private CertificatePanel getCredPanel(){
		if (credPanel == null) {
			try{
			credPanel = new CertificatePanel(CertUtil.loadCertificateFromString(user.getCertificate()
				.getCertificateAsString()));
			credPanel.setAllowImport(false);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return credPanel;
	}

}
