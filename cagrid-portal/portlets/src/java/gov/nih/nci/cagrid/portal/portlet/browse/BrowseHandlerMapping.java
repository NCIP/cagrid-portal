/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
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
