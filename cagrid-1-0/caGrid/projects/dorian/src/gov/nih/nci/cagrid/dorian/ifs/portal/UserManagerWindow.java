package gov.nih.nci.cagrid.gums.ifs.portal;

import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.gums.client.IFSAdministrationClient;
import gov.nih.nci.cagrid.gums.common.FaultUtil;
import gov.nih.nci.cagrid.gums.ifs.bean.TrustedIdP;
import gov.nih.nci.cagrid.gums.portal.GUMSServiceListComboBox;
import gov.nih.nci.cagrid.gums.portal.GumsLookAndFeel;
import gov.nih.nci.cagrid.gums.portal.ProxyCaddy;
import gov.nih.nci.cagrid.gums.portal.ProxyComboBox;
import gov.nih.nci.cagrid.security.commstyle.CommunicationStyle;
import gov.nih.nci.cagrid.security.commstyle.SecureConversationWithEncryption;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import org.projectmobius.common.MobiusRunnable;
import org.projectmobius.portal.GridPortalBaseFrame;
import org.projectmobius.portal.PortalResourceManager;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: UserManagerWindow.java,v 1.2 2005-12-08 20:53:42 langella Exp $
 */
public class UserManagerWindow extends GridPortalBaseFrame {

	private javax.swing.JPanel jContentPane = null;

	private JPanel mainPanel = null;

	private JPanel contentPanel = null;

	private JPanel buttonPanel = null;

	private JButton cancel = null;

	private UsersTable usersTable = null;

	private JScrollPane jScrollPane = null;

	private JButton manageUser = null;

	private JPanel jPanel = null;

	private JPanel jPanel2 = null;

	private JLabel jLabel14 = null;

	private JPanel queryPanel = null;

	private JButton query = null;

	private JComboBox service = null;

	private boolean isQuerying = false;

	private Object mutex = new Object();

	private JLabel proxyLabel = null;

	private JComboBox proxy = null;

	private JPanel progressPanel = null;

	private JProgressBar progress = null;

	private JPanel filterPanel = null;

	private JLabel idpLabel = null;

	private JComboBox idp = null;

	private JLabel gidLabel = null;

	private JTextField gridIdentity = null;

	private JLabel emailLabel = null;

	private JTextField email = null;

	private JLabel statusLabel = null;

	private JComboBox userStatus = null;

	private JLabel roleLabel = null;

	private JComboBox userRole = null;

	private String lastService = null;

	private String lastGridIdentity = null;

	/**
	 * This is the default constructor
	 */
	public UserManagerWindow() {
		super();
		initialize();
		this.setFrameIcon(GumsLookAndFeel.getUsersIcon());
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(500, 500);
		this.setContentPane(getJContentPane());
		this.setTitle("Manage Users");

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
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints.gridy = 1;
			GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
			gridBagConstraints32.gridx = 0;
			gridBagConstraints32.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints32.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints32.weightx = 1.0D;
			gridBagConstraints32.gridy = 3;
			GridBagConstraints gridBagConstraints33 = new GridBagConstraints();
			gridBagConstraints33.gridx = 0;
			gridBagConstraints33.gridy = 2;
			GridBagConstraints gridBagConstraints35 = new GridBagConstraints();
			gridBagConstraints35.gridx = 0;
			gridBagConstraints35.weightx = 1.0D;
			gridBagConstraints35.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints35.gridy = 0;

			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 4;
			gridBagConstraints1.ipadx = 0;
			gridBagConstraints1.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints1.weighty = 1.0D;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 5;
			gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.SOUTH;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			mainPanel.add(getButtonPanel(), gridBagConstraints2);
			mainPanel.add(getContentPanel(), gridBagConstraints1);
			mainPanel.add(getJPanel(), gridBagConstraints35);
			mainPanel.add(getQueryPanel(), gridBagConstraints33);
			mainPanel.add(getProgressPanel(), gridBagConstraints32);
			mainPanel.add(getFilterPanel(), gridBagConstraints);
		}
		return mainPanel;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getContentPanel() {
		if (contentPanel == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			contentPanel = new JPanel();
			contentPanel.setLayout(new GridBagLayout());
			contentPanel
					.setBorder(javax.swing.BorderFactory
							.createTitledBorder(
									null,
									"Users",
									javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
									javax.swing.border.TitledBorder.DEFAULT_POSITION,
									null, GumsLookAndFeel.getPanelLabelColor()));
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.weighty = 1.0;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
			contentPanel.add(getJScrollPane(), gridBagConstraints4);
		}
		return contentPanel;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.add(getManageUser(), null);
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
			cancel.setIcon(GumsLookAndFeel.getCloseIcon());
			cancel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return cancel;
	}

	/**
	 * This method initializes jTable
	 * 
	 * @return javax.swing.JTable
	 */
	private UsersTable getUsersTable() {
		if (usersTable == null) {
			usersTable = new UsersTable(this);
		}
		return usersTable;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getUsersTable());
		}
		return jScrollPane;
	}

	/**
	 * This method initializes manageUser
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getManageUser() {
		if (manageUser == null) {
			manageUser = new JButton();
			manageUser.setText("Manage User");
			manageUser.setIcon(GumsLookAndFeel.getUserMagnifyIcon());
			manageUser.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					showUser();
				}

			});
		}

		return manageUser;
	}

	public void showUser() {
		/*
		 * final int row = getUsersTable().getSelectedRow();
		 * 
		 * if ((row >= 0) && (row < getUsersTable().getRowCount())) {
		 * MobiusRunnable runner = new MobiusRunnable() { public void execute() {
		 * IdPUser user = (IdPUser) getUsersTable().getValueAt(row, 0); String
		 * service = ((GUMSServiceListComboBox) getService())
		 * .getSelectedService(); try { GlobusCredential proxy =
		 * ((ProxyComboBox) getProxy()) .getSelectedProxy();
		 * 
		 * PortalResourceManager.getInstance().getGridPortal()
		 * .addGridPortalComponent( new UserWindow(service, proxy, user)); }
		 * catch (Exception e) { PortalUtils.showErrorMessage(e); } } }; try {
		 * PortalResourceManager.getInstance().getThreadManager()
		 * .executeInBackground(runner); } catch (Exception t) { t.getMessage(); } }
		 * else {
		 * JOptionPane.showMessageDialog(PortalResourceManager.getInstance()
		 * .getGridPortal(), "Please select a user to manage!!!"); }
		 */
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			GridBagConstraints gridBagConstraints34 = new GridBagConstraints();
			gridBagConstraints34.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints34.gridy = 0;
			gridBagConstraints34.weightx = 1.0D;
			gridBagConstraints34.gridx = 0;
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.add(getJPanel2(), gridBagConstraints34);
		}
		return jPanel;
	}

	/**
	 * This method initializes jPanel2
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			GridBagConstraints gridBagConstraints30 = new GridBagConstraints();
			gridBagConstraints30.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints30.gridx = 1;
			gridBagConstraints30.gridy = 1;
			gridBagConstraints30.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints30.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints30.weightx = 1.0;
			GridBagConstraints gridBagConstraints29 = new GridBagConstraints();
			gridBagConstraints29.gridx = 0;
			gridBagConstraints29.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints29.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints29.gridy = 1;
			proxyLabel = new JLabel();
			proxyLabel.setText("Proxy");
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
			jPanel2.setBorder(BorderFactory.createTitledBorder(null,
					"Login Information", TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, null, GumsLookAndFeel
							.getPanelLabelColor()));
			jPanel2.add(jLabel14, gridBagConstraints31);
			jPanel2.add(getService(), gridBagConstraints28);
			jPanel2.add(proxyLabel, gridBagConstraints29);
			jPanel2.add(getProxy(), gridBagConstraints30);
		}
		return jPanel2;
	}

	/**
	 * This method initializes queryPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getQueryPanel() {
		if (queryPanel == null) {
			queryPanel = new JPanel();
			queryPanel.add(getQuery(), null);
		}
		return queryPanel;
	}

	/**
	 * This method initializes query
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getQuery() {
		if (query == null) {
			query = new JButton();
			query.setText("Find Users");
			query.setIcon(GumsLookAndFeel.getQueryIcon());
			query.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MobiusRunnable runner = new MobiusRunnable() {
						public void execute() {
							findUsers();
						}
					};
					try {
						PortalResourceManager.getInstance().getThreadManager()
								.executeInBackground(runner);
					} catch (Exception t) {
						t.getMessage();
					}

				}
			});
		}
		return query;
	}

	private void findUsers() {
		/*
		 * 
		 * synchronized (mutex) { if (isQuerying) { PortalUtils
		 * .showErrorMessage("Query Already in Progress", "Please wait until the
		 * current query is finished before executing another."); return; } else {
		 * isQuerying = true; } }
		 * 
		 * this.getUsersTable().clearTable();
		 * this.updateProgress(true,"Querying...");
		 * 
		 * try { GlobusCredential proxy = ((ProxyComboBox) getProxy())
		 * .getSelectedProxy(); IdPUserFilter f = new IdPUserFilter(); JPanel
		 * panel = (JPanel) this.getJTabbedPane().getSelectedComponent(); if
		 * (panel.getName().equals(ROLE_PANEL)) {
		 * f.setRole(this.getUserRole().getSelectedUserRole()); } else if
		 * (panel.getName().equals(STATUS_PANEL)) {
		 * f.setStatus(this.getUserStatus().getSelectedUserStatus()); } else if
		 * (panel.getName().equals(INFO_PANEL)) {
		 * f.setUserId(format(getUsername().getText()));
		 * f.setFirstName(format(getFirstName().getText()));
		 * f.setLastName(format(getLastName().getText()));
		 * f.setOrganization(format(getOrganization().getText()));
		 * f.setAddress(format(getAddress().getText()));
		 * f.setAddress2(format(getAddress2().getText()));
		 * f.setCity(format(getCity().getText()));
		 * f.setState(getState().getSelectedState());
		 * f.setZipcode(format(getZipcode().getText()));
		 * f.setCountry(getCountry().getSelectedCountry());
		 * f.setPhoneNumber(format(getPhoneNumber().getText()));
		 * f.setEmail(format(getEmail().getText())); }
		 * 
		 * String service = ((GUMSServiceListComboBox) getService())
		 * .getSelectedService(); CommunicationStyle style = new
		 * SecureConversationWithEncryption( proxy); IdPAdministrationClient
		 * client = new IdPAdministrationClient( service, style); IdPUser[]
		 * users = client.findUsers(f); if (users != null) { for (int i = 0; i <
		 * users.length; i++) { this.getUsersTable().addUser(users[i]); } }
		 * this.updateProgress(false,"Querying Completed ["+users.length+" users
		 * found]"); } catch (PermissionDeniedFault pdf) {
		 * PortalUtils.showErrorMessage(pdf);
		 * this.updateProgress(false,"Error"); } catch (Exception e) {
		 * e.printStackTrace(); PortalUtils.showErrorMessage(e);
		 * this.updateProgress(false,"Error"); } isQuerying = false;
		 */
	}

	private String format(String s) {
		if ((s == null) || (s.trim().length() == 0)) {
			return null;
		} else {
			return s;
		}
	}

	/**
	 * This method initializes service
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getService() {
		if (service == null) {
			service = new GUMSServiceListComboBox();
		}
		return service;
	}

	/**
	 * This method initializes proxy
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getProxy() {
		if (proxy == null) {
			proxy = new ProxyComboBox();
		}
		return proxy;
	}

	/**
	 * This method initializes progressPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getProgressPanel() {
		if (progressPanel == null) {
			GridBagConstraints gridBagConstraints36 = new GridBagConstraints();
			gridBagConstraints36.insets = new java.awt.Insets(2, 20, 2, 20);
			gridBagConstraints36.gridy = 0;
			gridBagConstraints36.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints36.weightx = 1.0D;
			gridBagConstraints36.gridx = 0;
			progressPanel = new JPanel();
			progressPanel.setLayout(new GridBagLayout());
			progressPanel.add(getProgress(), gridBagConstraints36);
		}
		return progressPanel;
	}

	/**
	 * This method initializes progress
	 * 
	 * @return javax.swing.JProgressBar
	 */
	private JProgressBar getProgress() {
		if (progress == null) {
			progress = new JProgressBar();
			progress.setForeground(GumsLookAndFeel.getPanelLabelColor());
			progress.setString("");
			progress.setStringPainted(true);
		}
		return progress;
	}

	public void updateProgress(final boolean working, final String s) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				getProgress().setString(s);
				getProgress().setIndeterminate(working);
			}
		});

	}

	/**
	 * This method initializes filterPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getFilterPanel() {
		if (filterPanel == null) {
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints13.gridy = 4;
			gridBagConstraints13.weightx = 1.0;
			gridBagConstraints13.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints13.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints13.gridx = 1;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints12.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints12.gridy = 4;
			roleLabel = new JLabel();
			roleLabel.setText("User Role");
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.gridy = 3;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints11.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints11.gridx = 1;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints10.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints10.gridy = 3;
			statusLabel = new JLabel();
			statusLabel.setText("User Status");
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.gridy = 2;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints9.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints9.gridx = 1;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints8.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints8.gridy = 2;
			emailLabel = new JLabel();
			emailLabel.setText("Email");
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.gridy = 1;
			gridBagConstraints7.weightx = 1.0;
			gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints7.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints7.gridx = 1;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints6.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints6.gridy = 1;
			gidLabel = new JLabel();
			gidLabel.setText("Grid Identity");
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.gridy = 0;
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints5.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints5.weightx = 1.0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints3.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints3.gridy = 0;
			idpLabel = new JLabel();
			idpLabel.setText("Identity Provider");
			filterPanel = new JPanel();
			filterPanel.setLayout(new GridBagLayout());
			filterPanel.add(idpLabel, gridBagConstraints3);
			filterPanel.add(getIdp(), gridBagConstraints5);
			filterPanel.add(gidLabel, gridBagConstraints6);
			filterPanel.add(getGridIdentity(), gridBagConstraints7);
			filterPanel.add(emailLabel, gridBagConstraints8);
			filterPanel.add(getEmail(), gridBagConstraints9);
			filterPanel.add(statusLabel, gridBagConstraints10);
			filterPanel.add(getUserStatus(), gridBagConstraints11);
			filterPanel.add(roleLabel, gridBagConstraints12);
			filterPanel.add(getUserRole(), gridBagConstraints13);
		}
		return filterPanel;
	}

	/**
	 * This method initializes idp
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getIdp() {
		if (idp == null) {
			idp = new JComboBox();
			idp.addFocusListener(new FocusListener() {
				public void focusGained(FocusEvent e) {
					updateIdPs();

				}

				public void focusLost(FocusEvent e) {
					// TODO Auto-generated method stub

				}

			});
		}
		return idp;
	}

	private void updateIdPs() {
		String service = ((GUMSServiceListComboBox) getService())
				.getSelectedService();
		ProxyCaddy caddy = ((ProxyComboBox) getProxy()).getSelectedProxyCaddy();
		if ((service.equals(this.lastService))
				&& (caddy.getIdentity().equals(this.lastGridIdentity))) {
			return;
		} else {
			try {
				this.updateProgress(true, "Seaching for Trusted IdPs");
				this.lastService = service;
				this.lastGridIdentity = caddy.getIdentity();
				this.getIdp().removeAllItems();
				CommunicationStyle style = new SecureConversationWithEncryption(
						caddy.getProxy());
				IFSAdministrationClient client = new IFSAdministrationClient(
						service, style);
				TrustedIdP[] idps = client.getTrustedIdPs();
				for (int i = 0; i < idps.length; i++) {
					getIdp().addItem(new TrustedIdPCaddy(idps[0]));
				}
				this.updateProgress(false, "Found "+idps.length+" IdP(s)");
			} catch (Exception e) {
				FaultUtil.printFault(e);
				this.updateProgress(false, "Error");
				PortalUtils.showErrorMessage(e);
			}

		}
	}

	/**
	 * This method initializes gridIdentity
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getGridIdentity() {
		if (gridIdentity == null) {
			gridIdentity = new JTextField();
		}
		return gridIdentity;
	}

	/**
	 * This method initializes email
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getEmail() {
		if (email == null) {
			email = new JTextField();
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
			userStatus = new UserStatusComboBox(true);
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
			userRole = new UserRolesComboBox(true);
		}
		return userRole;
	}

	public class TrustedIdPCaddy {
		private TrustedIdP idp;

		public TrustedIdPCaddy(TrustedIdP idp) {
			this.idp = idp;
		}

		public TrustedIdP getTrustedIdP() {
			return idp;
		}

		public String toString() {
			return idp.getName() + " [IdP Id: " + idp.getId() + "]";
		}

	}

}
