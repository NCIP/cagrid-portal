package gov.nih.nci.cagrid.gts.portal;

import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.gridca.portal.ProxyCaddy;
import gov.nih.nci.cagrid.gridca.portal.ProxyComboBox;
import gov.nih.nci.cagrid.gts.client.GTSAdminClient;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.globus.gsi.GlobusCredential;
import org.projectmobius.common.MobiusRunnable;
import org.projectmobius.portal.GridPortalComponent;
import org.projectmobius.portal.PortalResourceManager;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class AddPermissionWindow extends GridPortalComponent {

	private JPanel jContentPane = null;

	private JPanel topPanel = null;

	private JLabel jLabel = null;

	private JComboBox gts = null;

	private JLabel jLabel1 = null;

	private JComboBox proxy = null;

	private JPanel buttonPanel = null;

	private JButton addButton = null;

	private JButton cancelButton = null;

	private PermissionPanel permissionPanel = null;


	/**
	 * This is the default constructor
	 */
	public AddPermissionWindow(String service, GlobusCredential cred) {
		super();
		initialize();
		this.gts.setSelectedItem(service);
		this.proxy.setSelectedItem(new ProxyCaddy(cred));
		syncServices();
	}


	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(600, 400);
		this.setContentPane(getJContentPane());
		this.setTitle("Add Permission");
		this.setFrameIcon(GTSLookAndFeel.getAddPermissionIcon());
	}


	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints1.weighty = 1.0D;
			gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints1.gridy = 1;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints12.gridy = 2;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.ipadx = 0;
			gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.gridy = 0;
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getTopPanel(), gridBagConstraints);
			jContentPane.add(getButtonPanel(), gridBagConstraints12);
			jContentPane.add(getPermissionPanel(), gridBagConstraints1);
		}
		return jContentPane;
	}


	/**
	 * This method initializes topPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getTopPanel() {
		if (topPanel == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridy = 1;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints5.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints5.gridx = 1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints4.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints4.gridy = 1;
			jLabel1 = new JLabel();
			jLabel1.setText("Select Proxy");
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints3.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.weightx = 1.0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints2.gridy = 0;
			jLabel = new JLabel();
			jLabel.setText("Grid Trust Service (GTS)");
			topPanel = new JPanel();
			topPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Service/Login Information",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, GTSLookAndFeel.getPanelLabelColor()));
			topPanel.setLayout(new GridBagLayout());
			topPanel.add(jLabel, gridBagConstraints2);
			topPanel.add(getGts(), gridBagConstraints3);
			topPanel.add(jLabel1, gridBagConstraints4);
			topPanel.add(getProxy(), gridBagConstraints5);
		}
		return topPanel;
	}


	/**
	 * This method initializes gts
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getGts() {
		if (gts == null) {
			gts = new GTSServiceListComboBox();
			gts.addActionListener(new java.awt.event.ActionListener() {
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
		return gts;
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
	 * This method initializes buttonPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.add(getAddButton(), null);
			buttonPanel.add(getCancelButton(), null);
		}
		return buttonPanel;
	}


	/**
	 * This method initializes addButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAddButton() {
		if (addButton == null) {
			addButton = new JButton();
			addButton.setText("Add Permission");
			addButton.setIcon(GTSLookAndFeel.getAddPermissionIcon());
			addButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MobiusRunnable runner = new MobiusRunnable() {
						public void execute() {
							addPermission();
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

		return addButton;
	}


	/**
	 * This method initializes cancelButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setText("Cancel");
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
			cancelButton.setIcon(GTSLookAndFeel.getCloseIcon());
		}
		return cancelButton;
	}


	private void addPermission() {
		try {
			getAddButton().setEnabled(false);
			GlobusCredential proxy = ((ProxyComboBox) getProxy()).getSelectedProxy();
			String service = ((GTSServiceListComboBox) getGts()).getSelectedService();
			GTSAdminClient client = new GTSAdminClient(service, proxy);
			client.addPermission(permissionPanel.getPermission());
			dispose();
		} catch (Exception e) {
			getAddButton().setEnabled(true);
			e.printStackTrace();
			PortalUtils.showErrorMessage(e);
		}

	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private PermissionPanel getPermissionPanel() {
		if (permissionPanel == null) {
			permissionPanel = new PermissionPanel(false);
			permissionPanel.setBorder(BorderFactory.createTitledBorder(null, "Permission",
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, GTSLookAndFeel
					.getPanelLabelColor()));
		}
		return permissionPanel;
	}


	private synchronized void syncServices() {
		String service = ((GTSServiceListComboBox) getGts()).getSelectedService();
		try {
			int length = permissionPanel.syncWithService(service);
		} catch (Exception e) {
			e.printStackTrace();
			PortalUtils.showErrorMessage(e);
		}
	}

}
