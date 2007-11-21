/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet;

import javax.portlet.PortletRequest;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public interface InterPortletMessageManager {

	boolean isTrue(PortletRequest request, String preferenceName);

	void send(PortletRequest request, String outputQueueName, Object value);

	Object receive(PortletRequest request, String inputQueueName);

}
