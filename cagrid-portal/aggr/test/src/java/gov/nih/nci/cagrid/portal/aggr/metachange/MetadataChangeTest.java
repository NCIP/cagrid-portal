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
package gov.nih.nci.cagrid.portal.aggr.metachange;

import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQuery;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal.domain.dataservice.SharedCQLQuery;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.util.Metadata;
import gov.nih.nci.cagrid.portal.util.PortalUtils;
import static junit.framework.Assert.fail;
import org.junit.Test;

import java.io.FileReader;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class MetadataChangeTest extends AbstractMetadataChangeTestBase {

    @Test
    public void metadataChange() {

        GridDataService dataService = new GridDataService();
        dataService
                .setUrl("http://cabio-gridservice.nci.nih.gov:80/wsrf-cabio/services/cagrid/CaBIOSvc");
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

        // Now associate some CQLQueryInstance and SharedCQLQuery objects
        String cql = null;
        try {
            cql = loadCQL("test/data/cabioMouseQuery.xml");
        } catch (Exception ex) {
            fail("Error loading test query: " + ex.getMessage());
            ex.printStackTrace();
        }

        CQLQuery query = new CQLQuery();
        query.setXml(cql);
        query.setHash(PortalUtils.createHash(cql));
        getCqlQueryDao().save(query);
        CQLQueryInstance instance = new CQLQueryInstance();
        instance.setQuery(query);
        instance.setDataService(dataService);
        getCqlQueryInstanceDao().save(instance);

        String targetClassName = "gov.nih.nci.cabio.domain.Gene";
        UMLClass targetClass = null;
        for (UMLClass klass : dataService.getDomainModel().getClasses()) {
            String className = klass.getPackageName() + "."
                    + klass.getClassName();
            if (className.equals(targetClassName)) {
                targetClass = klass;
                break;
            }
        }
        SharedCQLQuery sharedCqlQuery = new SharedCQLQuery();
        sharedCqlQuery.setTargetClass(targetClass);
        sharedCqlQuery.setTargetService(dataService);
        getSharedCqlQueryDao().save(sharedCqlQuery);

        verify(dataService, getSharedCqlQueryDao(), getCqlQueryInstanceDao(),
                targetClassName);

        try {
            getChangeListener().updateServiceMetadata(dataService.getUrl(), meta);
        } catch (Exception ex) {
            fail("Error updating metadata: " + ex.getMessage());
            ex.printStackTrace();
        }

        verify(dataService, getSharedCqlQueryDao(), getCqlQueryInstanceDao(),
                targetClassName);

    }

}
