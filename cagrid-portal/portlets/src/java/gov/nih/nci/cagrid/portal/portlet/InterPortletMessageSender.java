/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet;

import javax.portlet.PortletRequest;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public interface InterPortletMessageSender {
	
	boolean handles(PortletRequest request);
	
	void send(PortletRequest request, Object object);

}
