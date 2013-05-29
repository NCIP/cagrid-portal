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

import gov.nih.nci.cagrid.portal.dao.QueryResultTableDao;
import gov.nih.nci.cagrid.portal.domain.table.QueryResultTable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ExportQueryResultTableToHSSFWorkbookController extends
		AbstractController {

	private QueryResultTableDao queryResultTableDao;
	private QueryResultTableToHSSFWorkbookBuilder queryResultTableToHSSFWorkbookBuilder;

	/**
	 * 
	 */
	public ExportQueryResultTableToHSSFWorkbookController() {

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
		doExport(Integer.valueOf(instanceId), res);

		return null;
	}
	
	protected void doExport(Integer instanceId, HttpServletResponse res) throws Exception {
		QueryResultTable table = getQueryResultTableDao().getByQueryInstanceId(
				Integer.valueOf(instanceId));
		HSSFWorkbook wb = this.getQueryResultTableToHSSFWorkbookBuilder()
				.build(table.getId());
		res.setContentType("application/vnd.ms-excel");
		res.addHeader("Content-Disposition",
				"attachment;filename=\"query_results.xls\"");
		wb.write(res.getOutputStream());
	}

	public QueryResultTableToHSSFWorkbookBuilder getQueryResultTableToHSSFWorkbookBuilder() {
		return queryResultTableToHSSFWorkbookBuilder;
	}

	public void setQueryResultTableToHSSFWorkbookBuilder(
			QueryResultTableToHSSFWorkbookBuilder queryResultTableToHSSFWorkbookBuilder) {
		this.queryResultTableToHSSFWorkbookBuilder = queryResultTableToHSSFWorkbookBuilder;
	}

	public QueryResultTableDao getQueryResultTableDao() {
		return queryResultTableDao;
	}

	public void setQueryResultTableDao(QueryResultTableDao queryResultTableDao) {
		this.queryResultTableDao = queryResultTableDao;
	}

}
