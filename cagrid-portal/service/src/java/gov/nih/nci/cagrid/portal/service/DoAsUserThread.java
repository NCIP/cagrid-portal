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
/**
 * 
 */
package gov.nih.nci.cagrid.portal.service;

import gov.nih.nci.cagrid.portal.domain.PortalUser;

import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalThreadLocal;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.UserLocalServiceUtil;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public abstract class DoAsUserThread extends Thread {

	private Exception ex;
	private long userId;
	private User user;
	private Object returnValue;
	
	public DoAsUserThread(long userId) {
		this.userId = userId;
	}
	
	public DoAsUserThread(PortalUser portalUser){
		this.userId = PortalServiceUtils.getPortalUserPortalId(portalUser);
	}

	public Exception getEx() {
		return ex;
	}
	
	public User getUser(){
		return user;
	}
	
	public Object getReturnValue(){
		return returnValue;
	}

	public void run() {
		try {
			PrincipalThreadLocal.setName(userId);

			this.user = UserLocalServiceUtil.getUserById(userId);

			PermissionChecker permissionChecker =
				PermissionCheckerFactoryUtil.create(user, true);

			PermissionThreadLocal.setPermissionChecker(permissionChecker);

			this.returnValue = doRun();
		}
		catch (Exception ex) {
			this.ex = ex;
		}
	}

	protected abstract Object doRun() throws Exception;

}
