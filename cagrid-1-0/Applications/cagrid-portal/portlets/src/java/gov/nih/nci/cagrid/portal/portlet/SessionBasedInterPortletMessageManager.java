/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SessionBasedInterPortletMessageManager implements
		InterPortletMessageManager {
	
	private static final Log logger = LogFactory.getLog(SessionBasedInterPortletMessageManager.class);
	
	private Map<String,Object> queues = new HashMap<String,Object>();

	/**
	 * 
	 */
	public SessionBasedInterPortletMessageManager() {

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.portlet.InterPortletMessageManager#receive(javax.portlet.PortletRequest, java.lang.String)
	 */
	public Object receive(PortletRequest request, String inputQueueName) {
//		logger.debug("Receiving from " + inputQueueName);
		return queues.remove(inputQueueName);
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.portlet.InterPortletMessageManager#send(javax.portlet.ActionRequest, java.lang.String, java.lang.Object)
	 */
	public void send(PortletRequest request, String outputQueueName, Object value) {
//		logger.debug("Sending to " + outputQueueName + ": " + value);
		String[] inputQueueNames = request.getPreferences().getValues(outputQueueName, new String[0]);
		for(String inputQueueName : inputQueueNames){
//			logger.debug("sending to " + inputQueueName);
			queues.put(inputQueueName, value);
		}
	}

}
