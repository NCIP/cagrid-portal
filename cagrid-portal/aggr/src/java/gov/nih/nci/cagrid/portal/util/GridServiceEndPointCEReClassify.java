package gov.nih.nci.cagrid.portal.util;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.dao.catalog.GridServiceEndPointCatalogEntryDao;
import gov.nih.nci.cagrid.portal.dao.catalog.CatalogEntryRelationshipInstanceDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.catalog.GridServiceEndPointCatalogEntry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
@Transactional
public class GridServiceEndPointCEReClassify implements InitializingBean {
    private static Log log = LogFactory.getLog(GridServiceEndPointCEReClassify.class);
    private GridServiceDao gDao;
    private GridServiceEndPointCatalogEntryDao ceDao;
    private CatalogEntryRelationshipInstanceDao catalogEntryRelationshipInstanceDao;

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(
                new String[]{"applicationContext-aggr-utils.xml"});
    }

    public void afterPropertiesSet() throws Exception {

        for (GridServiceEndPointCatalogEntry entry : ceDao.getAll()) {
            if (entry.isData()) {
                log.debug("Service with id " + entry.getId() + " is a Data Service. Will re-classify");
                GridService service = entry.getAbout();

                DeleteCatalogs.deleteRelationships(ceDao.getHibernateTemplate(),getCatalogEntryRelationshipInstanceDao(),
                entry);
                
                ceDao.delete(entry);
                // resave so that correct CE is created
                service.setCatalog(null);
                gDao.save(service);


            }
        }


    }

    public CatalogEntryRelationshipInstanceDao getCatalogEntryRelationshipInstanceDao() {
        return catalogEntryRelationshipInstanceDao;
    }

    public void setCatalogEntryRelationshipInstanceDao(CatalogEntryRelationshipInstanceDao catalogEntryRelationshipInstanceDao) {
        this.catalogEntryRelationshipInstanceDao = catalogEntryRelationshipInstanceDao;
    }

    public GridServiceDao getGDao() {
        return gDao;
    }

    public void setGDao(GridServiceDao gDao) {
        this.gDao = gDao;
    }

    public GridServiceEndPointCatalogEntryDao getCeDao() {
        return ceDao;
    }

    public void setCeDao(GridServiceEndPointCatalogEntryDao ceDao) {
        this.ceDao = ceDao;
    }
}
