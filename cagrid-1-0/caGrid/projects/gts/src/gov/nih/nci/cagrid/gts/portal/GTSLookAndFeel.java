package gov.nih.nci.cagrid.gts.portal;

import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;

import javax.swing.ImageIcon;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class GTSLookAndFeel extends PortalLookAndFeel {
	public final static ImageIcon getGTSIcon() {
		return new javax.swing.ImageIcon(GTSLookAndFeel.class
				.getResource("/system-software-update.png"));
	}
	
	public final static ImageIcon getTrustedAuthorityIcon() {
		return new javax.swing.ImageIcon(GTSLookAndFeel.class
				.getResource("/key.png"));
	}
	
	public final static ImageIcon getAddTrustedAuthorityIcon() {
		return new javax.swing.ImageIcon(GTSLookAndFeel.class
				.getResource("/key_add.png"));
	}
	
	public final static ImageIcon getRemoveTrustedAuthorityIcon() {
		return new javax.swing.ImageIcon(GTSLookAndFeel.class
				.getResource("/key_delete.png"));
	}
	
	public final static ImageIcon getModifyTrustedAuthorityIcon() {
		return new javax.swing.ImageIcon(GTSLookAndFeel.class
				.getResource("/key_go.png"));
	}
	
}
