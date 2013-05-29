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
package gov.nih.nci.cagrid.portal.liferay.websso;

import org.acegisecurity.context.SecurityContextHolder;
import org.cagrid.websso.client.acegi.WebSSOUser;
import org.cagrid.websso.common.WebSSOConstants;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Properties;

/**
 * Duplicating functionality of
 * org.cagrid.websso.client.acegi.logout.SingleSignoutHelper because
 * we don't want to connect to GAARDS due to Axis incompatiblity
 * between caGrid and Liferay
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class SingleSignoutHelper {


    private Resource casClientResource;
    private String encoding = "UTF-8";

    public SingleSignoutHelper(Resource casClientResource) {
        this.casClientResource = casClientResource;
    }

    public String getLogoutURL() {
        Properties properties = new Properties();
        try {
            properties.load(casClientResource.getInputStream());
            WebSSOUser webssoUser = (WebSSOUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String delegationEPR = URLEncoder.encode(webssoUser.getDelegatedEPR(), encoding);

            String logoutURL = properties.getProperty("cas.server.url") + "/logout";
            String logoutLandingURL = properties.getProperty("logout.landing.url");
            logoutURL = logoutURL + "?service=" + logoutLandingURL;
            logoutURL = logoutURL + "&" + WebSSOConstants.CAGRID_SSO_DELEGATION_SERVICE_EPR + "=" + delegationEPR;
            return logoutURL;
        } catch (IOException e) {
            throw new RuntimeException("error occured handling logout " + e);
        }
    }

    public Resource getCasClientResource() {
        return casClientResource;
    }

    public void setCasClientResource(Resource casClientResource) {
        this.casClientResource = casClientResource;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}


