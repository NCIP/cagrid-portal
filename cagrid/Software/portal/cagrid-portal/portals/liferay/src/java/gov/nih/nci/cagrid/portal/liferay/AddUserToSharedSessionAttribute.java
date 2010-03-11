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
	 * @seecom.liferay.portal.kernel.events.Action#run(javax.servlet.http.
	 * HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void run(HttpServletRequest request, HttpServletResponse response)
			throws ActionException {
		try {
			User user = PortalUtil.getUser(request);
			if (user != null) {
				String userId = user.getCompanyId() + ":" + user.getUserId();
				request.getSession().setAttribute(
						SHARED_SESSION_ATTRIBUTE_NAME, userId);
			}
		} catch (Exception ex) {
			String msg = "Error getting user: " + ex.getMessage();
			logger.error(msg, ex);
			throw new ActionException(msg, ex);
		}
	}

}
