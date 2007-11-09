/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet;

import javax.portlet.PortletRequest;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public interface InterPortletMessageReceiver {

	boolean handles(PortletRequest request);
	
	Object receive(PortletRequest request);
	
}
