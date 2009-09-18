/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.results;

import gov.nih.nci.cagrid.portal.portlet.query.QueryModel;
import gov.nih.nci.cagrid.portal.portlet.util.PortletUtils;

import java.io.ByteArrayInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ExportResultsController extends AbstractController {

	private QueryModel queryModel;

	/**
	 * 
	 */
	public ExportResultsController() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String xml = getQueryModel().getSelectedQueryInstance().getResult();
		HSSFWorkbook wb = PortletUtils.buildWorkbookFromCQLResults(
				getQueryModel().getQueryResultsColumnNames(),
				new ByteArrayInputStream(xml.getBytes()));

		response.setContentType("application/vnd.ms-excel");
		response.addHeader("Content-Disposition",
				"attachment;filename=\"query_results.xls\"");

		wb.write(response.getOutputStream());

		return null;
	}

	@Required
	public QueryModel getQueryModel() {
		return queryModel;
	}

	public void setQueryModel(QueryModel queryModel) {
		this.queryModel = queryModel;
	}

}
