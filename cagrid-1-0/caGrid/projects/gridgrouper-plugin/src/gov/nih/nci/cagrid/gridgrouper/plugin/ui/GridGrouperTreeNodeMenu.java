package gov.nih.nci.cagrid.gridgrouper.plugin.ui;

import gov.nih.nci.cagrid.gridgrouper.ui.GridGrouperLookAndFeel;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

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
public abstract class GridGrouperTreeNodeMenu extends JPopupMenu {
	private GridGrouperExpressionBuilder editor;

	private GridGrouperTree tree;
	
	

	private JMenuItem refresh = null;

	public GridGrouperTreeNodeMenu(GridGrouperExpressionBuilder browser,
			GridGrouperTree tree) {
		super("");
		this.editor = browser;
		this.tree = tree;
		initialize();
		
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		
        this.add(getRefresh());
			
	}

	public GridGrouperExpressionBuilder getEditor() {
		return editor;
	}

	public GridGrouperTree getGridGrouperTree() {
		return tree;
	}

	/**
	 * This method initializes refresh	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getRefresh() {
		if (refresh == null) {
			refresh = new JMenuItem();
			refresh.setText("Refresh");
			refresh.setIcon(GridGrouperLookAndFeel.getLoadIcon());
			refresh.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MobiusRunnable runner = new MobiusRunnable() {
						public void execute() {
							getGridGrouperTree().getCurrentNode().refresh();
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
