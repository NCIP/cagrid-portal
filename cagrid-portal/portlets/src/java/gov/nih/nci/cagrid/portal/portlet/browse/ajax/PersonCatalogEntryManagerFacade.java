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
/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.browse.ajax;

import gov.nih.nci.cagrid.portal.domain.catalog.PersonCatalogEntry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 */
public class PersonCatalogEntryManagerFacade extends CatalogEntryManagerFacade {

    private static final Log logger = LogFactory
            .getLog(PersonCatalogEntryManagerFacade.class);

    /**
     *
     */
    public PersonCatalogEntryManagerFacade() {

    }

    public String setPhoneNumber(String phoneNumber) {
        String message = null;
        ((PersonCatalogEntry) getUserModel().getCurrentCatalogEntry())
                .setPhoneNumber(phoneNumber);
        return message;
    }

    public String setPhoneNumberPublic(boolean phoneNumberPublic) {
        String message = null;
        ((PersonCatalogEntry) getUserModel().getCurrentCatalogEntry())
                .setPhoneNumberPublic(phoneNumberPublic);
        return message;
    }

    public String setEmailAddress(String emailAddress) {
        String message = null;
        ((PersonCatalogEntry) getUserModel().getCurrentCatalogEntry())
                .setEmailAddress(emailAddress);
        return message;
    }

    public String setEmailAddressPublic(boolean emailAddressPublic) {
        String message = null;
        ((PersonCatalogEntry) getUserModel().getCurrentCatalogEntry())
                .setEmailAddressPublic(emailAddressPublic);
        return message;
    }

    public String setAddressPublic(boolean addressPublic) {
        String message = null;
        ((PersonCatalogEntry) getUserModel().getCurrentCatalogEntry())
                .setAddressPublic(addressPublic);
        return message;
    }

    public String setStreet1(String street1) {
        String message = null;
        ((PersonCatalogEntry) getUserModel().getCurrentCatalogEntry())
                .setStreet1(street1);
        return message;
    }

    public String setStreet2(String street2) {
        String message = null;
        ((PersonCatalogEntry) getUserModel().getCurrentCatalogEntry())
                .setStreet2(street2);
        return message;
    }

    public String setCountryCode(String countryCode) {
        String message = null;
        ((PersonCatalogEntry) getUserModel().getCurrentCatalogEntry())
                .setCountryCode(countryCode);
        return message;
    }

    public String setStateProvince(String stateProvince) {
        String message = null;
        ((PersonCatalogEntry) getUserModel().getCurrentCatalogEntry())
                .setStateProvince(stateProvince);
        return message;
    }

    public String setLocality(String locality) {
        String message = null;
        ((PersonCatalogEntry) getUserModel().getCurrentCatalogEntry())
                .setLocality(locality);
        return message;
    }

    public String setWebSite(String webSite) {
        String message = null;
        try {
            URL url = null;
            if (webSite != null) {
                url = new URL(webSite);
            }
            ((PersonCatalogEntry) getUserModel().getCurrentCatalogEntry())
                    .setWebSite(url);
        } catch (MalformedURLException ex) {
            message = "Invalid URL";
        }
        return message;
    }
}
