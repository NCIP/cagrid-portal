/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.results;

import gov.nih.nci.cagrid.portal.portlet.util.Table;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class QueryResultToTableHandler extends BaseQueryResultHandler {

	private static final Log logger = LogFactory
			.getLog(QueryResultToTableHandler.class);

	private Table table = new Table();
	private Map<String, Object> currentRow;

	/**
	 * 
	 */
	public QueryResultToTableHandler() {

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

				table.getHeaders().add("count");
				Map<String, Object> row = new HashMap<String, Object>();
				row.put("count", attributes.getValue("count"));
				table.getRows().add(row);

			} else if (ResultType.ATTRIBUTE.equals(resultType)) {

				if ("AttributeResult".equals(localName)) {

					currentRow = new HashMap<String, Object>();
					table.getRows().add(currentRow);

				} else if ("Attribute".equals(localName)) {

					String name = attributes.getValue("name");
					String value = attributes.getValue("value");
					if (!table.getHeaders().contains(name)) {
						table.getHeaders().add(name);
					}
					currentRow.put(name, value);
				}
			} else if (ResultType.OBJECT.equals(resultType)) {

				if ("ObjectResult".equals(localName)) {

					currentRow = new HashMap<String, Object>();
					table.getRows().add(currentRow);

				} else if ("ObjectResult".equals(elementStack.get(elementStack
						.size() - 2).localName)) {

					for (int i = 0; i < attributes.getLength(); i++) {
						String name = attributes.getLocalName(i);
						String value = attributes.getValue(i);
						if (!table.getHeaders().contains(name)) {
							table.getHeaders().add(name);
						}
						currentRow.put(name, value);
					}
				}
			}
		}

	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		elementStack.pop();
	}

	public void characters(char[] ch, int start, int length)
			throws org.xml.sax.SAXException {

	}

	public void endDocument() {
		if (ResultType.OBJECT.equals(resultType) && getColumnNames() != null
				&& getColumnNames().size() > 0) {
			table.setHeaders(getColumnNames());
		}
	}

	public Table getTable() {
		return table;
	}

	public static void main(String[] args) throws Exception {
		SAXParserFactory fact = SAXParserFactory.newInstance();
		fact.setNamespaceAware(true);
		SAXParser parser = fact.newSAXParser();
		QueryResultToTableHandler handler = new QueryResultToTableHandler();
		parser.parse(new FileInputStream("tissueQueryResults_dcql_atts.xml"),
				handler);
		System.out.println(handler.getTable());
	}
}
