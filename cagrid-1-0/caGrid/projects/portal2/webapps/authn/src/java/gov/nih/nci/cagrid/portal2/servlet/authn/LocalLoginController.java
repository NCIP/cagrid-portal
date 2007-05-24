/**
 * 
 */
package gov.nih.nci.cagrid.portal2.servlet.authn;

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
public class LocalLoginController extends SimpleFormController {

	private static final Log logger = LogFactory
			.getLog(LocalLoginController.class);

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
		doLogin(request, response, command);
		mav.addObject(getTargetUrlKey(), command.getTargetUrl());
		return mav;
	}

	private String doLogin(HttpServletRequest request,
			HttpServletResponse response, LoginCommand command) {
		// TODO: interact with local credential store
		request.getSession().setAttribute(getUserSessionKey(),
				command.getUsername());
		return command.getUsername();
	}

}
