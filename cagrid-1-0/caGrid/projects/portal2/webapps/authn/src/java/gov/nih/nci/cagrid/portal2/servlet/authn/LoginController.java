/**
 * 
 */
package gov.nih.nci.cagrid.portal2.servlet.authn;

import gov.nih.nci.cagrid.portal2.domain.PortalUser;
import gov.nih.nci.cagrid.portal2.webauthn.client.WebAuthnSvcClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class LoginController extends AbstractController {
	
	private String redirectView;
	private String loginSuccessView;
	private String selectActionView;
	private String userSessionKey;
	private String targetUrlKey;
	private WebAuthnSvcClient webAuthnSvcClient;

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ModelAndView mav = null;
		
		String targetUrl = request.getParameter(getTargetUrlKey());
		PortalUser user = (PortalUser)request.getSession().getAttribute(getUserSessionKey());
		if(user != null){
			String loginKey = AuthnUtils.generateLoginKey(getWebAuthnSvcClient(), user);
			
			if(targetUrl != null){
				mav = new ModelAndView(getRedirectView());
			}else{
				mav = new ModelAndView(getLoginSuccessView());
			}
			mav.addObject("loginKey", loginKey);
			
		}else{
			mav = new ModelAndView(getSelectActionView());
		}
		
		
		if(targetUrl != null){
			mav.addObject("targetUrl", targetUrl);
		}
		
		return mav;
	}

	

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

	public String getSelectActionView() {
		return selectActionView;
	}

	public void setSelectActionView(String selectActionView) {
		this.selectActionView = selectActionView;
	}

	public String getUserSessionKey() {
		return userSessionKey;
	}

	public void setUserSessionKey(String userSessionKey) {
		this.userSessionKey = userSessionKey;
	}

	public String getTargetUrlKey() {
		return targetUrlKey;
	}

	public void setTargetUrlKey(String targetUrlKey) {
		this.targetUrlKey = targetUrlKey;
	}

	public WebAuthnSvcClient getWebAuthnSvcClient() {
		return webAuthnSvcClient;
	}

	public void setWebAuthnSvcClient(WebAuthnSvcClient webAuthnSvcClient) {
		this.webAuthnSvcClient = webAuthnSvcClient;
	}

}
