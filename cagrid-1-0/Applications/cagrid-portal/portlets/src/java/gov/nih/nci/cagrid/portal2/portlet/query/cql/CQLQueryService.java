/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query.cql;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import gov.nih.nci.cagrid.portal2.dao.CQLQueryDao;
import gov.nih.nci.cagrid.portal2.dao.CQLQueryInstanceDao;
import gov.nih.nci.cagrid.portal2.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal2.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal2.domain.GridDataService;
import gov.nih.nci.cagrid.portal2.domain.PortalUser;
import gov.nih.nci.cagrid.portal2.domain.dataservice.CQLQuery;
import gov.nih.nci.cagrid.portal2.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal2.domain.dataservice.QueryInstance;
import gov.nih.nci.cagrid.portal2.portlet.SharedApplicationModel;
import gov.nih.nci.cagrid.portal2.util.PortalUtils;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class CQLQueryService {

	private PortalUserDao portalUserDao;

	private GridServiceDao gridServiceDao;

	private SharedApplicationModel sharedApplicationModel;

	private CQLQueryDao cqlQueryDao;

	private CQLQueryInstanceDao cqlQueryInstanceDao;

	/**
	 * 
	 */
	public CQLQueryService() {

	}

	@Transactional
	public CQLQueryInstance submitQuery(GridDataService service,
			final String cql) throws Exception {

		PortalUser user = getSharedApplicationModel().getPortalUser();
		String hash = PortalUtils.createHash(cql);
		CQLQuery query = cqlQueryDao.getByHash(hash);

		if (query == null) {
			query = new CQLQuery();
			query.setXml(cql);
			query.setHash(hash);
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
			user.getQueryInstances().add(inst);
			portalUserDao.save(user);
		}
		getSharedApplicationModel().submitCqlQuery(inst);

		return inst;
	}

	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}

	public SharedApplicationModel getSharedApplicationModel() {
		return sharedApplicationModel;
	}

	public void setSharedApplicationModel(
			SharedApplicationModel sharedApplicationModel) {
		this.sharedApplicationModel = sharedApplicationModel;
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

	public List<CQLQueryInstance> getSubmittedCqlQueries() {
		
		List<CQLQueryInstance> instances = null;
		PortalUser user = getSharedApplicationModel().getPortalUser();

		// If no user, get query instances from shared model
		if (user == null) {
			instances = getSharedApplicationModel().getSubmittedCqlQueries();
		} else {
			instances = new ArrayList<CQLQueryInstance>();
			for (QueryInstance inst : user.getQueryInstances()) {
				if (inst instanceof CQLQueryInstance) {
					instances.add((CQLQueryInstance)inst);
				}
			}
		}
		return instances;
	}

}
