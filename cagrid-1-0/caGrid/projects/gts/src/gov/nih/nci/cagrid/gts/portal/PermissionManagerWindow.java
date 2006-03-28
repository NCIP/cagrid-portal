package gov.nih.nci.cagrid.gts.portal;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.gridca.portal.ProxyComboBox;
import gov.nih.nci.cagrid.gts.bean.Permission;
import gov.nih.nci.cagrid.gts.bean.PermissionFilter;
import gov.nih.nci.cagrid.gts.bean.Role;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthority;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthorityFilter;
import gov.nih.nci.cagrid.gts.client.GTSAdminClient;
import gov.nih.nci.cagrid.gts.client.GTSSearchClient;
import gov.nih.nci.cagrid.gts.common.Constants;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

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

import org.globus.gsi.GlobusCredential;
import org.projectmobius.common.MobiusRunnable;
import org.projectmobius.portal.GridPortalBaseFrame;
import org.projectmobius.portal.PortalResourceManager;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: TrustedAuthoritiesWindow.java,v 1.2 2006/03/27 19:05:40
 *          langella Exp $
 */
public class PermissionManagerWindow extends GridPortalBaseFrame {

	public final static String ANY = "Any";

	private javax.swing.JPanel jContentPane = null;

	private JPanel mainPanel = null;

	private JPanel contentPanel = null;

	private JPanel buttonPanel = null;

	private PermissionsTable permissionsTable = null;

	private JScrollPane jScrollPane = null;

	private JButton viewTrustedAuthority = null;

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

	private JButton removeTrustedAuthorityButton = null;

	private JPanel filterPanel = null;

	private JLabel jLabel = null;

	private JTextField gridIdentity = null;

	private JLabel jLabel1 = null;

	private JLabel jLabel2 = null;

	private JComboBox trustedAuthorities = null;

	private JComboBox role = null;

	private String currentService = null;


	/**
	 * This is the default constructor
	 */
	public PermissionManagerWindow() {
		super();
		initialize();
		this.setFrameIcon(GTSLookAndFeel.getPermissionIcon());
		syncServices();
	}


	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(700, 500);
		this.setContentPane(getJContentPane());
		this.setTitle("GTS Access Management");
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
			contentPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Permission(s)",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, GTSLookAndFeel.getPanelLabelColor()));
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.gridx = 0;
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
			buttonPanel.add(getViewTrustedAuthority(), null);
			buttonPanel.add(getRemoveTrustedAuthorityButton(), null);
		}
		return buttonPanel;
	}


	/**
	 * This method initializes jTable
	 * 
	 * @return javax.swing.JTable
	 */
	private PermissionsTable getPermissionsTable() {
		if (permissionsTable == null) {
			permissionsTable = new PermissionsTable();
		}
		return permissionsTable;
	}


	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getPermissionsTable());
		}
		return jScrollPane;
	}


	/**
	 * This method initializes manageUser
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getViewTrustedAuthority() {
		if (viewTrustedAuthority == null) {
			viewTrustedAuthority = new JButton();
			viewTrustedAuthority.setText("View/Edit Permission");
			viewTrustedAuthority.setIcon(GTSLookAndFeel.getViewPermissionIcon());
			viewTrustedAuthority.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MobiusRunnable runner = new MobiusRunnable() {
						public void execute() {
							showPermission();
						}
					};
					try {
						PortalResourceManager.getInstance().getThreadManager().executeInBackground(runner);
					} catch (Exception t) {
						t.getMessage();
					}
				}

			});
		}

		return viewTrustedAuthority;
	}


	public void showPermission() {
		try {
			String service = ((GTSServiceListComboBox) getService()).getSelectedService();
			GlobusCredential proxy = ((ProxyComboBox) getProxy()).getSelectedProxy();
			/*
			 * TrustedAuthorityWindow window = new
			 * TrustedAuthorityWindow(service, proxy, this.getPermissionsTable()
			 * .getSelectedTrustedAuthority());
			 * PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(window);
			 */
		} catch (Exception e) {
			PortalUtils.showErrorMessage(e);
		}
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
			jPanel2.setBorder(BorderFactory.createTitledBorder(null, "GTS/Login Information",
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, GTSLookAndFeel
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
			query.setText("Find Permissions");
			query.setIcon(GTSLookAndFeel.getQueryIcon());
			query.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MobiusRunnable runner = new MobiusRunnable() {
						public void execute() {
							findPermissions();
						}
					};
					try {
						PortalResourceManager.getInstance().getThreadManager().executeInBackground(runner);
					} catch (Exception t) {
						t.getMessage();
					}

				}
			});
		}
		return query;
	}


	private void findPermissions() {

		synchronized (mutex) {
			if (isQuerying) {
				PortalUtils.showErrorMessage("Query Already in Progress",
					"Please wait until the current query is finished before executing another.");
				return;
			} else {
				isQuerying = true;
			}
		}

		this.getPermissionsTable().clearTable();
		this.updateProgress(true, "Finding Permissions...");

		try {
			String service = ((GTSServiceListComboBox) getService()).getSelectedService();
			GlobusCredential proxy = ((ProxyComboBox) getProxy()).getSelectedProxy();
			PermissionFilter f = new PermissionFilter();
			f.setGridIdentity(Utils.clean(this.gridIdentity.getText()));
			String ta = (String) trustedAuthorities.getSelectedItem();
			if (!ta.equals(ANY)) {
				f.setTrustedAuthorityName(ta);
			}

			if (!role.getSelectedItem().equals(ANY)) {
				f.setRole((Role) role.getSelectedItem());
			}
			GTSAdminClient client = new GTSAdminClient(service, proxy);
			Permission[] perms = client.findPermissions(f);
			int length = 0;
			if (perms != null) {
				length = perms.length;
				for (int i = 0; i < perms.length; i++) {
					this.permissionsTable.addPermission(perms[i]);
				}
			}
			this.updateProgress(false, "Completed [Found " + length + " Permission(s)]");

		} catch (Exception e) {
			e.printStackTrace();
			PortalUtils.showErrorMessage(e);
			this.updateProgress(false, "Error");
		}
		isQuerying = false;

	}


	/**
	 * This method initializes service
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getService() {
		if (service == null) {
			service = new GTSServiceListComboBox();
			service.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MobiusRunnable runner = new MobiusRunnable() {
						public void execute() {
							syncServices();
						}
					};
					try {
						PortalResourceManager.getInstance().getThreadManager().executeInBackground(runner);
					} catch (Exception t) {
						t.getMessage();
					}

				}
			});

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
			progress.setForeground(GTSLookAndFeel.getPanelLabelColor());
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
	 * This method initializes removeUser
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getRemoveTrustedAuthorityButton() {
		if (removeTrustedAuthorityButton == null) {
			removeTrustedAuthorityButton = new JButton();
			removeTrustedAuthorityButton.setText("Remove Permission");
			removeTrustedAuthorityButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MobiusRunnable runner = new MobiusRunnable() {
						public void execute() {
							removePermission();
						}
					};
					try {
						PortalResourceManager.getInstance().getThreadManager().executeInBackground(runner);
					} catch (Exception t) {
						t.getMessage();
					}
				}
			});
			removeTrustedAuthorityButton.setIcon(GTSLookAndFeel.getRevokePermissionIcon());
		}
		return removeTrustedAuthorityButton;
	}


	private void removePermission() {
		try {

			/*
			 * GTSAdminClient client = new GTSAdminClient(service, proxy);
			 * client.removeTrustedAuthority(this.getPermissionsTable().getSelectedTrustedAuthority()
			 * .getTrustedAuthorityName());
			 * this.getPermissionsTable().removeSelectedTrustedAuthority();
			 */

		} catch (Exception e) {
			PortalUtils.showErrorMessage(e);
		}
	}


	/**
	 * This method initializes filterPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getFilterPanel() {
		if (filterPanel == null) {
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints9.gridy = 0;
			gridBagConstraints9.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints9.gridx = 0;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints8.gridy = 2;
			gridBagConstraints8.weightx = 1.0;
			gridBagConstraints8.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints8.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints8.gridx = 1;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.gridy = 1;
			gridBagConstraints7.weightx = 1.0;
			gridBagConstraints7.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints7.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints7.gridx = 1;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints6.gridy = 2;
			gridBagConstraints6.gridx = 0;
			jLabel2 = new JLabel();
			jLabel2.setText("Role");
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints5.gridy = 1;
			gridBagConstraints5.gridx = 0;
			jLabel1 = new JLabel();
			jLabel1.setText("Trusted Authority");
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints3.gridx = 1;
			jLabel = new JLabel();
			jLabel.setText("Grid Identity");
			filterPanel = new JPanel();
			filterPanel.setLayout(new GridBagLayout());
			filterPanel.setBorder(BorderFactory.createTitledBorder(null, "Search Criteria",
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, GTSLookAndFeel
					.getPanelLabelColor()));
			filterPanel.add(jLabel, gridBagConstraints9);
			filterPanel.add(getGridIdentity(), gridBagConstraints3);
			filterPanel.add(jLabel1, gridBagConstraints5);
			filterPanel.add(jLabel2, gridBagConstraints6);
			filterPanel.add(getTrustedAuthorities(), gridBagConstraints7);
			filterPanel.add(getRole(), gridBagConstraints8);
		}
		return filterPanel;
	}


	/**
	 * This method initializes trustedAuthorityName
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
	 * This method initializes trustLevel
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getTrustedAuthorities() {
		if (trustedAuthorities == null) {
			trustedAuthorities = new JComboBox();
			trustedAuthorities.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					syncRoles();
				}
			});
		}
		return trustedAuthorities;
	}


	/**
	 * This method initializes status
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getRole() {
		if (role == null) {
			role = new JComboBox();
		}
		return role;
	}


	private synchronized void syncServices() {
		String service = ((GTSServiceListComboBox) getService()).getSelectedService();
		if ((currentService == null) || (!currentService.equals(service))) {
			this.currentService = service;
			try {
				updateProgress(true, "Discovery Trusted Authorities...");

				GTSSearchClient client = new GTSSearchClient(service);
				TrustedAuthority[] tas = client.findTrustedAuthorities(new TrustedAuthorityFilter());
				this.trustedAuthorities.removeAllItems();
				this.trustedAuthorities.addItem(ANY);
				this.trustedAuthorities.addItem(Constants.ALL_TRUST_AUTHORITIES);

				int length = 0;
				if (tas != null) {
					length = tas.length;
					for (int i = 0; i < tas.length; i++) {
						this.trustedAuthorities.addItem(tas[0].getTrustedAuthorityName());
					}
				}
				syncRoles();
				this.updateProgress(false, "Completed [Found " + length + " Trusted Authority(s)]");
			} catch (Exception e) {
				e.printStackTrace();
				PortalUtils.showErrorMessage(e);
				updateProgress(false, "Error!!!");
			}
		}
	}


	private synchronized void syncRoles() {
		this.role.removeAllItems();
		String ta = (String) this.trustedAuthorities.getSelectedItem();
		if (ta.equals(ANY)) {
			role.addItem(ANY);
			role.addItem(Role.TrustServiceAdmin);
			role.addItem(Role.TrustAuthorityManager);
		} else if (ta.equals(Constants.ALL_TRUST_AUTHORITIES)) {
			role.addItem(ANY);
			role.addItem(Role.TrustServiceAdmin);
		} else {
			role.addItem(ANY);
			role.addItem(Role.TrustAuthorityManager);
		}
	}
}
