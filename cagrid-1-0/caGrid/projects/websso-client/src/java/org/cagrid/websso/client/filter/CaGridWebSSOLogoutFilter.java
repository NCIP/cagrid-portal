package org.cagrid.websso.client.filter;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.cagrid.websso.common.WebSSOConstants;
import org.cagrid.websso.common.WebSSOClientHelper;
import org.springframework.core.io.Resource;

public class CaGridWebSSOLogoutFilter implements Filter {

	private static final String LOGOUT_LANDING_URL = "logout-landing-url";

	private String logoutLandingURL = null;

	private Resource casClientResource;
	
	public void setCasClientResource(Resource casClientResource) {
		this.casClientResource = casClientResource;
	}
	
	public void destroy() {
		// do nothing
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest) request).getSession();
		Boolean isSessionLoaded = (Boolean) session.getAttribute(WebSSOConstants.IS_SESSION_ATTRIBUTES_LOADED);
		if (null == isSessionLoaded || isSessionLoaded == Boolean.FALSE) {
			throw new ServletException("WebSSO Attributes are not loaded in the Session");
		} else {
			Properties properties = new Properties();
			properties.load(casClientResource.getInputStream());
			String delegationEPR = (String) session.getAttribute(WebSSOConstants.CAGRID_SSO_DELEGATION_SERVICE_EPR);
			String logoutURL = WebSSOClientHelper.getLogoutURL(properties,delegationEPR,logoutLandingURL);
			session.invalidate();
			((HttpServletResponse) response).sendRedirect(logoutURL);
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.logoutLandingURL = filterConfig.getInitParameter(LOGOUT_LANDING_URL);
	}
}
