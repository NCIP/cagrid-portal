/**
 * 
 */
package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.dao.AddressDao;
import gov.nih.nci.cagrid.portal.domain.Address;

import java.util.List;


/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class AddressDaoTest extends AbstractDaoTest {
	
	
    protected String getDataSetFileName(){
    	return "db/test/data/AddressDaoTest.xml";
    }
    
    public void testFind(){
    	AddressDao dao = (AddressDao) getApplicationContext().getBean("addressDao");
    	Address address = dao.getById(-1);
    	assertNotNull(address);
    }
    
    public void testCreateDelete(){
    	Address a = new Address();
    	a.setCountry("USA");
    	AddressDao dao = (AddressDao) getApplicationContext().getBean("addressDao");
    	dao.save(a);
    	Integer id = a.getId();
    	assertNotNull(id);
    	Address b = new Address();
    	b.setCountry("US");
    	List<Address> addresses = dao.searchByExample(b, true);
    	assertTrue("Should have gotten one address. Got " + addresses.size(), addresses.size() == 1);
    }
    public void testAddressType(){
//    	Address a = new Address();
//    	a.setType(AddressType.HOME);
//    	AddressDao dao = (AddressDao) getApplicationContext().getBean("addressDao");
//    	dao.save(a);
//    	Integer id = a.getId();
//    	assertNotNull(id);
//    	Address b = new Address();
//    	b.setType(AddressType.HOME);
//    	List<Address> addresses = dao.searchByExample(b, true);
//    	assertTrue("Should have gotten one address. Got " + addresses.size(), addresses.size() == 1);
    }

}
