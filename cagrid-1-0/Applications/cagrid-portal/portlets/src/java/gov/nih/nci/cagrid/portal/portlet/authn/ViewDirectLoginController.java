/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.authn;

import gov.nih.nci.cagrid.portal.domain.PortalUser;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ViewDirectLoginController extends AbstractController implements InitializingBean {

	private String portalUserAttributeName;
	private String viewName;
	private String errorsAttributeName;
	private String commandName;
	private String authnErrorMessageAttributeName;
	private String[] idpInfo;
	private List<IdPUrl> urls = new ArrayList<IdPUrl>();

	/**
	 * 
	 */
	public ViewDirectLoginController() {

	}

	protected ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {
		ModelAndView mav = null;
		BindException errors = null;
		if(getErrorsAttributeName() != null){
			errors = (BindException)request.getAttribute(getErrorsAttributeName());
		}
		if(errors != null){
			mav = new ModelAndView(getViewName(), errors.getModel());
		}else{
			mav = new ModelAndView(getViewName());
		}
		
		logger.debug("Looking for PortalUser under '" + getPortalUserAttributeName() + "'");
		
		PortalUser user = (PortalUser) request.getPortletSession()
				.getAttribute(getPortalUserAttributeName(),
						PortletSession.APPLICATION_SCOPE);
		if (user != null) {
			mav.addObject("portalUser", user);
		} else {
			
			
			mav.addObject("registerUrl", request.getPreferences().getValue("registerUrl", ""));
			mav.addObject(getCommandName(), new DirectLoginCommand());
			mav.addObject("idpUrls", urls);
		}
		String authnErrorMsg = (String) request.getAttribute(getAuthnErrorMessageAttributeName());
		if(authnErrorMsg != null){
			mav.addObject("authnErrorMessage", authnErrorMsg);
		}

		return mav;
	}

	@Required
	public String getPortalUserAttributeName() {
		return portalUserAttributeName;
	}

	public void setPortalUserAttributeName(String portalUserAttributeName) {
		this.portalUserAttributeName = portalUserAttributeName;
	}

	@Required
	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	@Required
	public String getErrorsAttributeName() {
		return errorsAttributeName;
	}

	public void setErrorsAttributeName(String errorsAttributeName) {
		this.errorsAttributeName = errorsAttributeName;
	}
	
	@Required
	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}
	
	@Required
	public String getAuthnErrorMessageAttributeName() {
		return authnErrorMessageAttributeName;
	}

	public void setAuthnErrorMessageAttributeName(
			String authnErrorMessageAttributeName) {
		this.authnErrorMessageAttributeName = authnErrorMessageAttributeName;
	}

	@Required
	public String[] getIdpInfo() {
		return idpInfo;
	}

	public void setIdpInfo(String[] idpUrls) {
		this.idpInfo = idpUrls;
	}

	public void afterPropertiesSet() throws Exception {
		logger.debug("idpInfo.length = " + idpInfo.length);
		for(String info : getIdpInfo()){
			logger.debug("info: " + info);
			String[] pair = info.split("\\|");
			logger.debug("Adding IdP: " + pair[0] + ":  " + pair[1]);
			urls.add(new IdPUrl(pair[0], pair[1]));
		}		
	}

}
