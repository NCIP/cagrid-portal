/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portal.jetspeed.security;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jetspeed.pipeline.PipelineException;
import org.apache.jetspeed.pipeline.valve.ValveContext;
import org.apache.jetspeed.request.RequestContext;
import org.apache.jetspeed.security.SecurityHelper;
import org.apache.jetspeed.security.UserPrincipal;
import org.apache.jetspeed.security.impl.AbstractSecurityValve;
import org.apache.jetspeed.security.impl.RolePrincipalImpl;
import org.apache.jetspeed.security.impl.UserPrincipalImpl;
import org.globus.gsi.GlobusCredential;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class GridSecurityValve extends AbstractSecurityValve {

	private static final Log logger = LogFactory
			.getLog(GridSecurityValve.class);
	
	private String loginApplicationUrl;

	public String getLoginApplicationUrl() {
		return loginApplicationUrl;
	}

	public void setLoginApplicationUrl(String loginApplicationUrl) {
		this.loginApplicationUrl = loginApplicationUrl;
	}

	public GridSecurityValve() {
	}

	public void invoke(RequestContext requestCtx, ValveContext valveCtx)
			throws PipelineException {

		logger.info("# invoking custom security valve #");

		try {
			HttpServletRequest request = requestCtx.getRequest();
			HttpServletResponse response = requestCtx.getResponse();

			Subject subject = getSubjectFromSession(requestCtx);
			if (subject != null) {

				GlobusCredential cred = getGlobusCredentialFromSession(requestCtx);
				if (cred != null && !isValid(cred)) {
					// Then the user's GlobusCredential is no longer valid.
					// So, we need to redirect to login application so that
					// a new SAML assertion can be generated and used to
					// retreive fresh credentials. NOTE: this will not cause
					// the user to have to log in again.
					doLogin(request, response);
				} else {

					// Then the user has already logged in. Just continue
					// processing as normal.
					super.invoke(requestCtx, valveCtx);
				}

			} else {

				// Then the user has either not yet logged in, or
				// is being redirected from the login application.

				// Check for login key in request to see if user
				// is being redirected from login application.
				String loginKey = getLoginKey(request);
				if (loginKey != null) {

					// Then the user is being redirected. The subject
					// will be constructed from the login key by the
					// call to getSubject
					super.invoke(requestCtx, valveCtx);

				} else {
					// This means the user has yet provided credentials to
					// the login application. So, we need to redirect.
					doLogin(requestCtx.getRequest(), requestCtx.getResponse());
				}

			}
		} catch (PipelineException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new PipelineException(
					"Error encountered: " + ex.getMessage(), ex);
		}

	}

	private Subject buildSubjectFromLoginKey(String loginKey) {
		logger.debug("Building new subject from login key: " + loginKey);
		
		Set<Principal> principals = new HashSet<Principal>();
		principals.add(new UserPrincipalImpl("admin"));
		principals.add(new RolePrincipalImpl("admin"));
		Subject subject = new Subject(true, principals, Collections.EMPTY_SET,
				Collections.EMPTY_SET);
		return subject;
	}

	private String getLoginKey(HttpServletRequest request) {
		return request.getParameter("cagrid.login.key");
	}

	private void doLogin(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String loginAppUrl = getLoginApplicationUrl();
            logger.info("Redirecting client at " + request.getRemoteAddr()
                + " to login application: " + loginAppUrl);
            response.sendRedirect(loginAppUrl + "?url="
                + request.getRequestURL());
        }catch (IOException e){

            try{
                response.sendError(500, "Unable to redirect WebKey server:  "
                    + e.toString());
            }catch (IOException e1){
                throw new PipelineException(
                    "Unable to send login error to the client: "
                        + e1.toString());
            }
        }
	}

	private boolean isValid(GlobusCredential cred) {
		// TODO Auto-generated method stub
		return false;
	}

	private GlobusCredential getGlobusCredentialFromSession(
			RequestContext request) {
		// TODO Auto-generated method stub
		return null;
	}

	protected Subject getSubject(RequestContext requestCtx) throws Exception {

		Subject subject = getSubjectFromSession(requestCtx);
		if (subject == null) {

			String loginKey = getLoginKey(requestCtx.getRequest());
			if (loginKey == null) {
				// This shouldn't happen
				throw new IllegalStateException(
						"Subject is null and there is no login key");
			}
			subject = buildSubjectFromLoginKey(loginKey);

		}
		return subject;
	}

	@Override
	protected Principal getUserPrincipal(RequestContext request)
			throws Exception {
		return SecurityHelper.getBestPrincipal(getSubject(request),
				UserPrincipal.class);
	}
}
