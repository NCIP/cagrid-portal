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
package gov.nih.nci.cagrid.portal.util;

import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.portal.domain.metadata.service.Service;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class BeanUtilsTest {

    @Test
    public void traverse() {
        GridService service = new GridService();
        ServiceMetadata meta = new ServiceMetadata();
        meta.setId(1);

        Service mockService = mock(Service.class);
        meta.setServiceDescription(mockService);
        when(mockService.getDescription()).thenReturn("description");
        service.setServiceMetadata(meta);


        ServiceMetadata utilMeta = (ServiceMetadata) BeanUtils.traverse(service, "serviceMetadata", ServiceMetadata.class);
        assertNotNull(utilMeta);
        assertEquals(meta, utilMeta);

        Service utilSer = (Service) BeanUtils.traverse(utilMeta, "serviceDescription", Service.class);
        assertNotNull(utilSer);

        utilSer = (Service) BeanUtils.traverse(service, "serviceMetadata.serviceDescription", Service.class);
        assertNotNull(utilSer);

        assertNotNull(BeanUtils.traverse(service, "serviceMetadata.serviceDescription.description"));


    }
}
