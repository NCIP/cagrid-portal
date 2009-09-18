/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.query.cql;

import gov.nih.nci.cagrid.portal.dao.QueryInstanceDao;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal.domain.dataservice.DCQLQueryInstance;
import gov.nih.nci.cagrid.portal.domain.dataservice.QueryInstance;
import gov.nih.nci.cagrid.portal.domain.dataservice.QueryInstanceState;
import gov.nih.nci.cagrid.portal.portlet.query.QueryModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class CQLQueryHistoryFacade {

    private static final Log logger = LogFactory
            .getLog(CQLQueryHistoryFacade.class);

    private QueryModel queryModel;
    private String requestAttributeName;
    private String renderServletUrl;
    private QueryInstanceDao queryInstanceDao;

    /**
     *
     */
    public CQLQueryHistoryFacade() {

    }

    public List<QueryInstance> getActiveInstances() {
        List<QueryInstance> activeInstances = new ArrayList<QueryInstance>();
        List<QueryInstance> submitted = getQueryModel()
                .getSubmittedQueries();
        for (QueryInstance instance : submitted) {
            if (isActive(instance)) {
                activeInstances.add(instance);
            }
        }
        return activeInstances;
    }

    private boolean isActive(QueryInstance instance) {
        return instance.getState() == QueryInstanceState.UNSCHEDULED
                || instance.getState() == QueryInstanceState.SCHEDULED
                || instance.getState() == QueryInstanceState.RUNNING;
    }

    public QueryInstance getInstance(Integer instanceId) {

        QueryInstance instance = null;
        List<QueryInstance> submitted = getQueryModel()
                .getSubmittedQueries();

        for (QueryInstance inst : submitted) {
            if(inst.getId().equals(instanceId)){
            	instance = inst;
            	break;
            }
        }

        if (instance != null) {
            //Refresh the object so that it shows correct state.
            String result = instance.getResult();
            instance = getQueryInstanceDao().getById(instanceId);
            instance.setResult(result);
        }

        if (instance == null) {
            instance = getQueryInstanceDao().getById(instanceId);
        }
        return instance;
    }
    
    public String renderCQLInstance(CQLQueryInstance bean, String namespace) {
    	return renderInstance(bean, namespace);
    }
    
    public String renderDCQLInstance(DCQLQueryInstance bean, String namespace) {
    	return renderInstance(bean, namespace);
    }

    public String renderInstance(QueryInstance bean, String namespace) {

        String html = null;

        QueryInstance instance = getQueryInstanceDao().getById(bean.getId());
        if (instance != null) {
            for (QueryInstance inst : getQueryModel().getSubmittedQueries()) {
                if (inst.getId().equals(instance.getId())) {
                    instance.setResult(inst.getResult());
                }
            }
        }

        WebContext webContext = WebContextFactory.get();
        HttpServletRequest request = webContext.getHttpServletRequest();
        request.setAttribute(getRequestAttributeName(), instance);
        request.setAttribute("namespace", namespace);
        try {
            html = webContext.forwardToString(getRenderServletUrl());
        } catch (Exception ex) {
            String msg = "Error rendering QueryInstance: " + ex.getMessage();
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

    public QueryInstanceDao getQueryInstanceDao() {
        return queryInstanceDao;
    }

    public void setQueryInstanceDao(QueryInstanceDao queryInstanceDao) {
        this.queryInstanceDao = queryInstanceDao;
    }

    public QueryModel getQueryModel() {
        return queryModel;
    }

    public void setQueryModel(QueryModel queryModel) {
        this.queryModel = queryModel;
    }

}
