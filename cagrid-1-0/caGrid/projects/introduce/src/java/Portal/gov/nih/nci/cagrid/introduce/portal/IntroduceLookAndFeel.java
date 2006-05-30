package gov.nih.nci.cagrid.introduce.portal;

import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;

import javax.swing.Icon;
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

	public final static ImageIcon getResyncIcon() {
		return new javax.swing.ImageIcon(IntroduceLookAndFeel.class
				.getResource("/view-refresh.png"));
	}

	public final static ImageIcon getServiceIcon() {
		return new javax.swing.ImageIcon(IntroduceLookAndFeel.class
				.getResource("/cog.png"));
	}

	public final static ImageIcon getModifyServiceIcon() {
		return new javax.swing.ImageIcon(IntroduceLookAndFeel.class
				.getResource("/cog_edit.png"));
	}

	public final static ImageIcon getCreateServiceIcon() {
		return new javax.swing.ImageIcon(IntroduceLookAndFeel.class
				.getResource("/cog_add.png"));
	}

	public final static ImageIcon getResourcePropertiesIcon() {
		return new javax.swing.ImageIcon(IntroduceLookAndFeel.class
				.getResource("/package_green.png"));
	}

	public final static ImageIcon getResourcePropertyIcon() {
		return new javax.swing.ImageIcon(IntroduceLookAndFeel.class
				.getResource("/tag.png"));
	}

	public final static ImageIcon getMethodIcon() {
		return new javax.swing.ImageIcon(IntroduceLookAndFeel.class
				.getResource("/page.png"));
	}

	public final static ImageIcon getMethodsIcon() {
		return new javax.swing.ImageIcon(IntroduceLookAndFeel.class
				.getResource("/page_gear.png"));
	}

	public final static ImageIcon getAddMethodIcon() {
		return new javax.swing.ImageIcon(IntroduceLookAndFeel.class
				.getResource("/page_add.png"));
	}

	public final static ImageIcon getRemoveMethodIcon() {
		return new javax.swing.ImageIcon(IntroduceLookAndFeel.class
				.getResource("/page_delete.png"));
	}

	public final static ImageIcon getCredentialsIcon() {
		return new javax.swing.ImageIcon(IntroduceLookAndFeel.class
				.getResource("/BCard.gif"));
	}

	public final static ImageIcon getModifyIcon() {
		return new javax.swing.ImageIcon(IntroduceLookAndFeel.class
				.getResource("/modify.png"));
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
				.getResource("/cog_go.png"));
	}

	public final static ImageIcon getNamespaceIcon() {
		return new javax.swing.ImageIcon(IntroduceLookAndFeel.class
				.getResource("/chart_organisation.png"));
	}

	public final static ImageIcon getSchemaTypeIcon() {
		return new javax.swing.ImageIcon(IntroduceLookAndFeel.class
				.getResource("/tag.png"));
	}

	public final static ImageIcon getCADSRIcon() {
		return new javax.swing.ImageIcon(IntroduceLookAndFeel.class
				.getResource("/caDSR_logo.gif"));
	}

	public static final Icon getDoneIcon() {
		return new javax.swing.ImageIcon(IntroduceLookAndFeel.class
				.getResource("/media-playback-start.png"));
	}

	public static final Icon getDiscoveryToolsIcon() {
		return new javax.swing.ImageIcon(IntroduceLookAndFeel.class
				.getResource("/system-search.png"));
	}

	public static final Icon getLoadCredentialsIcon() {
		return new javax.swing.ImageIcon(IntroduceLookAndFeel.class
				.getResource("/contact-new.png"));
	}

	public static final Icon getBrowseIcon() {
		return new javax.swing.ImageIcon(IntroduceLookAndFeel.class
				.getResource("/folder-open.png"));
	}

	public static final Icon getUpIcon() {
		return new javax.swing.ImageIcon(IntroduceLookAndFeel.class
				.getResource("/go-up.png"));
	}

	public static final Icon getDownIcon() {
		return new javax.swing.ImageIcon(IntroduceLookAndFeel.class
				.getResource("/go-down.png"));
	}

	public static final Icon getHelpIcon() {
		return new javax.swing.ImageIcon(IntroduceLookAndFeel.class
				.getResource("/help-browser.png"));
	}

}
