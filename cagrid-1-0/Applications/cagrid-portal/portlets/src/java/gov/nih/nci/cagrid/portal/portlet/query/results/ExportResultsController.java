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
		// Table table = PortletUtils
		// .buildTableFromCQLResults(new ByteArrayInputStream(xml
		// .getBytes()));
		HSSFWorkbook wb = PortletUtils
				.buildWorkbookFromCQLResults(new ByteArrayInputStream(xml
						.getBytes()));

		response.setContentType("application/vnd.ms-excel");
		response.addHeader("Content-Disposition",
				"attachment;filename=\"query_results.xls\"");
		// HSSFWorkbook wb = createWorkbook(table, "query_results");
		wb.write(response.getOutputStream());

		return null;
	}

	// public static HSSFWorkbook createWorkbook(Table table, String title) {
	// HSSFWorkbook wb = new HSSFWorkbook();
	// HSSFSheet spreadSheet = wb.createSheet("query_results");
	// List<String> headers = table.getHeaders();
	// HSSFRow headerRow = spreadSheet.createRow(0);
	// for (short i = 0; i < headers.size(); i++) {
	// HSSFCell cell = headerRow.createCell(i);
	// cell.setCellValue(headers.get(i));
	// }
	// List<Map<String, Object>> rows = table.getRows();
	// for (short rowNum = 1; rowNum <= rows.size(); rowNum++) {
	// Map<String, Object> rowData = rows.get(rowNum - 1);
	// HSSFRow row = spreadSheet.createRow(rowNum);
	// for (short colNum = 0; colNum < headers.size(); colNum++) {
	// HSSFCell cell = row.createCell(colNum);
	// Object value = rowData.get(headers.get(colNum));
	// if (value != null) {
	// cell.setCellValue(value.toString());
	// }
	// }
	// }
	// return wb;
	// }

	@Required
	public QueryModel getQueryModel() {
		return queryModel;
	}

	public void setQueryModel(QueryModel queryModel) {
		this.queryModel = queryModel;
	}

}
