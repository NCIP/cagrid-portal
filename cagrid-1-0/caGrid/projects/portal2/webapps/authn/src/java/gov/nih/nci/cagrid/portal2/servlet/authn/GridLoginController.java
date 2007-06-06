/**
 * 
 */
package gov.nih.nci.cagrid.portal2.servlet.authn;

import gov.nih.nci.cagrid.portal2.domain.GridPortalUser;
import gov.nih.nci.cagrid.portal2.webauthn.client.WebAuthnSvcClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class GridLoginController extends SimpleFormController {
	
	private WebAuthnSvcClient webAuthnSvcClient;
	private String userSessionKey;
	private String targetUrlKey;
	private Map<String,String> idpUrls = new HashMap<String,String>();
	
	public String getUserSessionKey() {
		return userSessionKey;
	}

	public void setUserSessionKey(String userSessionKey) {
		this.userSessionKey = userSessionKey;
	}
	
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map data = new HashMap();
		List<LabelValueBean> urlBeans = new ArrayList<LabelValueBean>();
		for(String label : getIdpUrls().keySet()){
			String value = getIdpUrls().get(label);
			urlBeans.add(new LabelValueBean(label, value));
		}
		data.put("idpUrls", urlBeans);
		return data;
	}

	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse resposne, Object commandObj,
			BindException errors) throws Exception {
		ModelAndView mav = new ModelAndView(getSuccessView());
		
		LoginCommand command = (LoginCommand)commandObj;
		GridPortalUser user = new GridPortalUser();
		user.setUsername(command.getUsername());
		user.setPassword(command.getPassword());
		user.setIdpUrl(command.getIdpUrl());
		String loginKey = AuthnUtils.generateLoginKey(getWebAuthnSvcClient(), user);
		
		//TODO: if user is not successfully authenticated, do some error
		//handling here.

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

	public String getTargetUrlKey() {
		return targetUrlKey;
	}

	public void setTargetUrlKey(String targetUrlKey) {
		this.targetUrlKey = targetUrlKey;
	}

	public Map<String, String> getIdpUrls() {
		return idpUrls;
	}

	public void setIdpUrls(Map<String, String> idpUrls) {
		this.idpUrls = idpUrls;
	}
}
