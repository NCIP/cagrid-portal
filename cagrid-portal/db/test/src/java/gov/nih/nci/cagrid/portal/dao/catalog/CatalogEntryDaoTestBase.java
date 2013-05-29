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

import gov.nih.nci.cagrid.portal.DaoTestBase;
import gov.nih.nci.cagrid.portal.dao.PersonDao;
import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.domain.Person;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.util.StaticTimestampProvider;
import org.junit.Before;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CatalogEntryDaoTestBase<T extends AboutCatalogEntryDao> extends DaoTestBase<T> {
    PortalUserDao pDao;
    PersonDao personDao;
    PortalUser p;

    @Override
    public T getDao() {
        T dao = super.getDao();    //To change body of overridden methods use File | Settings | File Templates.
        dao.setTimestampProvider(new StaticTimestampProvider());
        return dao;
    }

    @Before
    public void setup() {
        pDao = (PortalUserDao) getApplicationContext().getBean("portalUserDao");
        personDao = (PersonDao) getApplicationContext().getBean("personDao");

        p = new PortalUser();

        Person person = new Person();
        person.setFirstName("first");
        person.setLastName("last");
        person.setEmailAddress("email");
        personDao.save(person);
        p.setPerson(person);
        pDao.save(p);
    }
}
