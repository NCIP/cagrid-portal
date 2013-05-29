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
package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.domain.Address;
import gov.nih.nci.cagrid.portal.domain.Participant;
import gov.nih.nci.cagrid.portal.domain.catalog.InstitutionCatalogEntry;
import gov.nih.nci.cagrid.portal.annotation.UpdatesCatalogs;

import java.net.MalformedURLException;
import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class InstitutionCatalogEntryDao extends AboutCatalogEntryDao<InstitutionCatalogEntry, Participant> {

    public InstitutionCatalogEntryDao() {
    }

    /* (non-Javadoc)
    * @see gov.nih.nci.cagrid.portal.dao.AbstractDao#domainClass()
    */
    @Override
    public Class domainClass() {
        return InstitutionCatalogEntry.class;
    }

    @UpdatesCatalogs
    public InstitutionCatalogEntry createCatalogAbout(Participant participant) {
        InstitutionCatalogEntry entry = isAbout(participant);
        if (entry == null) {
            entry = new InstitutionCatalogEntry();
            entry.setAbout(participant);
            participant.setCatalog(entry);
        } else
            logger.debug("Catalog entry already exists. Will update the existing one");
        if (!entry.isPublished()) {
            logger.debug("Catalog not published. Will sync with domain object");
            entry.setName(participant.getName());
            entry.setEmailAddress(participant.getEmailAddress());
            try {
                entry.setWebSite(participant.getHomepageUrl());
            } catch (MalformedURLException e) {
                logger.warn("Cannot create URL for institution catalog entry");
            }
            entry.setDescription(participant.getInstitution());
            Address address = participant.getAddress();
            if (address != null) {
                entry.setCountryCode(address.getCountry());
                entry.setPostalCode(address.getPostalCode());
                entry.setStreet1(address.getStreet1());
                entry.setStreet2(address.getStreet2());
                entry.setLatitude(address.getLatitude());
                entry.setLongitude(address.getLongitude());
                entry.setStateProvince(address.getStateProvince());
                entry.setLocality(address.getLocality());
            }

        }

        save(entry);
        return entry;
    }

	public List<InstitutionCatalogEntry> searchByName(String name) {
		return getHibernateTemplate().find("from InstitutionCatalogEntry where name = ?", name);
	}


}