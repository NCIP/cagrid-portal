/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.validation.Errors;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public interface CommandActionResponseHandler {
	void handle(ActionRequest request, ActionResponse response, Object command, Errors errors);
}
