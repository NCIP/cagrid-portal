/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.util;

import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import gov.nih.nci.cagrid.portal.portlet.query.results.QueryResultToTableHandler;
import gov.nih.nci.cagrid.portal.portlet.query.results.QueryResultToWorkbookHandler;
import gov.nih.nci.cagrid.portal.util.PortalUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletRequest;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class PortletUtils {

	private static final Log logger = LogFactory.getLog(PortletUtils.class);

//	public static final String EMPTY_RESULT_PATTERN = "count(/CQLQueryResults/child::*) = 0";
//	public static final String OBJECT_RESULT_PATTERN = "count(/CQLQueryResults/ObjectResult) > 0";
//	public static final String ATTRIBUTE_RESULT_PATTERN = "count(/CQLQueryResults/AttributeResult) > 0";
//	public static final String COUNT_RESULT_PATTERN = "count(/CQLQueryResults/CountResult) = 1";

	private static SAXParser parser;
	
	public static HSSFWorkbook buildWorkbookFromCQLResults(InputStream in) throws Exception {
		if (parser == null) {
			SAXParserFactory fact = SAXParserFactory.newInstance();
			fact.setNamespaceAware(true);
			parser = fact.newSAXParser();
		}
		QueryResultToWorkbookHandler handler = new QueryResultToWorkbookHandler();
		parser.parse(in, handler);
		return handler.getWorkbook();
	}

	public static Table buildTableFromCQLResults(InputStream in)
			throws Exception {
		if (parser == null) {
			SAXParserFactory fact = SAXParserFactory.newInstance();
			fact.setNamespaceAware(true);
			parser = fact.newSAXParser();
		}
		QueryResultToTableHandler handler = new QueryResultToTableHandler();
		parser.parse(in, handler);
		return handler.getTable();

		// Table table = new Table();
		// Document doc = DocumentBuilderFactory.newInstance()
		// .newDocumentBuilder().parse(in);
		// XPathFactory xpFact = XPathFactory.newInstance();
		//
		// Boolean isEmpty = (Boolean) xpFact.newXPath().compile(
		// EMPTY_RESULT_PATTERN).evaluate(doc, XPathConstants.BOOLEAN);
		// if (isEmpty) {
		// return null;
		// }
		//
		// if (isObjectResult(doc)) {
		// populateObjectResults(doc, table);
		// } else if (isAttributeResult(doc)) {
		// populateAttributeResults(doc, table);
		// } else if (isCountResult(doc)) {
		// populateCountResult(doc, table);
		// }
		//
		// return table;

	}

	// private static void populateCountResult(Document doc, Table table)
	// throws Exception {
	// table.getHeaders().add("count");
	// XPathFactory xpFact = XPathFactory.newInstance();
	// Element countEl = (Element) xpFact.newXPath().compile(
	// "/CQLQueryResults/CountResult").evaluate(doc,
	// XPathConstants.NODE);
	// Map<String, Object> row = new HashMap<String, Object>();
	// row.put("count", countEl.getAttribute("count"));
	// table.getRows().add(row);
	// }
	//
	// private static boolean isCountResult(Document doc)
	// throws XPathExpressionException {
	// return (Boolean) XPathFactory.newInstance().newXPath().compile(
	// COUNT_RESULT_PATTERN).evaluate(doc, XPathConstants.BOOLEAN);
	//
	// }
	//
	// private static void populateAttributeResults(Document doc, Table table)
	// throws Exception {
	// XPathFactory xpFact = XPathFactory.newInstance();
	// NodeList attResults = (NodeList) xpFact.newXPath().compile(
	// "/CQLQueryResults/AttributeResult").evaluate(doc,
	// XPathConstants.NODESET);
	// for (int i = 0; i < attResults.getLength(); i++) {
	// Element attResult = (Element) attResults.item(i);
	// NodeList attEls = (NodeList) xpFact.newXPath().compile(
	// "./Attribute").evaluate(attResult, XPathConstants.NODESET);
	// Map<String, Object> row = new HashMap<String, Object>();
	// for (int j = 0; j < attEls.getLength(); j++) {
	// Element attEl = (Element) attEls.item(j);
	// String name = attEl.getAttribute("name");
	// String value = attEl.getAttribute("value");
	// if (!table.getHeaders().contains(name)) {
	// table.getHeaders().add(name);
	// }
	//
	// row.put(name, value);
	// }
	// table.getRows().add(row);
	// }
	//
	// }
	//
	// private static boolean isAttributeResult(Document doc)
	// throws XPathExpressionException {
	// return (Boolean) XPathFactory.newInstance().newXPath().compile(
	// ATTRIBUTE_RESULT_PATTERN).evaluate(doc, XPathConstants.BOOLEAN);
	// }
	//
	// private static void populateObjectResults(Document doc, Table table)
	// throws Exception {
	//
	// XPathFactory xpFact = XPathFactory.newInstance();
	// NodeList objResults = (NodeList) xpFact.newXPath().compile(
	// "/CQLQueryResults/ObjectResult/child::*").evaluate(doc,
	// XPathConstants.NODESET);
	// for (int i = 0; i < objResults.getLength(); i++) {
	// Element objResult = (Element) objResults.item(i);
	// NodeList atts = (NodeList) xpFact.newXPath().compile(
	// "./attribute::*").evaluate(objResult,
	// XPathConstants.NODESET);
	// Map<String, Object> row = new HashMap<String, Object>();
	// for (int j = 0; j < atts.getLength(); j++) {
	// Attr att = (Attr) atts.item(j);
	// if (!table.getHeaders().contains(att.getName())) {
	// table.getHeaders().add(att.getName());
	// }
	//
	// row.put(att.getName(), att.getValue());
	// }
	// table.getRows().add(row);
	// }
	// }
	//
	// private static boolean isObjectResult(Document doc)
	// throws XPathExpressionException {
	// return (Boolean) XPathFactory.newInstance().newXPath().compile(
	// OBJECT_RESULT_PATTERN).evaluate(doc, XPathConstants.BOOLEAN);
	// }

	public static void doScrollOp(PortletRequest request, Scroller scroller) {
		String scrollOp = request.getParameter("scrollOp");
		logger.debug("scrollOp = '" + scrollOp + "'");
		if (!PortalUtils.isEmpty(scrollOp)) {
			if ("first".equals(scrollOp)) {
				scroller.first();
			} else if ("previous".equals(scrollOp)) {
				scroller.previous();
			} else if ("next".equals(scrollOp)) {
				scroller.next();
			} else if ("last".equals(scrollOp)) {
				scroller.last();
			} else {
				logger.warn("Invalid scroll operation: '" + scrollOp + "'");
			}
		}
	}

	public static String[] parsePath(String path) {
		String[] parts = null;
		int idx = path.indexOf("/");
		if (idx == -1) {
			parts = new String[] { path };
		} else {
			parts = new String[] { path.substring(0, idx),
					path.substring(idx + 1) };
		}
		return parts;
	}

	public static List<GridService> filterBannedServices(List<GridService> in) {
		return filterServicesByStatus(in, ServiceStatus.BANNED);
	}

	public static List<GridService> filterDormantServices(List<GridService> in) {
		return filterServicesByStatus(in, ServiceStatus.DORMANT);

	}

	public static List<GridService> filterServicesByInvalidMetadata(
			List<GridService> in) {
		List<GridService> out = new ArrayList<GridService>();
		for (GridService svc : in) {
			boolean filter = false;
			try {
				if (svc.getServiceMetadata() == null) {
					filter = true;
				} else if (svc.getServiceMetadata().getServiceDescription() == null) {
					filter = true;
				} else if (svc.getServiceMetadata().getServiceDescription()
						.getName() == null) {
					filter = true;
				}
			} catch (Exception e) {
				filter = true;
			}
			if (!filter) {
				out.add(svc);
			}
		}
		return out;
	}

	public static List<GridService> filterServicesByStatus(
			List<GridService> in, ServiceStatus... statuses) {
		List<GridService> out = new ArrayList<GridService>();
		for (GridService svc : in) {
			boolean filter = false;
			for (ServiceStatus status : statuses) {
				if (status.equals(svc.getCurrentStatus())) {
					filter = true;
					break;
				}
			}
			if (!filter) {
				out.add(svc);
			}
		}
		return out;
	}

}
