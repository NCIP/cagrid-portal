
package gov.nih.nci.cagrid.gums.portal;

import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;

import javax.swing.ImageIcon;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class GumsLookAndFeel extends PortalLookAndFeel{

	 
	 public final static ImageIcon getCredentialManagementIcon(){
	 	return new javax.swing.ImageIcon(GumsLookAndFeel.class.getResource("/Key.gif"));
	 }
	 
	 public final static ImageIcon getChangePasswordIcon(){
	 	return new javax.swing.ImageIcon(GumsLookAndFeel.class.getResource("/Key.gif"));
	 }
	 
	 public final static ImageIcon getNoteIcon(){
	 	return new javax.swing.ImageIcon(GumsLookAndFeel.class.getResource("/Document.gif"));
	 }
	 
	 public final static ImageIcon getProxyInformationIcon(){
	 	return new javax.swing.ImageIcon(GumsLookAndFeel.class.getResource("/EnvelopeOpen.gif"));
	 }
	 
	 public final static ImageIcon getRegistrationIcon(){
	 	return new javax.swing.ImageIcon(GumsLookAndFeel.class.getResource("/DocumentDraw.gif"));
	 }
	 
	 public final static ImageIcon getApplyIcon(){
	 	return new javax.swing.ImageIcon(GumsLookAndFeel.class.getResource("/DocumentIn.gif"));
	 }
	 

	 public final static ImageIcon getHomeIcon(){
	 	return new javax.swing.ImageIcon(GumsLookAndFeel.class.getResource("/Home.gif"));
	 }
	 
	 public final static ImageIcon getAdministrationIcon(){
	 	return new javax.swing.ImageIcon(GumsLookAndFeel.class.getResource("/timeseriesMagnify.gif"));
	 }
	 
	 public final static ImageIcon getUsersIcon(){
	 	return new javax.swing.ImageIcon(GumsLookAndFeel.class.getResource("/Users.gif"));
	 }
	 
	 public final static ImageIcon getUserIcon(){
	 	return new javax.swing.ImageIcon(GumsLookAndFeel.class.getResource("/User.gif"));
	 }
	 public final static ImageIcon getManageUserIcon(){
		 	return new javax.swing.ImageIcon(GumsLookAndFeel.class.getResource("/UserMagnify.gif"));
		 }
	 
	 public final static ImageIcon getUpdateUserIcon(){
	 	return new javax.swing.ImageIcon(GumsLookAndFeel.class.getResource("/Forward.gif"));
	 }
	 
	 
	 public final static ImageIcon getLoginIcon(){
	 	return new javax.swing.ImageIcon(GumsLookAndFeel.class.getResource("/Lock.gif"));
	 }
	 
	 public final static ImageIcon getProcessApplicationIcon(){
	 	return new javax.swing.ImageIcon(GumsLookAndFeel.class.getResource("/DocumentDraw.gif"));
	 }
	 
	 public final static ImageIcon getReviewDocumentIcon(){
		 	return new javax.swing.ImageIcon(GumsLookAndFeel.class.getResource("/DocumentMag.gif"));
		 }

}
