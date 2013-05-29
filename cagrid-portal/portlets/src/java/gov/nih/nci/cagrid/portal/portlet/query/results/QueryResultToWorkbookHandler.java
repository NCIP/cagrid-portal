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

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class QueryResultToWorkbookHandler extends BaseQueryResultHandler {

	private static final Log logger = LogFactory
			.getLog(QueryResultToWorkbookHandler.class);

	private HSSFWorkbook workbook;
	private HSSFSheet sheet;
	private HSSFRow headerRow;
	private HSSFRow currentRow;
	private int rowNum = 1;
	private List<String> colHeaders = new ArrayList<String>();
	private Map<String, String> currentRowValues = new HashMap<String, String>();

	/**
	 * 
	 */
	public QueryResultToWorkbookHandler() {
		workbook = new HSSFWorkbook();
		sheet = workbook.createSheet("query_results");
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		elementStack.push(new ElementInfo(uri, localName, qName, attributes));

		if (queryType == null) {
			if ("DCQLQueryResultsCollection".equals(localName)) {
				queryType = QueryType.DCQL;
			} else {
				queryType = QueryType.CQL;
			}
		} else if (resultType == null) {
			int size = elementStack.size();
			if (size == 2 && QueryType.CQL.equals(queryType) || size == 4
					&& QueryType.DCQL.equals(queryType)) {
				if ("ObjectResult".equals(localName)) {
					resultType = ResultType.OBJECT;
				} else if ("AttributeResult".equals(localName)) {
					resultType = ResultType.ATTRIBUTE;
				} else if ("CountResult".equals(localName)) {
					resultType = ResultType.COUNT;
				}
			}
		}

		if (resultType != null) {
			if (ResultType.COUNT.equals(resultType)) {

				headerRow = sheet.createRow(0);
				HSSFCell headerCell = headerRow.createCell((short) 0);
				headerCell.setCellValue("count");
				HSSFRow valueRow = sheet.createRow(1);
				HSSFCell valueCell = valueRow.createCell((short) 0);
				valueCell.setCellValue(attributes.getValue("count"));

			} else if (ResultType.ATTRIBUTE.equals(resultType)) {

				if ("AttributeResult".equals(localName)) {

					currentRow = sheet.createRow(rowNum++);
					currentRowValues.clear();

				} else if ("Attribute".equals(localName)) {

					String name = attributes.getValue("name");
					String value = attributes.getValue("value");
					if (headerRow == null) {
						colHeaders.add(name);
					}
					currentRowValues.put(name, value);
				}
			} else if (ResultType.OBJECT.equals(resultType)) {

				if ("ObjectResult".equals(localName)) {

					currentRow = sheet.createRow(rowNum++);
					currentRowValues.clear();

				} else if ("ObjectResult".equals(elementStack.get(elementStack
						.size() - 2).localName)) {

					for (int i = 0; i < attributes.getLength(); i++) {
						String name = attributes.getLocalName(i);
						String value = attributes.getValue(i);
						if (headerRow == null) {
							colHeaders.add(name);
						}
						currentRowValues.put(name, value);
					}
					if(headerRow == null){
						initHeaderRow();
					}
					setRowValues();
				}
			}
		}

	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		
		if ("AttributeResult".equals(localName)) {
			if (headerRow == null) {
				initHeaderRow();
			}
			setRowValues();
		}
		elementStack.pop();
	}

	public void characters(char[] ch, int start, int length)
			throws org.xml.sax.SAXException {

	}

	public void endDocument() {

	}
	
	private void setRowValues(){
		short colNum = 0;
		for(String colName : colHeaders){
			HSSFCell cell = currentRow.createCell(colNum++);
			cell.setCellValue(currentRowValues.get(colName));
		}	
	}
	
	private void initHeaderRow(){
		headerRow = sheet.createRow(0);
		if (getColumnNames() != null && getColumnNames().size() > 0) {
			colHeaders = getColumnNames();
		}
		short colNum = 0;
		for (String colName : colHeaders) {
			HSSFCell headerCell = headerRow.createCell(colNum++);
			headerCell.setCellValue(colName);
		}		
	}

	public HSSFWorkbook getWorkbook() {
		return workbook;
	}

	public static void main(String[] args) throws Exception {
		List<String> colNames = new ArrayList();
		colNames.add("description");
		colNames.add("histology");
		colNames.add("name");
		colNames.add("yadda");
		colNames.add("organ");
		colNames.add("id");
		SAXParserFactory fact = SAXParserFactory.newInstance();
		fact.setNamespaceAware(true);
		SAXParser parser = fact.newSAXParser();
		QueryResultToWorkbookHandler handler = new QueryResultToWorkbookHandler();
//		handler.setColumnNames(colNames);
		parser.parse(new FileInputStream("tissueQueryResults_dcql_atts.xml"),
				handler);
		handler.getWorkbook().write(
				new java.io.FileOutputStream("query_results.xls"));
	}

}
