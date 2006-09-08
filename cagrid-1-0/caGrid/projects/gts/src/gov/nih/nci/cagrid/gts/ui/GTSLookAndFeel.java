package gov.nih.nci.cagrid.gts.ui;

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

	public final static ImageIcon getTrustLevelIcon() {
		return new javax.swing.ImageIcon(GTSLookAndFeel.class
				.getResource("/trust_level.png"));
	}

	public final static ImageIcon getAddTrustLevelIcon() {
		return new javax.swing.ImageIcon(GTSLookAndFeel.class
				.getResource("/add_trust_level.png"));
	}

	public final static ImageIcon getRemoveTrustLevelIcon() {
		return new javax.swing.ImageIcon(GTSLookAndFeel.class
				.getResource("/remove_trust_level.png"));
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

	public final static ImageIcon getCertificateIcon() {
		return new javax.swing.ImageIcon(GTSLookAndFeel.class
				.getResource("/contact-new.png"));
	}

	public final static ImageIcon getCRLIcon() {
		return new javax.swing.ImageIcon(GTSLookAndFeel.class
				.getResource("/contact-delete.png"));
	}

	public final static ImageIcon getPermissionIcon() {
		return new javax.swing.ImageIcon(GTSLookAndFeel.class
				.getResource("/shield.png"));
	}

	public final static ImageIcon getAddPermissionIcon() {
		return new javax.swing.ImageIcon(GTSLookAndFeel.class
				.getResource("/shield_add.png"));
	}

	public final static ImageIcon getRevokePermissionIcon() {
		return new javax.swing.ImageIcon(GTSLookAndFeel.class
				.getResource("/shield_delete.png"));
	}

	public final static ImageIcon getViewPermissionIcon() {
		return new javax.swing.ImageIcon(GTSLookAndFeel.class
				.getResource("/shield_go.png"));
	}

	public final static ImageIcon getAuthorityIcon() {
		return new javax.swing.ImageIcon(GTSLookAndFeel.class
				.getResource("/plugin.png"));
	}

	public final static ImageIcon getAuthorityEditIcon() {
		return new javax.swing.ImageIcon(GTSLookAndFeel.class
				.getResource("/plugin_edit.png"));
	}

	public final static ImageIcon getAuthorityAddIcon() {
		return new javax.swing.ImageIcon(GTSLookAndFeel.class
				.getResource("/plugin_add.png"));
	}

	public final static ImageIcon getAuthorityDeleteIcon() {
		return new javax.swing.ImageIcon(GTSLookAndFeel.class
				.getResource("/plugin_delete.png"));
	}

	public final static ImageIcon getAuthorityUpdateIcon() {
		return new javax.swing.ImageIcon(GTSLookAndFeel.class
				.getResource("/plugin_go.png"));
	}

	public final static ImageIcon getIncreasePriorityIcon() {
		return new javax.swing.ImageIcon(GTSLookAndFeel.class
				.getResource("/go-up.png"));
	}

	public final static ImageIcon getDecresePriorityIcon() {
		return new javax.swing.ImageIcon(GTSLookAndFeel.class
				.getResource("/go-down.png"));
	}

}
