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
package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.domain.Person;
import gov.nih.nci.cagrid.portal.domain.metadata.common.PointOfContact;

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
	 * @see gov.nih.nci.cagrid.portal.dao.AbstractDao#domainClass()
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
