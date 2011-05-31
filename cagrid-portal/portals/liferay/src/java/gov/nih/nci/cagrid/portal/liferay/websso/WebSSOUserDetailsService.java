package gov.nih.nci.cagrid.portal.liferay.websso;

import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.cagrid.websso.client.acegi.DefaultUserDetailsService;
import org.cagrid.websso.client.acegi.WebSSOUser;
import org.cagrid.websso.common.WebSSOConstants;
import org.springframework.dao.DataAccessException;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Extending DefaultUserDetailsService
 * from caGrid webSSO to avoid making
 * calls to CDS.
 * <p/>
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class WebSSOUserDetailsService extends DefaultUserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String casUserId)
            throws UsernameNotFoundException, DataAccessException {
        Map<String, String> userAttributesMap = getUserAttributes(casUserId);
        String gridId = getUserIdFromGridIdentity(userAttributesMap.get(WebSSOConstants.CAGRID_SSO_GRID_IDENTITY));
        WebSSOUser user = loadUserByGridId(gridId);
        loadSessionAttributes(userAttributesMap, user);
        return user;
    }

    private void loadSessionAttributes(Map<String, String> userAttributesMap, WebSSOUser user) {
        user.setFirstName(userAttributesMap.get(WebSSOConstants.CAGRID_SSO_FIRST_NAME));
        user.setGridId(userAttributesMap.get(WebSSOConstants.CAGRID_SSO_GRID_IDENTITY));
        user.setLastName(userAttributesMap.get(WebSSOConstants.CAGRID_SSO_LAST_NAME));
        user.setDelegatedEPR(userAttributesMap.get(WebSSOConstants.CAGRID_SSO_DELEGATION_SERVICE_EPR));
        user.setEmailId(userAttributesMap.get(WebSSOConstants.CAGRID_SSO_EMAIL_ID));
    }


    public String getUserIdFromGridIdentity(String gridIdentity) {
        String[] sections = gridIdentity.split("=");
        return sections[sections.length - 1];
    }

    public Map<String, String> getUserAttributes(String attributeString) {
        Map<String, String> userAttributes = new HashMap<String, String>();
        StringTokenizer stringTokenizer = new StringTokenizer(attributeString, WebSSOConstants.ATTRIBUTE_DELIMITER);
        while (stringTokenizer.hasMoreTokens()) {
            String attributeKeyValuePair = stringTokenizer.nextToken();
            final int index = attributeKeyValuePair.indexOf(WebSSOConstants.KEY_VALUE_PAIR_DELIMITER);
            if (index == -1)
                throw new RuntimeException("Invalid UserAttributes from WebSSO-Server ");
            final String key = attributeKeyValuePair.substring(0, index);
            final String value = attributeKeyValuePair.substring(index + 1, attributeKeyValuePair.length());
            userAttributes.put(key, value);
        }
        return userAttributes;
    }
}
