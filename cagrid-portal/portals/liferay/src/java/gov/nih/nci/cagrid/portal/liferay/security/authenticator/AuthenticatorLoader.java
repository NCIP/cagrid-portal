package gov.nih.nci.cagrid.portal.liferay.security.authenticator;

import com.liferay.portal.security.auth.AuthException;
import com.liferay.portal.security.auth.Authenticator;
import gov.nih.nci.cagrid.portal.liferay.security.util.LiferayLoginHelper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;


/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class AuthenticatorLoader extends LiferayLoginHelper implements Authenticator {

    private Authenticator instance;


    public AuthenticatorLoader() {
        try {
            ApplicationContext ctx = new ClassPathXmlApplicationContext(
                    new String[]{
                            "classpath:applicationContext-liferay.xml"});

            this.instance = (Authenticator) ctx.getBean("authenticator");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error loading application context: "
                    + ex.getMessage(), ex);
        }


    }

    public int authenticateByEmailAddress(long l, String s, String s1, Map<String, String[]> stringMap, Map<String, String[]> stringMap1) throws AuthException {
        return instance.authenticateByEmailAddress(l, s, s1, stringMap, stringMap1);
    }

    public int authenticateByScreenName(long l, String s, String s1, Map<String, String[]> stringMap, Map<String, String[]> stringMap1) throws AuthException {
        return instance.authenticateByScreenName(l, s, s1, stringMap, stringMap1);
    }

    public int authenticateByUserId(long l, long l1, String s, Map<String, String[]> stringMap, Map<String, String[]> stringMap1) throws AuthException {
        return instance.authenticateByUserId(l, l1, s, stringMap, stringMap1);
    }
}
