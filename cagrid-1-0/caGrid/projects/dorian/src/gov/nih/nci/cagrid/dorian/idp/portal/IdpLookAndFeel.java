
package gov.nih.nci.cagrid.gums.idp.portal;

import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;

import javax.swing.ImageIcon;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class IdpLookAndFeel extends PortalLookAndFeel{	 
	 public final static ImageIcon getIdpIcon(){
	 	return new javax.swing.ImageIcon(IdpLookAndFeel.class.getResource("/LuggageCounter.gif"));
	 }
}
