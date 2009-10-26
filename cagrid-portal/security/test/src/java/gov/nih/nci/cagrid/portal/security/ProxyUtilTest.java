package gov.nih.nci.cagrid.portal.security;

import gov.nih.nci.cagrid.portal.authn.ProxyUtil;
import org.globus.gsi.GlobusCredential;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class ProxyUtilTest {

    @Test
    public void getProxyString() {
        try {
            ProxyUtil.getProxyString(null);
            fail("Should have thrown an exception");
        } catch (AuthnServiceException e) {
            //expected
        }

        try {
            InputStream is = new FileInputStream("test/data/proxy.pem");
            GlobusCredential cred = new GlobusCredential(is);
            assertNotNull(ProxyUtil.getProxyString(cred));
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }
}
