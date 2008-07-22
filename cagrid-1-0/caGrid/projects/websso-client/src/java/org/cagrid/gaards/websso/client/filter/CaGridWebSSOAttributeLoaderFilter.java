package org.cagrid.gaards.websso.client.filter;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.web.filter.AbstractCasFilter;

public class CaGridWebSSOAttributeLoaderFilter implements Filter
{

	public static final String IS_SESSION_ATTRIBUTES_LOADED = "IS_SESSION_ATTRIBUTES_LOADED";
	public static final String ATTRIBUTE_DELIMITER = "$";
	public static final String KEY_VALUE_PAIR_DELIMITER = "^";
	
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
			Principal principal = assertion.getPrincipal();
			String attributesString = principal.getId();
			
			StringTokenizer stringTokenizer = new StringTokenizer(attributesString, ATTRIBUTE_DELIMITER);
			while (stringTokenizer.hasMoreTokens())
			{
				String attributeKeyValuePair = stringTokenizer.nextToken();
				session.setAttribute(attributeKeyValuePair.substring(0, attributeKeyValuePair.indexOf(KEY_VALUE_PAIR_DELIMITER)), attributeKeyValuePair.substring(attributeKeyValuePair.indexOf(KEY_VALUE_PAIR_DELIMITER) + 1, attributeKeyValuePair.length()));
			}
			session.setAttribute(IS_SESSION_ATTRIBUTES_LOADED, Boolean.TRUE);			
		}
		filterChain.doFilter(request, response);
	}

	public void init(FilterConfig arg0) throws ServletException
	{
		// do nothing
	}
}
