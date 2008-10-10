package org.cagrid.websso.client.acegi;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.ui.logout.LogoutHandler;
import org.cagrid.websso.common.WebSSOClientHelper;

import org.springframework.core.io.Resource;

public class WebSSOLogoutHandler implements LogoutHandler  {
	
	private Resource casClientResource;
	private String logoutLandingURL;	
	private String logoutURL;
	
	public WebSSOLogoutHandler(Resource casClientResource,String logoutLandingURL) {
		this.casClientResource = casClientResource;
		this.logoutLandingURL = logoutLandingURL;
	}
	
	public void logout(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication) {
		Properties properties = new Properties();
		try {
			properties.load(casClientResource.getInputStream());
			WebSSOUser webssoUser = (WebSSOUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String delegationEPR = webssoUser.getDelegatedEPR();
			this.logoutURL = WebSSOClientHelper.getLogoutURL(properties,delegationEPR, logoutLandingURL);			
		} catch (IOException e) {
			throw new RuntimeException("error occured handling logout " + e);
		}
	}
	
	public String getLogoutURL() {
		return logoutURL;
	}
}
