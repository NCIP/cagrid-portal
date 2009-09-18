/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.query.results;

import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.dao.QueryInstanceDao;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.dataservice.QueryInstance;
import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryActionController;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class DeleteQueryInstanceController extends
        AbstractQueryActionController {

    private QueryInstanceDao queryInstanceDao;
    private PortalUserDao portalUserDao;

    /**
     *
     */
    public DeleteQueryInstanceController() {

    }

    /**
     * @param commandClass
     */
    public DeleteQueryInstanceController(Class commandClass) {
        super(commandClass);

    }

    /**
     * @param commandClass
     * @param commandName
     */
    public DeleteQueryInstanceController(Class commandClass, String commandName) {
        super(commandClass, commandName);

    }

    /* (non-Javadoc)
      * @see gov.nih.nci.cagrid.portal.portlet.AbstractActionResponseHandlerCommandController#doHandleAction(javax.portlet.ActionRequest, javax.portlet.ActionResponse, java.lang.Object, org.springframework.validation.BindException)
      */
    @Override
    protected void doHandleAction(ActionRequest request,
                                  ActionResponse response, Object obj, BindException errors)
            throws Exception {
        SelectQueryInstanceCommand command = (SelectQueryInstanceCommand) obj;
        QueryInstance instance = getQueryModel().getQueryInstance(command.getInstanceId());
        if (instance != null) {
            //Will be null if it was created in previous http session.
            getQueryModel().deleteQueryInstance(instance.getId());
        }
        instance = getQueryInstanceDao().getById(command.getInstanceId());
        if (getQueryModel().getPortalUser() != null) {
            PortalUser user = getQueryModel().getPortalUser();
            user = getPortalUserDao().getById(user.getId());
            user.getQueryInstances().remove(instance);
            getPortalUserDao().save(user);
            getPortalUserDao().getHibernateTemplate().flush();
        }
        getQueryInstanceDao().delete(instance);
        getQueryInstanceDao().getHibernateTemplate().flush();
    }

    @Required
    public QueryInstanceDao getQueryInstanceDao() {
        return queryInstanceDao;
    }

    public void setQueryInstanceDao(QueryInstanceDao queryInstanceDao) {
        this.queryInstanceDao = queryInstanceDao;
    }

    @Required
    public PortalUserDao getPortalUserDao() {
        return portalUserDao;
    }

    public void setPortalUserDao(PortalUserDao portalUserDao) {
        this.portalUserDao = portalUserDao;
    }

}
