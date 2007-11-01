/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.discovery.details;

import gov.nih.nci.cagrid.portal2.portlet.AbstractActionResponseHandlerCommandController;
import gov.nih.nci.cagrid.portal2.portlet.discovery.DiscoveryModel;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public abstract class AbstractDiscoverySelectDetailsController extends AbstractActionResponseHandlerCommandController {
	
	private DiscoveryModel discoveryModel;

	/**
	 * 
	 */
	public AbstractDiscoverySelectDetailsController() {

	}

	/**
	 * @param commandClass
	 */
	public AbstractDiscoverySelectDetailsController(Class commandClass) {
		super(commandClass);

	}

	/**
	 * @param commandClass
	 * @param commandName
	 */
	public AbstractDiscoverySelectDetailsController(Class commandClass, String commandName) {
		super(commandClass, commandName);
	}
	
	protected void doHandleAction(ActionRequest request, ActionResponse response, Object obj, BindException errors) throws Exception{
		doSelect(getDiscoveryModel(), ((SelectDetailsCommand)obj).getSelectedId());
	}
	
	protected abstract void doSelect(DiscoveryModel model, Integer selectedId);


	@Required
	public DiscoveryModel getDiscoveryModel() {
		return discoveryModel;
	}

	public void setDiscoveryModel(DiscoveryModel discoveryModel) {
		this.discoveryModel = discoveryModel;
	}

}
