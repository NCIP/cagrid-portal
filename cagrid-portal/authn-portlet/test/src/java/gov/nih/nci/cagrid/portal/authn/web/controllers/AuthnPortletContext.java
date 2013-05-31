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
package gov.nih.nci.cagrid.portal.authn.web.controllers;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class AuthnPortletContext extends TestPortletContextClassBase {
    public String[] getConfigLocations() {
        return new String[]{"classpath*:applicationContext-authn-beans.xml",
                "classpath*:cagrid-authn-portlet.xml"
               };
    }

}

