/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.cql;

import gov.nih.nci.cagrid.portal.dao.CQLQueryDao;
import gov.nih.nci.cagrid.portal.dao.CQLQueryInstanceDao;
import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQuery;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal.domain.dataservice.QueryInstance;
import gov.nih.nci.cagrid.portal.portlet.query.QueryModel;
import gov.nih.nci.cagrid.portal.util.PortalUtils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class CQLQueryService {
	
	private static final Log logger = LogFactory.getLog(CQLQueryService.class);

	private PortalUserDao portalUserDao;

	private GridServiceDao gridServiceDao;

	private CQLQueryDao cqlQueryDao;

	private CQLQueryInstanceDao cqlQueryInstanceDao;
	
	private QueryModel queryModel;

	/**
	 * 
	 */
	public CQLQueryService() {

	}

    @Transactional
    public CQLQuery loadQuery(String cql){
		String hash = PortalUtils.createHash(cql);
		return cqlQueryDao.getByHash(hash);
    }


	@Transactional
	public CQLQueryInstance submitQuery(PortalUser user, GridDataService service,
			final String cql) throws Exception {

		logger.debug("Submitted Query: "+ cql);
	       CQLQuery query = loadQuery(cql);

		if (query == null) {
			query = new CQLQuery();
			query.setXml(cql);
			query.setHash(PortalUtils.createHash(cql));
			cqlQueryDao.save(query);
		}

		CQLQueryInstance inst = new CQLQueryInstance();
		inst.setDataService(service);
		if (user != null) {
			inst.setPortalUser(user);
		}
		inst.setQuery(query);
		cqlQueryInstanceDao.save(inst);

		query.getInstances().add(inst);
		cqlQueryDao.save(query);

		if (user != null) {
			PortalUser p = getPortalUserDao().getById(user.getId());
			p.getQueryInstances().add(inst);
			portalUserDao.save(p);
		}
		getQueryModel().submitCqlQuery(inst);

		return inst;
	}

	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}

	public PortalUserDao getPortalUserDao() {
		return portalUserDao;
	}

	public void setPortalUserDao(PortalUserDao portalUserDao) {
		this.portalUserDao = portalUserDao;
	}

	public CQLQueryDao getCqlQueryDao() {
		return cqlQueryDao;
	}

	public void setCqlQueryDao(CQLQueryDao cqlQueryDao) {
		this.cqlQueryDao = cqlQueryDao;
	}

	public CQLQueryInstanceDao getCqlQueryInstanceDao() {
		return cqlQueryInstanceDao;
	}

	public void setCqlQueryInstanceDao(CQLQueryInstanceDao cqlQueryInstanceDao) {
		this.cqlQueryInstanceDao = cqlQueryInstanceDao;
	}

	public List<CQLQueryInstance> getSubmittedCqlQueries(PortalUser user) {
		
		List<CQLQueryInstance> instances = null;

		// If no user, get query instances from shared model
		if (user == null) {
			instances = getQueryModel().getSubmittedCqlQueries();
		} else {
			PortalUser p = getPortalUserDao().getById(user.getId());
			instances = new ArrayList<CQLQueryInstance>();
			for (QueryInstance inst : p.getQueryInstances()) {
				if (inst instanceof CQLQueryInstance) {
					instances.add((CQLQueryInstance)inst);
				}
			}
		}
		return instances;
	}

	public QueryModel getQueryModel() {
		return queryModel;
	}

	public void setQueryModel(QueryModel queryModel) {
		this.queryModel = queryModel;
	}

}
