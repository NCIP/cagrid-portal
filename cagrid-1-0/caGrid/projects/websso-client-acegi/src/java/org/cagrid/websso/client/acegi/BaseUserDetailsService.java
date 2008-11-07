package org.cagrid.websso.client.acegi;

import java.util.Map;

import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.cagrid.websso.common.WebSSOConstants;
import org.cagrid.websso.common.WebSSOClientHelper;
import org.springframework.dao.DataAccessException;

public abstract class BaseUserDetailsService implements UserDetailsService {

	/**
	 * implement by application specific UserDetailsServices to load from user
	 * details from CSM.
	 * @param userName
	 * @return
	 */
	abstract protected WebSSOUser loadUser(String userName);

	public UserDetails loadUserByUsername(String casUserId)
			throws UsernameNotFoundException, DataAccessException {
		Map<String, String> userAttributesMap = WebSSOClientHelper.getUserAttributes(casUserId);
		String userName = getUserIdFromGridIdentity(userAttributesMap.get(WebSSOConstants.CAGRID_SSO_GRID_IDENTITY));
		WebSSOUser user = loadUser(userName);
		loadSessionAttributes(userAttributesMap, user);
		return user;
	}
	
	private void loadSessionAttributes(Map<String,String> userAttributesMap,WebSSOUser user){
		user.setFirstName(userAttributesMap.get(WebSSOConstants.CAGRID_SSO_FIRST_NAME));
		user.setGridId(userAttributesMap.get(WebSSOConstants.CAGRID_SSO_GRID_IDENTITY));
		user.setLastName(userAttributesMap.get(WebSSOConstants.CAGRID_SSO_LAST_NAME));
		user.setDelegatedEPR(userAttributesMap.get(WebSSOConstants.CAGRID_SSO_DELEGATION_SERVICE_EPR));
		user.setEmailId(userAttributesMap.get(WebSSOConstants.CAGRID_SSO_EMAIL_ID));
		user.setDelegatedEPR(userAttributesMap.get(WebSSOConstants.CAGRID_SSO_DELEGATION_SERVICE_EPR));
	}
	
	private String getUserIdFromGridIdentity(String gridIdentity) {
		String[] sections = gridIdentity.split("=");
		return sections[sections.length - 1];
	}
}