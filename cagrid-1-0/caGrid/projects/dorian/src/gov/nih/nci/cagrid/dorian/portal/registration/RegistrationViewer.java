package gov.nih.nci.cagrid.gums.portal.registration;

import gov.nih.nci.cagrid.gums.ifs.bean.AttributeDescriptor;
import gov.nih.nci.cagrid.gums.portal.GumsLookAndFeel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.projectmobius.portal.GridPortalBaseFrame;
import org.projectmobius.portal.GridPortalComponent;
import org.projectmobius.portal.PortalResourceManager;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class RegistrationViewer extends GridPortalBaseFrame {

	private static final String USERNAME_LABEL = "Username";

	private static final String PASSWORD_LABEL = "Password";

	private static final String VERIFY_LABEL = "Verify Password";

	private static final String EMAIL_LABEL = "Email";

	private javax.swing.JPanel jContentPane = null;

	private JPanel mainPanel = null;

	private JPanel generalInformation = null;

	private JPanel requiredAttributes = null;

	private JPanel buttonPanel = null;

	private JButton apply = null;

	private JButton cancel = null;

	private RequiredInformationTable requiredInformationTable = null;

	private JScrollPane jScrollPane = null;

	private JButton jButton = null;

	private int lastSelectedRow=-1;
	
	private AttributeDescriptor[] requiredAtts;

	public RegistrationViewer(AttributeDescriptor[] requiredAtts) {
		super();
		this.requiredAtts = requiredAtts;
		initialize();
	}

	private void initialize() {
		this.setSize(500, 300);
		this.setContentPane(getJContentPane());
		this.setFrameIcon(GumsLookAndFeel.getRegistrationIcon());
		this.setTitle("GUMS Registration");
	}

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
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints3.weightx = 1.0D;
			gridBagConstraints3.weighty = 1.0D;
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints4.weightx = 1.0D;
			gridBagConstraints4.weighty = 1.0D;
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.gridy = 2;
			gridBagConstraints5.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints5.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints5.weightx = 1.0D;
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridy = 2;
			gridBagConstraints6.insets = new java.awt.Insets(5, 5, 5, 5);
			gridBagConstraints6.weightx = 1.0D;
			gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
			mainPanel.add(getGeneralInformation(), gridBagConstraints3);
			mainPanel.add(getRequiredAttributes(), gridBagConstraints4);
			mainPanel.add(getButtonPanel(), gridBagConstraints6);
		}
		return mainPanel;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getGeneralInformation() {
		if (generalInformation == null) {

			generalInformation = new JPanel();
			generalInformation.setLayout(new GridBagLayout());

			generalInformation
					.setBorder(javax.swing.BorderFactory
							.createTitledBorder(
									null,
									"Required Information",
									javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
									javax.swing.border.TitledBorder.DEFAULT_POSITION,
									null, GumsLookAndFeel.getPanelLabelColor()));

			this.addTextField(generalInformation, USERNAME_LABEL, "", 0, true);
			this.addPasswordField(generalInformation, PASSWORD_LABEL, "", 1,
					true);
			this
					.addPasswordField(generalInformation, VERIFY_LABEL, "", 2,
							true);
			this.addTextField(generalInformation, EMAIL_LABEL, "", 3, true);

		}
		return generalInformation;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getRequiredAttributes() {
		if (requiredAttributes == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
			gridBagConstraints.gridy = 1;
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			requiredAttributes = new JPanel();
			requiredAttributes.setLayout(new GridBagLayout());
			requiredAttributes
					.setBorder(javax.swing.BorderFactory
							.createTitledBorder(
									null,
									"Required Attributes",
									javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
									javax.swing.border.TitledBorder.DEFAULT_POSITION,
									null, GumsLookAndFeel.getPanelLabelColor()));
			gridBagConstraints16.gridx = 0;
			gridBagConstraints16.gridy = 0;
			gridBagConstraints16.gridheight = 1;
			gridBagConstraints16.weightx = 1.0;
			gridBagConstraints16.weighty = 1.0;
			gridBagConstraints16.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints16.ipadx = 0;
			requiredAttributes.add(getJScrollPane(), gridBagConstraints16);
			requiredAttributes.add(getJButton(), gridBagConstraints);
		}
		return requiredAttributes;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.add(getApply(), null);
			buttonPanel.add(getCancel(), null);
		}
		return buttonPanel;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getApply() {
		if (apply == null) {
			apply = new JButton();
			apply.setText("Apply");
			apply.setIcon(GumsLookAndFeel.getApplyIcon());
			apply.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					submitApplication();
				}
			});
		}
		return apply;
	}

	private void submitApplication() {
		/*
		RegistrationApplication app = new RegistrationApplication();
		String username = this.getTextFieldValue(USERNAME_LABEL).trim();
		if ((username == null) || (username.trim().length() <= 0)) {
			showError("No username specified.");
			return;
		}

		app.setUsername(username);
		String password = this.getPasswordFieldValue(PASSWORD_LABEL).trim();

		if ((password == null) || (password.trim().length() < 6)
				|| (password.trim().length() > 10)) {
			showError("Password must be 6-10 characters in length!!!");
			return;
		}

		String verifyPass = this.getPasswordFieldValue(VERIFY_LABEL).trim();

		if (!password.equals(verifyPass)) {
			showError("The passwords entered do not match!!!");
			return;
		}
		app.setPassword(password);

		String email = this.getTextFieldValue(EMAIL_LABEL).trim();
		if (!GumsUtil.verifyEmail(email)) {
			showError("Invalid email address specified!!!");
			return;
		}
		app.setEmail(email);
		List viewers = this.getRequiredInformationTable().getAttributeViewers();
		UserInformation[] info = new UserInformation[viewers.size()];
		for (int i = 0; i < viewers.size(); i++) {
			AttributeViewer viewer = (AttributeViewer) viewers.get(i);
			info[i] = new UserInformation();
			info[i].setInformationNamespace(viewer.getXMLNamespace());
			info[i].setInformationName(viewer.getXMLName());
			try {
				String xml = viewer.toXML();
				info[i].setInformationXML(xml);
			} catch (Exception e) {
				showError(e.getMessage());
				return;
			}
		}
		app.setUserInfo(info);

		try {
			GumsPortalConf conf = (GumsPortalConf) PortalResourceManager
					.getInstance().getResource(GumsPortalConf.RESOURCE);
			GumsRegistrationClient client = new GumsRegistrationClient(new URL(
					conf.getGumsService()));
			client.register(app);
		} catch (Exception e) {
			showError(e.getMessage());
			e.printStackTrace();
		}

		String message = "You application was successfully submitted to GUMS. \n"
				+ "Your application will be reviewed and you will be \n"
				+ "contacted at "
				+ app.getEmail()
				+ " with the \n"
				+ "with the results of the review.";

		JOptionPane.showMessageDialog(this, message, "Application Submitted",
				JOptionPane.INFORMATION_MESSAGE);
		dispose();
		*/
	}

	

	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCancel() {
		if (cancel == null) {
			cancel = new JButton();
			cancel.setText("Cancel");
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
	private RequiredInformationTable getRequiredInformationTable() {
		if (requiredInformationTable == null) {
			requiredInformationTable = new RequiredInformationTable();
			try {
			
		
				for (int i = 0; i < requiredAtts.length; i++) {
					try {
						this.requiredInformationTable
								.addRequiredInformation(requiredAtts[i]);
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(this, ex.getMessage(),
								"Error", JOptionPane.ERROR_MESSAGE);
						ex.printStackTrace();

					}

				}

			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);

				e.printStackTrace();
			}
		}
		return requiredInformationTable;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(getRequiredInformationTable());
			jScrollPane.setPreferredSize(new java.awt.Dimension(0, 0));
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setText("Edit Attribute");
			jButton.setIcon(GumsLookAndFeel.getEditIcon());
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int row = getRequiredInformationTable().getSelectedRow();
					System.err.println("ROW: " + row);
					if ((row >= 0)&&(row < getRequiredInformationTable().getRowCount())) {
						lastSelectedRow = row;
					} else {
						row = lastSelectedRow;
					}
					if ((row >= 0)
							&& (row < getRequiredInformationTable()
									.getRowCount())) {
						AttributeDescriptor des = new AttributeDescriptor();
						des.setNamespace((String) getRequiredInformationTable()
								.getValueAt(row, 0));
						des.setName((String) getRequiredInformationTable()
								.getValueAt(row, 1));
						GridPortalComponent iframe = getRequiredInformationTable()
								.getAttributeViewer(des)
								.getGumsPortalInternalFrame();
						PortalResourceManager.getInstance().getGridPortal()
								.addGridPortalComponent(iframe);
					} else {
						JOptionPane.showMessageDialog(PortalResourceManager
								.getInstance().getGridPortal(),
								"Please select an attribute to edit!!!");
					}
				}
			});
		}
		return jButton;
	}
}