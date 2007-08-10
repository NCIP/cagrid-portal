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
		return IconUtils.loadIcon("/users.png");
	}

	
	public final static ImageIcon getAdminIcon() {
		return IconUtils.loadIcon("/preferences-desktop-theme.png");
	}

	public final static ImageIcon getRenewIcon() {
		return IconUtils.loadIcon("/view-refresh.png");
	}

	public final static ImageIcon getTrustedIdPIcon() {
		return IconUtils.loadIcon("/trusted-idp.png");
	}
	
	public final static ImageIcon getPasswordIcon() {
		return IconUtils.loadIcon("/password.png");
	}


	
	public final static ImageIcon getHostIcon() {
		return IconUtils.loadIcon("/computer.png");
	}


	public final static ImageIcon getCertificateIcon() {
		return IconUtils.loadIcon("/certificate.png");
	}


	public final static ImageIcon getCertificateActionIcon() {
		return IconUtils.loadIcon("/contact-new.png");
	}


	public final static ImageIcon getAuthenticateIcon() {
		return IconUtils.loadIcon("/login.png");
	}


	public final static ImageIcon getUsersIcon() {
		return IconUtils.loadIcon("/users.png");
	}


	public final static ImageIcon getUserIcon() {
		return IconUtils.loadIcon("/user.png");
	}




	public final static ImageIcon getApplicationIcon() {
		return IconUtils.loadIcon("/application.png");
	}


	public final static ImageIcon getAttributesIcon() {
		return IconUtils.loadIcon("/application.png");
	}
	
	public final static ImageIcon getHostsIcon() {
		return IconUtils.loadIcon("/computers.png");
	}

}
