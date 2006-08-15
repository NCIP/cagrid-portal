package gov.nih.nci.cagrid.gridgrouper.ui;

import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.gridgrouper.grouper.GroupI;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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
public class GroupBrowser extends JPanel {

	private static final long serialVersionUID = 1L;

	private GroupTreeNode node;

	private GroupI group;

	private JPanel groupProperties = null;

	private JLabel jLabel = null;

	private JTextField serviceURI = null;

	private JLabel jLabel1 = null;

	private JTextField groupName = null;

	private JLabel jLabel2 = null;

	private JTextField credentials = null;

	private JTabbedPane groupDetails = null;

	private JPanel details = null;

	private JPanel privileges = null;

	private JPanel members = null;

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

	private JButton updateGroup = null;

	private JLabel jLabel14 = null;

	private JTextField created = null;

	private JLabel jLabel15 = null;

	private JLabel jLabel16 = null;

	private JLabel jLabel17 = null;

	private JTextField creator = null;

	private JTextField lastModified = null;

	private JTextField lastModifiedBy = null;

	/**
	 * This is the default constructor
	 */
	public GroupBrowser(GroupTreeNode node) {
		super();
		this.node = node;
		this.group = node.getGroup();
		initialize();
		this.setGroup();

	}

	private void setGroup() {
		this.serviceURI.setText(this.node.getGridGrouper().getName());
		this.groupName.setText(group.getDisplayName());
		if (node.getGridGrouper().getProxyIdentity() == null) {
			this.credentials.setText("None");
		} else {
			this.credentials.setText(node.getGridGrouper().getProxyIdentity());
		}
		this.groupId.setText(group.getUuid());
		this.getDisplayName().setText(group.getDisplayName());
		this.getSystemName().setText(group.getName());
		this.getDisplayExtension().setText(group.getDisplayExtension());
		this.getSystemExtension().setText(group.getExtension());
		this.getDescription().setText(group.getDescription());
		this.getCreated().setText(group.getCreateTime().toString());
		try {
			this.getCreator().setText(group.getCreateSubject().getId());
		} catch (Exception e) {

		}
		this.getLastModified().setText(group.getModifyTime().toString());
		try {
			this.getLastModifiedBy().setText(group.getModifySubject().getId());
		} catch (Exception e) {

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
		this.add(getGroupProperties(), gridBagConstraints);
		this.add(getGroupDetails(), gridBagConstraints11);

	}

	/**
	 * This method initializes groupProperties
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getGroupProperties() {
		if (groupProperties == null) {
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
			groupProperties = new JPanel();
			groupProperties.setLayout(new GridBagLayout());
			groupProperties.add(jLabel, gridBagConstraints2);
			groupProperties.add(getServiceURI(), gridBagConstraints1);
			groupProperties.add(jLabel1, gridBagConstraints3);
			groupProperties.add(getGroupName(), gridBagConstraints4);
			groupProperties.add(jLabel2, gridBagConstraints5);
			groupProperties.add(getCredentials(), gridBagConstraints6);
			groupProperties
					.setBorder(javax.swing.BorderFactory
							.createTitledBorder(
									null,
									"Grid Grouper Group",
									javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
									javax.swing.border.TitledBorder.DEFAULT_POSITION,
									null, GridGrouperLookAndFeel
											.getPanelLabelColor()));
		}
		return groupProperties;
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
	 * This method initializes groupName
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getGroupName() {
		if (groupName == null) {
			groupName = new JTextField();
			groupName.setEditable(false);
		}
		return groupName;
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

	/**
	 * This method initializes groupDetails
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getGroupDetails() {
		if (groupDetails == null) {
			groupDetails = new JTabbedPane();
			groupDetails.addTab("Details", GridGrouperLookAndFeel
					.getDetailsIcon(), getDetails(), null);
			groupDetails.addTab("Privileges", GridGrouperLookAndFeel
					.getPrivilegesIcon(), getPrivileges(), null);
			groupDetails.addTab("Members", GridGrouperLookAndFeel
					.getGroupIcon22x22(), getMembers(), null);
		}
		return groupDetails;
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
			privileges = new JPanel();
			privileges.setLayout(new GridBagLayout());
		}
		return privileges;
	}

	/**
	 * This method initializes members
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMembers() {
		if (members == null) {
			members = new JPanel();
			members.setLayout(new GridBagLayout());
		}
		return members;
	}

	/**
	 * This method initializes detailsPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getDetailsPanel() {
		if (detailsPanel == null) {
			GridBagConstraints gridBagConstraints61 = new GridBagConstraints();
			gridBagConstraints61.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints61.gridy = 8;
			gridBagConstraints61.weightx = 1.0;
			gridBagConstraints61.anchor = GridBagConstraints.WEST;
			gridBagConstraints61.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints61.gridx = 1;
			GridBagConstraints gridBagConstraints60 = new GridBagConstraints();
			gridBagConstraints60.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints60.gridy = 7;
			gridBagConstraints60.weightx = 1.0;
			gridBagConstraints60.anchor = GridBagConstraints.WEST;
			gridBagConstraints60.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints60.gridx = 1;
			GridBagConstraints gridBagConstraints59 = new GridBagConstraints();
			gridBagConstraints59.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints59.gridy = 6;
			gridBagConstraints59.weightx = 1.0;
			gridBagConstraints59.anchor = GridBagConstraints.WEST;
			gridBagConstraints59.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints59.gridx = 1;
			GridBagConstraints gridBagConstraints58 = new GridBagConstraints();
			gridBagConstraints58.gridx = 0;
			gridBagConstraints58.anchor = GridBagConstraints.WEST;
			gridBagConstraints58.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints58.gridy = 8;
			jLabel17 = new JLabel();
			jLabel17.setText("Last Modified By");
			GridBagConstraints gridBagConstraints57 = new GridBagConstraints();
			gridBagConstraints57.gridx = 0;
			gridBagConstraints57.anchor = GridBagConstraints.WEST;
			gridBagConstraints57.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints57.gridy = 7;
			jLabel16 = new JLabel();
			jLabel16.setText("Last Modified");
			GridBagConstraints gridBagConstraints56 = new GridBagConstraints();
			gridBagConstraints56.gridx = 0;
			gridBagConstraints56.anchor = GridBagConstraints.WEST;
			gridBagConstraints56.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints56.gridy = 6;
			jLabel15 = new JLabel();
			jLabel15.setText("Created By");
			GridBagConstraints gridBagConstraints55 = new GridBagConstraints();
			gridBagConstraints55.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints55.gridy = 5;
			gridBagConstraints55.weightx = 1.0;
			gridBagConstraints55.anchor = GridBagConstraints.WEST;
			gridBagConstraints55.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints55.gridx = 1;
			GridBagConstraints gridBagConstraints54 = new GridBagConstraints();
			gridBagConstraints54.gridx = 0;
			gridBagConstraints54.anchor = GridBagConstraints.WEST;
			gridBagConstraints54.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints54.gridy = 5;
			jLabel14 = new JLabel();
			jLabel14.setText("Created");
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.gridx = 0;
			gridBagConstraints20.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints20.gridwidth = 2;
			gridBagConstraints20.gridy = 11;
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.fill = GridBagConstraints.BOTH;
			gridBagConstraints19.weighty = 1.0;
			gridBagConstraints19.gridx = 0;
			gridBagConstraints19.gridy = 10;
			gridBagConstraints19.gridwidth = 2;
			gridBagConstraints19.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints19.weightx = 1.0;
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.gridx = 0;
			gridBagConstraints18.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints18.gridwidth = 2;
			gridBagConstraints18.gridy = 9;
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
			detailsPanel.setBorder(BorderFactory.createTitledBorder(null,
					"Stem Details",
					javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
					javax.swing.border.TitledBorder.DEFAULT_POSITION, null,
					GridGrouperLookAndFeel.getPanelLabelColor()));
			detailsPanel.add(jLabel7, gridBagConstraints18);
			detailsPanel.add(getJScrollPane(), gridBagConstraints19);
			detailsPanel.add(getUpdateGroup(), gridBagConstraints20);
			detailsPanel.add(jLabel14, gridBagConstraints54);
			detailsPanel.add(getCreated(), gridBagConstraints55);
			detailsPanel.add(jLabel15, gridBagConstraints56);
			detailsPanel.add(jLabel16, gridBagConstraints57);
			detailsPanel.add(jLabel17, gridBagConstraints58);
			detailsPanel.add(getCreator(), gridBagConstraints59);
			detailsPanel.add(getLastModified(), gridBagConstraints60);
			detailsPanel.add(getLastModifiedBy(), gridBagConstraints61);
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
		if (!getDisplayExtension().getText()
				.equals(group.getDisplayExtension())) {
			this.getUpdateGroup().setEnabled(true);
		} else if (!getDescription().getText().equals(group.getDescription())) {
			this.getUpdateGroup().setEnabled(true);
		}else if (!getSystemExtension().getText().equals(group.getExtension())) {
			this.getUpdateGroup().setEnabled(true);
		}  else {
			this.getUpdateGroup().setEnabled(false);
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
			systemExtension.setEditable(true);
			systemExtension.addCaretListener(new javax.swing.event.CaretListener() {
				public void caretUpdate(javax.swing.event.CaretEvent e) {
					monitorUpdate();
				}
			});
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
	 * This method initializes updateGroup
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getUpdateGroup() {
		if (updateGroup == null) {
			updateGroup = new JButton();
			updateGroup.setText("Update Group");
			updateGroup.setEnabled(false);
			updateGroup.setIcon(GridGrouperLookAndFeel.getGroupIcon22x22());
			updateGroup.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MobiusRunnable runner = new MobiusRunnable() {
						public void execute() {
							updateGroup();
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
		return updateGroup;
	}

	private void updateGroup() {
		try {
			
			if (!getSystemExtension().getText().equals(
					group.getExtension())) {
				 group.setExtension(getSystemExtension().getText());
			}
			if (!getDisplayExtension().getText().equals(
					group.getDisplayExtension())) {
				 group.setDisplayExtension(getDisplayExtension().getText());
			}
			if (!getDescription().getText().equals(group.getDescription())) {
				group.setDescription(getDescription().getText());
			}
			node.refresh();
			setGroup();
			this.monitorUpdate();
		} catch (Exception e) {
			PortalUtils.showErrorMessage(e);
			node.refresh();
			this.monitorUpdate();
		}
	}

	/**
	 * This method initializes created
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getCreated() {
		if (created == null) {
			created = new JTextField();
			created.setEditable(false);
		}
		return created;
	}

	/**
	 * This method initializes creator
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getCreator() {
		if (creator == null) {
			creator = new JTextField();
			creator.setEditable(false);
		}
		return creator;
	}

	/**
	 * This method initializes lastModified
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getLastModified() {
		if (lastModified == null) {
			lastModified = new JTextField();
			lastModified.setEditable(false);
		}
		return lastModified;
	}

	/**
	 * This method initializes lastModifiedBy
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getLastModifiedBy() {
		if (lastModifiedBy == null) {
			lastModifiedBy = new JTextField();
			lastModifiedBy.setEditable(false);
		}
		return lastModifiedBy;
	}
	protected GroupTreeNode getGroupNode(){
		return node;
	}
}
