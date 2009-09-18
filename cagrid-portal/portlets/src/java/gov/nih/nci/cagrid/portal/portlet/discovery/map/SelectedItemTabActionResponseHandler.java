/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.discovery.map;

import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryType;
import gov.nih.nci.cagrid.portal.portlet.map.SelectItemCommand;
import gov.nih.nci.cagrid.portal.portlet.tab.TabActionResponseHandler;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.validation.BindException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class SelectedItemTabActionResponseHandler extends
		TabActionResponseHandler {

	private String serviceSelectedTabPath;
	private String participantSelectedTabPath;
	private String pocSelectedTabPath;

	/**
	 * 
	 */
	public SelectedItemTabActionResponseHandler() {

	}

	public void handle(ActionRequest request, ActionResponse response,
			Object command, BindException errors) {
		SelectItemCommand item = (SelectItemCommand) command;
		if (errors != null && errors.hasErrors()) {
			if (getErrorsAttributeName() != null) {
				request.setAttribute(getErrorsAttributeName(), errors);
			}
			handle(request, response, getErrorTabPath());
		} else {
			String p = null;
			if (DiscoveryType.SERVICE.equals(item.getType())) {
				p = getServiceSelectedTabPath();
			} else if (DiscoveryType.PARTICIPANT.equals(item.getType())) {
				p = getParticipantSelectedTabPath();
			} else if (DiscoveryType.POC.equals(item.getType())) {
				p = getPocSelectedTabPath();
			}
			handle(request, response, p);
		}
	}

	public String getServiceSelectedTabPath() {
		return serviceSelectedTabPath;
	}

	public void setServiceSelectedTabPath(String serviceSelectedTabPath) {
		this.serviceSelectedTabPath = serviceSelectedTabPath;
	}

	public String getParticipantSelectedTabPath() {
		return participantSelectedTabPath;
	}

	public void setParticipantSelectedTabPath(String participantSelectedTabPath) {
		this.participantSelectedTabPath = participantSelectedTabPath;
	}

	public String getPocSelectedTabPath() {
		return pocSelectedTabPath;
	}

	public void setPocSelectedTabPath(String pocSelectedTabPath) {
		this.pocSelectedTabPath = pocSelectedTabPath;
	}

}
