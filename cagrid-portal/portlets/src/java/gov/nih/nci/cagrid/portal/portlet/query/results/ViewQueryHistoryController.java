/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.query.results;

import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.dataservice.QueryInstance;
import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryRenderController;
import org.springframework.beans.factory.annotation.Required;

import javax.portlet.RenderRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
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
        List<QueryInstance> instances = getQueryModel().getSubmittedQueries();
        if (user != null) {
            //Since results are not stored, need to map results
            //in http session to instances pulled from DB.
            Map<Integer, QueryInstance> map = new HashMap<Integer, QueryInstance>();
            for (QueryInstance instance : instances) {
                map.put(instance.getId(), instance);
            }
            instances = new ArrayList<QueryInstance>();
            PortalUser p = getPortalUserDao().getById(user.getId());
            instances = new ArrayList<QueryInstance>();
            for (QueryInstance inst : p.getQueryInstances()) {
                if (map.containsKey(inst.getId())) {
                    inst.setResult(map.get(inst.getId()).getResult());
                }
                instances.add(inst);

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
