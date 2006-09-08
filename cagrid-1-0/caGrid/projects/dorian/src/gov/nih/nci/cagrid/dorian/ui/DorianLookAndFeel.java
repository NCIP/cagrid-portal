package gov.nih.nci.cagrid.dorian.ui;

import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;

import javax.swing.ImageIcon;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class DorianLookAndFeel extends PortalLookAndFeel {
	public final static ImageIcon getIdpIcon() {
		return new javax.swing.ImageIcon(DorianLookAndFeel.class
				.getResource("/system-users.png"));
	}

	public final static ImageIcon getIFSIcon() {
		return new javax.swing.ImageIcon(DorianLookAndFeel.class
				.getResource("/applications-internet.png"));
	}
	
	public final static ImageIcon getTrustedIdPIcon() {
		return new javax.swing.ImageIcon(DorianLookAndFeel.class
				.getResource("/group_link.png"));
	}
	
	public final static ImageIcon getAddTrustedIdPIcon() {
		return new javax.swing.ImageIcon(DorianLookAndFeel.class
				.getResource("/group_add.png"));
	}
	
	public final static ImageIcon getRemoveTrustedIdPIcon() {
		return new javax.swing.ImageIcon(DorianLookAndFeel.class
				.getResource("/group_delete.png"));
	}

	public final static ImageIcon getCertificateIcon() {
		return new javax.swing.ImageIcon(DorianLookAndFeel.class
				.getResource("/contact-new.png"));
	}
	

	
	public final static ImageIcon getCertificateActionIcon() {
		return new javax.swing.ImageIcon(DorianLookAndFeel.class
				.getResource("/contact-new.png"));
	}

	



	public final static ImageIcon getAuthenticateIcon() {
		return new javax.swing.ImageIcon(DorianLookAndFeel.class
				.getResource("/key.png"));
	}



	public final static ImageIcon getUsersIcon() {
		return new javax.swing.ImageIcon(DorianLookAndFeel.class
				.getResource("/system-users.png"));
	}
	
	public final static ImageIcon getUserIcon() {
		return new javax.swing.ImageIcon(DorianLookAndFeel.class
				.getResource("/user.png"));
	}
	
	public final static ImageIcon getRemoveUserIcon() {
		return new javax.swing.ImageIcon(DorianLookAndFeel.class
				.getResource("/user_delete.png"));
	}

	public final static ImageIcon getUpdateUserIcon() {
		return new javax.swing.ImageIcon(DorianLookAndFeel.class
				.getResource("/user_edit.png"));
	}

	public final static ImageIcon getUserBrowse() {
		return new javax.swing.ImageIcon(DorianLookAndFeel.class
				.getResource("/user_edit.png"));
	}

	public final static ImageIcon getApplicationIcon() {
		return new javax.swing.ImageIcon(DorianLookAndFeel.class
				.getResource("/edit-paste.png"));
	}
	
	public final static ImageIcon getAttributesIcon() {
		return new javax.swing.ImageIcon(DorianLookAndFeel.class
				.getResource("/edit-paste.png"));
	}

}
