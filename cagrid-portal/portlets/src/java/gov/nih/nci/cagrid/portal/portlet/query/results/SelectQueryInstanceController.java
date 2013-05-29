/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.query.results;

import gov.nih.nci.cagrid.portal.dao.CQLQueryInstanceDao;
import gov.nih.nci.cagrid.portal.dao.DCQLQueryInstanceDao;
import gov.nih.nci.cagrid.portal.domain.dataservice.QueryInstance;
import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryActionController;
import org.springframework.validation.BindException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class SelectQueryInstanceController extends
        AbstractQueryActionController {

    private CQLQueryInstanceDao cqlQueryInstanceDao;
    private DCQLQueryInstanceDao dcqlQueryInstanceDao;

    /**
     *
     */
    public SelectQueryInstanceController() {

    }

    /**
     * @param commandClass
     */
    public SelectQueryInstanceController(Class commandClass) {
        super(commandClass);

    }

    /**
     * @param commandClass
     * @param commandName
     */
    public SelectQueryInstanceController(Class commandClass, String commandName) {
        super(commandClass, commandName);

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
        SelectQueryInstanceCommand command = (SelectQueryInstanceCommand) obj;
        QueryInstance instance = getQueryService().getQueryInstance(
                command.getInstanceId());
        if (instance == null) {
            // Will be null if created in previous http session
            instance = getCqlQueryInstanceDao()
                    .getById(command.getInstanceId());
            if (instance == null) {
                throw new Exception("Couldn't find query instance with ID: "
                        + command.getInstanceId());
            }
        }
        getUserModel().setSelectedQueryInstance(instance);
    }

    public CQLQueryInstanceDao getCqlQueryInstanceDao() {
        return cqlQueryInstanceDao;
    }

    public void setCqlQueryInstanceDao(CQLQueryInstanceDao cqlQueryInstanceDao) {
        this.cqlQueryInstanceDao = cqlQueryInstanceDao;
    }

    public DCQLQueryInstanceDao getDcqlQueryInstanceDao() {
        return dcqlQueryInstanceDao;
    }

    public void setDcqlQueryInstanceDao(DCQLQueryInstanceDao dcqlQueryInstanceDao) {
        this.dcqlQueryInstanceDao = dcqlQueryInstanceDao;
    }
}
