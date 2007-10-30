/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public interface ActionResponseHandler {
	void handle(ActionRequest request, ActionResponse response);
}
