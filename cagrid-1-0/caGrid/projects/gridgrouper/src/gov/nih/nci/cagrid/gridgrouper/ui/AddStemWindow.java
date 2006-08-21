package gov.nih.nci.cagrid.gridgrouper.ui;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.PortalUtils;
import gov.nih.nci.cagrid.gridgrouper.grouper.StemI;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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
public class AddStemWindow extends GridPortalComponent {

	private static final long serialVersionUID = 1L;

	private StemTreeNode node;

	private JPanel addStemPanel = null;

	private JLabel jLabel10 = null;

	private JTextField childName = null;

	private JLabel jLabel11 = null;

	private JTextField childDisplayName = null;

	private JButton addChildStem = null;

	private JPanel buttonPanel = null;

	private JButton cancelButton = null;

	private JLabel jLabel = null;

	private JTextField gridGrouper = null;

	private JLabel jLabel1 = null;

	private JTextField credentials = null;

	private JLabel jLabel2 = null;

	private JTextField parentStem = null;

	/**
	 * This is the default constructor
	 */
	public AddStemWindow(StemTreeNode node) {
		super();
		this.node = node;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(500, 225);
		this.setContentPane(getAddStemPanel());
		this.setTitle("Add Stem");
		this.setFrameIcon(GridGrouperLookAndFeel.getStemIcon16x16());
	}

	/**
	 * This method initializes addStemPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getAddStemPanel() {
		if (addStemPanel == null) {
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.gridy = 2;
			gridBagConstraints7.weightx = 1.0;
			gridBagConstraints7.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints7.gridx = 1;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints6.gridy = 2;
			jLabel2 = new JLabel();
			jLabel2.setText("Parent Stem");
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.gridy = 1;
			gridBagConstraints5.weightx = 1.0;
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints5.gridx = 1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints4.gridy = 1;
			jLabel1 = new JLabel();
			jLabel1.setText("Credentials");
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints2.gridy = 0;
			jLabel = new JLabel();
			jLabel.setText("Grid Grouper");
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridwidth = 2;
			gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints1.gridy = 5;
			GridBagConstraints gridBagConstraints38 = new GridBagConstraints();
			gridBagConstraints38.anchor = GridBagConstraints.WEST;
			gridBagConstraints38.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints38.gridx = 1;
			gridBagConstraints38.gridy = 4;
			gridBagConstraints38.weightx = 1.0;
			gridBagConstraints38.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints37 = new GridBagConstraints();
			gridBagConstraints37.anchor = GridBagConstraints.WEST;
			gridBagConstraints37.gridx = 0;
			gridBagConstraints37.gridy = 4;
			gridBagConstraints37.insets = new Insets(2, 2, 2, 2);
			jLabel11 = new JLabel();
			jLabel11.setText("Local Display Name");
			GridBagConstraints gridBagConstraints36 = new GridBagConstraints();
			gridBagConstraints36.anchor = GridBagConstraints.WEST;
			gridBagConstraints36.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints36.gridx = 1;
			gridBagConstraints36.gridy = 3;
			gridBagConstraints36.weightx = 1.0;
			gridBagConstraints36.fill = GridBagConstraints.HORIZONTAL;
			GridBagConstraints gridBagConstraints35 = new GridBagConstraints();
			gridBagConstraints35.anchor = GridBagConstraints.WEST;
			gridBagConstraints35.gridx = 0;
			gridBagConstraints35.gridy = 3;
			gridBagConstraints35.insets = new Insets(2, 2, 2, 2);
			jLabel10 = new JLabel();
			jLabel10.setText("Local Name");
			addStemPanel = new JPanel();
			addStemPanel.setLayout(new GridBagLayout());
			addStemPanel.setBorder(BorderFactory.createTitledBorder(null, "Add Stem", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, GridGrouperLookAndFeel.getPanelLabelColor()));
			addStemPanel.add(jLabel10, gridBagConstraints35);
			addStemPanel.add(getChildName(), gridBagConstraints36);
			addStemPanel.add(jLabel11, gridBagConstraints37);
			addStemPanel.add(getChildDisplayName(), gridBagConstraints38);
			addStemPanel.add(getButtonPanel(), gridBagConstraints1);
			addStemPanel.add(jLabel, gridBagConstraints2);
			addStemPanel.add(getGridGrouper(), gridBagConstraints3);
			addStemPanel.add(jLabel1, gridBagConstraints4);
			addStemPanel.add(getCredentials(), gridBagConstraints5);
			addStemPanel.add(jLabel2, gridBagConstraints6);
			addStemPanel.add(getParentStem(), gridBagConstraints7);
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
			addChildStem.setIcon(GridGrouperLookAndFeel.getAddIcon());
			addChildStem.setText("Add Stem");
			addChildStem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MobiusRunnable runner = new MobiusRunnable() {
						public void execute() {
							StemI stem = node.getStem();
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
								node.getBrowser().getProgress().stopEvent(eid,
										"Successfully added a child stem!!!");
								dispose();
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
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints.gridx = -1;
			gridBagConstraints.gridy = -1;
			gridBagConstraints.gridwidth = 2;
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout());
			buttonPanel.add(getAddChildStem(), null);
			buttonPanel.add(getCancelButton(), null);
		}
		return buttonPanel;
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
			cancelButton.setIcon(GridGrouperLookAndFeel.getCloseIcon());
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return cancelButton;
	}

	/**
	 * This method initializes gridGrouper	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getGridGrouper() {
		if (gridGrouper == null) {
			gridGrouper = new JTextField();
			gridGrouper.setEditable(false);
			gridGrouper.setText(node.getGridGrouper().getName());
		}
		return gridGrouper;
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
			credentials.setText(node.getGridGrouper().getProxyIdentity());
		}
		return credentials;
	}

	/**
	 * This method initializes parentStem	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getParentStem() {
		if (parentStem == null) {
			parentStem = new JTextField();
			parentStem.setEditable(false);
			GridGrouperBaseTreeNode parent = (GridGrouperBaseTreeNode)node.getParent();
			if(parent instanceof StemTreeNode){
				parentStem.setText(((StemTreeNode)parent).getStem().getDisplayName());	
			}else{
				parentStem.setText("Root");
			}
		}
		return parentStem;
	}

}
