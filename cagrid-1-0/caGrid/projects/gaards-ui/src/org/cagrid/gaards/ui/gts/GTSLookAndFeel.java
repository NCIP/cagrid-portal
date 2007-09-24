package org.cagrid.gaards.ui.gts;

import javax.swing.ImageIcon;

import org.cagrid.grape.LookAndFeel;
import org.cagrid.grape.utils.IconUtils;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class GTSLookAndFeel extends LookAndFeel {
	public final static ImageIcon getGTSIcon() {
		return IconUtils.loadIcon("/system-software-update.png");
	}


	public final static ImageIcon getTrustLevelIcon() {
		return IconUtils.loadIcon("/trust_level.png");
	}


	public final static ImageIcon getAddTrustLevelIcon() {
		return IconUtils.loadIcon("/add_trust_level.png");
	}


	public final static ImageIcon getRemoveTrustLevelIcon() {
		return IconUtils.loadIcon("/remove_trust_level.png");
	}


	public final static ImageIcon getTrustedAuthorityIcon() {
		return IconUtils.loadIcon("/key.png");
	}


	public final static ImageIcon getAddTrustedAuthorityIcon() {
		return IconUtils.loadIcon("/key_add.png");
	}


	public final static ImageIcon getRemoveTrustedAuthorityIcon() {
		return IconUtils.loadIcon("/key_delete.png");
	}


	public final static ImageIcon getModifyTrustedAuthorityIcon() {
		return IconUtils.loadIcon("/key_go.png");
	}


	public final static ImageIcon getCertificateIcon() {
		return IconUtils.loadIcon("/contact-new.png");
	}


	public final static ImageIcon getCRLIcon() {
		return IconUtils.loadIcon("/contact-delete.png");
	}


	public final static ImageIcon getPermissionIcon() {
		return IconUtils.loadIcon("/shield.png");
	}


	public final static ImageIcon getAddPermissionIcon() {
		return IconUtils.loadIcon("/shield_add.png");
	}


	public final static ImageIcon getRevokePermissionIcon() {
		return IconUtils.loadIcon("/shield_delete.png");
	}


	public final static ImageIcon getViewPermissionIcon() {
		return IconUtils.loadIcon("/shield_go.png");
	}


	public final static ImageIcon getAuthorityIcon() {
		return IconUtils.loadIcon("/plugin.png");
	}


	public final static ImageIcon getAuthorityEditIcon() {
		return IconUtils.loadIcon("/plugin_edit.png");
	}


	public final static ImageIcon getAuthorityAddIcon() {
		return IconUtils.loadIcon("/plugin_add.png");
	}


	public final static ImageIcon getAuthorityDeleteIcon() {
		return IconUtils.loadIcon("/plugin_delete.png");
	}


	public final static ImageIcon getAuthorityUpdateIcon() {
		return IconUtils.loadIcon("/plugin_go.png");
	}


	public final static ImageIcon getIncreasePriorityIcon() {
		return IconUtils.loadIcon("/go-up.png");
	}


	public final static ImageIcon getDecresePriorityIcon() {
		return IconUtils.loadIcon("/go-down.png");
	}

}
