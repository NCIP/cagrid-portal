/**
 * 
 */
package gov.nih.nci.cagrid.portal2.dao;

import gov.nih.nci.cagrid.portal2.domain.Person;
import gov.nih.nci.cagrid.portal2.domain.metadata.common.PointOfContact;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:parmarv@mail.nih.gov">Vijay Parmar</a>
 * 
 */
public class PointOfContactDao extends AbstractDao<PointOfContact> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal2.dao.AbstractDao#domainClass()
	 */
	@Override
	public Class domainClass() {
		return PointOfContact.class;
	}

	public List getAllPointOfContactPersons() {
		List<Person> pocPersons = getHibernateTemplate()
				.find(
						"select p from Person p " +
						"join p.pointOfContacts " +
						"group by p.id " +
						"order by p.lastName");
		return pocPersons;
	}

}
