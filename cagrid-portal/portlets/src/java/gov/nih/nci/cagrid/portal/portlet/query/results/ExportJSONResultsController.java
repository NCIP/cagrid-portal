/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.results;

import gov.nih.nci.cagrid.portal.dao.QueryResultTableDao;
import gov.nih.nci.cagrid.portal.domain.table.QueryResultRow;
import gov.nih.nci.cagrid.portal.domain.table.QueryResultTable;
import gov.nih.nci.cagrid.portal.portlet.util.PortletUtils;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis.utils.StringUtils;
import org.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ExportJSONResultsController extends AbstractController {

	private QueryResultTableDao queryResultTableDao;
	private QueryResultTableToJSONObjectBuilder queryResultTableToJSONObjectBuilder;

	/**
	 * 
	 */
	public ExportJSONResultsController() {

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

		String instanceId = req.getParameter("instanceId");
		String sort = req.getParameter("sort");
		String dir = req.getParameter("dir");
		String startIndex = req.getParameter("startIndex");
		String results = req.getParameter("results");

		QueryResultTable table = getQueryResultTableDao().getByQueryInstanceId(
				Integer.valueOf(instanceId));
		JSONObject tableJO = build(table.getId(), sort, dir, Integer
				.valueOf(startIndex), Integer.valueOf(results));

		res.setContentType("application/json");
		res.getWriter().write(tableJO.toString());

		return null;
	}

	public JSONObject build(Integer tableId, String pSort, String pDir,
			Integer pStartIndex, Integer pResults) {
		try {
			Integer numRows = getQueryResultTableDao().getRowCount(tableId);

			String sort = StringUtils.isEmpty(pSort) ? "id" : pSort;
			String dir = StringUtils.isEmpty(pDir) ? "asc" : pDir;
			Integer startIndex = pStartIndex == null ? 0 : pStartIndex;
			Integer results = pResults == null ? numRows - startIndex : Math
					.min(pResults, numRows - startIndex);

			QueryResultTable table = getQueryResultTableDao().getById(tableId);
			
			if(PortletUtils.isCountQuery(table.getQueryInstance().getQuery().getXml())){
				sort = "count";
			}

			List<QueryResultRow> rows = null;
			if (ResultConstants.DATA_SERVICE_URL_COL_NAME.equals(sort)) {
				rows = getQueryResultTableDao().getSortedRowsByDataServiceUrl(
						table.getId(), dir, startIndex, results);
			} else {
				rows = getQueryResultTableDao().getSortedRows(table.getId(),
						sort, dir, startIndex, results);
			}

			return getQueryResultTableToJSONObjectBuilder().build(rows);

		} catch (Exception ex) {
			throw new RuntimeException("Error building JSON: "
					+ ex.getMessage(), ex);
		}

	}

	public QueryResultTableDao getQueryResultTableDao() {
		return queryResultTableDao;
	}

	public void setQueryResultTableDao(QueryResultTableDao queryResultTableDao) {
		this.queryResultTableDao = queryResultTableDao;
	}

	public QueryResultTableToJSONObjectBuilder getQueryResultTableToJSONObjectBuilder() {
		return queryResultTableToJSONObjectBuilder;
	}

	public void setQueryResultTableToJSONObjectBuilder(
			QueryResultTableToJSONObjectBuilder queryResultTableToJSONObjectBuilder) {
		this.queryResultTableToJSONObjectBuilder = queryResultTableToJSONObjectBuilder;
	}

}
