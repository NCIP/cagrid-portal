/**
 * 
 */
package gov.nih.nci.cagrid.portal2.servlet.authn;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class LogoutController extends AbstractController {
	
	private String targetUrlKey;
	private String loginSuccessView;
	private String redirectView;

	public String getLoginSuccessView() {
		return loginSuccessView;
	}

	public void setLoginSuccessView(String loginSuccessView) {
		this.loginSuccessView = loginSuccessView;
	}

	public String getRedirectView() {
		return redirectView;
	}

	public void setRedirectView(String redirectView) {
		this.redirectView = redirectView;
	}

	public String getTargetUrlKey() {
		return targetUrlKey;
	}

	public void setTargetUrlKey(String targetUrlKey) {
		this.targetUrlKey = targetUrlKey;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mav = null;
		String targetUrl = request.getParameter(getTargetUrlKey());
		if(targetUrl == null){
			mav = new ModelAndView(getLoginSuccessView());
		}else{
			mav = new ModelAndView(getRedirectView());
			mav.addObject("targetUrl", targetUrl);
		}
		request.getSession(true).invalidate();
	
		return mav;
	}

}
