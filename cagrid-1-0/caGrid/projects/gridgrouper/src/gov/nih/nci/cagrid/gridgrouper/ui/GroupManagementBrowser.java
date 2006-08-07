package gov.nih.nci.cagrid.gridgrouper.ui;

import gov.nih.nci.cagrid.common.portal.PortalUtils;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

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
public class GroupManagementBrowser extends GridPortalComponent {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

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

	private JSplitPane jSplitPane = null;

	private ContentManager tabbedContent = null;

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
			jContentPane.add(getJSplitPane(), BorderLayout.CENTER);
		}
		return jContentPane;
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
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridx = 0;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			contentPanel = new JPanel();
			contentPanel.setLayout(new GridBagLayout());
			contentPanel.add(getTabbedContent(), gridBagConstraints);
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
			view.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MobiusRunnable runner = new MobiusRunnable() {
						public void execute() {
							getTabbedContent().addNode(getGroupTree().getCurrentNode());
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

	/**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
	private JSplitPane getJSplitPane() {
		if (jSplitPane == null) {
			jSplitPane = new JSplitPane();
			jSplitPane.setLeftComponent(getTreePanel());
			jSplitPane.setRightComponent(getContentPanel());
		}
		return jSplitPane;
	}

	/**
	 * This method initializes tabbedContent	
	 * 	
	 * @return javax.swing.JTabbedPane	
	 */
	private ContentManager getTabbedContent() {
		if (tabbedContent == null) {
			tabbedContent = new ContentManager();
		}
		return tabbedContent;
	}

}
