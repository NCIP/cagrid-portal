/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query.cql;

import gov.nih.nci.cagrid.portal2.dao.CQLQueryInstanceDao;
import gov.nih.nci.cagrid.portal2.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal2.domain.dataservice.QueryInstanceState;
import gov.nih.nci.cagrid.portal2.portlet.SharedApplicationModel;
import gov.nih.nci.cagrid.portal2.portlet.query.QueryModel;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class CQLQueryHistoryFacade {

	private static final Log logger = LogFactory
			.getLog(CQLQueryHistoryFacade.class);

	private QueryModel queryModel;
	private String requestAttributeName;
	private String renderServletUrl;
	private CQLQueryInstanceDao cqlQueryInstanceDao;

	/**
	 * 
	 */
	public CQLQueryHistoryFacade() {

	}

	public List<CQLQueryInstance> getActiveInstances() {
		List<CQLQueryInstance> activeInstances = new ArrayList<CQLQueryInstance>();
		List<CQLQueryInstance> submitted = getQueryModel()
				.getSubmittedCqlQueries();
		for (CQLQueryInstance instance : submitted) {
			if (isActive(instance)) {
				activeInstances.add(instance);
			}
		}
		return activeInstances;
	}

	private boolean isActive(CQLQueryInstance instance) {
		return instance.getState() == QueryInstanceState.UNSCHEDULED
				|| instance.getState() == QueryInstanceState.SCHEDULED
				|| instance.getState() == QueryInstanceState.RUNNING;
	}

	public CQLQueryInstance getInstance(Integer instanceId) {
		
		CQLQueryInstance instance = null;
		List<CQLQueryInstance> submitted = getQueryModel()
				.getSubmittedCqlQueries();
		
		logger.debug("Looking for instance '" + instanceId + "'");
		logger.debug("submitted.size() = " + submitted.size());
		
		for (CQLQueryInstance inst : submitted) {
			inst.getId().equals(instanceId);
			instance = inst;
			break;
		}
		
		if(instance == null){
			logger.debug("...didn't find instance");
		}else{
			logger.debug("..found it.");
		}
		
		return instance;
	}

	public String renderInstance(CQLQueryInstance bean, String namespace) {
		
		String html = null;

		CQLQueryInstance instance = getCqlQueryInstanceDao().getById(bean.getId());
		
		if(instance == null){
			logger.debug("Didn't find instance");
		}else{
			logger.debug("Found instance:" + instance.getId());
		}
		
		WebContext webContext = WebContextFactory.get();
		HttpServletRequest request = webContext.getHttpServletRequest();
		request.setAttribute(getRequestAttributeName(), instance);
		request.setAttribute("namespace", namespace);
		try {
			html = webContext.forwardToString(getRenderServletUrl());
		} catch (Exception ex) {
			String msg = "Error rendering CQLQueryInstance: " + ex.getMessage();
			logger.error(msg, ex);
			throw new RuntimeException(msg, ex);
		}

		return html;
	}

	public String getRequestAttributeName() {
		return requestAttributeName;
	}

	public void setRequestAttributeName(String requestAttributeName) {
		this.requestAttributeName = requestAttributeName;
	}

	public String getRenderServletUrl() {
		return renderServletUrl;
	}

	public void setRenderServletUrl(String renderServletUrl) {
		this.renderServletUrl = renderServletUrl;
	}

	public CQLQueryInstanceDao getCqlQueryInstanceDao() {
		return cqlQueryInstanceDao;
	}

	public void setCqlQueryInstanceDao(CQLQueryInstanceDao cqlQueryInstanceDao) {
		this.cqlQueryInstanceDao = cqlQueryInstanceDao;
	}

	public QueryModel getQueryModel() {
		return queryModel;
	}

	public void setQueryModel(QueryModel queryModel) {
		this.queryModel = queryModel;
	}

}
