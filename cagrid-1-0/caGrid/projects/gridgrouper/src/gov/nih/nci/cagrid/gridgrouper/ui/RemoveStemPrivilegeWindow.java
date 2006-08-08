package gov.nih.nci.cagrid.gridgrouper.ui;

import edu.internet2.middleware.grouper.NamingPrivilege;
import edu.internet2.middleware.subject.Subject;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.gridgrouper.common.SubjectUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.projectmobius.common.MobiusRunnable;
import org.projectmobius.portal.GridPortalComponent;
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
public class RemoveStemPrivilegeWindow extends GridPortalComponent {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JPanel mainPanel = null;

	private StemPrivilegeCaddy caddy;

	private JLabel jLabel = null;

	private JTextField identity = null;

	private JPanel privsPanel = null;

	private JCheckBox stem = null;

	private JLabel jLabel1 = null;

	private JCheckBox create = null;

	private JLabel jLabel2 = null;

	private JButton remove = null;

	private StemBrowser browser;

	/**
	 * This is the default constructor
	 */
	public RemoveStemPrivilegeWindow(StemBrowser browser,
			StemPrivilegeCaddy caddy) {
		super();
		this.caddy = caddy;
		this.browser = browser;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(500, 175);
		this.setContentPane(getJContentPane());
		this.setTitle("Remove Stem Privilege");
		this.setFrameIcon(GridGrouperLookAndFeel.getPrivilegesIcon());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getMainPanel(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes mainPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints61 = new GridBagConstraints();
			gridBagConstraints61.gridx = 0;
			gridBagConstraints61.gridwidth = 2;
			gridBagConstraints61.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints61.anchor = GridBagConstraints.SOUTH;
			gridBagConstraints61.gridy = 2;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.gridy = 1;
			gridBagConstraints5.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridwidth = 2;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints2.weightx = 1.0;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
			jLabel = new JLabel();
			jLabel.setText("Identity");
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1.0D;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.gridx = 0;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.setBorder(BorderFactory.createTitledBorder(null,
					"Remove Privilege", TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog",
							Font.BOLD, 12), new Color(62, 109, 181)));
			mainPanel.add(jLabel, gridBagConstraints1);
			mainPanel.add(getIdentity(), gridBagConstraints2);
			mainPanel.add(getPrivsPanel(), gridBagConstraints5);
			mainPanel.add(getRemove(), gridBagConstraints61);
		}
		return mainPanel;
	}

	/**
	 * This method initializes identity
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getIdentity() {
		if (identity == null) {
			identity = new JTextField();
			identity.setEditable(false);
			identity.setText(caddy.getIdentity());
		}
		return identity;
	}

	/**
	 * This method initializes privsPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getPrivsPanel() {
		if (privsPanel == null) {
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 3;
			gridBagConstraints7.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints7.gridy = 0;
			jLabel2 = new JLabel();
			jLabel2.setText("Create");
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 2;
			gridBagConstraints6.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints6.gridy = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 1;
			gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints4.gridy = 0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.gridx = 0;
			jLabel1 = new JLabel();
			jLabel1.setText("Stem");
			privsPanel = new JPanel();
			privsPanel.setLayout(new GridBagLayout());
			privsPanel.setBorder(BorderFactory.createTitledBorder(null,
					"Privilege(s)", TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, new Font("Dialog",
							Font.BOLD, 12), new Color(62, 109, 181)));
			privsPanel.add(getStem(), gridBagConstraints3);
			privsPanel.add(jLabel1, gridBagConstraints4);
			privsPanel.add(getCreate(), gridBagConstraints6);
			privsPanel.add(jLabel2, gridBagConstraints7);
		}
		return privsPanel;
	}

	/**
	 * This method initializes stem
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getStem() {
		if (stem == null) {
			stem = new JCheckBox();
			if (caddy.hasStem()) {
				stem.setSelected(true);
			} else {
				stem.setEnabled(false);
			}
		}
		return stem;
	}

	/**
	 * This method initializes create
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private JCheckBox getCreate() {
		if (create == null) {
			create = new JCheckBox();
			if (caddy.hasCreate()) {
				create.setSelected(true);
			} else {
				create.setEnabled(false);
			}
		}
		return create;
	}

	/**
	 * This method initializes remove
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getRemove() {
		if (remove == null) {
			remove = new JButton();
			remove.setText("Remove Privilege");
			remove.setIcon(GridGrouperLookAndFeel.getRemoveIcon());
			remove.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MobiusRunnable runner = new MobiusRunnable() {
						public void execute() {
							try {

								String id = Utils
										.clean(getIdentity().getText());
								if (id == null) {
									PortalUtils
											.showErrorMessage("Please enter a valid identity!!!");
								}
								Subject sub = SubjectUtils.getSubject(id);
								boolean reload = false;
								if (getStem().isSelected()) {
									browser.getStem().revokePriv(sub, NamingPrivilege.STEM);
									reload = true;
								}
								
								if (getCreate().isSelected()) {
									browser.getStem().revokePriv(sub, NamingPrivilege.CREATE);
									reload = true;
								}
								dispose();
								if(reload){
								browser.loadPrivileges();
								}
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
				}

			});
		}
		return remove;
	}

}
