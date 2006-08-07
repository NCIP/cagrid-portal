package gov.nih.nci.cagrid.gridgrouper.ui;

import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella</A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Jul 12, 2004
 * @version $Id: GridGrouperBaseTreeNode.java,v 1.1 2006/08/04 03:49:26 langella
 *          Exp $
 */
public class ContentManager extends JTabbedPane {

	private static final long serialVersionUID = 1L;
	private JPanel welcomePanel = null;
	private static final String WELCOME = "Grid Grouper";
	private JLabel gridGrouperImage = null;

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

		this.addTab(WELCOME, GridGrouperLookAndFeel.getGrouperIcon22x22(), getWelcomePanel(), null);
	}

	/**
	 * This method initializes welcomePanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getWelcomePanel() {
		if (welcomePanel == null) {
			gridGrouperImage = new JLabel(GridGrouperLookAndFeel.getGrouperIconNoBackground());
			welcomePanel = new JPanel();
			welcomePanel.setLayout(new GridBagLayout());
			welcomePanel.add(gridGrouperImage, new GridBagConstraints());
		}
		return welcomePanel;
	}

}
