/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.util;

import java.util.Date;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.portlet.HandlerExceptionResolver;
import org.springframework.web.portlet.ModelAndView;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class DefaultExceptionResolver implements HandlerExceptionResolver {
	
	

	private static final Log logger = LogFactory.getLog(DefaultExceptionResolver.class);
	
	private String viewName;
	private String adminEmail;
	
	/**
	 * 
	 */
	public DefaultExceptionResolver() {

	}
	
	public ModelAndView resolveException(RenderRequest request,
            RenderResponse response,
            Object handler,
            Exception ex){
		ModelAndView mav = new ModelAndView(getViewName());
		String handlerClass = null;
		if(handler != null){
			mav.addObject("handler", handler);
			handlerClass = handler.getClass().getName();
		}
		String message = null;
		String exClass = null;
		if(ex != null){
			mav.addObject("exception", ex);
			exClass = ex.getClass().getName();
			message = ex.getMessage();
		}
		
		if(message != null){
			mav.addObject("message", message);
		}
		
		mav.addObject("now", new Date());
		mav.addObject("adminEmail", getAdminEmail());

		logger.error(handlerClass + " threw " + exClass + ": " + message, ex);
		
		return mav;
	}

	@Required
	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	@Required
	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

}
