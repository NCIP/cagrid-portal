package gov.nih.nci.cagrid.portal.authn;

import org.globus.gsi.GlobusCredential;

import java.io.ByteArrayOutputStream;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class ProxyUtil {

    public static String getProxyString(GlobusCredential cred) throws AuthnServiceException {
        String proxyStr = null;
        try {
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            cred.save(buf);
            proxyStr = buf.toString();
        } catch (Exception ex) {
            throw new AuthnServiceException("Error writing proxy to string: "
                    + ex.getMessage(), ex);
        }
        return proxyStr;
    }
}
