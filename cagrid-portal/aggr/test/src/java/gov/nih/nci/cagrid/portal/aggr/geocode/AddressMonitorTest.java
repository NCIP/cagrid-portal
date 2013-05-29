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
package gov.nih.nci.cagrid.portal.aggr.geocode;

import gov.nih.nci.cagrid.portal.dao.AddressDao;
import gov.nih.nci.cagrid.portal.domain.Address;
import gov.nih.nci.cagrid.portal.domain.Geocode;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class AddressMonitorTest extends AbstractGeocodeTest {


    public void testCheckAddresses() throws Exception {
        AddressDao mockDao = mock(AddressDao.class);
        final Address address = new Address();
        address.setCountry("US");
        address.setPostalCode("20855");

        when(mockDao.getAll()).thenReturn(new ArrayList<Address>() {{
            add(address);
        }});
        Geocoder mockGeocoder = mock(Geocoder.class);
        when(mockGeocoder.getGeocode(address)).thenReturn(new Geocode());

        AddressMonitor addressMonitor = (AddressMonitor) getApplicationContext().getBean("addressMonitor");
        addressMonitor.setAddressDao(mockDao);
        addressMonitor.setGeocoder(mockGeocoder);

        addressMonitor.checkAddresses();
        verify(mockDao, times(1)).save(address);
    }


}
