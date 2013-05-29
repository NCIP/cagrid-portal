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
package gov.nih.nci.cagrid.portal.security;

import gov.nih.nci.cagrid.portal.authn.ProxyUtil;
import gov.nih.nci.cagrid.portal.authn.AuthnServiceException;
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
