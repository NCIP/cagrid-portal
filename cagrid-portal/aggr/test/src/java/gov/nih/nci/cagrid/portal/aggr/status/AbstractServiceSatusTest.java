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
package gov.nih.nci.cagrid.portal.aggr.status;

import gov.nih.nci.cagrid.portal.aggr.ServiceUrlProvider;
import junit.framework.TestCase;

import java.util.HashSet;
import java.util.Set;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class AbstractServiceSatusTest extends TestCase {
    final String svcInIndex1 = "http://service1";
    final String svcInIndex2 = "http://service2";
    final String svcInIndex3 = "http://service3";
    final String validSvcNotInIndex = "http://cagrid-service.nci.nih.gov:8080/wsrf/services/cagrid/CaDSRService";
    IdxServiceStatusProvider idxProvider;

    public AbstractServiceSatusTest() {
        String indexSvcUrl = "http://some.index.svc";

        idxProvider = new IdxServiceStatusProvider();
        idxProvider.setIndexServiceUrls(new String[]{
                indexSvcUrl
        }
        );

        idxProvider.setStrictIndexVerification(true);

        ServiceUrlProvider dynProv = new ServiceUrlProvider() {
            public Set<String> getUrls(String indexServiceUrl) {
                Set<String> urls = new HashSet<String>();
                urls.add(svcInIndex1);
                urls.add(svcInIndex2);
                urls.add(svcInIndex3);
                return urls;
            }
        };

        idxProvider.setDynamicServiceUrlProvider(dynProv);

    }
}
