package gov.nih.nci.cagrid.portal.liferay.websso;

import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.io.FileUtils;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class WebSSOUserDetailsServiceTest {


    @Test
    public void loadUserByUsername() throws Exception {
        WebSSOUserDetailsService service = new WebSSOUserDetailsService();
        try {
            service.loadUserByUsername(null);
            fail("Should not be able to parse null token");
        } catch (Exception e) {
            //expected
        }

        Resource resource = new FileSystemResource("liferay/test/data/webSSOToken.xml");
        String token = FileUtils.readFileToString(resource.getFile());

        UserDetails casUserId = service.loadUserByUsername(token);
        assertNotNull(casUserId);
        assertTrue(casUserId.getAuthorities().length > 0);
        assertNotNull(casUserId.getUsername());
    }


}

