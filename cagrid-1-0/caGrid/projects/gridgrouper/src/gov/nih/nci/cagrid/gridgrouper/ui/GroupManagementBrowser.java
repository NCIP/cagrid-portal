package gov.nih.nci.cagrid.gridgrouper.ui;

import gov.nih.nci.cagrid.common.portal.PortalUtils;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.projectmobius.common.MobiusRunnable;
import org.projectmobius.portal.GridPortalComponent;
import org.projectmobius.portal.PortalResourceManager;

public class GroupManagementBrowser extends GridPortalComponent {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JPanel mainPanel = null;

	private JPanel treePanel = null;

	private JPanel contentPanel = null;

	private JScrollPane treePane = null;

	private GridGrouperTree groupTree = null;

	private GridGrouperProgressBar progress = null;

	private JPanel buttonPanel = null;

	private JButton addGridGrouper = null;

	private JButton removeGridGrouper = null;

	private JButton view = null;

	private JButton refresh = null;

	/**
	 * This is the default constructor
	 */
	public GroupManagementBrowser() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(800, 500);
		this.setContentPane(getJContentPane());
		this.setTitle("Group Management Browser");
		this.setFrameIcon(GridGrouperLookAndFeel.getGrouperIcon22x22());
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
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.weightx = 1.0D;
			gridBagConstraints3.weighty = 1.0D;
			gridBagConstraints3.gridy = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 0.0D;
			gridBagConstraints.weighty = 1.0D;
			gridBagConstraints.gridx = 0;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getTreePanel(), gridBagConstraints);
			mainPanel.add(getContentPanel(), gridBagConstraints3);
		}
		return mainPanel;
	}

	/**
	 * This method initializes treePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getTreePanel() {
		if (treePanel == null) {
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 2;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.insets = new Insets(2, 10, 2, 10);
			gridBagConstraints7.weightx = 1.0D;
			gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.gridy = 1;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = GridBagConstraints.BOTH;
			gridBagConstraints6.weighty = 1.0;
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints6.weightx = 1.0;
			treePanel = new JPanel();
			treePanel.setLayout(new GridBagLayout());
			treePanel.add(getTreePane(), gridBagConstraints6);
			treePanel.add(getProgress(), gridBagConstraints7);
			treePanel.add(getButtonPanel(), gridBagConstraints1);
		}
		return treePanel;
	}

	/**
	 * This method initializes contentPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getContentPanel() {
		if (contentPanel == null) {
			contentPanel = new JPanel();
			contentPanel.setLayout(new GridBagLayout());
		}
		return contentPanel;
	}

	/**
	 * This method initializes treePane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getTreePane() {
		if (treePane == null) {
			treePane = new JScrollPane();
			treePane.setViewportView(getGroupTree());
		}
		return treePane;
	}

	/**
	 * This method initializes groupTree
	 * 
	 * @return javax.swing.JTree
	 */
	public GridGrouperTree getGroupTree() {
		if (groupTree == null) {
			groupTree = new GridGrouperTree(this);
		}
		return groupTree;
	}

	/**
	 * This method initializes progress
	 * 
	 * @return javax.swing.JProgressBar
	 */
	public GridGrouperProgressBar getProgress() {
		if (progress == null) {
			progress = new GridGrouperProgressBar();
			progress.setForeground(GridGrouperLookAndFeel.getPanelLabelColor());
			progress.setString("");
			progress.setStringPainted(true);
		}
		return progress;
	}



	/**
	 * This method initializes buttonPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 1;
			gridBagConstraints8.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints8.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints8.weightx = 1.0D;
			gridBagConstraints8.gridy = 1;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.weightx = 1.0D;
			gridBagConstraints5.gridy = 1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 1;
			gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints4.weightx = 1.0D;
			gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.gridy = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.weightx = 1.0D;
			gridBagConstraints2.gridy = 0;
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new GridBagLayout());
			buttonPanel.add(getAddGridGrouper(), gridBagConstraints2);
			buttonPanel.add(getRemoveGridGrouper(), gridBagConstraints4);
			buttonPanel.add(getView(), gridBagConstraints5);
			buttonPanel.add(getRefresh(), gridBagConstraints8);
		}
		return buttonPanel;
	}

	/**
	 * This method initializes addGridGrouper	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getAddGridGrouper() {
		if (addGridGrouper == null) {
			addGridGrouper = new JButton();
			addGridGrouper.setText("Add Grid Grouper");
			addGridGrouper.setIcon(GridGrouperLookAndFeel.getGrouperAddIcon22x22());
			addGridGrouper.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MobiusRunnable runner = new MobiusRunnable() {
						public void execute() {
							PortalResourceManager.getInstance().getGridPortal().addGridPortalComponent(new AddGridGrouperWindow(getGroupTree().getRootNode()),400,150);
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
		return addGridGrouper;
	}

	/**
	 * This method initializes removeGridGrouper	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getRemoveGridGrouper() {
		if (removeGridGrouper == null) {
			removeGridGrouper = new JButton();
			removeGridGrouper.setText("Remove Grid Grouper");
			removeGridGrouper.setIcon(GridGrouperLookAndFeel.getGrouperRemoveIcon22x22());
			removeGridGrouper.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MobiusRunnable runner = new MobiusRunnable() {
						public void execute() {
							getGroupTree().getRootNode().removeSelectedGridGrouper();
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
		return removeGridGrouper;
	}

	/**
	 * This method initializes view	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getView() {
		if (view == null) {
			view = new JButton();
			view.setText("View");
			view.setIcon(GridGrouperLookAndFeel.getQueryIcon());
		}
		return view;
	}

	/**
	 * This method initializes refresh	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getRefresh() {
		if (refresh == null) {
			refresh = new JButton();
			refresh.setText("Refresh");
			refresh.setIcon(GridGrouperLookAndFeel.getLoadIcon());
			refresh.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MobiusRunnable runner = new MobiusRunnable() {
						public void execute() {
							GridGrouperBaseTreeNode node = getGroupTree().getCurrentNode();
							if(node!=null){
								node.refresh();
							}else{
								PortalUtils.showErrorMessage("Please select a node to refresh!!!");
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
		return refresh;
	}

}
