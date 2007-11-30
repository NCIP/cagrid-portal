package org.cagrid.gaards.ui.common;

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
public class GAARDSLookAndFeel extends LookAndFeel {

	public final static ImageIcon getCertificateIcon() {
		return IconUtils.loadIcon("/certificate.png");
	}

	public final static ImageIcon getCertificateActionIcon() {
		return IconUtils.loadIcon("/contact-new.png");
	}
	
	public final static ImageIcon getRefreshIcon() {
		return IconUtils.loadIcon("/view-refresh.png");
	}

}
