package org.cagrid.gaards.websso.client.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;


public class CaGridWebSSOAttributeLoaderFilter implements Filter {

	public static final String IS_SESSION_ATTRIBUTES_LOADED = "IS_SESSION_ATTRIBUTES_LOADED";
	public static final String ATTRIBUTE_DELIMITER = "$";
	public static final String KEY_VALUE_PAIR_DELIMITER = "^";
	
	public void destroy() {
		// do nothing
	}

	public void doFilter(ServletRequest request, ServletResponse response,FilterChain filterChain) throws IOException, ServletException {
		HttpSession session = ((HttpServletRequest) request).getSession();
		Boolean isSessionLoaded = (Boolean) session.getAttribute(IS_SESSION_ATTRIBUTES_LOADED);
		if (null == isSessionLoaded || isSessionLoaded == Boolean.FALSE) {
			Assertion assertion = (Assertion) session.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
			AttributePrincipal attributePrincipal = assertion.getPrincipal();
			String attributesString = attributePrincipal.getName();			
			Map<String,String> userAttributesMap=getUserAttributes(attributesString);
			Iterator<String> iterator = userAttributesMap.keySet().iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				String value = userAttributesMap.get(key);
				session.setAttribute(key, value);
			}
			session.setAttribute(IS_SESSION_ATTRIBUTES_LOADED, Boolean.TRUE);			
		}
		filterChain.doFilter(request, response);
	}

	protected Map<String, String> getUserAttributes(String attributeString) throws ServletException {
		Map<String, String> userAttributes=new HashMap<String, String>();
 		StringTokenizer stringTokenizer = new StringTokenizer(attributeString, ATTRIBUTE_DELIMITER);
		while (stringTokenizer.hasMoreTokens()) {
			String attributeKeyValuePair = stringTokenizer.nextToken();
			final int index = attributeKeyValuePair.indexOf(KEY_VALUE_PAIR_DELIMITER);
			
			if (index == -1)
				throw new ServletException("Invalid UserAttributes from WebSSO-Server "+ attributeString);
			
			final String key = attributeKeyValuePair.substring(0, index);
			final String value = attributeKeyValuePair.substring(index + 1,attributeKeyValuePair.length());
			userAttributes.put(key, value);
		}
		return userAttributes;
	}

	public void init(FilterConfig arg0) throws ServletException {
		// do nothing
	}
}