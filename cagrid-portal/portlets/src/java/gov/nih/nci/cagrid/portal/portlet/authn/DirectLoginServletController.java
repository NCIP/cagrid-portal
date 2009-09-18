/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.authn;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class DirectLoginServletController extends AbstractController {

	/**
	 * 
	 */
	public DirectLoginServletController() {

	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String redirectUrl = request.getParameter("redirectUrl");
		logger.debug("Redirecting to: " + redirectUrl);
		response.sendRedirect(redirectUrl);
		return null;
	}

}
