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

import gov.nih.nci.cagrid.portal.dao.QueryResultTableDao;
import gov.nih.nci.cagrid.portal.domain.dataservice.QueryInstance;
import gov.nih.nci.cagrid.portal.domain.table.QueryResultTable;
import gov.nih.nci.cagrid.portal.portlet.UserModel;
import org.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
 */
public class ExportJSONResultsMetadataController extends AbstractController {

    private UserModel userModel;
    private QueryResultTableDao queryResultTableDao;
    private QueryResultTableToDataTableMetadataBuilder queryResultTableToDataTableMetadataBuilder;

    /**
     *
     */
    public ExportJSONResultsMetadataController() {

    }

    /*
      * (non-Javadoc)
      *
      * @see
      * org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal
      * (javax.servlet.http.HttpServletRequest,
      * javax.servlet.http.HttpServletResponse)
      */
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest req,
                                                 HttpServletResponse res) throws Exception {

        QueryInstance instance = getUserModel().getCurrentQueryInstance();
        QueryResultTable table = getQueryResultTableDao().getByQueryInstanceId(
                instance.getId());
        JSONObject meta = queryResultTableToDataTableMetadataBuilder
                .build(table);
        res.setContentType("application/json");
        res.getWriter().write(meta.toString());

        return null;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public QueryResultTableDao getQueryResultTableDao() {
        return queryResultTableDao;
    }

    public void setQueryResultTableDao(QueryResultTableDao queryResultTableDao) {
        this.queryResultTableDao = queryResultTableDao;
    }

    public QueryResultTableToDataTableMetadataBuilder getQueryResultTableToDataTableMetadataBuilder() {
        return queryResultTableToDataTableMetadataBuilder;
    }

    public void setQueryResultTableToDataTableMetadataBuilder(QueryResultTableToDataTableMetadataBuilder queryResultTableToDataTableMetadataBuilder) {
        this.queryResultTableToDataTableMetadataBuilder = queryResultTableToDataTableMetadataBuilder;
    }
}
