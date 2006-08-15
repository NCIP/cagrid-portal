package gov.nih.nci.cagrid.gridgrouper.ui;

import gov.nih.nci.cagrid.common.portal.PortalUtils;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella</A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @version $Id: GridGrouperBaseTreeNode.java,v 1.1 2006/08/04 03:49:26 langella
 *          Exp $
 */
public class ContentManager extends JTabbedPane {

	private static final long serialVersionUID = 1L;

	private JPanel welcomePanel = null;

	private static final String WELCOME = "Grid Grouper";

	private JLabel gridGrouperImage = null;

	private Map stems = new HashMap(); // @jve:decl-index=0:

	private Map groups = new HashMap(); // @jve:decl-index=0:

	/**
	 * This is the default constructor
	 */
	public ContentManager() {
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
		this.addTab(WELCOME, GridGrouperLookAndFeel.getGrouperIcon22x22(),
				getWelcomePanel(), null);
	}

	public void addNode(GridGrouperBaseTreeNode node) {
		if (node instanceof StemTreeNode) {
			this.addStem((StemTreeNode) node);
		} else if (node instanceof GroupTreeNode) {
			this.addGroup((GroupTreeNode) node);
		} else {
			PortalUtils
					.showErrorMessage("Please select a stem or group to view!!!");
		}
	}

	public void removeNode(GridGrouperBaseTreeNode node) throws Exception {
		if (node instanceof StemTreeNode) {
			this.removeStem((StemTreeNode) node);
		} else {
			PortalUtils
					.showErrorMessage("Please select a stem or group to remove!!!");
		}
	}

	public void addStem(StemTreeNode node) {
		String stemId = node.getStem().getUuid();
		this.removeStem(node, true);
		StemBrowser browser = new StemBrowser(node);
		stems.put(stemId, browser);
		this.addTab(node.getStem().getDisplayExtension(), new CombinedIcon(
				new ContentManagerTabCloseIcon(), GridGrouperLookAndFeel
						.getStemIcon()), browser, null);
		this.remove(getWelcomePanel());
		this.setSelectedComponent(browser);

	}

	public void addGroup(GroupTreeNode node) {
		String groupId = node.getGroup().getUuid();
		this.removeGroup(node, true);
		GroupBrowser browser = new GroupBrowser(node);
		groups.put(groupId, browser);
		this.addTab(node.getGroup().getDisplayExtension(), new CombinedIcon(
				new ContentManagerTabCloseIcon(), GridGrouperLookAndFeel
						.getGroupIcon16x16()), browser, null);
		this.remove(getWelcomePanel());
		this.setSelectedComponent(browser);
	}

	public void removeSelectedNode() {
		Component c = this.getSelectedComponent();
		if (c instanceof StemBrowser) {
			StemBrowser sb = (StemBrowser) c;
			removeStem(sb.getStemNode());
		}
		
		if (c instanceof GroupBrowser) {
			GroupBrowser sb = (GroupBrowser) c;
			removeGroup(sb.getGroupNode());
		}
	}

	public void removeGroup(GroupTreeNode node) {
		this.removeGroup(node, false);
	}

	private void removeGroup(GroupTreeNode node, boolean internal) {
		String groupId = node.getGroup().getUuid();
		if (groups.containsKey(groupId)) {
			GroupBrowser sb = (GroupBrowser) groups.remove(groupId);
			this.remove(sb);
		}
		if (!internal) {
			if ((stems.size() == 0) && (groups.size() == 0)) {
				this.addTab(WELCOME, GridGrouperLookAndFeel
						.getGrouperIcon22x22(), getWelcomePanel(), null);
				this.setSelectedComponent(getWelcomePanel());
			}
		}
	}

	public void removeStem(StemTreeNode node) {
		this.removeStem(node, false);
	}

	private void removeStem(StemTreeNode node, boolean internal) {
		String stemId = node.getStem().getUuid();
		if (stems.containsKey(stemId)) {
			StemBrowser sb = (StemBrowser) stems.remove(stemId);
			this.remove(sb);
		}
		if (!internal) {
			if ((stems.size() == 0) && (groups.size() == 0)) {
				this.addTab(WELCOME, GridGrouperLookAndFeel
						.getGrouperIcon22x22(), getWelcomePanel(), null);
				this.setSelectedComponent(getWelcomePanel());
			}
		}
	}

	/**
	 * This method initializes welcomePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getWelcomePanel() {
		if (welcomePanel == null) {
			gridGrouperImage = new JLabel(GridGrouperLookAndFeel
					.getGrouperIconNoBackground());
			welcomePanel = new JPanel();
			welcomePanel.setLayout(new GridBagLayout());
			welcomePanel.add(gridGrouperImage, new GridBagConstraints());
		}
		return welcomePanel;
	}
}
