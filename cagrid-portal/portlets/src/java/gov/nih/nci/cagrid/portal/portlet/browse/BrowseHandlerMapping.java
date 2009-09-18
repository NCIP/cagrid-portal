/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.browse;

import javax.portlet.PortletRequest;

import org.springframework.web.portlet.handler.PortletModeParameterHandlerMapping;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 *
 */
public class BrowseHandlerMapping extends PortletModeParameterHandlerMapping {
	
	protected Object getHandlerInternal(PortletRequest request) throws Exception {
		Object lookupKey = getLookupKey(request);
		logger.debug("### Lookup Key: " + lookupKey);
		return super.getHandlerInternal(request);
	}

}
