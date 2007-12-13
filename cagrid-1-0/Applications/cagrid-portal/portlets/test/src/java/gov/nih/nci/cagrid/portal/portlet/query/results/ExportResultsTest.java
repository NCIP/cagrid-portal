/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.results;

import gov.nih.nci.cagrid.portal.portlet.util.PortletUtils;
import gov.nih.nci.cagrid.portal.portlet.util.Table;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ExportResultsTest extends TestCase {

	/**
	 * 
	 */
	public ExportResultsTest() {

	}

	/**
	 * @param name
	 */
	public ExportResultsTest(String name) {
		super(name);
	}

	public void testExportResults() {
		try {
			StringBuilder sb = new StringBuilder();
			BufferedReader br = new BufferedReader(new FileReader(
					"portlets/test/data/cabioGeneQueryResults.xml"));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			Table table = null;
			try {
				table = PortletUtils
						.buildTableFromCQLResults(new ByteArrayInputStream(sb
								.toString().getBytes()));
			} catch (Exception ex) {
				fail("Error building table: " + ex.getMessage());
				ex.printStackTrace();
			}

			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet spreadSheet = wb.createSheet("query results");
			List<String> headers = table.getHeaders();
			HSSFRow headerRow = spreadSheet.createRow(0);
			for (short i = 0; i < headers.size(); i++) {
				HSSFCell cell = headerRow.createCell(i);
				cell.setCellValue(headers.get(i));
			}
			List<Map<String, Object>> rows = table.getRows();
			for (short rowNum = 1; rowNum < rows.size(); rowNum++) {
				Map<String, Object> rowData = rows.get(rowNum);
				HSSFRow row = spreadSheet.createRow(rowNum);
				for (short colNum = 0; colNum < headers.size(); colNum++) {
					HSSFCell cell = row.createCell(colNum);
					Object value = rowData.get(headers.get(colNum));
					if (value != null) {
						cell.setCellValue(value.toString());
					}
				}
			}
			assertTrue("expected 90 rows; lastRowNum is " + spreadSheet.getLastRowNum(), spreadSheet.getLastRowNum() == 89);
//			FileOutputStream output = new FileOutputStream(new File(
//					"results.xls"));
//			wb.write(output);
//			output.flush();
//			output.close();

		} catch (Exception ex) {
			fail("Error encountered: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
}
