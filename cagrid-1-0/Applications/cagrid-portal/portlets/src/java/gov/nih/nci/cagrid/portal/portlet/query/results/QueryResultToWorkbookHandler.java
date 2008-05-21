/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.results;

import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Set;

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
	private short colNum = 0;
	private Set<String> colHeaders = new HashSet<String>();

	/**
	 * 
	 */
	public QueryResultToWorkbookHandler() {
		workbook = new HSSFWorkbook();
		sheet = workbook.createSheet("query_results");
		headerRow = sheet.createRow(0);
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

				HSSFRow headerRow = sheet.createRow(0);
				HSSFCell headerCell = headerRow.createCell((short) 0);
				headerCell.setCellValue("count");
				HSSFRow valueRow = sheet.createRow(1);
				HSSFCell valueCell = valueRow.createCell((short) 0);
				valueCell.setCellValue(attributes.getValue("count"));

			} else if (ResultType.ATTRIBUTE.equals(resultType)) {

				if ("AttributeResult".equals(localName)) {

					currentRow = sheet.createRow(rowNum++);

				} else if ("Attribute".equals(localName)) {

					String name = attributes.getValue("name");
					String value = attributes.getValue("value");
					if (!colHeaders.contains(name)) {
						colHeaders.add(name);
						HSSFCell headerCell = headerRow.createCell(colNum);
						headerCell.setCellValue(name);
					}
					HSSFCell valueCell = currentRow.createCell(colNum);
					valueCell.setCellValue(value);
					colNum++;
				}
			} else if (ResultType.OBJECT.equals(resultType)) {

				if ("ObjectResult".equals(localName)) {

					currentRow = sheet.createRow(rowNum++);

				} else if ("ObjectResult".equals(elementStack.get(elementStack
						.size() - 2).localName)) {

					for (int i = 0; i < attributes.getLength(); i++) {
						String name = attributes.getLocalName(i);
						String value = attributes.getValue(i);
						if (!colHeaders.contains(name)) {
							colHeaders.add(name);
							HSSFCell headerCell = headerRow.createCell(colNum);
							headerCell.setCellValue(name);
						}
						HSSFCell valueCell = currentRow.createCell((short) i);
						valueCell.setCellValue(value);
					}
				}
			}
		}

	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if ("AttributeResult".equals(localName)) {
			colNum = 0;
		}
		elementStack.pop();
	}

	public void characters(char[] ch, int start, int length)
			throws org.xml.sax.SAXException {

	}

	public void endDocument() {

	}

	public HSSFWorkbook getWorkbook() {
		return workbook;
	}

	public static void main(String[] args) throws Exception {
		SAXParserFactory fact = SAXParserFactory.newInstance();
		fact.setNamespaceAware(true);
		SAXParser parser = fact.newSAXParser();
		QueryResultToWorkbookHandler handler = new QueryResultToWorkbookHandler();
		parser.parse(new FileInputStream("tissueQueryResults_cql_count.xml"),
				handler);
		handler.getWorkbook().write(
				new java.io.FileOutputStream("query_results.xls"));
	}

}
