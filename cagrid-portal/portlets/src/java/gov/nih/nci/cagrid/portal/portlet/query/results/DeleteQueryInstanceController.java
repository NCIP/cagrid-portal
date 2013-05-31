/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
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
        QueryInstance instance = getQueryService().getQueryInstance(command.getInstanceId());
        if (instance != null) {
            //Will be null if it was created in previous http session.
            getQueryService().deleteQueryInstance(instance.getId());
        }
        instance = getQueryInstanceDao().getById(command.getInstanceId());
        if (getUserModel().getPortalUser() != null) {
            PortalUser user = getUserModel().getPortalUser();
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
