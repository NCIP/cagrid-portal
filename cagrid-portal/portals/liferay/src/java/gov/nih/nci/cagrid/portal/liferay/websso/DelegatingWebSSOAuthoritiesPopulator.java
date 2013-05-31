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

import org.acegisecurity.AuthenticationException;
import org.acegisecurity.providers.cas.CasAuthoritiesPopulator;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.springframework.beans.factory.annotation.Required;

/**
 * Class just calls the supplied user details service to load the user and authorities
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class DelegatingWebSSOAuthoritiesPopulator implements CasAuthoritiesPopulator {

    private UserDetailsService userDetailsService;

    /**
     * Obtains the granted authorities for the specified user.
          * <P>
          * May throw any <code>AuthenticationException</code> or return <code>null</code> if the
          * authorities are unavailable.
          * </p>
          */
         public UserDetails getUserDetails(String casUserId) throws AuthenticationException {
             return userDetailsService.loadUserByUsername(casUserId);
         }

    public UserDetailsService getUserDetailsService() {
             return userDetailsService;
         }

    @Required
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


}

