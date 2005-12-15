package gov.nih.nci.cagrid.dorian.portal;

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
				.getResource("/LuggageCounter.gif"));
	}

	public final static ImageIcon getIFSIcon() {
		return new javax.swing.ImageIcon(DorianLookAndFeel.class
				.getResource("/FlowGraph.gif"));
	}
	
	public final static ImageIcon getTrustedIdPIcon() {
		return new javax.swing.ImageIcon(DorianLookAndFeel.class
				.getResource("/Chain.gif"));
	}
	
	public final static ImageIcon getAddTrustedIdPIcon() {
		return new javax.swing.ImageIcon(DorianLookAndFeel.class
				.getResource("/NewPlug.gif"));
	}

	public final static ImageIcon getProxyIcon() {
		return new javax.swing.ImageIcon(DorianLookAndFeel.class
				.getResource("/BCard.gif"));
	}
	
	public final static ImageIcon getRenewCredentialsIcon() {
		return new javax.swing.ImageIcon(DorianLookAndFeel.class
				.getResource("/EditBCard.gif"));
	}

	public final static ImageIcon getProxyManagerIcon() {
		return new javax.swing.ImageIcon(DorianLookAndFeel.class
				.getResource("/EditBCard.gif"));
	}
	
	public final static ImageIcon getDeleteProxyIcon() {
		return new javax.swing.ImageIcon(DorianLookAndFeel.class
				.getResource("/DeleteBCard.gif"));
	}


	public final static ImageIcon getAuthenticateIcon() {
		return new javax.swing.ImageIcon(DorianLookAndFeel.class
				.getResource("/Lock.gif"));
	}

	public final static ImageIcon getSaveIcon() {
		return new javax.swing.ImageIcon(DorianLookAndFeel.class
				.getResource("/Save.gif"));
	}

	public final static ImageIcon getGreenFlagIcon() {
		return new javax.swing.ImageIcon(DorianLookAndFeel.class
				.getResource("/GreenFlag.gif"));
	}

	public final static ImageIcon getUsersIcon() {
		return new javax.swing.ImageIcon(DorianLookAndFeel.class
				.getResource("/Users.gif"));
	}
	
	public final static ImageIcon getUserIcon() {
		return new javax.swing.ImageIcon(DorianLookAndFeel.class
				.getResource("/User.gif"));
	}
	
	public final static ImageIcon getRemoveUserIcon() {
		return new javax.swing.ImageIcon(DorianLookAndFeel.class
				.getResource("/DeleteUser.gif"));
	}

	public final static ImageIcon getUpdateUserIcon() {
		return new javax.swing.ImageIcon(DorianLookAndFeel.class
				.getResource("/Reply.gif"));
	}

	public final static ImageIcon getUserMagnifyIcon() {
		return new javax.swing.ImageIcon(DorianLookAndFeel.class
				.getResource("/UserMagnify.gif"));
	}

	public final static ImageIcon getApplicationIcon() {
		return new javax.swing.ImageIcon(DorianLookAndFeel.class
				.getResource("/Paste.gif"));
	}

	public final static ImageIcon getLoginIcon() {
		return new javax.swing.ImageIcon(DorianLookAndFeel.class
				.getResource("/Lock.gif"));
	}
}
