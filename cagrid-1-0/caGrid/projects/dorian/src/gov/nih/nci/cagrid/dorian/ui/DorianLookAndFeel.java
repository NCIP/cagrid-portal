package gov.nih.nci.cagrid.dorian.ui;

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
public class DorianLookAndFeel extends LookAndFeel {
	public final static ImageIcon getIdpIcon() {
		return IconUtils.loadIcon("/system-users.png");
	}


	public final static ImageIcon getIFSIcon() {
		return IconUtils.loadIcon("/applications-internet.png");
	}
	
	public final static ImageIcon getAdminIcon() {
		return IconUtils.loadIcon("/preferences-desktop-theme.png");
	}


	public final static ImageIcon getTrustedIdPIcon() {
		return IconUtils.loadIcon("/group_link.png");
	}


	public final static ImageIcon getAddTrustedIdPIcon() {
		return IconUtils.loadIcon("/group_add.png");
	}


	public final static ImageIcon getRemoveTrustedIdPIcon() {
		return IconUtils.loadIcon("/group_delete.png");
	}


	public final static ImageIcon getCertificateIcon() {
		return IconUtils.loadIcon("/contact-new.png");
	}


	public final static ImageIcon getCertificateActionIcon() {
		return IconUtils.loadIcon("/contact-new.png");
	}


	public final static ImageIcon getAuthenticateIcon() {
		return IconUtils.loadIcon("/key.png");
	}


	public final static ImageIcon getUsersIcon() {
		return IconUtils.loadIcon("/system-users.png");
	}


	public final static ImageIcon getUserIcon() {
		return IconUtils.loadIcon("/user.png");
	}


	public final static ImageIcon getRemoveUserIcon() {
		return IconUtils.loadIcon("/user_delete.png");
	}


	public final static ImageIcon getUpdateUserIcon() {
		return IconUtils.loadIcon("/user_edit.png");
	}


	public final static ImageIcon getUserBrowse() {
		return IconUtils.loadIcon("/user_edit.png");
	}


	public final static ImageIcon getApplicationIcon() {
		return IconUtils.loadIcon("/edit-paste.png");
	}


	public final static ImageIcon getAttributesIcon() {
		return IconUtils.loadIcon("/edit-paste.png");
	}

}
