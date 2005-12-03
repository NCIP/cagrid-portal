
package gov.nih.nci.cagrid.gums.ifs.portal;

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
public class IFSLookAndFeel extends PortalLookAndFeel{	 
	 public final static ImageIcon getIFSIcon(){
		 	return new javax.swing.ImageIcon(IFSLookAndFeel.class.getResource("/FlowGraph.gif"));
		 }
	 
	 public final static ImageIcon getProxyIcon(){
		 	return new javax.swing.ImageIcon(IFSLookAndFeel.class.getResource("/Envelope.gif"));
		 }
	 
	 public final static ImageIcon getProxyManagerIcon(){
		 	return new javax.swing.ImageIcon(IFSLookAndFeel.class.getResource("/EnvelopeOpen.gif"));
		 }
	 
	 public final static ImageIcon getAuthenticateIcon(){
		 	return new javax.swing.ImageIcon(GumsLookAndFeel.class.getResource("/Lock.gif"));
		 }
	
}
