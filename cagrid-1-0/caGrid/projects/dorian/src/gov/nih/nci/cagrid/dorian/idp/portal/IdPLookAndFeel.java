
package gov.nih.nci.cagrid.gums.idp.portal;

import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.gums.portal.GumsLookAndFeel;

import javax.swing.ImageIcon;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class IdPLookAndFeel extends PortalLookAndFeel{	 
	 public final static ImageIcon getIdpIcon(){
	 	return new javax.swing.ImageIcon(IdPLookAndFeel.class.getResource("/LuggageCounter.gif"));
	 }
	 
	 public final static ImageIcon getUsersIcon(){
		 	return new javax.swing.ImageIcon(IdPLookAndFeel.class.getResource("/Users.gif"));
		 }
	 
	 public final static ImageIcon getUpdateUserIcon(){
		 	return new javax.swing.ImageIcon(IdPLookAndFeel.class.getResource("/Reply.gif"));
		 }
	 
	 public final static ImageIcon getUserMagnifyIcon(){
		 	return new javax.swing.ImageIcon(IdPLookAndFeel.class.getResource("/UserMagnify.gif"));
		 }
	 
	 public final static ImageIcon getApplicationIcon(){
		 	return new javax.swing.ImageIcon(IdPLookAndFeel.class.getResource("/Paste.gif"));
		 }
	 
	 public final static ImageIcon getLoginIcon(){
		 	return new javax.swing.ImageIcon(GumsLookAndFeel.class.getResource("/Lock.gif"));
		 }
}
