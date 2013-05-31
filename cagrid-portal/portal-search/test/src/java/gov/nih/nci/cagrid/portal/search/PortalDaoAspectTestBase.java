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
package gov.nih.nci.cagrid.portal.search;

import gov.nih.nci.cagrid.portal.DBIntegrationTestBase;
import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.dao.catalog.PersonCatalogEntryDao;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class PortalDaoAspectTestBase extends DBIntegrationTestBase {

    PersonCatalogEntryDao personCatalogEntryDao;
    PortalUserDao portalUserDao;

    @Override
    protected ConfigurableApplicationContext loadContext(Object o) throws Exception {
        GenericApplicationContext ctx = new GenericApplicationContext();

        XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(ctx);
        xmlReader.loadBeanDefinitions(new ClassPathResource("applicationContext-db.xml"));
        xmlReader.loadBeanDefinitions(new ClassPathResource("applicationContext-portal-search-aspects.xml"));

        ctx.registerBeanDefinition("defaultHttpClient", new RootBeanDefinition(MockHttpClient.class));
        ctx.refresh();


        return ctx;
    }
}
