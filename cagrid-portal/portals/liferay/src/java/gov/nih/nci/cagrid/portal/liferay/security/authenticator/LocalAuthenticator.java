/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.liferay.security.authenticator;

import com.liferay.portal.NoSuchUserException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.AuthException;
import com.liferay.portal.security.auth.Authenticator;
import com.liferay.portal.security.pwd.PwdEncryptor;
import com.liferay.portal.service.persistence.UserUtil;

import java.util.Map;

/**
 * Liferay database authentication
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class LocalAuthenticator implements Authenticator {

    public int authenticateByEmailAddress(long companyId, String emailAddress, String password, Map headerMap, Map parameterMap)
            throws AuthException {

        try {
            return authenticate(companyId, emailAddress, StringPool.BLANK, 0, password);
        }
        catch (Exception e) {
            _log.error(e, e);
            throw new AuthException(e);
        }
    }

    public int authenticateByScreenName(long companyId, String screenName, String password, Map headerMap, Map parameterMap)
            throws AuthException {

        try {
            return authenticate(companyId, StringPool.BLANK, screenName, 0, password);
        }
        catch (Exception e) {
            _log.error(e, e);
            throw new AuthException(e);
        }
    }

    public int authenticateByUserId(long companyId, long userId, String password, Map headerMap, Map parameterMap)
            throws AuthException {

        try {
            return authenticate(companyId, StringPool.BLANK, StringPool.BLANK, userId, password);
        }
        catch (Exception e) {
            _log.error(e, e);

            throw new AuthException(e);
        }
    }

    protected int authenticate(long companyId, String emailAddress, String screenName, long userId, String password) throws Exception {
        if (_log.isDebugEnabled()) {
            _log.debug("Authenticator is enabled");
        }

        User user = null;
        try {
            if (!emailAddress.equals(StringPool.BLANK)) {
                user = UserUtil.findByC_EA(companyId, emailAddress);
            } else if (!screenName.equals(StringPool.BLANK)) {
                user = UserUtil.findByC_SN(companyId, screenName);
            } else if (userId > 0) {
                user = UserUtil.findByC_U(companyId, userId);
            } else {
                return Authenticator.DNE;
            }
        }
        catch (NoSuchUserException nsue) {
            return Authenticator.DNE;
        }
        String encPwd = PwdEncryptor.encrypt(password, user.getPassword());
        if (!user.getPassword().equals(encPwd)) {
            _log.debug("Passwords did not match: " + user.getPassword() + " || " + encPwd);
            return FAILURE;
        }
        return SUCCESS;
    }

    private static Log _log = LogFactoryUtil.getLog(LocalAuthenticator.class);
}




