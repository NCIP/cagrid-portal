package gov.nih.nci.cagrid.introduce.portal;


import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;

import javax.swing.ImageIcon;

/**
 * *
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @version $Id: AnalyticalLookAndFeel.java,v 1.1 2005/11/09 19:30:23 hastings
 *          Exp $
 */
public class IntroduceLookAndFeel extends PortalLookAndFeel {

	public final static ImageIcon getCreateIcon() {
		return new javax.swing.ImageIcon(IntroduceLookAndFeel.class
				.getResource("/Plus20X20.gif"));
	}

	public final static ImageIcon getModifyIcon() {
		return new javax.swing.ImageIcon(IntroduceLookAndFeel.class
				.getResource("/Hammer.gif"));
	}

	public final static ImageIcon getMobiusIcon() {
		return new javax.swing.ImageIcon(IntroduceLookAndFeel.class
				.getResource("/mobiusIcon.gif"));
	}

	public final static ImageIcon getUndoIcon() {
		return new javax.swing.ImageIcon(IntroduceLookAndFeel.class
				.getResource("/edit-undos.png"));
	}
	
	public final static ImageIcon getRedoIcon() {
		return new javax.swing.ImageIcon(IntroduceLookAndFeel.class
				.getResource("/edit-redos.png"));
	}
	
	public final static ImageIcon getDeployIcon() {
		return new javax.swing.ImageIcon(IntroduceLookAndFeel.class
				.getResource("/World20X20.gif"));
	}
	
	public final static ImageIcon getCADSRIcon() {
		return new javax.swing.ImageIcon(IntroduceLookAndFeel.class
				.getResource("/caDSR_logo.gif"));
	}
}
