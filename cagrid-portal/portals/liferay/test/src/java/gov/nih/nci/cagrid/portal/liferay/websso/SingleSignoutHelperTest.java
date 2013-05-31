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
package gov.nih.nci.cagrid.portal.liferay.websso;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.TestingAuthenticationToken;
import org.cagrid.websso.client.acegi.DefaultUserDetailsService;
import org.cagrid.websso.client.acegi.WebSSOUser;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class SingleSignoutHelperTest {

    @Before
    public void setup() {

        TestUserDetailsService userDetailsService = new TestUserDetailsService();
        WebSSOUser user = userDetailsService.load();
        user.setDelegatedEPR("");

        Authentication auth = new TestingAuthenticationToken(user, user.getGridId(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

    }

    @Test
    public void getLogoutUrl() {

        ClassPathResource resource = new ClassPathResource("cas-client.properties");

        SingleSignoutHelper helper = new SingleSignoutHelper(resource);
        assertNotNull(helper.getLogoutURL());
        System.out.println(helper.getLogoutURL());
    }


}

class TestUserDetailsService extends DefaultUserDetailsService {

    public WebSSOUser load() {
        return super.loadUserByGridId("gridId");
    }

}
