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
package gov.nih.nci.cagrid.portal.domain;

import java.io.Serializable;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class LiferayUser implements Serializable {

    public static String HTTP_SESSION_ATTR_KEY_USER_ROLE = "USER_LIFERAY_USER_ROLE";
    public static String HTTP_SESSION_ATTR_VALUE_ROLE_ADMIN = "Administrator";
    public static String HTTP_SESSION_ATTR_VALUE_ROLE_USER = "User";

    private boolean admin;

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
