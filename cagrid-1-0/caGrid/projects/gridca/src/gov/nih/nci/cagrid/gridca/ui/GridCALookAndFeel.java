package gov.nih.nci.cagrid.gridca.ui;

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
public class GridCALookAndFeel extends LookAndFeel {

	public final static ImageIcon getCertificateIcon() {
		return IconUtils.loadIcon("/contact-new.png");
	}


	public final static ImageIcon getProxyManagerIcon() {
		return IconUtils.loadIcon("/contact-new.png");
	}


	public final static ImageIcon getDefaultIcon() {
		return IconUtils.loadIcon("/go-home.png");
	}
}
