/**
 * 
 */
package gov.nih.nci.cagrid.portal2.dao;

import java.util.List;

import gov.nih.nci.cagrid.portal2.dao.exception.NonUniqueResultException;
import gov.nih.nci.cagrid.portal2.domain.GridService;
import gov.nih.nci.cagrid.portal2.domain.metadata.ServiceMetadata;

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

	public GridService getGridServiceByUrl(String url) {
		GridService svc = null;
		List svcs = getHibernateTemplate().find("from GridService where url = '" + url + "'");
		if(svcs.size() > 1){
			throw new NonUniqueResultException("Found " + svcs.size() + " GridService objects for url '" + url + "'");
		}else if(svcs.size() == 1){
			svc = (GridService)svcs.get(0);
		}
		return svc;
	}

	public void saveGridService(GridService svc) {
		getHibernateTemplate().saveOrUpdate(svc);
	}

}
