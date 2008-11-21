package com.cagrid.liferay.websso.client;


import com.liferay.portal.NoSuchUserException;
import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.AutoLogin;
import com.liferay.portal.security.auth.AutoLoginException;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.service.UserServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PrefsPropsUtil;
import com.liferay.portal.util.PropsUtil;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.websso.client.acegi.WebSSOUser;

/**
 * <a href="CaGridLiferayCASAutoLoginHook.java.html"><b><i>View Source</i></b></a>
 * 
 * Liferay portal provides a seperate hooking mechanism to fetch valid credentials from 
 * any third party security system.Inside this hook method ,we have to populate current user 
 * info into liferay if that's user information doesn't exist before.
 */
public class CaGridLiferayCASAutoLoginHook implements AutoLogin {

	public String[] login(HttpServletRequest req, HttpServletResponse res)
		throws AutoLoginException {

		try {
			String[] credentials = null;

			long companyId = PortalUtil.getCompanyId(req);

			if (!PrefsPropsUtil.getBoolean(
					companyId, PropsUtil.CAS_AUTH_ENABLED)) {
				return credentials;
			}
			WebSSOUser webssoUser = (WebSSOUser) SecurityContextHolder
									.getContext().getAuthentication().getPrincipal();
			String screenName=webssoUser.getUsername(); 
			if (screenName != null) {
				User user = null;
				try {
					user = UserLocalServiceUtil.getUserByScreenName(companyId,
							screenName);
				} catch (NoSuchUserException nsue) {
					user = addUser(companyId, webssoUser);
				}
				credentials = new String[3];
				credentials[0] = String.valueOf(user.getUserId());
				credentials[1] = user.getPassword();
				credentials[2] = Boolean.TRUE.toString();
			}
			return credentials;
		} catch (Exception e) {
			throw new AutoLoginException(e);
		}
	}
	
	//username is unique for companyId
	protected User addUser(long companyId, WebSSOUser webssoUser)
		throws PortalException, SystemException {
		
		//autoPassword ? true ------ generate passwords
		//password1 ? dummy passwords
		//password2 ? dummy passwords
		//autoScreenName ? true
		//screenName ? -- cagrid user name - unique for each user
		//emailAddress ?  ?????????????
		//locale ?
		//firstName ?  ????????
		//middleName ?  ????????? null
		//lastName ?  ???????????
		//prefixId ?               null
		//suffixId ?               null 
		//male ?    ??????         null 
		//birthdayMonth ?  ?????   null
		//birthdayDay ?   ???????  null
		//birthdayYear ?   ??????? null
		//jobTitle ?
		//organizationId ?
		//locationId ?
		//sendEmail ?			  true	

		User user=null;
		try {
		 user=UserServiceUtil.addUser(companyId, true, null, null,
					false, webssoUser.getGridId(), webssoUser.getEmailId(), null, webssoUser.getFirstName(),
					null, webssoUser.getLastName(), 0,0,true,1,
					1, 1970, null, 1,1, false);
			
		} catch (RemoteException e) {
			throw new SystemException(e);
		}
		return user;
	}
	private static Log _log = LogFactory.getLog(CaGridLiferayCASAutoLoginHook.class);

}