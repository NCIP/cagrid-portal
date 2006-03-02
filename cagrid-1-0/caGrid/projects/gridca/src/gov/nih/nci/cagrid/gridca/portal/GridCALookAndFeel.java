package gov.nih.nci.cagrid.gridca.portal;

import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;

import javax.swing.ImageIcon;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class GridCALookAndFeel extends PortalLookAndFeel {


	public final static ImageIcon getCertificateIcon() {
		return new javax.swing.ImageIcon(GridCALookAndFeel.class
				.getResource("/BCard.gif"));
	}
	
	public final static ImageIcon getProxyManagerIcon() {
		return new javax.swing.ImageIcon( GridCALookAndFeel.class
				.getResource("/EditBCard.gif"));
	}
	
	public final static ImageIcon getDeleteProxyIcon() {
		return new javax.swing.ImageIcon( GridCALookAndFeel.class
				.getResource("/DeleteBCard.gif"));
	}
	
	public final static ImageIcon getGreenFlagIcon() {
		return new javax.swing.ImageIcon(GridCALookAndFeel.class
				.getResource("/GreenFlag.gif"));
	}
}
