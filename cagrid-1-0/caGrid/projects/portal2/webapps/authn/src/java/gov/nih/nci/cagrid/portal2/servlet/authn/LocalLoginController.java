/**
 * 
 */
package gov.nih.nci.cagrid.portal2.servlet.authn;

import gov.nih.nci.cagrid.portal2.domain.PortalUser;
import gov.nih.nci.cagrid.portal2.webauthn.client.WebAuthnSvcClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
//TODO: refactor similarities with GridLoginController
public class LocalLoginController extends SimpleFormController {

	private static final Log logger = LogFactory
			.getLog(LocalLoginController.class);

	
	private WebAuthnSvcClient webAuthnSvcClient;
	private String userSessionKey;

	private String targetUrlKey;

	public String getTargetUrlKey() {
		return targetUrlKey;
	}

	public void setTargetUrlKey(String targetUrlKey) {
		this.targetUrlKey = targetUrlKey;
	}

	public String getUserSessionKey() {
		return userSessionKey;
	}

	public void setUserSessionKey(String userSessionKey) {
		this.userSessionKey = userSessionKey;
	}

	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object commandObj,
			BindException errors) throws Exception {
		logger.info("Handling login submit");
		ModelAndView mav = new ModelAndView(getSuccessView());
		LoginCommand command = (LoginCommand) commandObj;
		
		PortalUser user = new PortalUser();
		user.setUsername(command.getUsername());
		user.setPassword(command.getPassword());
		String loginKey = AuthnUtils.generateLoginKey(getWebAuthnSvcClient(), user);
		mav.addObject("loginKey", loginKey);
		mav.addObject(getTargetUrlKey(), command.getTargetUrl());
		request.getSession().setAttribute(getUserSessionKey(), user);
		return mav;
	}

	public WebAuthnSvcClient getWebAuthnSvcClient() {
		return webAuthnSvcClient;
	}

	public void setWebAuthnSvcClient(WebAuthnSvcClient webAuthnSvcClient) {
		this.webAuthnSvcClient = webAuthnSvcClient;
	}

}
