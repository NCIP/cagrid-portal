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
package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.DBTestBase;
import gov.nih.nci.cagrid.portal.domain.Address;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class AddressDaoTest extends DBTestBase<AddressDao> {

    @Test
    public void save() {

        Address address = new Address();
        address.setStreet1("stree1");

        getDao().save(address);

    }

    @Test
    public void retreive() {
        getDao().getAll();
        Address address = getDao().getById(-3);
        assertEquals("street3",address.getStreet1());

    }

    @Test
    public void search() {
        Address sample = new Address();
        sample.setPostalCode("code3");

        assertNotNull(getDao().getByExample(sample));
    }

}
