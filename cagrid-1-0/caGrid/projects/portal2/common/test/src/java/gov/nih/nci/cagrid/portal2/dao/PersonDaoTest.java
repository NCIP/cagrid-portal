/**
 * 
 */
package gov.nih.nci.cagrid.portal2.dao;

import gov.nih.nci.cagrid.portal2.domain.Address;
import gov.nih.nci.cagrid.portal2.domain.AddressType;
import gov.nih.nci.cagrid.portal2.domain.Person;

import java.util.List;

import org.dbunit.operation.DatabaseOperation;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class PersonDaoTest extends AbstractDaoTest {

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal2.dao.AbstractDaoTest#getDataSetFileName()
	 */
	@Override
	protected String getDataSetFileName() {
		return getDataSetFileName("test/data/PersonDaoTest.xml");
	}
	
	public void testGetAll(){
		PersonDao dao = (PersonDao)getApplicationContext().getBean("personDao");
		List<Person> persons = dao.getAll();
		assertTrue("Expected 1 persons. Got " + persons.size(), persons.size() == 1);
	}
	
	public void testGetPersonWithAddresses(){
		PersonDao dao = (PersonDao)getApplicationContext().getBean("personDao");
		Person p = dao.getById(-1);
		assertNotNull(p);
		List<Address> addresses = p.getAddresses();
		assertTrue("Expected 3 addresses, got " + addresses.size(), addresses.size() == 3);
	}
	
	public void testFindPersonByAddresses(){
		Person personProt = new Person();
		Address addressProt = new Address();
		addressProt.setType(AddressType.HOME);
		personProt.getAddresses().add(addressProt);
		PersonDao dao = (PersonDao)getApplicationContext().getBean("personDao");
		List<Person> persons = dao.searchByExample(personProt);
		assertTrue("Expected 1 persons. Got " + persons.size(), persons.size() == 1);
	}
	
	public void testUpdatePerson(){
		AddressDao addressDao = (AddressDao)getApplicationContext().getBean("addressDao");
		PersonDao personDao = (PersonDao)getApplicationContext().getBean("personDao");
		Person p = personDao.getById(-1);
		assertNotNull(p);
		Address newAddress = new Address();
		newAddress.setCity("Baltimore");
		addressDao.save(newAddress);
		p.getAddresses().add(newAddress);
		personDao.save(p);
		interruptSession();
		p = personDao.getById(-1);
		assertNotNull(p);
		List<Address> addresses = p.getAddresses();
		assertTrue("Expected 4 addresses, got " + addresses.size(), addresses.size() == 4);
	}
	
	protected DatabaseOperation getTearDownOperation() throws Exception {
		return DatabaseOperation.DELETE_ALL;
	}

}
