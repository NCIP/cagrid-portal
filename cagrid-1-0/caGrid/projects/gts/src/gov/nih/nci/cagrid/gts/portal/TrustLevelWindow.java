package gov.nih.nci.cagrid.gts.portal;

import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.gridca.portal.ProxyCaddy;
import gov.nih.nci.cagrid.gridca.portal.ProxyComboBox;
import gov.nih.nci.cagrid.gts.bean.TrustLevel;
import gov.nih.nci.cagrid.gts.client.GTSAdminClient;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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
public class TrustLevelWindow extends GridPortalComponent {

	private JPanel jContentPane = null;

	private JPanel topPanel = null;

	private JLabel jLabel = null;

	private JComboBox gts = null;

	private JLabel jLabel1 = null;

	private JComboBox proxy = null;

	private JPanel buttonPanel = null;

	private JButton addButton = null;

	private JButton cancelButton = null;

	private JPanel trustLevelPanel = null;

	private JLabel jLabel2 = null;

	private JTextField trustLevelName = null;

	private JLabel jLabel3 = null;

	private JTextArea description = null;

	private boolean update = false;

	private JScrollPane jScrollPane = null;

	private TrustLevelRefresher refresher;


	/**
	 * This is the default constructor
	 */
	public TrustLevelWindow(String service, GlobusCredential cred, TrustLevelRefresher refresher) {
		this(service, cred, null, refresher);
	}


	public TrustLevelWindow(String service, GlobusCredential cred, TrustLevel level, TrustLevelRefresher refresher) {
		super();
		this.refresher = refresher;
		if (level != null) {
			update = true;
		} else {
			update = false;
		}
		initialize();
		this.gts.setSelectedItem(service);
		this.proxy.setSelectedItem(new ProxyCaddy(cred));
		if (level != null) {
			this.getGts().setEnabled(false);
			this.getTrustLevelName().setEnabled(false);
			this.getTrustLevelName().setText(level.getName());
			this.getDescription().setText(level.getDescription());
			this.getDescription().setCaretPosition(0);
		}
	}


	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(600, 400);
		this.setContentPane(getJContentPane());
		if (update) {
			this.setTitle("View/Modify Trust Level");
			this.setFrameIcon(GTSLookAndFeel.getTrustLevelIcon());
		} else {
			this.setTitle("Add Trust Level");
			this.setFrameIcon(GTSLookAndFeel.getAddTrustLevelIcon());
		}
	}


	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.gridx = 0;
			gridBagConstraints31.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints31.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints31.weightx = 1.0D;
			gridBagConstraints31.weighty = 1.0D;
			gridBagConstraints31.gridy = 1;
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
			jContentPane.add(getTrustLevelPanel(), gridBagConstraints31);
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
			if (update) {
				addButton.setText("Update Trust Level");
				addButton.setIcon(GTSLookAndFeel.getTrustLevelIcon());
			} else {
				addButton.setText("Add Trust Level");
				addButton.setIcon(GTSLookAndFeel.getAddTrustLevelIcon());
			}
			addButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MobiusRunnable runner = new MobiusRunnable() {
						public void execute() {
							addUpdateTrustLevel();
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


	private void addUpdateTrustLevel() {
		try {
			getAddButton().setEnabled(false);
			GlobusCredential proxy = ((ProxyComboBox) getProxy()).getSelectedProxy();
			String service = ((GTSServiceListComboBox) getGts()).getSelectedService();
			TrustLevel level = new TrustLevel();
			level.setName(getTrustLevelName().getText().trim());
			level.setDescription(getDescription().getText().trim());
			GTSAdminClient client = new GTSAdminClient(service, proxy);
			if (update) {
				client.updateTrustLevel(level);
			} else {
				client.addTrustLevel(level);
			}
			refresher.refreshTrustLevels();
			dispose();
			if (update) {
				PortalUtils.showMessage("Successfully added the trust level " + level.getName() + "!!!");
			} else {
				PortalUtils.showMessage("Successfully updated the trust level " + level.getName() + "!!!");
			}
		} catch (Exception e) {
			getAddButton().setEnabled(true);
			e.printStackTrace();
			PortalUtils.showErrorMessage(e);
		}

	}


	/**
	 * This method initializes trustLevelPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getTrustLevelPanel() {
		if (trustLevelPanel == null) {
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints9.weighty = 1.0;
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.gridy = 2;
			gridBagConstraints9.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints9.gridwidth = 2;
			gridBagConstraints9.weightx = 1.0;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.gridwidth = 2;
			gridBagConstraints7.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints7.gridy = 1;
			jLabel3 = new JLabel();
			jLabel3.setText("Description");
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.gridx = 1;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints6.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints6.weightx = 1.0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints1.gridx = 0;
			jLabel2 = new JLabel();
			jLabel2.setText("Name");
			trustLevelPanel = new JPanel();
			trustLevelPanel.setLayout(new GridBagLayout());
			trustLevelPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Trust Level",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, GTSLookAndFeel.getPanelLabelColor()));
			trustLevelPanel.add(jLabel2, gridBagConstraints1);
			trustLevelPanel.add(getTrustLevelName(), gridBagConstraints6);
			trustLevelPanel.add(jLabel3, gridBagConstraints7);
			trustLevelPanel.add(getJScrollPane(), gridBagConstraints9);
		}
		return trustLevelPanel;
	}


	/**
	 * This method initializes name
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTrustLevelName() {
		if (trustLevelName == null) {
			trustLevelName = new JTextField();
		}
		return trustLevelName;
	}


	/**
	 * This method initializes description
	 * 
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getDescription() {
		if (description == null) {
			description = new JTextArea();
			description.setLineWrap(false);
		}
		return description;
	}


	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getDescription());
		}
		return jScrollPane;
	}

}
