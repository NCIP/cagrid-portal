package org.cagrid.gaards.websso.client.filter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.web.filter.AbstractCasFilter;

public class CaGridWebSSOAttributeLoaderFilter implements Filter
{

	public static final String IS_SESSION_ATTRIBUTES_LOADED = "IS_SESSION_ATTRIBUTES_LOADED";
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
			Assertion assertion = (Assertion)session.getAttribute(AbstractCasFilter.CONST_ASSERTION);
			Map<String, String> attributeMap = assertion.getAttributes();
			Set<String> keySet = attributeMap.keySet();
			for (String key : keySet)
			{
				String value = attributeMap.get(key);
				session.setAttribute(key, value);
			}
			session.setAttribute(IS_SESSION_ATTRIBUTES_LOADED, Boolean.TRUE);
		}	
	}

	public void init(FilterConfig arg0) throws ServletException
	{
		// do nothing
	}

}
