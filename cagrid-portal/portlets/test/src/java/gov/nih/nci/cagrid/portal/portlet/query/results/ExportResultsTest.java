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
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
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
					"test/data/cabioGeneQueryResults.xml"));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			// Table table = null;
			// try {
			// table = PortletUtils
			// .buildTableFromCQLResults(new ByteArrayInputStream(sb
			// .toString().getBytes()));
			// } catch (Exception ex) {
			// fail("Error building table: " + ex.getMessage());
			// ex.printStackTrace();
			// }

			// HSSFWorkbook wb = ExportResultsController.createWorkbook(table,
			// "query_results");
			HSSFWorkbook wb = PortletUtils
					.buildWorkbookFromCQLResults(null, new ByteArrayInputStream(sb
							.toString().getBytes()));
			HSSFSheet spreadSheet = wb.getSheetAt(0);
			assertTrue("expected 91 rows; lastRowNum is "
					+ spreadSheet.getLastRowNum(),
					spreadSheet.getLastRowNum() == 90);
			// FileOutputStream output = new FileOutputStream(new File(
			// "results.xls"));
			// wb.write(output);
			// output.flush();
			// output.close();

		} catch (Exception ex) {
			fail("Error encountered: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
}
