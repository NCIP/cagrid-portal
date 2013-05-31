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
package gov.nih.nci.cagrid.portal.aggr.metachange;

import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.portal.TestDB;
import gov.nih.nci.cagrid.portal.dao.CQLQueryInstanceDao;
import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.dao.ServiceMetadataDao;
import gov.nih.nci.cagrid.portal.dao.SharedCQLQueryDao;
import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.portal.util.Metadata;
import static junit.framework.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.io.FileReader;


/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
 */

public class MetadataChangeTransactionTest extends AbstractMetadataChangeTestBase {

    @Test
    public void testTransaction() throws Exception {

        String serviceUrl = "http://service.url";
        GridDataService dataService = new GridDataService();
        dataService
                .setUrl(serviceUrl);
        getGridServiceDao().save(dataService);
        Metadata meta = new Metadata();

        try {
            meta.dmodel = MetadataUtils.deserializeDomainModel(new FileReader(
                    "test/data/cabioModelSnippet.xml"));
            meta.smeta = MetadataUtils
                    .deserializeServiceMetadata(new FileReader(
                            "test/data/cabioServiceMetadata.xml"));
        } catch (Exception ex) {
            fail("Error deserializing test data: " + ex.getMessage());
            ex.printStackTrace();
        }
        try {
            getMetadataListener().loadMetadata(dataService, meta);
        } catch (Exception ex) {
            fail("Error loading metadata: " + ex.getMessage());
            ex.printStackTrace();
        }
        GridServiceDao testDao = new TestGridServiceDao();

        testDao.setHibernateTemplate(getGridServiceDao().getHibernateTemplate());
        testDao.setSessionFactory(getGridServiceDao().getHibernateTemplate().getSessionFactory());
        GridServiceDao _mockDao = mock(GridServiceDao.class);

        try {
            doThrow(new RuntimeException()).when(_mockDao).save(new GridService());
            doReturn(getGridServiceDao().getByUrl(serviceUrl)).when(_mockDao).getByUrl(anyString());

            getChangeListener().setGridServiceDao(testDao);
            getChangeListener().updateServiceMetadata(serviceUrl, meta);
            fail("Transaction should have failed");
        } catch (RuntimeException e) {
            ServiceMetadataDao serviceMetadataDao = (ServiceMetadataDao) TestDB
                    .getApplicationContext().getBean("serviceMetadataDao");
            ServiceMetadata loadedMetadata = serviceMetadataDao.getById(1);
            assertNull("Service metadata was not deleted", loadedMetadata.getService());

            try {
                //reattach and save
                loadedMetadata.setService(dataService);
                serviceMetadataDao.save(loadedMetadata);

                MetadataChangeListener springProvidedListener = (MetadataChangeListener) getMetadataListener().getApplicationContext().getBean("metadataChangeListener");

                springProvidedListener.setGridServiceDao(testDao);

                springProvidedListener.updateServiceMetadata(serviceUrl, meta);
                fail("Transaction should have been interrupted");
            } catch (RuntimeException e1) {
                assertNotSame("NPE is unexpected", e1.getClass(), NullPointerException.class);
                assertNotNull("Metadata was not persisted", serviceMetadataDao.getById(1));
                assertNotNull("Service metadata was deleted. Transaction was not applied" + e1.getMessage(), serviceMetadataDao.getById(1).getService());
            }

        } catch (Exception ex) {
            fail("Error updating metadata: " + ex.getMessage());
            ex.printStackTrace();
        }

    }

    class TestGridServiceDao extends GridServiceDao {
        @Override
        public void save(GridService domainObject) {
            throw new RuntimeException();
        }
    }

    protected void verify(GridDataService dataService,
                          SharedCQLQueryDao sharedCqlQueryDao,
                          CQLQueryInstanceDao cqlQueryInstanceDao, String targetClassName) {

        verify(dataService, sharedCqlQueryDao, cqlQueryInstanceDao, targetClassName);
    }


}
