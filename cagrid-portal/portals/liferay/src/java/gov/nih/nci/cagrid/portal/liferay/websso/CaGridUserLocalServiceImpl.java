package gov.nih.nci.cagrid.portal.liferay.websso;


import com.liferay.portal.*;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.PasswordPolicy;
import com.liferay.portal.security.pwd.PwdToolkitUtil;
import com.liferay.portal.service.PasswordPolicyLocalServiceUtil;
import com.liferay.portal.service.impl.UserLocalServiceImpl;
import com.liferay.portal.util.PrefsPropsUtil;
import com.liferay.portal.util.PropsKeys;
import com.liferay.portal.util.PropsValues;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */

public class CaGridUserLocalServiceImpl extends UserLocalServiceImpl {

    Log _log = LogFactory.getLog(CaGridUserLocalServiceImpl.class);

    @Override
    protected void validate(
            long companyId, long userId, boolean autoPassword, String password1,
            String password2, boolean autoScreenName, String screenName,
            String emailAddress, String firstName, String lastName,
            long[] organizationIds)
            throws PortalException, SystemException {

        _log.debug("Using  CaGridUserLocalServiceImpl  for validation of user");

        if (!autoScreenName) {
            validateScreenName(companyId, userId, screenName);
        }

        if (!autoPassword) {
            PasswordPolicy passwordPolicy = PasswordPolicyLocalServiceUtil
                    .getDefaultPasswordPolicy(companyId);

            PwdToolkitUtil.validate(companyId, 0, password1, password2,
                    passwordPolicy);
        }

        if (!Validator.isEmailAddress(emailAddress)) {
            throw new UserEmailAddressException();
        } else {
            // As per caGrid requirements portal can have duplicate email address for a particular company Id.
            /*try {
                   User user = UserUtil.findByC_EA(companyId, emailAddress);

                   if (user != null) {
                       throw new DuplicateUserEmailAddressException();
                   }
               } catch (NoSuchUserException nsue) {
               }*/

            String[] reservedEmailAddresses = PrefsPropsUtil.getStringArray(
                    companyId, PropsKeys.ADMIN_RESERVED_EMAIL_ADDRESSES, StringPool.NEW_LINE,
                    PropsValues.ADMIN_RESERVED_EMAIL_ADDRESSES);

            for (int i = 0; i < reservedEmailAddresses.length; i++) {
                if (emailAddress.equalsIgnoreCase(reservedEmailAddresses[i])) {
                    throw new ReservedUserEmailAddressException();
                }
            }
        }

        if (Validator.isNull(firstName)) {
            throw new ContactFirstNameException();
        } else if (Validator.isNull(lastName)) {
            throw new ContactLastNameException();
        }
    }

    @Override
    protected void validate(long l, String s, String s1, String s2, String s3, String s4) throws PortalException, SystemException {
        super.validate(l, s, s1, s2, s3, s4);    //To change body of overridden methods use File | Settings | File Templates.
    }
}