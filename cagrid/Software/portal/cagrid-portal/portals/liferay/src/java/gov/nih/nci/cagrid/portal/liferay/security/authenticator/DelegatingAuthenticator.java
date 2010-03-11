package gov.nih.nci.cagrid.portal.liferay.security.authenticator;

import com.liferay.portal.security.auth.AuthException;
import com.liferay.portal.security.auth.Authenticator;

import java.util.Collection;
import java.util.Map;

/**
 * Delegates authentication to a collection of
 * authenticator classes. Will return SUCCESS if ANY ONE
 * of the supplied authenticators can authenticate the user
 * <p/>
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class DelegatingAuthenticator implements Authenticator {

    private Collection<Authenticator> authenticators;

    public int authenticateByEmailAddress(long companyId, String emailAddress, String password, Map<String, String[]> headerMap, Map<String, String[]> parameterMap) throws AuthException {
        for (Authenticator auth : authenticators) {
            int authentication = auth.authenticateByEmailAddress(companyId, emailAddress, password, headerMap, parameterMap);
            if (authentication == SUCCESS) {
                return authentication;
            }
        }
        return FAILURE;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int authenticateByScreenName(long companyId, String screenName, String password, Map<String, String[]> headerMap, Map<String, String[]> parameterMap) throws AuthException {
        for (Authenticator auth : authenticators) {
            int authentication = auth.authenticateByScreenName(companyId, screenName, password, headerMap, parameterMap);
            if (authentication == SUCCESS) {
                return authentication;
            }
        }
        return FAILURE;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int authenticateByUserId(long companyId, long userId, String password, Map<String, String[]> headerMap, Map<String, String[]> parameterMap) throws AuthException {
        for (Authenticator auth : authenticators) {
            int authentication = auth.authenticateByUserId(companyId, userId, password, headerMap, parameterMap);
            if (authentication == SUCCESS) {
                return authentication;
            }
        }
        return FAILURE;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Collection<Authenticator> getAuthenticators() {
        return authenticators;
    }

    public void setAuthenticators(Collection<Authenticator> authenticators) {
        this.authenticators = authenticators;
    }
}
