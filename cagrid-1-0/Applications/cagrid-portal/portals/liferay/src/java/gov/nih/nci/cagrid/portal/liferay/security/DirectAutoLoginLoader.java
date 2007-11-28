/**
 * 
 */
package gov.nih.nci.cagrid.portal.liferay.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.liferay.portal.security.auth.AutoLogin;
import com.liferay.portal.security.auth.AutoLoginException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class DirectAutoLoginLoader implements AutoLogin {

	private DirectAutoLogin instance;
	
	/**
	 * 
	 */
	public DirectAutoLoginLoader() {
		try {
			ApplicationContext ctx = new ClassPathXmlApplicationContext(
					new String[] { "classpath:applicationContext-db.xml",
							"classpath:applicationContext-liferay.xml",
							"classpath:applicationContext-security.xml" });
			this.instance = (DirectAutoLogin) ctx.getBean("directAutoLogin");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException("Error loading application context: "
					+ ex.getMessage(), ex);
		}
	}

	/* (non-Javadoc)
	 * @see com.liferay.portal.security.auth.AutoLogin#login(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public String[] login(HttpServletRequest request, HttpServletResponse response)
			throws AutoLoginException {
		return instance.login(request, response);
	}

}
