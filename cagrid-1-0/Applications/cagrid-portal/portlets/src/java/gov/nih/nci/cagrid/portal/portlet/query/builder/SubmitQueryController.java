/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.query.builder;

import gov.nih.nci.cagrid.portal.dao.*;
import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.dataservice.*;
import gov.nih.nci.cagrid.portal.portlet.CaGridPortletApplicationException;
import gov.nih.nci.cagrid.portal.portlet.PortletConstants;
import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryActionController;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryCommand;
import gov.nih.nci.cagrid.portal.portlet.util.XSSFilterEditor;
import gov.nih.nci.cagrid.portal.util.PortalUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.BindException;
import org.springframework.web.portlet.bind.PortletRequestDataBinder;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class SubmitQueryController extends AbstractQueryActionController {

    private GridServiceDao gridServiceDao;

    private PortalUserDao portalUserDao;
    private CQLQueryDao cqlQueryDao;
    private DCQLQueryDao dcqlQueryDao;

    private CQLQueryInstanceDao cqlQueryInstanceDao;
    private DCQLQueryInstanceDao dcqlQueryInstanceDao;
    private String fqpService;

    private int maxActiveQueries = 3;

    /**
     *
     */
    public SubmitQueryController() {

    }

    /**
     * @param commandClass
     */
    public SubmitQueryController(Class commandClass) {
        super(commandClass);

    }

    /**
     * @param commandClass
     * @param commandName
     */
    public SubmitQueryController(Class commandClass, String commandName) {
        super(commandClass, commandName);

    }

    protected void initBinder(PortletRequest request,
                              PortletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(String.class, "dataServiceUrl",
                new XSSFilterEditor(binder.getBindingResult(), "dataServiceUrl"));
    }

    /*
      * (non-Javadoc)
      *
      * @see gov.nih.nci.cagrid.portal.portlet.AbstractActionResponseHandlerCommandController#doHandleAction(javax.portlet.ActionRequest,
      *      javax.portlet.ActionResponse, java.lang.Object,
      *      org.springframework.validation.BindException)
      */
    @Override
    protected void doHandleAction(ActionRequest request,
                                  ActionResponse response, Object obj, BindException errors)
            throws Exception {

        if (errors.hasErrors()) {
            return;
        }

        CQLQueryCommand command = (CQLQueryCommand) obj;
        getQueryModel().setWorkingQuery(command);

        GridDataService dataService = null;
        try {
            dataService = (GridDataService) getGridServiceDao().getByUrl(
                    command.getDataServiceUrl().trim());
        } catch (Exception ex) {
            throw new CaGridPortletApplicationException(
                    "Error looking up GridDataService '"
                            + command.getDataServiceUrl() + "': "
                            + ex.getMessage(), ex);
        }

        // Make sure the user has not exceeded the maximum
        // number of simultaneous queries.
        int active = 0;
        for (QueryInstance instance : getQueryModel()
                .getSubmittedQueries()) {
            if (QueryInstanceState.RUNNING.equals(instance.getState())
                    || QueryInstanceState.SCHEDULED.equals(instance.getState())
                    || QueryInstanceState.UNSCHEDULED.equals(instance
                    .getState())) {
                active++;
            }
        }
        if (active >= getMaxActiveQueries()) {
            errors
                    .reject(
                            PortletConstants.MAX_ACTIVE_QUERIES_MSG,
                            new String[]{String.valueOf(getMaxActiveQueries())},
                            "You have reached the maximum number of" +
                                    " simultaneous queries: " + getMaxActiveQueries() +
                                    ". Please cancel a" +
                                    " running query, or wait for one to complete.");
            return;
        }

        try {
            if (command.isDcql()) {
                submitDCQLQuery(getQueryModel().getPortalUser(), command.getCqlQuery());
            } else
                submitCQLQuery(getQueryModel().getPortalUser(), dataService, command
                        .getCqlQuery());
        } catch (Exception ex) {
            logger.error(ex);
            errors.reject(PortletConstants.CQL_QUERY_SUBMIT_ERROR_MSG,
                    new String[]{ex.getMessage()},
                    "Error submitting CQL query.");
        }

    }

    private void submitDCQLQuery(PortalUser user, String dcql) throws CaGridPortletApplicationException {
        logger.debug("Submitted Query: " + dcql);

        String hash = PortalUtils.createHash(dcql);
        DCQLQuery query = getDcqlQueryDao().getByHash(hash);

        if (query == null) {
            query = new DCQLQuery();
            query.setXml(dcql);
            query.setHash(hash);
            getDcqlQueryDao().save(query);
        }

        DCQLQueryInstance inst = new DCQLQueryInstance();
        if (user != null) {
            inst.setPortalUser(user);
        }
        inst.setQuery(query);

        GridService _fqpService = null;
        try {
            _fqpService = getGridServiceDao().getByUrl(
                    getFqpService());
            inst.setFqpService(_fqpService);
        } catch (Exception ex) {
            throw new CaGridPortletApplicationException(
                    "Error looking up FQP Service '"
                            + fqpService + "': "
                            + ex.getMessage(), ex);
        }

        getDcqlQueryInstanceDao().save(inst);
        query.getInstances().add(inst);
        getDcqlQueryDao().save(query);

        if (user != null) {
            PortalUser p = getPortalUserDao().getById(user.getId());
            p.getQueryInstances().add(inst);
            getPortalUserDao().save(p);
        }
        getQueryModel().submitDcqlQuery(inst);
    }

    private void submitCQLQuery(PortalUser user, GridDataService service,
                                String cql) {
        logger.debug("Submitted Query: " + cql);

        String hash = PortalUtils.createHash(cql);
        CQLQuery query = getCqlQueryDao().getByHash(hash);

        if (query == null) {
            query = new CQLQuery();
            query.setXml(cql);
            query.setHash(hash);
            getCqlQueryDao().save(query);
        }

        CQLQueryInstance inst = new CQLQueryInstance();
        inst.setDataService(service);
        if (user != null) {
            inst.setPortalUser(user);
        }
        inst.setQuery(query);
        getCqlQueryInstanceDao().save(inst);

        query.getInstances().add(inst);
        getCqlQueryDao().save(query);

        if (user != null) {
            PortalUser p = getPortalUserDao().getById(user.getId());
            p.getQueryInstances().add(inst);
            getPortalUserDao().save(p);
        }
        getQueryModel().submitCqlQuery(inst);
    }

    @Required
    public GridServiceDao getGridServiceDao() {
        return gridServiceDao;
    }

    public void setGridServiceDao(GridServiceDao gridServiceDao) {
        this.gridServiceDao = gridServiceDao;
    }

    @Required
    public PortalUserDao getPortalUserDao() {
        return portalUserDao;
    }

    public void setPortalUserDao(PortalUserDao portalUserDao) {
        this.portalUserDao = portalUserDao;
    }

    @Required
    public CQLQueryDao getCqlQueryDao() {
        return cqlQueryDao;
    }

    public void setCqlQueryDao(CQLQueryDao cqlQueryDao) {
        this.cqlQueryDao = cqlQueryDao;
    }

    @Required
    public CQLQueryInstanceDao getCqlQueryInstanceDao() {
        return cqlQueryInstanceDao;
    }

    public void setCqlQueryInstanceDao(CQLQueryInstanceDao cqlQueryInstanceDao) {
        this.cqlQueryInstanceDao = cqlQueryInstanceDao;
    }

    public int getMaxActiveQueries() {
        return maxActiveQueries;
    }

    public void setMaxActiveQueries(int maxActiveQueries) {
        this.maxActiveQueries = maxActiveQueries;
    }

    @Required
    public DCQLQueryDao getDcqlQueryDao() {
        return dcqlQueryDao;
    }

    public void setDcqlQueryDao(DCQLQueryDao dcqlQueryDao) {
        this.dcqlQueryDao = dcqlQueryDao;
    }

    @Required
    public DCQLQueryInstanceDao getDcqlQueryInstanceDao() {
        return dcqlQueryInstanceDao;
    }

    public void setDcqlQueryInstanceDao(DCQLQueryInstanceDao dcqlQueryInstanceDao) {
        this.dcqlQueryInstanceDao = dcqlQueryInstanceDao;
    }

    public String getFqpService() {
        return fqpService;
    }

    public void setFqpService(String fqpService) {
        this.fqpService = fqpService;
    }
}
