package gov.nih.nci.cagrid.gridgrouper.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.projectmobius.portal.GridPortalComponent;

public class GroupManagementBrowser extends GridPortalComponent {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

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
		this.setSize(300, 200);
		this.setContentPane(getJContentPane());
		this.setTitle("Group Management Browser");
		this.setFrameIcon(GridGrouperLookAndFeel.getGroupIcon());
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
		}
		return jContentPane;
	}

}
