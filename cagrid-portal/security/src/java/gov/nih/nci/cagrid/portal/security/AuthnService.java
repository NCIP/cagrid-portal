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
package gov.nih.nci.cagrid.portal.security;

import gov.nih.nci.cagrid.portal.authn.EncryptionService;
import gov.nih.nci.cagrid.portal.authn.service.BaseAuthnService;
import gov.nih.nci.cagrid.portal.dao.AuthnTicketDao;
import gov.nih.nci.cagrid.portal.dao.PersonDao;
import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.domain.AuthnTicket;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;
 

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
 */
@Transactional
public class AuthnService extends BaseAuthnService {

    private EncryptionService encryptionService;

	private PortalUserDao portalUserDao;
	private PersonDao personDao;

    /**
     *
     */
	public AuthnService() {

	}



	public PortalUserDao getPortalUserDao() {
		return portalUserDao;
	}

	public void setPortalUserDao(PortalUserDao portalUserDao) {
		this.portalUserDao = portalUserDao;
	}

	public PersonDao getPersonDao() {
		return personDao;
	}

	public void setPersonDao(PersonDao personDao) {
		this.personDao = personDao;
	}

    public EncryptionService getEncryptionService() {
		return encryptionService;
	}

	public void setEncryptionService(EncryptionService encryptionService) {
		this.encryptionService = encryptionService;
	}

}
