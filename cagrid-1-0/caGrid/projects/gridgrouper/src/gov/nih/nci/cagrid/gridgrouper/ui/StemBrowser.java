package gov.nih.nci.cagrid.gridgrouper.ui;

import edu.internet2.middleware.grouper.NamingPrivilege;
import edu.internet2.middleware.grouper.Privilege;
import edu.internet2.middleware.subject.Subject;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.gridgrouper.common.SubjectUtils;
import gov.nih.nci.cagrid.gridgrouper.grouper.Stem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.projectmobius.common.MobiusRunnable;
import org.projectmobius.portal.PortalResourceManager;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella</A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @version $Id: GridGrouperBaseTreeNode.java,v 1.1 2006/08/04 03:49:26 langella
 *          Exp $
 */
public class StemBrowser extends JPanel {

	private static final long serialVersionUID = 1L;

	private StemTreeNode node;

	private Stem stem;

	private JPanel stemProperties = null;

	private JLabel jLabel = null;

	private JTextField serviceURI = null;

	private JLabel jLabel1 = null;

	private JTextField stemName = null;

	private JLabel jLabel2 = null;

	private JTextField credentials = null;

	private JTabbedPane stemDetails = null;

	private JPanel details = null;

	private JPanel privileges = null;

	private JPanel childStems = null;

	private JPanel groups = null;

	private JPanel detailsPanel = null;

	private JLabel jLabel3 = null;

	private JTextField groupId = null;

	private JLabel jLabel4 = null;

	private JTextField displayName = null;

	private JLabel jLabel5 = null;

	private JTextField systemName = null;

	private JLabel displayExtensionLabel = null;

	private JTextField displayExtension = null;

	private JLabel jLabel6 = null;

	private JTextField systemExtension = null;

	private JLabel jLabel7 = null;

	private JScrollPane jScrollPane = null;

	private JTextArea description = null;

	private JButton updateStem = null;

	private JPanel privsList = null;

	private JScrollPane jScrollPane1 = null;

	private StemPrivilegesTable privs = null;

	private JPanel privButtons = null;

	private JButton getPrivileges = null;

	private JButton removePrivilege = null;

	private JPanel addPrivPanel = null;

	private JLabel jLabel8 = null;

	private JTextField identity = null;

	private JLabel jLabel9 = null;

	private JComboBox stemPrivilege = null;

	private JButton addPriv = null;

	private JPanel stemsPanel = null;

	private JScrollPane jScrollPane2 = null;

	private StemsTable childStemsTable = null;

	private JPanel addStemPanel = null;

	private JLabel jLabel10 = null;

	private JTextField childName = null;

	private JLabel jLabel11 = null;

	private JTextField childDisplayName = null;

	private JButton addChildStem = null;

	private JPanel buttonPanel = null;

	private JButton viewStem = null;

	private JButton removeStem = null;

	/**
	 * This is the default constructor
	 */
	public StemBrowser(StemTreeNode node) {
		super();
		this.node = node;
		this.stem = node.getStem();
		initialize();
		this.setStem();

	}

	private void setStem() {
		this.serviceURI.setText(this.node.getGridGrouper().getName());
		this.stemName.setText(stem.getDisplayName());
		if (node.getGridGrouper().getProxyIdentity() == null) {
			this.credentials.setText("None");
		} else {
			this.credentials.setText(node.getGridGrouper().getProxyIdentity());
		}
		this.groupId.setText(stem.getUuid());
		this.getDisplayName().setText(stem.getDisplayName());
		this.getSystemName().setText(stem.getName());
		this.getDisplayExtension().setText(stem.getDisplayExtension());
		this.getSystemExtension().setText(stem.getExtension());
		this.getDescription().setText(stem.getDescription());
		getChildStemsTable().clearTable();
		int count = node.getChildCount();
		for (int i = 0; i < count; i++) {
			GridGrouperBaseTreeNode child = (GridGrouperBaseTreeNode) node
					.getChildAt(i);
			if (child instanceof StemTreeNode) {
				getChildStemsTable().addStem((StemTreeNode) child);
			}
		}
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.fill = GridBagConstraints.BOTH;
		gridBagConstraints11.weighty = 1.0;
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.gridy = 1;
		gridBagConstraints11.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints11.weightx = 1.0;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = GridBagConstraints.NORTH;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints.weightx = 1.0D;
		gridBagConstraints.gridy = 0;
		this.setSize(400, 400);
		this.setLayout(new GridBagLayout());
		this.add(getStemProperties(), gridBagConstraints);
		this.add(getStemDetails(), gridBagConstraints11);

	}

	/**
	 * This method initializes stemProperties
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getStemProperties() {
		if (stemProperties == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.gridy = 2;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints6.gridx = 1;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints5.gridy = 2;
			jLabel2 = new JLabel();
			jLabel2.setText("Credentials");
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints4.gridx = 1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints3.gridy = 1;
			jLabel1 = new JLabel();
			jLabel1.setText("Name");
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints2.gridy = 0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridx = 1;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints1.weightx = 1.0;
			jLabel = new JLabel();
			jLabel.setText("Grid Grouper");
			stemProperties = new JPanel();
			stemProperties.setLayout(new GridBagLayout());
			stemProperties.add(jLabel, gridBagConstraints2);
			stemProperties.add(getServiceURI(), gridBagConstraints1);
			stemProperties.add(jLabel1, gridBagConstraints3);
			stemProperties.add(getStemName(), gridBagConstraints4);
			stemProperties.add(jLabel2, gridBagConstraints5);
			stemProperties.add(getCredentials(), gridBagConstraints6);
			stemProperties
					.setBorder(javax.swing.BorderFactory
							.createTitledBorder(
									null,
									"Grid Grouper Stem",
									javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
									javax.swing.border.TitledBorder.DEFAULT_POSITION,
									null, GridGrouperLookAndFeel
											.getPanelLabelColor()));
		}
		return stemProperties;
	}

	/**
	 * This method initializes serviceURI
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getServiceURI() {
		if (serviceURI == null) {
			serviceURI = new JTextField();
			this.serviceURI.setEditable(false);
		}
		return serviceURI;
	}

	/**
	 * This method initializes stemName
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getStemName() {
		if (stemName == null) {
			stemName = new JTextField();
			stemName.setEditable(false);
		}
		return stemName;
	}

	/**
	 * This method initializes credentials
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getCredentials() {
		if (credentials == null) {
			credentials = new JTextField();
			credentials.setEditable(false);
		}
		return credentials;
	}

	public StemTreeNode getStemNode() {
		return node;
	}

	/**
	 * This method initializes stemDetails
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getStemDetails() {
		if (stemDetails == null) {
			stemDetails = new JTabbedPane();
			stemDetails.addTab("Details", GridGrouperLookAndFeel
					.getDetailsIcon(), getDetails(), null);
			stemDetails.addTab("Privileges", GridGrouperLookAndFeel
					.getPrivilegesIcon(), getPrivileges(), null);
			stemDetails.addTab("Child Stems", GridGrouperLookAndFeel
					.getStemIcon(), getChildStems(), null);
			stemDetails.addTab("Groups", GridGrouperLookAndFeel.getGroupIcon22x22(),
					getGroups(), null);
		}
		return stemDetails;
	}

	/**
	 * This method initializes details
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getDetails() {
		if (details == null) {
			details = new JPanel();
			details.setLayout(new BorderLayout());
			details.add(getDetailsPanel(), BorderLayout.CENTER);
		}
		return details;
	}

	/**
	 * This method initializes privileges
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getPrivileges() {
		if (privileges == null) {
			GridBagConstraints gridBagConstraints26 = new GridBagConstraints();
			gridBagConstraints26.gridx = 0;
			gridBagConstraints26.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints26.fill = GridBagConstraints.BOTH;
			gridBagConstraints26.weightx = 1.0D;
			gridBagConstraints26.gridy = 1;
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.gridx = 0;
			gridBagConstraints22.fill = GridBagConstraints.BOTH;
			gridBagConstraints22.weighty = 1.0D;
			gridBagConstraints22.weightx = 1.0D;
			gridBagConstraints22.gridy = 0;
			privileges = new JPanel();
			privileges.setLayout(new GridBagLayout());
			privileges.add(getPrivsList(), gridBagConstraints22);
			privileges.add(getAddPrivPanel(), gridBagConstraints26);
		}
		return privileges;
	}

	/**
	 * This method initializes childStems
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getChildStems() {
		if (childStems == null) {
			GridBagConstraints gridBagConstraints34 = new GridBagConstraints();
			gridBagConstraints34.gridx = 0;
			gridBagConstraints34.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints34.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints34.weightx = 1.0D;
			gridBagConstraints34.gridy = 1;
			GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
			gridBagConstraints32.gridx = 0;
			gridBagConstraints32.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints32.fill = GridBagConstraints.BOTH;
			gridBagConstraints32.weightx = 1.0D;
			gridBagConstraints32.weighty = 1.0D;
			gridBagConstraints32.gridy = 0;
			gridBagConstraints32.gridx = 0;
			gridBagConstraints32.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints32.weightx = 1.0D;
			gridBagConstraints32.weighty = 1.0D;
			gridBagConstraints32.fill = GridBagConstraints.BOTH;
			gridBagConstraints32.gridy = 0;
			childStems = new JPanel();
			childStems.setLayout(new GridBagLayout());
			childStems.add(getStemsPanel(), gridBagConstraints32);
			childStems.add(getAddStemPanel(), gridBagConstraints34);
		}
		return childStems;
	}

	/**
	 * This method initializes groups
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getGroups() {
		if (groups == null) {
			groups = new JPanel();
			groups.setLayout(new GridBagLayout());
		}
		return groups;
	}

	/**
	 * This method initializes detailsPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getDetailsPanel() {
		if (detailsPanel == null) {
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.gridx = 0;
			gridBagConstraints20.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints20.gridwidth = 2;
			gridBagConstraints20.gridy = 7;
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.fill = GridBagConstraints.BOTH;
			gridBagConstraints19.weighty = 1.0;
			gridBagConstraints19.gridx = 0;
			gridBagConstraints19.gridy = 6;
			gridBagConstraints19.gridwidth = 2;
			gridBagConstraints19.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints19.weightx = 1.0;
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.gridx = 0;
			gridBagConstraints18.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints18.gridwidth = 2;
			gridBagConstraints18.gridy = 5;
			jLabel7 = new JLabel();
			jLabel7.setText("Description");
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints17.gridy = 4;
			gridBagConstraints17.weightx = 1.0;
			gridBagConstraints17.anchor = GridBagConstraints.WEST;
			gridBagConstraints17.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints17.gridx = 1;
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.anchor = GridBagConstraints.WEST;
			gridBagConstraints16.gridx = 0;
			gridBagConstraints16.gridy = 4;
			gridBagConstraints16.insets = new Insets(2, 2, 2, 2);
			jLabel6 = new JLabel();
			jLabel6.setText("System Extension");
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints15.gridy = 3;
			gridBagConstraints15.weightx = 1.0;
			gridBagConstraints15.anchor = GridBagConstraints.WEST;
			gridBagConstraints15.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints15.gridx = 1;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 0;
			gridBagConstraints14.anchor = GridBagConstraints.WEST;
			gridBagConstraints14.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints14.gridy = 3;
			displayExtensionLabel = new JLabel();
			displayExtensionLabel.setText("Display Extension");
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints13.gridy = 2;
			gridBagConstraints13.weightx = 1.0;
			gridBagConstraints13.anchor = GridBagConstraints.WEST;
			gridBagConstraints13.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints13.gridx = 1;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.anchor = GridBagConstraints.WEST;
			gridBagConstraints12.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints12.gridy = 2;
			jLabel5 = new JLabel();
			jLabel5.setText("System Name");
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints10.gridy = 1;
			gridBagConstraints10.weightx = 1.0;
			gridBagConstraints10.anchor = GridBagConstraints.WEST;
			gridBagConstraints10.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints10.gridx = 1;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 0;
			gridBagConstraints9.anchor = GridBagConstraints.WEST;
			gridBagConstraints9.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints9.gridy = 1;
			jLabel4 = new JLabel();
			jLabel4.setText("Display Name");
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints8.gridx = 1;
			gridBagConstraints8.gridy = 0;
			gridBagConstraints8.anchor = GridBagConstraints.WEST;
			gridBagConstraints8.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints8.weightx = 1.0;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.anchor = GridBagConstraints.WEST;
			gridBagConstraints7.gridy = 0;
			gridBagConstraints7.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints7.gridx = 0;
			jLabel3 = new JLabel();
			jLabel3.setText("Group Id");
			detailsPanel = new JPanel();
			detailsPanel.setLayout(new GridBagLayout());
			detailsPanel.add(jLabel3, gridBagConstraints7);
			detailsPanel.add(getGroupId(), gridBagConstraints8);
			detailsPanel.add(jLabel4, gridBagConstraints9);
			detailsPanel.add(getDisplayName(), gridBagConstraints10);
			detailsPanel.add(jLabel5, gridBagConstraints12);
			detailsPanel.add(getSystemName(), gridBagConstraints13);
			detailsPanel.add(displayExtensionLabel, gridBagConstraints14);
			detailsPanel.add(getDisplayExtension(), gridBagConstraints15);
			detailsPanel.add(jLabel6, gridBagConstraints16);
			detailsPanel.add(getSystemExtension(), gridBagConstraints17);
			detailsPanel.add(jLabel7, gridBagConstraints18);
			detailsPanel.add(getJScrollPane(), gridBagConstraints19);
			detailsPanel.add(getUpdateStem(), gridBagConstraints20);
		}
		return detailsPanel;
	}

	/**
	 * This method initializes groupId
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getGroupId() {
		if (groupId == null) {
			groupId = new JTextField();
			groupId.setEnabled(true);
			groupId.setEditable(false);
		}
		return groupId;
	}

	/**
	 * This method initializes displayName
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getDisplayName() {
		if (displayName == null) {
			displayName = new JTextField();
			displayName.setEditable(false);
		}
		return displayName;
	}

	/**
	 * This method initializes systemName
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getSystemName() {
		if (systemName == null) {
			systemName = new JTextField();
			systemName.setEditable(false);
		}
		return systemName;
	}

	/**
	 * This method initializes displayExtension
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getDisplayExtension() {
		if (displayExtension == null) {
			displayExtension = new JTextField();
			displayExtension
					.addCaretListener(new javax.swing.event.CaretListener() {
						public void caretUpdate(javax.swing.event.CaretEvent e) {
							monitorUpdate();
						}
					});

		}
		return displayExtension;
	}

	private void monitorUpdate() {
		if (!getDisplayExtension().getText().equals(stem.getDisplayExtension())) {
			this.getUpdateStem().setEnabled(true);
		} else if (!getDescription().getText().equals(stem.getDescription())) {
			this.getUpdateStem().setEnabled(true);
		} else {
			this.getUpdateStem().setEnabled(false);
		}
	}

	/**
	 * This method initializes systemExtension
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getSystemExtension() {
		if (systemExtension == null) {
			systemExtension = new JTextField();
			systemExtension.setEditable(false);
		}
		return systemExtension;
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

	/**
	 * This method initializes description
	 * 
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getDescription() {
		if (description == null) {
			description = new JTextArea();
			description.setLineWrap(true);
			description.addCaretListener(new javax.swing.event.CaretListener() {
				public void caretUpdate(javax.swing.event.CaretEvent e) {
					monitorUpdate();
				}
			});
		}
		return description;
	}

	/**
	 * This method initializes updateStem
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getUpdateStem() {
		if (updateStem == null) {
			updateStem = new JButton();
			updateStem.setText("Update Stem");
			updateStem.setEnabled(false);
			updateStem.setIcon(GridGrouperLookAndFeel.getStemIcon());
			updateStem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MobiusRunnable runner = new MobiusRunnable() {
						public void execute() {
							updateStem();
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
		return updateStem;
	}

	private void updateStem() {
		try {
			if (!getDisplayExtension().getText().equals(
					stem.getDisplayExtension())) {
				stem.setDisplayExtension(getDisplayExtension().getText());
			} else if (!getDescription().getText()
					.equals(stem.getDescription())) {
				stem.setDescription(getDescription().getText());
			}
			node.refresh();
			setStem();
			this.monitorUpdate();
		} catch (Exception e) {
			PortalUtils.showErrorMessage(e);
			node.refresh();
			this.monitorUpdate();
		}
	}

	/**
	 * This method initializes privsList
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getPrivsList() {
		if (privsList == null) {
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.gridx = 0;
			gridBagConstraints23.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints23.weightx = 1.0D;
			gridBagConstraints23.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints23.gridy = 1;
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.fill = GridBagConstraints.BOTH;
			gridBagConstraints21.weighty = 1.0;
			gridBagConstraints21.gridx = 0;
			gridBagConstraints21.gridy = 0;
			gridBagConstraints21.weightx = 1.0;
			privsList = new JPanel();
			privsList.setLayout(new GridBagLayout());
			privsList.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, "Privileges",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
					GridGrouperLookAndFeel.getPanelLabelColor()));
			privsList.add(getJScrollPane1(), gridBagConstraints21);
			privsList.add(getPrivButtons(), gridBagConstraints23);
		}
		return privsList;
	}

	/**
	 * This method initializes jScrollPane1
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setViewportView(getPrivs());
		}
		return jScrollPane1;
	}

	/**
	 * This method initializes privs
	 * 
	 * @return javax.swing.JTable
	 */
	private StemPrivilegesTable getPrivs() {
		if (privs == null) {
			privs = new StemPrivilegesTable();
		}
		return privs;
	}

	/**
	 * This method initializes privButtons
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getPrivButtons() {
		if (privButtons == null) {
			GridBagConstraints gridBagConstraints25 = new GridBagConstraints();
			gridBagConstraints25.gridx = 0;
			gridBagConstraints25.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints25.gridy = 0;
			GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
			gridBagConstraints24.gridx = 1;
			gridBagConstraints24.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints24.gridy = 0;
			privButtons = new JPanel();
			privButtons.setLayout(new GridBagLayout());
			privButtons.add(getGetPrivileges(), gridBagConstraints25);
			privButtons.add(getRemovePrivilege(), gridBagConstraints24);
		}
		return privButtons;
	}

	/**
	 * This method initializes getPrivileges
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getGetPrivileges() {
		if (getPrivileges == null) {
			getPrivileges = new JButton();
			getPrivileges.setText("Get Privileges");
			getPrivileges.setIcon(GridGrouperLookAndFeel.getPrivilegesIcon());
			getPrivileges
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							MobiusRunnable runner = new MobiusRunnable() {
								public void execute() {
									loadPrivileges();
								}
							};
							try {
								PortalResourceManager.getInstance()
										.getThreadManager()
										.executeInBackground(runner);
							} catch (Exception t) {
								t.getMessage();
							}
						}

					});
		}
		return getPrivileges;
	}

	protected void loadPrivileges() {
		synchronized (getPrivs()) {
			int eid = node.getBrowser().getProgress().startEvent(
					"Loading the privileges for " + stem.getDisplayExtension()
							+ "...");
			try {

				getPrivs().clearTable();
				Map map = new HashMap();
				Set s1 = stem.getStemmers();
				Iterator itr1 = s1.iterator();
				while (itr1.hasNext()) {
					Subject sub = (Subject) itr1.next();
					StemPrivilegeCaddy caddy = new StemPrivilegeCaddy(sub
							.getId());
					caddy.setStem(true);
					map.put(caddy.getIdentity(), caddy);
				}

				Set s2 = stem.getCreators();
				Iterator itr2 = s2.iterator();
				while (itr2.hasNext()) {
					Subject sub = (Subject) itr2.next();
					StemPrivilegeCaddy caddy = null;
					if (map.containsKey(sub.getId())) {
						caddy = (StemPrivilegeCaddy) map.get(sub.getId());
					} else {
						caddy = new StemPrivilegeCaddy(sub.getId());
						map.put(caddy.getIdentity(), caddy);
					}
					caddy.setCreate(true);
				}

				Iterator itr3 = map.values().iterator();
				while (itr3.hasNext()) {
					getPrivs().addPrivilege((StemPrivilegeCaddy) itr3.next());
				}
				node.getBrowser().getProgress().stopEvent(
						eid,
						"Loaded the privileges for "
								+ stem.getDisplayExtension() + "!!!");
			} catch (Exception e) {
				node.getBrowser().getProgress().stopEvent(
						eid,
						"Error loading the privileges for "
								+ stem.getDisplayExtension() + "!!!");
				PortalUtils.showErrorMessage(e);
			}
		}
	}

	/**
	 * This method initializes removePrivilege
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getRemovePrivilege() {
		if (removePrivilege == null) {
			removePrivilege = new JButton();
			removePrivilege.setText("Remove Privilege");
			removePrivilege.setIcon(GridGrouperLookAndFeel.getRemoveIcon());
			final StemBrowser sb = this;
			removePrivilege
					.addActionListener(new java.awt.event.ActionListener() {
						public void actionPerformed(java.awt.event.ActionEvent e) {
							MobiusRunnable runner = new MobiusRunnable() {
								public void execute() {
									try {
										StemPrivilegeCaddy caddy = getPrivs()
												.getSelectedPrivilege();
										PortalResourceManager
												.getInstance()
												.getGridPortal()
												.addGridPortalComponent(
														new RemoveStemPrivilegeWindow(
																sb, caddy),
														500, 200);

									} catch (Exception e) {
										PortalUtils.showErrorMessage(e);
									}
								}
							};
							try {
								PortalResourceManager.getInstance()
										.getThreadManager()
										.executeInBackground(runner);
							} catch (Exception t) {
								t.getMessage();
							}
						}

					});
		}
		return removePrivilege;
	}

	/**
	 * This method initializes addPrivPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getAddPrivPanel() {
		if (addPrivPanel == null) {
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.gridx = 0;
			gridBagConstraints31.gridwidth = 2;
			gridBagConstraints31.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints31.gridy = 2;
			GridBagConstraints gridBagConstraints30 = new GridBagConstraints();
			gridBagConstraints30.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints30.gridy = 1;
			gridBagConstraints30.weightx = 1.0;
			gridBagConstraints30.anchor = GridBagConstraints.WEST;
			gridBagConstraints30.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints30.gridx = 1;
			GridBagConstraints gridBagConstraints29 = new GridBagConstraints();
			gridBagConstraints29.anchor = GridBagConstraints.WEST;
			gridBagConstraints29.gridy = 1;
			gridBagConstraints29.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints29.gridx = 0;
			jLabel9 = new JLabel();
			jLabel9.setText("Privilege");
			GridBagConstraints gridBagConstraints28 = new GridBagConstraints();
			gridBagConstraints28.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints28.anchor = GridBagConstraints.WEST;
			gridBagConstraints28.gridx = 1;
			gridBagConstraints28.gridy = 0;
			gridBagConstraints28.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints28.weightx = 1.0;
			GridBagConstraints gridBagConstraints27 = new GridBagConstraints();
			gridBagConstraints27.gridx = 0;
			gridBagConstraints27.anchor = GridBagConstraints.WEST;
			gridBagConstraints27.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints27.gridy = 0;
			jLabel8 = new JLabel();
			jLabel8.setText("Identity");
			addPrivPanel = new JPanel();
			addPrivPanel.setLayout(new GridBagLayout());
			addPrivPanel
					.setBorder(javax.swing.BorderFactory
							.createTitledBorder(
									null,
									"Add Privilege",
									javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
									javax.swing.border.TitledBorder.DEFAULT_POSITION,
									null, GridGrouperLookAndFeel
											.getPanelLabelColor()));
			addPrivPanel.add(jLabel8, gridBagConstraints27);
			addPrivPanel.add(getIdentity(), gridBagConstraints28);
			addPrivPanel.add(jLabel9, gridBagConstraints29);
			addPrivPanel.add(getStemPrivilege(), gridBagConstraints30);
			addPrivPanel.add(getAddPriv(), gridBagConstraints31);
		}
		return addPrivPanel;
	}

	/**
	 * This method initializes identity
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getIdentity() {
		if (identity == null) {
			identity = new JTextField();
		}
		return identity;
	}

	/**
	 * This method initializes stemPrivilege
	 * 
	 * @return javax.swing.JComboBox
	 */
	private JComboBox getStemPrivilege() {
		if (stemPrivilege == null) {
			stemPrivilege = new JComboBox();
			stemPrivilege.addItem(NamingPrivilege.CREATE);
			stemPrivilege.addItem(NamingPrivilege.STEM);
		}
		return stemPrivilege;
	}

	/**
	 * This method initializes addPriv
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAddPriv() {
		if (addPriv == null) {
			addPriv = new JButton();
			addPriv.setText("Add Privilege");
			addPriv.setIcon(GridGrouperLookAndFeel.getAddIcon());
			addPriv.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MobiusRunnable runner = new MobiusRunnable() {
						public void execute() {
							int eid = node.getBrowser().getProgress()
									.startEvent("Adding privilege....");
							try {

								String id = Utils
										.clean(getIdentity().getText());

								Subject sub = SubjectUtils.getSubject(id);
								if (id == null) {
									getIdentity().setText(sub.getId());
								}
								stem.grantPriv(sub,
										(Privilege) getStemPrivilege()
												.getSelectedItem());
								node.getBrowser().getProgress().stopEvent(eid,
										"Successfully added privilege!!!");
								loadPrivileges();
							} catch (Exception e) {
								node.getBrowser().getProgress().stopEvent(eid,
										"Error adding privilege!!!");
								PortalUtils.showErrorMessage(e);
							}
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
		return addPriv;
	}

	public Stem getStem() {
		return stem;
	}

	/**
	 * This method initializes stemsPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getStemsPanel() {
		if (stemsPanel == null) {
			GridBagConstraints gridBagConstraints40 = new GridBagConstraints();
			gridBagConstraints40.gridx = 0;
			gridBagConstraints40.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints40.weightx = 1.0D;
			gridBagConstraints40.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints40.gridy = 1;
			GridBagConstraints gridBagConstraints33 = new GridBagConstraints();
			gridBagConstraints33.fill = GridBagConstraints.BOTH;
			gridBagConstraints33.weighty = 1.0;
			gridBagConstraints33.gridx = 0;
			gridBagConstraints33.gridy = 0;
			gridBagConstraints33.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints33.weightx = 1.0;
			stemsPanel = new JPanel();
			stemsPanel.setLayout(new GridBagLayout());
			stemsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
					null, "Child Stems",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
					GridGrouperLookAndFeel.getPanelLabelColor()));
			stemsPanel.add(getJScrollPane2(), gridBagConstraints33);
			stemsPanel.add(getButtonPanel(), gridBagConstraints40);
		}
		return stemsPanel;
	}

	/**
	 * This method initializes jScrollPane2
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane2() {
		if (jScrollPane2 == null) {
			jScrollPane2 = new JScrollPane();
			jScrollPane2.setViewportView(getChildStemsTable());
		}
		return jScrollPane2;
	}

	/**
	 * This method initializes childStemsTable
	 * 
	 * @return javax.swing.JTable
	 */
	private StemsTable getChildStemsTable() {
		if (childStemsTable == null) {
			childStemsTable = new StemsTable();
		}
		return childStemsTable;
	}

	/**
	 * This method initializes addStemPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getAddStemPanel() {
		if (addStemPanel == null) {
			GridBagConstraints gridBagConstraints39 = new GridBagConstraints();
			gridBagConstraints39.gridx = 0;
			gridBagConstraints39.gridwidth = 2;
			gridBagConstraints39.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints39.gridy = 2;
			GridBagConstraints gridBagConstraints38 = new GridBagConstraints();
			gridBagConstraints38.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints38.gridy = 1;
			gridBagConstraints38.weightx = 1.0;
			gridBagConstraints38.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints38.anchor = GridBagConstraints.WEST;
			gridBagConstraints38.gridx = 1;
			GridBagConstraints gridBagConstraints37 = new GridBagConstraints();
			gridBagConstraints37.anchor = GridBagConstraints.WEST;
			gridBagConstraints37.gridy = 1;
			gridBagConstraints37.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints37.gridx = 0;
			jLabel11 = new JLabel();
			jLabel11.setText("Local Display Name");
			GridBagConstraints gridBagConstraints36 = new GridBagConstraints();
			gridBagConstraints36.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints36.gridx = 1;
			gridBagConstraints36.gridy = 0;
			gridBagConstraints36.anchor = GridBagConstraints.WEST;
			gridBagConstraints36.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints36.weightx = 1.0;
			GridBagConstraints gridBagConstraints35 = new GridBagConstraints();
			gridBagConstraints35.anchor = GridBagConstraints.WEST;
			gridBagConstraints35.gridy = 0;
			gridBagConstraints35.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints35.gridx = 0;
			jLabel10 = new JLabel();
			jLabel10.setText("Local Name");
			addStemPanel = new JPanel();
			addStemPanel.setLayout(new GridBagLayout());
			addStemPanel.setBorder(BorderFactory.createTitledBorder(null,
					"Add Stem", TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog",
							Font.BOLD, 12), new Color(62, 109, 181)));
			addStemPanel.add(jLabel10, gridBagConstraints35);
			addStemPanel.add(getChildName(), gridBagConstraints36);
			addStemPanel.add(jLabel11, gridBagConstraints37);
			addStemPanel.add(getChildDisplayName(), gridBagConstraints38);
			addStemPanel.add(getAddChildStem(), gridBagConstraints39);
		}
		return addStemPanel;
	}

	/**
	 * This method initializes childName
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getChildName() {
		if (childName == null) {
			childName = new JTextField();
		}
		return childName;
	}

	/**
	 * This method initializes childDisplayName
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getChildDisplayName() {
		if (childDisplayName == null) {
			childDisplayName = new JTextField();
		}
		return childDisplayName;
	}

	/**
	 * This method initializes addChildStem
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getAddChildStem() {
		if (addChildStem == null) {
			addChildStem = new JButton();
			addChildStem.setText("Add Child Stem");
			addChildStem.setIcon(GridGrouperLookAndFeel.getAddIcon());
			addChildStem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MobiusRunnable runner = new MobiusRunnable() {
						public void execute() {
							int eid = node.getBrowser().getProgress()
									.startEvent("Adding a child stem....");
							try {

								String ext = Utils.clean(childName.getText());
								if (ext == null) {
									PortalUtils
											.showErrorMessage("You must enter a local name for the stem!!!");
									return;
								}

								String disExt = Utils.clean(childDisplayName
										.getText());
								if (disExt == null) {
									PortalUtils
											.showErrorMessage("You must enter a local display name for the stem!!!");
									return;
								}

								stem.addChildStem(ext, disExt);
								node.refresh();
								setStem();
								node.getBrowser().getProgress().stopEvent(eid,
										"Successfully added a child stem!!!");
							} catch (Exception e) {
								node.getBrowser().getProgress().stopEvent(eid,
										"Error adding a child stem!!!");
								PortalUtils.showErrorMessage(e);
							}
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
		return addChildStem;
	}

	/**
	 * This method initializes buttonPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			GridBagConstraints gridBagConstraints42 = new GridBagConstraints();
			gridBagConstraints42.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints42.gridy = 0;
			gridBagConstraints42.gridx = 1;
			GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
			gridBagConstraints41.gridx = 0;
			gridBagConstraints41.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints41.gridy = 0;
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			buttonPanel.add(getViewStem(), gridBagConstraints41);
			buttonPanel.add(getRemoveStem(), gridBagConstraints42);
		}
		return buttonPanel;
	}

	/**
	 * This method initializes viewStem
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getViewStem() {
		if (viewStem == null) {
			viewStem = new JButton();
			viewStem.setText("View Stem");
			viewStem.setIcon(GridGrouperLookAndFeel.getQueryIcon());
			viewStem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try {
						getChildStemsTable().doubleClick();
					} catch (Exception ex) {
						PortalUtils.showErrorMessage(ex);
					}
				}
			});
		}
		return viewStem;
	}

	/**
	 * This method initializes removeStem
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getRemoveStem() {
		if (removeStem == null) {
			removeStem = new JButton();
			removeStem.setText("Remove Stem");
			removeStem.setIcon(GridGrouperLookAndFeel.getRemoveIcon());
			removeStem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MobiusRunnable runner = new MobiusRunnable() {
						public void execute() {

							StemTreeNode child = null;
							try {
								child = getChildStemsTable().getSelectedStem();
							} catch (Exception ex) {
								PortalUtils.showErrorMessage(ex);
								return;
							}

							int eid = node.getBrowser().getProgress()
									.startEvent("Removing child stem....");
							try {
								child.getStem().delete();
								node.refresh();
								setStem();
								node
										.getBrowser()
										.getProgress()
										.stopEvent(eid,
												"Successfully removed the child stem!!!");
							} catch (Exception e) {
								node.getBrowser().getProgress().stopEvent(eid,
										"Error removing the child stem!!!");
								PortalUtils.showErrorMessage(e);
							}
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
		return removeStem;
	}
}
