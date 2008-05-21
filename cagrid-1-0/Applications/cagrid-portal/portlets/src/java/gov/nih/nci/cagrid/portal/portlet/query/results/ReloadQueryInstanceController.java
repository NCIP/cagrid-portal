/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.query.results;

import gov.nih.nci.cagrid.portal.dao.QueryInstanceDao;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal.domain.dataservice.DCQLQueryInstance;
import gov.nih.nci.cagrid.portal.domain.dataservice.QueryInstance;
import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryActionController;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryCommand;
import org.springframework.validation.BindException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class ReloadQueryInstanceController extends AbstractQueryActionController {

    private QueryInstanceDao queryInstanceDao;

    /**
     *
     */
    public ReloadQueryInstanceController() {

    }

    /**
     * @param commandClass
     */
    public ReloadQueryInstanceController(Class commandClass) {
        super(commandClass);

    }

    /**
     * @param commandClass
     * @param commandName
     */
    public ReloadQueryInstanceController(Class commandClass, String commandName) {
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
        QueryInstance instance = getQueryInstanceDao().getById(command.getInstanceId());
        CQLQueryCommand workingQuery = new CQLQueryCommand();
        workingQuery.setCqlQuery(instance.getQuery().getXml());
        if (instance instanceof DCQLQueryInstance)
            workingQuery.setDcql(true);

        if (instance instanceof CQLQueryInstance)
            workingQuery.setDataServiceUrl(((CQLQueryInstance) instance).getDataService().getUrl());
        getQueryModel().setWorkingQuery(workingQuery);
    }

    public QueryInstanceDao getQueryInstanceDao() {
        return queryInstanceDao;
    }

    public void setQueryInstanceDao(QueryInstanceDao queryInstanceDao) {
        this.queryInstanceDao = queryInstanceDao;
    }
}
