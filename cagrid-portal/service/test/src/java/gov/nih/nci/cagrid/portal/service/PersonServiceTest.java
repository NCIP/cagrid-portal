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
/**
 *
 */
package gov.nih.nci.cagrid.portal.service;

import com.liferay.portal.model.User;
import com.liferay.portal.service.RoleServiceUtil;
import com.liferay.portal.util.TestPropsValues;
import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.dao.catalog.PersonCatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.catalog.PersonCatalogEntry;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class PersonServiceTest extends DuelDBServiceTestBase {


    @Test
    public void testDeletePerson() {

        ApplicationContext ctx = getServiceApplicationContext();
        PortalUserDao portalUserDao = (PortalUserDao) ctx.getBean("portalUserDao");
        PersonCatalogEntryDao personCatalogEntryDao = (PersonCatalogEntryDao) ctx.getBean("personCatalogEntryDao");
        PersonService personService = (PersonService) ctx.getBean("personService");

        // Create the user
        User adminUser = null;
        try {
            adminUser = addUser();
        } catch (Exception ex) {
            fail("Error creating admin user: " + ex.getMessage());
        }
        try {
            RoleServiceUtil.addUserRoles(adminUser.getUserId(),
                    new long[]{getCatalogAdminRole().getRoleId()});
        } catch (Exception ex) {
            fail("Error adding Catalog Admin role to user: " + ex.getMessage());
        }
        PortalUser portalAdminUser = new PortalUser();
        portalAdminUser.setPortalId(TestPropsValues.COMPANY_ID + ":" + adminUser.getUserId());


        User user = null;
        try {
            user = addUser();
        } catch (Exception ex) {
            fail("Error creating user: " + ex.getMessage());
        }
        PortalUser portalUser = new PortalUser();
        portalUser.setPortalId(TestPropsValues.COMPANY_ID + ":" + user.getUserId());
        portalUserDao.save(portalUser);
        PersonCatalogEntry personCe = new PersonCatalogEntry();
        personCe.setAbout(portalUser);
        personCatalogEntryDao.save(personCe);

        //TODO: This is still failing because of the permissions.
        try {
            personService.deletePerson(portalAdminUser, personCe);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error deleting person: " + ex.getMessage());
        }

    }
}
