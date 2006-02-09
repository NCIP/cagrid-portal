package gov.nih.nci.cagrid.introduce.portal.security;

import gov.nih.nci.cagrid.introduce.portal.IntroduceLookAndFeel;

import javax.swing.JPanel;

import org.projectmobius.portal.GridPortalComponent;


/**
 * SecurityTestViewer
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created Jun 22, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class SecurityTestViewer extends GridPortalComponent {

	private JPanel jPanel = null;


	/**
	 * This method initializes
	 */
	public SecurityTestViewer() {
		super();
		initialize();
	}


	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(469, 446);
		this.setContentPane(getJPanel());
		this.setFrameIcon(IntroduceLookAndFeel.getCreateIcon());
		this.setTitle("Test Security");

	}


	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */    
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new SecurityMethodPanel();
		}
		return jPanel;
	}

} // @jve:decl-index=0:visual-constraint="10,4"
