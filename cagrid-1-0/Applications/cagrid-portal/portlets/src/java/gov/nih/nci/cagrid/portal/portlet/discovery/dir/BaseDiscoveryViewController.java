/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.discovery.dir;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import gov.nih.nci.cagrid.portal.portlet.CaGridPortletApplicationException;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryModel;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryResults;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public abstract class BaseDiscoveryViewController extends AbstractController {
	
	private String successViewName;
	private DiscoveryModel discoveryModel;
	private String commandName;
	private String resultsAttributeName = "results";

	/**
	 * 
	 */
	public BaseDiscoveryViewController() {

	}
	
	protected ModelAndView handleRenderRequestInternal(RenderRequest request, RenderResponse response) throws Exception {
		
		ModelAndView mav = new ModelAndView(getSuccessViewName());
		
		DiscoveryDirectory selectedDirectory = getDiscoveryModel().getSelectedDirectory();
		DiscoveryResults selectedResults = getDiscoveryModel().getSelectedResults();
		if(selectedDirectory != null && selectedResults != null){
			throw new CaGridPortletApplicationException("Both a directory and a results obect are selected.");
		}
		if(selectedResults == null && selectedDirectory == null){
			selectedDirectory = getDiscoveryModel().getDefaultDirectory();
		}
		AbstractDirectoryBean dirBean = null;
		
		if(selectedResults != null){
			dirBean = doHandleResults(request, response, selectedResults);
		}
		if(selectedDirectory != null){
			dirBean = doHandleDirectory(request, response, selectedDirectory);
		}
		mav.addObject(getCommandName(), dirBean);
		mav.addObject(getResultsAttributeName(), getDiscoveryModel().getResults());
		
		return mav;
	}
	
	
	protected abstract AbstractDirectoryBean doHandleResults(RenderRequest request, RenderResponse response, DiscoveryResults res) throws Exception;
	protected abstract AbstractDirectoryBean doHandleDirectory(RenderRequest request, RenderResponse response, DiscoveryDirectory dir) throws Exception;
	
	protected DiscoveryResults getSelectedResults(){
		return getDiscoveryModel().getSelectedResults();
	}
	
	protected abstract AbstractDirectoryBean newDirectoryBean();
	
	@Required
	public DiscoveryModel getDiscoveryModel() {
		return discoveryModel;
	}

	public void setDiscoveryModel(DiscoveryModel discoveryModel) {
		this.discoveryModel = discoveryModel;
	}

	@Required
	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}
	
	@Required
	public String getSuccessViewName() {
		return successViewName;
	}

	public void setSuccessViewName(String successViewName) {
		this.successViewName = successViewName;
	}

	public String getResultsAttributeName() {
		return resultsAttributeName;
	}

	public void setResultsAttributeName(String resultsAttributeName) {
		this.resultsAttributeName = resultsAttributeName;
	}

}
