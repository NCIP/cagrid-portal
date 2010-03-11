/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.results;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class XMLQueryResultToJSONHandler extends BaseQueryResultHandler {

	private static final Log logger = LogFactory
			.getLog(XMLQueryResultToJSONHandler.class);

	private JSONObject table = new JSONObject();
	private Map<String, Object> currentRow;
	private int numRows = 0;
	private int maxValueLength = 256;

	/**
	 * 
	 */
	public XMLQueryResultToJSONHandler() {

	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		try {

			elementStack
					.push(new ElementInfo(uri, localName, qName, attributes));

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

					Map<String, Object> row = new HashMap<String, Object>();
					row.put("count", attributes.getValue("count"));
					table.append("rows", row);
					numRows++;

				} else if (ResultType.ATTRIBUTE.equals(resultType)) {

					if ("AttributeResult".equals(localName)) {

						currentRow = new HashMap<String, Object>();
						table.append("rows", currentRow);
						numRows++;

					} else if ("Attribute".equals(localName)) {

						String name = attributes.getValue("name");
						String value = attributes.getValue("value");
						currentRow.put(name, sizeValue(value));
					}
				} else if (ResultType.OBJECT.equals(resultType)) {

					if ("ObjectResult".equals(localName)) {

						currentRow = new HashMap<String, Object>();
						table.append("rows", currentRow);
						numRows++;

					} else if ("ObjectResult".equals(elementStack
							.get(elementStack.size() - 2).localName)) {

						for (int i = 0; i < attributes.getLength(); i++) {
							String name = attributes.getLocalName(i);
							String value = attributes.getValue(i);
							currentRow.put(name, sizeValue(value));
						}
					}
				}
			}
		} catch (Exception ex) {
			throw new SAXException("Error parsing: " + ex.getMessage(), ex);
		}
	}

	private String sizeValue(String value) {
		String out = null;
		if (value != null) {
			if (value.length() > getMaxValueLength()) {
				out = value.substring(0, getMaxValueLength())
						+ "[export to see full results]";
			} else {
				out = value;
			}
		}
		return out;
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		elementStack.pop();
	}

	public void characters(char[] ch, int start, int length)
			throws org.xml.sax.SAXException {

	}

	public void endDocument() {
		try{
			table.put("numRows", numRows);
		}catch(Exception ex){
			throw new RuntimeException("Error finishing document: " + ex.getMessage(), ex);
		}
	}

	public JSONObject getTable() {
		return table;
	}

	public static void main(String[] args) throws Exception {
		SAXParserFactory fact = SAXParserFactory.newInstance();
		fact.setNamespaceAware(true);
		SAXParser parser = fact.newSAXParser();
		XMLQueryResultToJSONHandler handler = new XMLQueryResultToJSONHandler();
		parser.parse(
				new FileInputStream("test/data/cabioGeneQueryResults.xml"),
				handler);
		System.out.println(handler.getTable());

	}

	public int getMaxValueLength() {
		return maxValueLength;
	}

	public void setMaxValueLength(int maxValueLength) {
		this.maxValueLength = maxValueLength;
	}
}
