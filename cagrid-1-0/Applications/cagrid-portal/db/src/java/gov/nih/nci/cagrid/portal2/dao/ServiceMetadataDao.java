/**
 * 
 */
package gov.nih.nci.cagrid.portal2.dao;

import gov.nih.nci.cagrid.portal2.domain.GridService;
import gov.nih.nci.cagrid.portal2.domain.metadata.ServiceMetadata;

import java.util.List;

import javax.persistence.NonUniqueResultException;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ServiceMetadataDao extends AbstractDao<ServiceMetadata> {

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal2.dao.AbstractDao#domainClass()
	 */
	@Override
	public Class domainClass() {
		return ServiceMetadata.class;
	}

	

	public void saveGridService(GridService svc) {
		getHibernateTemplate().saveOrUpdate(svc);
	}

}
