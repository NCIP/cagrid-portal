/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.validation.BindException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public interface CommandActionResponseHandler {
	void handle(ActionRequest request, ActionResponse response, Object command, BindException errors);
}
