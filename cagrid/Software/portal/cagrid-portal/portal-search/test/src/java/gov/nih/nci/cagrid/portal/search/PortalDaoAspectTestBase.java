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
