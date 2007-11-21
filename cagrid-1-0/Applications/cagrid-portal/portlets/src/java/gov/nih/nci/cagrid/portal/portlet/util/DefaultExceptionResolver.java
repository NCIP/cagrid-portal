/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.util;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.handler.SimpleMappingExceptionResolver;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class DefaultExceptionResolver extends SimpleMappingExceptionResolver {

	private static final Log logger = LogFactory.getLog(DefaultExceptionResolver.class);
	
	/**
	 * 
	 */
	public DefaultExceptionResolver() {

	}
	
	public ModelAndView resolveException(RenderRequest request,
            RenderResponse response,
            Object handler,
            Exception ex){
		
		String handlerClass = null;
		if(handler != null){
			handlerClass = handler.getClass().getName();
		}
		String message = null;
		String exClass = null;
		if(ex != null){
			exClass = ex.getClass().getName();
			message = ex.getMessage();
		}

		logger.error(handlerClass + " threw " + exClass + ": " + message, ex);
		
		return super.resolveException(request, response, handler, ex);
	}

}
