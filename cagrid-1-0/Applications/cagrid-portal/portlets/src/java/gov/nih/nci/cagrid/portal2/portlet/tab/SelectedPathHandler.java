/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.tab;

import javax.portlet.PortletRequest;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public interface SelectedPathHandler {
	
	String getSelectedPath(PortletRequest request);

}