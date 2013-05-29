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

import gov.nih.nci.cagrid.portal.portlet.util.PortletUtils;
import junit.framework.TestCase;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;

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
