/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.liferay;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 * 
 */
public class AddUserToSharedSessionAttribute extends Action {

	private static final Log logger = LogFactory
			.getLog(AddUserToSharedSessionAttribute.class);

	public static final String SHARED_SESSION_ATTRIBUTE_NAME = "CAGRIDPORTAL_ATTS_liferayUserId";

	/**
	 * 
	 */
	public AddUserToSharedSessionAttribute() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.liferay.portal.kernel.events.Action#run(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void run(HttpServletRequest request, HttpServletResponse response)
			throws ActionException {
		System.out.println("#########################################");
		System.out.println("#########################################");
		System.out.println("#########################################");
		try {
			User user = PortalUtil.getUser(request);
			if (user != null) {
				String userId = user.getCompanyId() + ":" + user.getUserId();
				System.out.println("Adding " + SHARED_SESSION_ATTRIBUTE_NAME + "="
				+ userId + " to session.");
				logger.debug("Adding " + SHARED_SESSION_ATTRIBUTE_NAME + "="
						+ userId + " to session.");
				request.getSession().setAttribute(
						SHARED_SESSION_ATTRIBUTE_NAME, userId);
			} else {
				System.out.println("No user found.");
				logger.debug("No user found.");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ActionException("Error getting user: " + ex.getMessage(),
					ex);
		}
	}

}
