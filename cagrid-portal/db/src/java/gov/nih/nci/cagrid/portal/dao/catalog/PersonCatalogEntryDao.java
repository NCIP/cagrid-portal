package gov.nih.nci.cagrid.portal.dao.catalog;

import java.util.List;

import gov.nih.nci.cagrid.portal.domain.Address;
import gov.nih.nci.cagrid.portal.domain.Person;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.catalog.PersonCatalogEntry;
import gov.nih.nci.cagrid.portal.util.BeanUtils;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class PersonCatalogEntryDao extends
        AboutCatalogEntryDao<PersonCatalogEntry, PortalUser> {

    public PersonCatalogEntryDao() {
    }

    /*
      * (non-Javadoc)
      *
      * @see gov.nih.nci.cagrid.portal.dao.AbstractDao#domainClass()
      */
    @Override
    public Class domainClass() {
        return PersonCatalogEntry.class;
    }

    public PersonCatalogEntry createCatalogAbout(PortalUser user) {
        user = (PortalUser) getSession().load(PortalUser.class, new Integer(user.getId()));
        PersonCatalogEntry entry = isAbout(user);
        if (entry == null) {
            entry = new PersonCatalogEntry();
            entry.setAbout(user);
            user.setCatalog(entry);
            entry.setCreatedAt(getTimestampProvider().getTimestamp());
            entry.setDescription("Portal User");
            
        } else
            logger
                    .debug("Catalog entry already exists. Will update the existing entry");

        if (!entry.isPublished()) {
            logger
                    .debug("Catalog entry has not been published. Will sync with domain object");


            entry.setEmailAddress(BeanUtils.traverse(user,
                    "person.emailAddress"));
            entry.setLastName(BeanUtils.traverse(user, "person.lastName"));
            entry.setFirstName(BeanUtils.traverse(user, "person.firstName"));
            entry.setName(entry.getFirstName() + " " + entry.getLastName());
            entry.setPhoneNumber(BeanUtils.traverse(user, "person.phoneNumber"));

            Person p = user.getPerson();
            if (p != null) {
                if (p.getAddresses() != null && p.getAddresses().size() > 0) {
                    Address address = user.getPerson().getAddresses().get(0);
                    entry.setCountryCode(address.getCountry());
                    entry.setPostalCode(address.getPostalCode());
                    entry.setStreet1(address.getStreet1());
                    entry.setStreet2(address.getStreet2());
                    entry.setLatitude(address.getLatitude());
                    entry.setLongitude(address.getLongitude());
                    entry.setStateProvince(address.getStateProvince());
                }
            }
        }
        save(entry);
        return entry;
    }

    public List<PersonCatalogEntry> searchByNameAndEmail(String firstName,
                                                         String lastName, String emailAddress) {
        List<PersonCatalogEntry> l = getHibernateTemplate()
                .find(
                        "from PersonCatalogEntry where firstName = ? and lastName = ? and emailAddress = ?",
                        new Object[]{firstName, lastName, emailAddress});
        return l;
    }

}