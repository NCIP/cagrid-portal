/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.results;

import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal.domain.dataservice.QueryInstance;
import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryRenderController;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.RenderRequest;

import org.springframework.beans.factory.annotation.Required;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ViewQueryHistoryController extends AbstractQueryRenderController {

	private PortalUserDao portalUserDao;

	/**
	 * 
	 */
	public ViewQueryHistoryController() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.portlet.AbstractViewObjectController#getObject(javax.portlet.RenderRequest)
	 */
	@Override
	protected Object getObject(RenderRequest request) {
		PortalUser user = getQueryModel().getPortalUser();
		List<CQLQueryInstance> instances = getQueryModel().getSubmittedCqlQueries();
		if(user != null){
			//Since results are not stored, need to map results
			//in http session to instances pulled from DB.
			Map<Integer,CQLQueryInstance> map = new HashMap<Integer,CQLQueryInstance>();
			for(CQLQueryInstance instance : instances){
				map.put(instance.getId(), instance);
			}
			instances = new ArrayList<CQLQueryInstance>();
			PortalUser p = getPortalUserDao().getById(user.getId());
			instances = new ArrayList<CQLQueryInstance>();
			for (QueryInstance inst : p.getQueryInstances()) {
				if (inst instanceof CQLQueryInstance) {
					CQLQueryInstance instance = (CQLQueryInstance)inst;
					if(map.containsKey(instance.getId())){
						instance.setResult(map.get(instance.getId()).getResult());
					}
					instances.add(instance);
				}
			}
		}
		return instances;
	}

	@Required
	public PortalUserDao getPortalUserDao() {
		return portalUserDao;
	}

	public void setPortalUserDao(PortalUserDao portalUserDao) {
		this.portalUserDao = portalUserDao;
	}

}
