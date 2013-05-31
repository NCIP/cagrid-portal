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
package gov.nih.nci.cagrid.portal.aggr.status;

import gov.nih.nci.cagrid.portal.aggr.ServiceUrlProvider;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;

import java.util.HashSet;
import java.util.Set;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class IdxServiceStatusProviderTest extends AbstractServiceSatusTest {


    public void testgetStatus() {

        assertEquals("Service is not in index. Should be inactive",
                ServiceStatus.INACTIVE, idxProvider.getStatus(validSvcNotInIndex));
        assertEquals("Service is in index. Should be active",
                ServiceStatus.ACTIVE, idxProvider.getStatus(svcInIndex1));

        idxProvider.setStrictIndexVerification(false);
        assertEquals("Index verification is turned off. Should be Active",
                ServiceStatus.ACTIVE, idxProvider.getStatus(validSvcNotInIndex));

        idxProvider.setStrictIndexVerification(true);

        ServiceUrlProvider dynProv2 = new ServiceUrlProvider() {
            public Set<String> getUrls(String indexServiceUrl) {
                Set<String> urls = new HashSet<String>();
                urls.add(validSvcNotInIndex);
                return urls;
            }
        };

        idxProvider.setDynamicServiceUrlProvider(dynProv2);
        assertEquals("Service was added to index. Should be active",
                ServiceStatus.ACTIVE, idxProvider.getStatus(validSvcNotInIndex));


    }
}
