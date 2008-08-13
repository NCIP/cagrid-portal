package org.cagrid.gaards.websso.client.filter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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

import org.springframework.core.io.ClassPathResource;


public class CaGridWebSSOLogoutFilter implements Filter
{

	public static final String IS_SESSION_ATTRIBUTES_LOADED = "IS_SESSION_ATTRIBUTES_LOADED";
	private static final String CAGRID_SSO_DELEGATION_SERVICE_EPR = "CAGRID_SSO_DELEGATION_SERVICE_EPR";
	private static final String LOGOUT_LANDING_URL = "logout-landing-url";
	
	private String logoutLandingURL = null;
	public void destroy()
	{
		// do nothing
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException
	{
		HttpSession session = ((HttpServletRequest)request).getSession();
		Boolean isSessionLoaded = (Boolean) session.getAttribute(IS_SESSION_ATTRIBUTES_LOADED);
		if (null == isSessionLoaded || isSessionLoaded == Boolean.FALSE)
		{
			throw new ServletException("WebSSO Attributes are not loaded in the Session");
		}
		else
		{
			Properties properties = new Properties();
			ClassPathResource casResource=new ClassPathResource("cas-client.properties");
			properties.load(casResource.getInputStream());
			String logoutURL = properties.getProperty("cas.server.url") + "logout";
			String delegationEPR = (String)session.getAttribute(CAGRID_SSO_DELEGATION_SERVICE_EPR);
			logoutURL = logoutURL + "?service=" + this.logoutLandingURL;
			logoutURL = logoutURL + "&" + CAGRID_SSO_DELEGATION_SERVICE_EPR + "=" + delegationEPR;
			((HttpServletRequest)request).getSession().invalidate();
            ((HttpServletResponse)response).sendRedirect(logoutURL);
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException
	{
		this.logoutLandingURL = filterConfig.getInitParameter(LOGOUT_LANDING_URL);
	}

}
