package gov.nih.nci.cagrid.gums.idp.portal;

import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.gums.bean.PermissionDeniedFault;
import gov.nih.nci.cagrid.gums.client.IdPAdministrationClient;
import gov.nih.nci.cagrid.gums.idp.bean.IdPUser;
import gov.nih.nci.cagrid.gums.idp.bean.IdPUserFilter;
import gov.nih.nci.cagrid.gums.portal.GUMSServiceListComboBox;
import gov.nih.nci.cagrid.gums.portal.GumsLookAndFeel;
import gov.nih.nci.cagrid.gums.portal.ProxyComboBox;
import gov.nih.nci.cagrid.security.commstyle.CommunicationStyle;
import gov.nih.nci.cagrid.security.commstyle.SecureConversationWithEncryption;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
 * @version $Id: UserManagerWindow.java,v 1.15 2005-12-05 17:49:20 langella Exp $
 */
public class UserManagerWindow extends GridPortalBaseFrame {

	private final static String ROLE_PANEL = "Role";

	private final static String STATUS_PANEL = "Status";

	private final static String INFO_PANEL = "User Information";

	private javax.swing.JPanel jContentPane = null;

	private JPanel mainPanel = null;

	private JPanel contentPanel = null;

	private JPanel buttonPanel = null;

	private JButton cancel = null;

	private UsersTable usersTable = null;

	private JScrollPane jScrollPane = null;

	private JButton manageUser = null;

	private JPanel jPanel = null;

	private JTabbedPane jTabbedPane = null;

	private JPanel jPanel1 = null;

	private JLabel jLabel = null;

	private JTextField firstName = null;

	private JLabel jLabel1 = null;

	private JTextField lastName = null;

	private JLabel jLabel2 = null;

	private JTextField organization = null;

	private JLabel jLabel3 = null;

	private JTextField address = null;

	private JLabel jLabel4 = null;

	private JTextField address2 = null;

	private JLabel jLabel5 = null;

	private JTextField city = null;

	private JLabel jLabel6 = null;

	private StateListComboBox state = null;

	private JLabel jLabel7 = null;

	private JTextField zipcode = null;

	private JLabel jLabel8 = null;

	private JTextField phoneNumber = null;

	private JLabel jLabel9 = null;

	private JTextField email = null;

	private JLabel jLabel10 = null;

	private CountryListComboBox country = null;

	private JLabel jLabel11 = null;

	private JTextField username = null;

	private JPanel jPanel2 = null;

	private JLabel jLabel14 = null;

	private JPanel queryPanel = null;

	private JButton query = null;

	private JPanel role = null;

	private JLabel userRoleLabel = null;

	private UserRolesComboBox userRole = null;

	private JPanel status = null;

	private JLabel statusLabel = null;

	private UserStatusComboBox userStatus = null;

	private JComboBox service = null;

	private boolean isQuerying = false;

	private Object mutex = new Object();

	private JLabel proxyLabel = null;

	private JComboBox proxy = null;

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
			GridBagConstraints gridBagConstraints33 = new GridBagConstraints();
			gridBagConstraints33.gridx = 0;
			gridBagConstraints33.gridy = 1;
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
			gridBagConstraints1.gridy = 2;
			gridBagConstraints1.ipadx = 0;
			gridBagConstraints1.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints1.weightx = 1.0D;
			gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints1.weighty = 1.0D;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 3;
			gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints2.anchor = java.awt.GridBagConstraints.SOUTH;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			mainPanel.add(getContentPanel(), gridBagConstraints1);
			mainPanel.add(getButtonPanel(), gridBagConstraints2);
			mainPanel.add(getJPanel(), gridBagConstraints35);
			mainPanel.add(getQueryPanel(), gridBagConstraints33);
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
		final int row = getUsersTable().getSelectedRow();

		if ((row >= 0) && (row < getUsersTable().getRowCount())) {
			MobiusRunnable runner = new MobiusRunnable() {
				public void execute() {
					IdPUser user = (IdPUser) getUsersTable().getValueAt(row, 0);
					String service = ((GUMSServiceListComboBox) getService())
							.getSelectedService();
					try {
						GlobusCredential proxy = ((ProxyComboBox) getProxy())
								.getSelectedProxy();

						PortalResourceManager.getInstance().getGridPortal()
								.addGridPortalComponent(
										new UserWindow(service, proxy, user));
					} catch (Exception e) {
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

		} else {
			JOptionPane.showMessageDialog(PortalResourceManager.getInstance()
					.getGridPortal(), "Please select a user to manage!!!");
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
			GridBagConstraints gridBagConstraints27 = new GridBagConstraints();
			gridBagConstraints27.fill = GridBagConstraints.BOTH;
			gridBagConstraints27.gridy = 1;
			gridBagConstraints27.weightx = 1.0;
			gridBagConstraints27.weighty = 0.0D;
			gridBagConstraints27.gridx = 0;
			jPanel = new JPanel();
			jPanel.setLayout(new GridBagLayout());
			jPanel.add(getJTabbedPane(), gridBagConstraints27);
			jPanel.add(getJPanel2(), gridBagConstraints34);
		}
		return jPanel;
	}

	/**
	 * This method initializes jTabbedPane
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();
			jTabbedPane.setBorder(BorderFactory.createTitledBorder(null,
					"Search Criteria", TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, null, GumsLookAndFeel
							.getPanelLabelColor()));
			jTabbedPane.addTab(STATUS_PANEL, null, getStatus(), null);
			jTabbedPane.addTab(INFO_PANEL, null, getJPanel1(), null);
			jTabbedPane.addTab(ROLE_PANEL, null, getRole(), null);
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
			GridBagConstraints gridBagConstraints26 = new GridBagConstraints();
			gridBagConstraints26.anchor = GridBagConstraints.WEST;
			gridBagConstraints26.insets = new Insets(1, 1, 1, 1);
			gridBagConstraints26.gridwidth = 1;
			gridBagConstraints26.gridx = 1;
			gridBagConstraints26.gridy = 0;
			gridBagConstraints26.weightx = 1.0D;
			gridBagConstraints26.weighty = 0.0D;
			gridBagConstraints26.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints25 = new GridBagConstraints();
			gridBagConstraints25.anchor = GridBagConstraints.WEST;
			gridBagConstraints25.gridwidth = 1;
			gridBagConstraints25.gridx = 0;
			gridBagConstraints25.gridy = 0;
			gridBagConstraints25.insets = new Insets(1, 1, 1, 1);
			jLabel11 = new JLabel();
			jLabel11.setText("Username");
			GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
			gridBagConstraints24.anchor = GridBagConstraints.WEST;
			gridBagConstraints24.insets = new Insets(1, 1, 1, 1);
			gridBagConstraints24.gridx = 3;
			gridBagConstraints24.gridy = 5;
			gridBagConstraints24.weightx = 1.0D;
			gridBagConstraints24.weighty = 0.0D;
			gridBagConstraints24.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.anchor = GridBagConstraints.WEST;
			gridBagConstraints23.gridx = 2;
			gridBagConstraints23.gridy = 5;
			gridBagConstraints23.insets = new Insets(1, 1, 1, 1);
			jLabel10 = new JLabel();
			jLabel10.setText("Country");
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints22.gridx = 1;
			gridBagConstraints22.gridy = 5;
			gridBagConstraints22.weightx = 1.0D;
			gridBagConstraints22.weighty = 0.0D;
			gridBagConstraints22.insets = new Insets(1, 1, 1, 1);
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.anchor = GridBagConstraints.WEST;
			gridBagConstraints21.gridx = 0;
			gridBagConstraints21.gridy = 5;
			gridBagConstraints21.insets = new Insets(1, 1, 1, 1);
			jLabel9 = new JLabel();
			jLabel9.setText("Email");
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.anchor = GridBagConstraints.WEST;
			gridBagConstraints20.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints20.gridx = 1;
			gridBagConstraints20.gridy = 4;
			gridBagConstraints20.weightx = 1.0D;
			gridBagConstraints20.weighty = 0.0D;
			gridBagConstraints20.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.anchor = GridBagConstraints.WEST;
			gridBagConstraints19.gridx = 0;
			gridBagConstraints19.gridy = 4;
			gridBagConstraints19.insets = new Insets(2, 2, 2, 2);
			jLabel8 = new JLabel();
			jLabel8.setText("Phone Number");
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.anchor = GridBagConstraints.WEST;
			gridBagConstraints18.insets = new Insets(1, 1, 1, 1);
			gridBagConstraints18.gridx = 3;
			gridBagConstraints18.gridy = 4;
			gridBagConstraints18.weightx = 1.0D;
			gridBagConstraints18.weighty = 0.0D;
			gridBagConstraints18.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.anchor = GridBagConstraints.WEST;
			gridBagConstraints17.gridx = 2;
			gridBagConstraints17.gridy = 4;
			gridBagConstraints17.insets = new Insets(1, 1, 1, 1);
			jLabel7 = new JLabel();
			jLabel7.setText("Zipcode");
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.anchor = GridBagConstraints.WEST;
			gridBagConstraints16.insets = new Insets(1, 1, 1, 1);
			gridBagConstraints16.gridx = 3;
			gridBagConstraints16.gridy = 3;
			gridBagConstraints16.weightx = 1.0D;
			gridBagConstraints16.weighty = 0.0D;
			gridBagConstraints16.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.anchor = GridBagConstraints.WEST;
			gridBagConstraints15.gridx = 2;
			gridBagConstraints15.gridy = 3;
			gridBagConstraints15.insets = new Insets(1, 1, 1, 1);
			jLabel6 = new JLabel();
			jLabel6.setText("State");
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.anchor = GridBagConstraints.WEST;
			gridBagConstraints14.insets = new Insets(1, 1, 1, 1);
			gridBagConstraints14.gridx = 3;
			gridBagConstraints14.gridy = 2;
			gridBagConstraints14.weightx = 1.0D;
			gridBagConstraints14.weighty = 0.0D;
			gridBagConstraints14.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.anchor = GridBagConstraints.WEST;
			gridBagConstraints13.gridx = 2;
			gridBagConstraints13.gridy = 2;
			gridBagConstraints13.insets = new Insets(1, 1, 1, 1);
			jLabel5 = new JLabel();
			jLabel5.setText("City");
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.anchor = GridBagConstraints.WEST;
			gridBagConstraints12.insets = new Insets(1, 1, 1, 1);
			gridBagConstraints12.gridx = 3;
			gridBagConstraints12.gridy = 1;
			gridBagConstraints12.weightx = 1.0D;
			gridBagConstraints12.weighty = 0.0D;
			gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.anchor = GridBagConstraints.WEST;
			gridBagConstraints11.gridx = 2;
			gridBagConstraints11.gridy = 1;
			gridBagConstraints11.insets = new Insets(1, 1, 1, 1);
			jLabel4 = new JLabel();
			jLabel4.setText("Address2");
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.anchor = GridBagConstraints.WEST;
			gridBagConstraints10.insets = new Insets(1, 1, 1, 1);
			gridBagConstraints10.gridx = 3;
			gridBagConstraints10.gridy = 0;
			gridBagConstraints10.weightx = 1.0D;
			gridBagConstraints10.weighty = 0.0D;
			gridBagConstraints10.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.anchor = GridBagConstraints.WEST;
			gridBagConstraints9.gridx = 2;
			gridBagConstraints9.gridy = 0;
			gridBagConstraints9.insets = new Insets(1, 1, 1, 1);
			jLabel3 = new JLabel();
			jLabel3.setText("Address");
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.anchor = GridBagConstraints.WEST;
			gridBagConstraints8.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints8.gridwidth = 1;
			gridBagConstraints8.gridx = 1;
			gridBagConstraints8.gridy = 3;
			gridBagConstraints8.weightx = 1.0D;
			gridBagConstraints8.weighty = 0.0D;
			gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.anchor = GridBagConstraints.WEST;
			gridBagConstraints7.gridwidth = 1;
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.gridy = 3;
			gridBagConstraints7.insets = new Insets(1, 1, 1, 1);
			jLabel2 = new JLabel();
			jLabel2.setText("Organization");
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.insets = new Insets(1, 1, 1, 1);
			gridBagConstraints6.gridx = 1;
			gridBagConstraints6.gridy = 2;
			gridBagConstraints6.weightx = 1.0D;
			gridBagConstraints6.weighty = 0.0D;
			gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.gridy = 2;
			gridBagConstraints5.insets = new Insets(1, 1, 1, 1);
			jLabel1 = new JLabel();
			jLabel1.setText("Last Name");
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.insets = new Insets(1, 1, 1, 1);
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 1.0D;
			gridBagConstraints3.weighty = 0.0D;
			gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.gridwidth = 2;
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.insets = new Insets(1, 1, 1, 1);
			jLabel = new JLabel();
			jLabel.setText("First Name");
			jPanel1 = new JPanel();
			jPanel1.setName(INFO_PANEL);
			jPanel1.setLayout(new GridBagLayout());
			jPanel1.add(jLabel, gridBagConstraints);
			jPanel1.add(getFirstName(), gridBagConstraints3);
			jPanel1.add(jLabel1, gridBagConstraints5);
			jPanel1.add(getLastName(), gridBagConstraints6);
			jPanel1.add(jLabel2, gridBagConstraints7);
			jPanel1.add(getOrganization(), gridBagConstraints8);
			jPanel1.add(jLabel3, gridBagConstraints9);
			jPanel1.add(getAddress(), gridBagConstraints10);
			jPanel1.add(jLabel4, gridBagConstraints11);
			jPanel1.add(getAddress2(), gridBagConstraints12);
			jPanel1.add(jLabel5, gridBagConstraints13);
			jPanel1.add(getCity(), gridBagConstraints14);
			jPanel1.add(jLabel6, gridBagConstraints15);
			jPanel1.add(getState(), gridBagConstraints16);
			jPanel1.add(jLabel7, gridBagConstraints17);
			jPanel1.add(getZipcode(), gridBagConstraints18);
			jPanel1.add(jLabel8, gridBagConstraints19);
			jPanel1.add(getPhoneNumber(), gridBagConstraints20);
			jPanel1.add(jLabel9, gridBagConstraints21);
			jPanel1.add(getEmail(), gridBagConstraints22);
			jPanel1.add(jLabel10, gridBagConstraints23);
			jPanel1.add(getCountry(), gridBagConstraints24);
			jPanel1.add(jLabel11, gridBagConstraints25);
			jPanel1.add(getUsername(), gridBagConstraints26);
		}
		return jPanel1;
	}

	/**
	 * This method initializes jTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getFirstName() {
		if (firstName == null) {
			firstName = new JTextField();
		}
		return firstName;
	}

	/**
	 * This method initializes jTextField1
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getLastName() {
		if (lastName == null) {
			lastName = new JTextField();
		}
		return lastName;
	}

	/**
	 * This method initializes jTextField2
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getOrganization() {
		if (organization == null) {
			organization = new JTextField();
		}
		return organization;
	}

	/**
	 * This method initializes jTextField3
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getAddress() {
		if (address == null) {
			address = new JTextField();
		}
		return address;
	}

	/**
	 * This method initializes jTextField4
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getAddress2() {
		if (address2 == null) {
			address2 = new JTextField();
		}
		return address2;
	}

	/**
	 * This method initializes jTextField5
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getCity() {
		if (city == null) {
			city = new JTextField();
		}
		return city;
	}

	/**
	 * This method initializes stateListComboBox
	 * 
	 * @return gov.nih.nci.cagrid.gums.idp.portal.StateListComboBox
	 */
	private StateListComboBox getState() {
		if (state == null) {
			state = new StateListComboBox(true);
		}
		return state;
	}

	/**
	 * This method initializes jTextField6
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getZipcode() {
		if (zipcode == null) {
			zipcode = new JTextField();
		}
		return zipcode;
	}

	/**
	 * This method initializes jTextField7
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getPhoneNumber() {
		if (phoneNumber == null) {
			phoneNumber = new JTextField();
		}
		return phoneNumber;
	}

	/**
	 * This method initializes jTextField8
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
	 * This method initializes countryListComboBox
	 * 
	 * @return gov.nih.nci.cagrid.gums.idp.portal.CountryListComboBox
	 */
	private CountryListComboBox getCountry() {
		if (country == null) {
			country = new CountryListComboBox(true);
		}
		return country;
	}

	/**
	 * This method initializes jTextField9
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getUsername() {
		if (username == null) {
			username = new JTextField();
		}
		return username;
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

		synchronized (mutex) {
			if (isQuerying) {
				PortalUtils
						.showErrorMessage("Query Already in Progress",
								"Please wait until the current query is finished before executing another.");
				return;
			} else {
				isQuerying = true;
			}
		}

		this.getUsersTable().clearTable();

		try {
		GlobusCredential proxy = ((ProxyComboBox) getProxy())
				.getSelectedProxy();
		IdPUserFilter f = new IdPUserFilter();
		JPanel panel = (JPanel) this.getJTabbedPane().getSelectedComponent();
		if (panel.getName().equals(ROLE_PANEL)) {
			f.setRole(this.getUserRole().getSelectedUserRole());
		} else if (panel.getName().equals(STATUS_PANEL)) {
			f.setStatus(this.getUserStatus().getSelectedUserStatus());
		} else if (panel.getName().equals(INFO_PANEL)) {
			f.setUserId(format(getUsername().getText()));
			f.setFirstName(format(getFirstName().getText()));
			f.setLastName(format(getLastName().getText()));
			f.setOrganization(format(getOrganization().getText()));
			f.setAddress(format(getAddress().getText()));
			f.setAddress2(format(getAddress2().getText()));
			f.setCity(format(getCity().getText()));
			f.setState(getState().getSelectedState());
			f.setZipcode(format(getZipcode().getText()));
			f.setCountry(getCountry().getSelectedCountry());
			f.setPhoneNumber(format(getPhoneNumber().getText()));
			f.setEmail(format(getEmail().getText()));
		}

			String service = ((GUMSServiceListComboBox) getService())
					.getSelectedService();
			CommunicationStyle style = new SecureConversationWithEncryption(
					proxy);
			IdPAdministrationClient client = new IdPAdministrationClient(
					service, style);
			IdPUser[] users = client.findUsers(f);
			if (users != null) {
				for (int i = 0; i < users.length; i++) {
					this.getUsersTable().addUser(users[i]);
				}
			}
			PortalUtils.showMessage("Query Completed.");
		} catch (PermissionDeniedFault pdf) {
			PortalUtils.showErrorMessage(pdf);
		} catch (Exception e) {
			e.printStackTrace();
			PortalUtils.showErrorMessage(e);
		}
		isQuerying = false;
	}

	private String format(String s) {
		if ((s == null) || (s.trim().length() == 0)) {
			return null;
		} else {
			return s;
		}
	}

	/**
	 * This method initializes allUsers
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getRole() {
		if (role == null) {
			userRoleLabel = new JLabel();
			userRoleLabel.setText("User Role");
			role = new JPanel();
			role.setName(ROLE_PANEL);
			role.add(userRoleLabel, null);
			role.add(getUserRole(), null);
		}
		return role;
	}

	/**
	 * This method initializes userRole
	 * 
	 * @return javax.swing.JComboBox
	 */
	private UserRolesComboBox getUserRole() {
		if (userRole == null) {
			userRole = new UserRolesComboBox(true);
		}
		return userRole;
	}

	/**
	 * This method initializes status
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getStatus() {
		if (status == null) {
			statusLabel = new JLabel();
			statusLabel.setText("User Status");
			status = new JPanel();
			status.setName(STATUS_PANEL);
			status.add(statusLabel, null);
			status.add(getUserStatus(), null);
		}
		return status;
	}

	/**
	 * This method initializes userStatus
	 * 
	 * @return javax.swing.JComboBox
	 */
	private UserStatusComboBox getUserStatus() {
		if (userStatus == null) {
			userStatus = new UserStatusComboBox(true);
		}
		return userStatus;
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

}
