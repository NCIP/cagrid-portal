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
package gov.nih.nci.cagrid.portal.authn.service;

import gov.nih.nci.cagrid.portal.authn.domain.IdPBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.gaards.authentication.client.AuthenticationClient;
import org.cagrid.gaards.dorian.client.GridUserClient;
import org.cagrid.gaards.dorian.federation.TrustedIdentityProvider;
import org.globus.wsrf.impl.security.authorization.IdentityAuthorization;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class IdPService {
    private static final String BASIC_AUTH_PROFILE = "{http://gaards.cagrid.org/authentication}BasicAuthentication";

    private Log logger = LogFactory.getLog(IdPService.class);

    public List<IdPBean> listIdPsFromDorian(String ifsUrl) {

           List<IdPBean> idpBeans = new ArrayList<IdPBean>();

           try {
               GridUserClient guc = new GridUserClient(ifsUrl);
               List<TrustedIdentityProvider> idps = guc
                       .getTrustedIdentityProviders();
               for (TrustedIdentityProvider idp : idps) {
                   String idpUrl = idp.getAuthenticationServiceURL();

                   try {
                       AuthenticationClient ac = new AuthenticationClient(idpUrl);
                       String idpIdent = idp.getAuthenticationServiceIdentity();
                       if (idpIdent != null && idpIdent.trim().length() > 0) {
                           ac
                                   .setAuthorization(new IdentityAuthorization(
                                           idpIdent));
                       }
                       try {
                           for (QName profileQName : ac
                                   .getSupportedAuthenticationProfiles()) {
                               if (profileQName.toString().trim().equals(BASIC_AUTH_PROFILE)) {
                                   IdPBean bean = new IdPBean(
                                           idp.getDisplayName(), idpUrl);
                                   idpBeans.add(bean);
                               }
                           }
                       } catch (Exception ex) {
                           String msg = "Couldn't get profiles for: " + idpUrl
                                   + "\n" + ex.getMessage();
                           logger.info(msg, ex);
                       }
                   } catch (Exception ex) {
                       String msg = "Error connecting to " + idpUrl + ": "
                               + ex.getMessage();
                       logger.info(msg, ex);
                   }
               }
           } catch (Exception ex) {
               logger.error("Error getting identity provider list: "
                       + ex.getMessage(), ex);
               throw new RuntimeException("Error getting identity provider list.",
                       ex);
           }

           return idpBeans;
       }

}
