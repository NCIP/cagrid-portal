package org.cagrid.gaards.ui.gts;

import gov.nih.nci.cagrid.common.Runner;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.gts.bean.TrustLevel;
import gov.nih.nci.cagrid.gts.bean.TrustLevels;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthority;
import gov.nih.nci.cagrid.gts.bean.TrustedAuthorityFilter;
import gov.nih.nci.cagrid.gts.client.GTSAdminClient;
import gov.nih.nci.cagrid.gts.client.GTSPublicClient;

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

import org.cagrid.gaards.ui.common.CredentialComboBox;
import org.cagrid.grape.ApplicationComponent;
import org.cagrid.grape.GridApplication;
import org.cagrid.grape.LookAndFeel;
import org.cagrid.grape.utils.ErrorDialog;
import org.globus.gsi.GlobusCredential;


/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: TrustedAuthoritiesWindow.java,v 1.2 2006/03/27 19:05:40
 *          langella Exp $
 */
public class TrustedAuthoritiesWindow extends ApplicationComponent implements TrustedAuthorityRefresher {
	
	private static final long serialVersionUID = 1L;

	private final static String ANY = "Any";

	private javax.swing.JPanel jContentPane = null;

	private JPanel mainPanel = null;

	private JPanel contentPanel = null;

	private JPanel buttonPanel = null;

	private TrustedAuthorityTable trustedAuthorityTable = null;

	private JScrollPane jScrollPane = null;

	private JButton viewTrustedAuthority = null;

	private JPanel jPanel = null;

	private JPanel jPanel2 = null;

	private JLabel jLabel14 = null;

	private JPanel queryPanel = null;

	private JButton query = null;

	private JComboBox service = null;

	private JLabel proxyLabel = null;

	private JComboBox proxy = null;

	private JPanel progressPanel = null;

	private JProgressBar progress = null;

	private JButton removeTrustedAuthorityButton = null;

	private JPanel filterPanel = null;

	private JLabel jLabel = null;

	private JTextField trustedAuthorityName = null;

	private JLabel jLabel1 = null;

	private JLabel jLabel2 = null;

	private JComboBox trustLevel = null;

	private JComboBox status = null;

	private JButton addButton = null;

	private boolean searchDone = false;

	private JLabel jLabel3 = null;

	private LifetimeComboBox lifetime = null;

	private JLabel jLabel4 = null;

	private IsAuthorityComboBox isAuthority = null;

	private JLabel jLabel5 = null;

	private GTSServiceListComboBox authorityGTS = null;

	private JLabel jLabel6 = null;

	private GTSServiceListComboBox sourceGTS = null;


	/**
	 * This is the default constructor
	 */
	public TrustedAuthoritiesWindow() {
		super();
		initialize();
		this.setFrameIcon(GTSLookAndFeel.getTrustedAuthorityIcon());
	}


	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setSize(500, 500);
		this.setContentPane(getJContentPane());
		this.setTitle("Trusted Certificate Authority Management");
		updateTrustLevels();

	}


	private void updateTrustLevels() {
		trustLevel.removeAllItems();
		trustLevel.addItem(ANY);
		String selectedService = Utils.clean((String) getService().getSelectedItem());
		if (selectedService != null) {
			try {
				GTSPublicClient client = new GTSPublicClient(selectedService);
				TrustLevel[] levels = client.getTrustLevels();
				if (levels != null) {
					for (int i = 0; i < levels.length; i++) {
						trustLevel.addItem(levels[i].getName());
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
				ErrorDialog.showError("Error obtaining the trust levels from " 
                    + selectedService + ":\n" + e.getMessage());
			}
		}
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
			contentPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Trusted Authority(s)",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, LookAndFeel.getPanelLabelColor()));
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
			buttonPanel.add(getAddButton(), null);
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
	private TrustedAuthorityTable getTrustedAuthorityTable() {
		if (trustedAuthorityTable == null) {
			trustedAuthorityTable = new TrustedAuthorityTable(this);
		}
		return trustedAuthorityTable;
	}


	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getTrustedAuthorityTable());
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
			viewTrustedAuthority.setText("View/Edit Trusted Authority");
			viewTrustedAuthority.setIcon(GTSLookAndFeel.getQueryIcon());
			viewTrustedAuthority.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Runner runner = new Runner() {
						public void execute() {
							disableAllActions();
							showTrustedAuthority();
							enableAllActions();
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

		return viewTrustedAuthority;
	}


	public void showTrustedAuthority() {
		try {
			String selectedService = ((GTSServiceListComboBox) getService()).getSelectedService();
			GlobusCredential selectedProxy = ((CredentialComboBox) getProxy()).getSelectedCredential();
			TrustedAuthorityWindow window = new TrustedAuthorityWindow(selectedService, selectedProxy, this.getTrustedAuthorityTable()
				.getSelectedTrustedAuthority(), this);
			GridApplication.getContext().addApplicationComponent(window, 800, 675);
		} catch (Exception e) {
			ErrorDialog.showError(e);
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
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, 
                LookAndFeel.getPanelLabelColor()));
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
			query.setText("Find Trusted Authorities");
			query.setIcon(LookAndFeel.getQueryIcon());
			query.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Runner runner = new Runner() {
						public void execute() {
							disableAllActions();
							findTrustedAuthorities();
							enableAllActions();
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
		return query;
	}


	private void findTrustedAuthorities() {

		this.getTrustedAuthorityTable().clearTable();
		this.updateProgress(true, "Finding Trusted Authorities...");

		try {
			String selectedService = ((GTSServiceListComboBox) getService()).getSelectedService();

			TrustedAuthorityFilter filter = new TrustedAuthorityFilter();
			filter.setName(Utils.clean(trustedAuthorityName.getText()));
			String tl = (String) trustLevel.getSelectedItem();
			TrustLevels levels = new TrustLevels();
			if (!tl.equals(ANY)) {
				String[] list = new String[1];
				list[0] = tl;
				levels.setTrustLevel(list);
			}
			filter.setTrustLevels(levels);
			filter.setStatus(((StatusComboBox) status).getStatus());
			filter.setLifetime(this.lifetime.getLifetime());
			filter.setIsAuthority(this.getIsAuthority().getIsAuthority());
			filter.setAuthorityGTS(this.getAuthorityGTS().getSelectedService());
			filter.setSourceGTS(this.getSourceGTS().getSelectedService());
			GTSPublicClient client = new GTSPublicClient(selectedService);
			int length = 0;
			TrustedAuthority[] tas = client.findTrustedAuthorities(filter);
			if (tas != null) {
				length = tas.length;
			}
			for (int i = 0; i < length; i++) {
				this.trustedAuthorityTable.addTrustedAuthority(tas[i]);
			}
			searchDone = true;

			this.updateProgress(false, "Completed [Found " + length + " Trusted Authority(s)]");

		} catch (Exception e) {
			ErrorDialog.showError(e);
			this.updateProgress(false, "Error");
		}
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
					updateTrustLevels();
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
			proxy = new CredentialComboBox();
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
			progress.setForeground(LookAndFeel.getPanelLabelColor());
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
			removeTrustedAuthorityButton.setText("Remove Trusted Authority");
			removeTrustedAuthorityButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Runner runner = new Runner() {
						public void execute() {
							disableAllActions();
							removeTrustedAuthority();
							enableAllActions();
						}
					};
					try {
						GridApplication.getContext().executeInBackground(runner);
					} catch (Exception t) {
						t.getMessage();
					}
				}
			});
			removeTrustedAuthorityButton.setIcon(GTSLookAndFeel.getRemoveIcon());
		}
		return removeTrustedAuthorityButton;
	}


	private void removeTrustedAuthority() {
		try {
			String selectedService = ((GTSServiceListComboBox) getService()).getSelectedService();
			GlobusCredential selectedProxy = ((CredentialComboBox) getProxy()).getSelectedCredential();
			GTSAdminClient client = new GTSAdminClient(selectedService, selectedProxy);
			client.removeTrustedAuthority(this.getTrustedAuthorityTable().getSelectedTrustedAuthority().getName());
			this.getTrustedAuthorityTable().removeSelectedTrustedAuthority();
			refreshTrustedAuthorities();

		} catch (Exception e) {
			ErrorDialog.showError(e);
		}
	}


	/**
	 * This method initializes filterPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getFilterPanel() {
		if (filterPanel == null) {
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints17.gridy = 6;
			gridBagConstraints17.weightx = 1.0;
			gridBagConstraints17.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints17.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints17.gridx = 1;
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.gridx = 0;
			gridBagConstraints16.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints16.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints16.gridy = 6;
			jLabel6 = new JLabel();
			jLabel6.setText("Source GTS");
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints15.gridy = 5;
			gridBagConstraints15.weightx = 1.0;
			gridBagConstraints15.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints15.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints15.gridx = 1;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 0;
			gridBagConstraints14.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints14.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints14.gridy = 5;
			jLabel5 = new JLabel();
			jLabel5.setText("Authority GTS");
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints13.gridy = 4;
			gridBagConstraints13.weightx = 1.0;
			gridBagConstraints13.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints13.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints13.gridx = 1;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints12.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints12.gridy = 4;
			jLabel4 = new JLabel();
			jLabel4.setText("Is Authority");
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.gridy = 3;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints11.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints11.gridx = 1;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints10.gridy = 0;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints9.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints9.gridy = 3;
			jLabel3 = new JLabel();
			jLabel3.setText("Lifetime");
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
			gridBagConstraints6.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints6.gridx = 0;
			jLabel2 = new JLabel();
			jLabel2.setText("Status");
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints5.gridy = 1;
			gridBagConstraints5.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints5.gridx = 0;
			jLabel1 = new JLabel();
			jLabel1.setText("Level of Assurance");
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints3.gridx = 1;
			jLabel = new JLabel();
			jLabel.setText("Trusted Authority Name");
			filterPanel = new JPanel();
			filterPanel.setLayout(new GridBagLayout());
			filterPanel.setBorder(BorderFactory.createTitledBorder(null, "Search Criteria",
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, 
                LookAndFeel.getPanelLabelColor()));
			filterPanel.add(jLabel, gridBagConstraints10);
			filterPanel.add(getTrustedAuthorityName(), gridBagConstraints3);
			filterPanel.add(jLabel1, gridBagConstraints5);
			filterPanel.add(jLabel2, gridBagConstraints6);
			filterPanel.add(getTrustLevel(), gridBagConstraints7);
			filterPanel.add(getStatus(), gridBagConstraints8);
			filterPanel.add(jLabel3, gridBagConstraints9);
			filterPanel.add(getLifetime(), gridBagConstraints11);
			filterPanel.add(jLabel4, gridBagConstraints12);
			filterPanel.add(getIsAuthority(), gridBagConstraints13);
			filterPanel.add(jLabel5, gridBagConstraints14);
			filterPanel.add(getAuthorityGTS(), gridBagConstraints15);
			filterPanel.add(jLabel6, gridBagConstraints16);
			filterPanel.add(getSourceGTS(), gridBagConstraints17);
		}
		return filterPanel;
	}


	/**
	 * This method initializes trustedAuthorityName
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTrustedAuthorityName() {
		if (trustedAuthorityName == null) {
			trustedAuthorityName = new JTextField();
		}
		return trustedAuthorityName;
	}


	/**
	 * This method initializes trustLevel
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getTrustLevel() {
		if (trustLevel == null) {
			trustLevel = new JComboBox();
			trustLevel.addItem(ANY);
		}
		return trustLevel;
	}


	/**
	 * This method initializes status
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getStatus() {
		if (status == null) {
			status = new StatusComboBox(true);
		}
		return status;
	}


	/**
	 * This method initializes addButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAddButton() {
		if (addButton == null) {
			addButton = new JButton();
			addButton.setText("Add Trusted Authority");
			addButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					disableAllActions();
					addTrustedAuthority();
					enableAllActions();
				}
			});
			addButton.setIcon(GTSLookAndFeel.getAddIcon());
		}
		return addButton;
	}


	private void addTrustedAuthority() {
		GridApplication.getContext().addApplicationComponent(new TrustedAuthorityWindow(this));
	}


	private void disableAllActions() {
		getQuery().setEnabled(false);
		getAddButton().setEnabled(false);
		getViewTrustedAuthority().setEnabled(false);
		getRemoveTrustedAuthorityButton().setEnabled(false);
	}


	private void enableAllActions() {
		getQuery().setEnabled(true);
		getAddButton().setEnabled(true);
		getViewTrustedAuthority().setEnabled(true);
		getRemoveTrustedAuthorityButton().setEnabled(true);
	}


	public void refreshTrustedAuthorities() {
		if (searchDone) {
			disableAllActions();
			findTrustedAuthorities();
			enableAllActions();
		}
	}


	/**
	 * This method initializes lifetime
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getLifetime() {
		if (lifetime == null) {
			lifetime = new LifetimeComboBox();
		}
		return lifetime;
	}


	/**
	 * This method initializes isAuthority
	 * 
	 * @return javax.swing.JComboBox
	 */
	private IsAuthorityComboBox getIsAuthority() {
		if (isAuthority == null) {
			isAuthority = new IsAuthorityComboBox();
		}
		return isAuthority;
	}


	/**
	 * This method initializes authorityGTS
	 * 
	 * @return javax.swing.JComboBox
	 */
	private GTSServiceListComboBox getAuthorityGTS() {
		if (authorityGTS == null) {
			authorityGTS = new GTSServiceListComboBox(true);
		}
		return authorityGTS;
	}


	/**
	 * This method initializes sourceGTS
	 * 
	 * @return javax.swing.JComboBox
	 */
	private GTSServiceListComboBox getSourceGTS() {
		if (sourceGTS == null) {
			sourceGTS = new GTSServiceListComboBox(true);
		}
		return sourceGTS;
	}
}
