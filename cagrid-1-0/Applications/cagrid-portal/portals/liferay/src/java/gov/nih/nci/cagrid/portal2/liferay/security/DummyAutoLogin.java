/**
 * 
 */
package gov.nih.nci.cagrid.portal2.liferay.security;

import gov.nih.nci.cagrid.portal2.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal2.domain.PortalUser;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.gsi.GlobusCredential;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.AutoLogin;
import com.liferay.portal.security.auth.AutoLoginException;
import com.liferay.portal.service.UserLocalServiceUtil;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class DummyAutoLogin implements AutoLogin {

	private static final Log logger = LogFactory.getLog(DummyAutoLogin.class);

	private PortalUserDao portalUserDao;

	public static final String CAGRID_PORTAL_USER = "cagrid.portal.user";

	private String proxyPath;

	/**
	 * 
	 */
	public DummyAutoLogin() {
		try{
			ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] {
					"classpath:applicationContext-db.xml", "classpath:applicationContext-liferay.xml" });
			setProxyPath((String)ctx.getBean("proxyPath"));
			setPortalUserDao((PortalUserDao)ctx.getBean("portalUserDao"));
		}catch(Exception ex){
			throw new RuntimeException("Error loading application context: " + ex.getMessage(), ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.liferay.portal.security.auth.AutoLogin#login(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	public String[] login(HttpServletRequest request,
			HttpServletResponse response) throws AutoLoginException {

		try {
			String[] credentials = null;
			if ("true".equals(request.getParameter("doAutoLogin"))) {
				logger.debug("Doing auto login.");
				
				Long companyId = Long.parseLong(request
						.getParameter("companyId"));
				Long userId = Long.parseLong(request.getParameter("userId"));
				User user = UserLocalServiceUtil.getUserById(companyId, userId);

				PortalUser portalUser = getPortalUser(user);
				request.getSession().setAttribute(CAGRID_PORTAL_USER,
						portalUser);

				credentials = new String[3];
				credentials[0] = String.valueOf(user.getUserId());
				credentials[1] = user.getPassword();
				credentials[2] = Boolean.TRUE.toString();
			}else{
				logger.debug("Not doing auto login.");
			}
			return credentials;
		} catch (Exception ex) {
			logger.error("Error doing auto login: " + ex.getMessage(), ex);
			throw new AutoLoginException(ex);
		}
	}

	private PortalUser getPortalUser(User user) {
		PortalUser portalUser = getPortalUserDao().getByPortalId(
				user.getUserId());
		if (portalUser == null) {
			logger.debug("Creating PortalUser for portalId = "
					+ user.getUserId());
			portalUser = new PortalUser();
			portalUser.setPortalId(String.valueOf(user.getUserId()));
			getPortalUserDao().save(portalUser);
		}

		InputStream proxyIn = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(getProxyPath());
		GlobusCredential proxy = null;
		try {
			proxy = new GlobusCredential(proxyIn);
		} catch (Exception ex) {
			throw new RuntimeException("Error reading proxy: "
					+ ex.getMessage(), ex);
		}
		String proxyStr = null;
		try{
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
            proxy.save(buf);
            proxyStr = buf.toString();
		}catch(Exception ex){
			throw new RuntimeException("Error writing proxy to string: " + ex.getMessage(), ex);
		}
		
		portalUser.setGridCredential(proxyStr);

		return portalUser;
	}

	public PortalUserDao getPortalUserDao() {
		return portalUserDao;
	}

	public void setPortalUserDao(PortalUserDao portalUserDao) {
		this.portalUserDao = portalUserDao;
	}

	public String getProxyPath() {
		return proxyPath;
	}

	public void setProxyPath(String proxyPath) {
		this.proxyPath = proxyPath;
	}

}
