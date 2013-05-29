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
/**
 * 
 */
package gov.nih.nci.cagrid.portal.util;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.portal.aggr.regsvc.RegisteredServiceEvent;
import gov.nih.nci.cagrid.portal.aggr.regsvc.RegisteredServiceListener;
import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.dao.IndexServiceDao;
import gov.nih.nci.cagrid.portal.dao.PersonDao;
import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.dao.catalog.PersonCatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.Person;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.catalog.PersonCatalogEntry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
@Transactional
public class CreateUser {
	
	private static final Log logger = LogFactory.getLog(CreateUser.class);
	
	private PersonDao personDao;
	private PortalUserDao portalUserDao;
	private PersonCatalogEntryDao personCatalogEntryDao;

	public static void main(String[] args) throws Exception {

		String firstName = args[0];
		String lastName = args[1];
		String emailAddress = args[2];
		String portalId = args[3];

		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				new String[] { "../db/etc/applicationContext-db.xml",
						"test/etc/applicationContext-aggr-util.xml"});
		CreateUser c = (CreateUser) ctx.getBean("createUserPrototype");
		c.create(firstName, lastName, emailAddress, portalId);
	}
	
	public void create(String firstName, String lastName, String emailAddress, String portalId){
		Person person = new Person();
		person.setFirstName(firstName);
		person.setLastName(lastName);
		person.setEmailAddress(emailAddress);
		getPersonDao().save(person);

		PortalUser portalUser = new PortalUser();
		portalUser.setPortalId(portalId);
		portalUser.setPerson(person);
		getPortalUserDao().save(portalUser);

		PersonCatalogEntry personCatalogEntry = getPersonCatalogEntryDao()
				.createCatalogAbout(portalUser);
		personCatalogEntry.setName(person.getFirstName() + " "
				+ person.getLastName());
		personCatalogEntry.setAuthor(portalUser);
		personCatalogEntry.setPublished(false);
		getPersonCatalogEntryDao().save(personCatalogEntry);

		portalUser.setCatalog(personCatalogEntry);
		getPortalUserDao().save(portalUser);
	}


	public PersonDao getPersonDao() {
		return personDao;
	}

	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}

	public PortalUserDao getPortalUserDao() {
		return portalUserDao;
	}

	public void setPortalUserDao(PortalUserDao portalUserDao) {
		this.portalUserDao = portalUserDao;
	}

	public PersonCatalogEntryDao getPersonCatalogEntryDao() {
		return personCatalogEntryDao;
	}

	public void setPersonCatalogEntryDao(PersonCatalogEntryDao personCatalogEntryDao) {
		this.personCatalogEntryDao = personCatalogEntryDao;
	}
}
