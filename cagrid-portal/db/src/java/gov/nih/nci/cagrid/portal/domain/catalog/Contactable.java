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
package gov.nih.nci.cagrid.portal.domain.catalog;

import java.net.URL;

public interface Contactable extends Geolocatable {

    public String getEmailAddress();

    public URL getWebSite();

    public boolean isEmailAddressPublic();

    public String getPhoneNumber();

    public boolean isPhoneNumberPublic();

    public boolean isAddressPublic();
}