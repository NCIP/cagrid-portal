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
package gov.nih.nci.cagrid.portal.liferay.security;

import com.liferay.portal.security.auth.AutoLogin;
import com.liferay.portal.security.auth.AutoLoginException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * /**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class AutoLoginLoader implements AutoLogin {

    private AbstractAutoLogin instance;

    /**
     *
     */
    public AutoLoginLoader() {
        try {
            ApplicationContext ctx = new ClassPathXmlApplicationContext(
                    new String[]{
                            "classpath:applicationContext-liferay.xml"
                    });

            this.instance = (AbstractAutoLogin) ctx.getBean("autoLogin");
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
