/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.util;

import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQuery;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.portlet.query.results.QueryResultToTableHandler;
import gov.nih.nci.cagrid.portal.portlet.query.results.QueryResultToWorkbookHandler;
import gov.nih.nci.cagrid.portal.util.PortalUtils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletRequest;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class PortletUtils {

	private static final Log logger = LogFactory.getLog(PortletUtils.class);

	// public static final String EMPTY_RESULT_PATTERN =
	// "count(/CQLQueryResults/child::*) = 0";
	// public static final String OBJECT_RESULT_PATTERN =
	// "count(/CQLQueryResults/ObjectResult) > 0";
	// public static final String ATTRIBUTE_RESULT_PATTERN =
	// "count(/CQLQueryResults/AttributeResult) > 0";
	// public static final String COUNT_RESULT_PATTERN =
	// "count(/CQLQueryResults/CountResult) = 1";

	private static SAXParser parser;

	public static HSSFWorkbook buildWorkbookFromCQLResults(
			List<String> colNames, InputStream in) throws Exception {
		if (parser == null) {
			SAXParserFactory fact = SAXParserFactory.newInstance();
			fact.setNamespaceAware(true);
			parser = fact.newSAXParser();
		}
		QueryResultToWorkbookHandler handler = new QueryResultToWorkbookHandler();
		if (colNames != null) {
			handler.setColumnNames(colNames);
		}
		parser.parse(in, handler);
		return handler.getWorkbook();
	}

	public static Table buildTableFromCQLResults(List<String> colNames,
			InputStream in) throws Exception {
		if (parser == null) {
			SAXParserFactory fact = SAXParserFactory.newInstance();
			fact.setNamespaceAware(true);
			parser = fact.newSAXParser();
		}
		QueryResultToTableHandler handler = new QueryResultToTableHandler();
		if (colNames != null) {
			handler.setColumnNames(colNames);
		}
		parser.parse(in, handler);
		return handler.getTable();
	}

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

	public static String getTargetUMLClassName(String cqlQuery) {
		String targetClassName = null;
		try {

			// NOTE: We don't need to worry about XML bomb here since,
			// CQL was already validated (i.e. parsed with Axis API which
			// disables DOCTYPE).
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(
							new ByteArrayInputStream(cqlQuery.getBytes()));
			XPathFactory xpFact = XPathFactory.newInstance();
			Element targetEl = (Element) xpFact.newXPath().compile(
					"/CQLQuery/Target").evaluate(doc, XPathConstants.NODE);
			if (targetEl == null) {
				targetEl = (Element) xpFact.newXPath().compile(
						"/DCQLQuery/TargetObject").evaluate(doc,
						XPathConstants.NODE);
			}
			if (targetEl != null) {
				targetClassName = targetEl.getAttribute("name");
			}
		} catch (Exception ex) {
			logger.error("Error getting target class name: " + ex.getMessage(),
					ex);
		}
		return targetClassName;
	}

	public static void main(String[] args) throws Exception {
		String targetClassName = null;
		Document doc = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder().parse(new FileInputStream("aggr/test/data/cabioMouseQuery.xml"));
		XPathFactory xpFact = XPathFactory.newInstance();
		Element targetEl = (Element) xpFact.newXPath().compile(
				"/CQLQuery/Target").evaluate(doc, XPathConstants.NODE);
		if (targetEl == null) {
			targetEl = (Element) xpFact.newXPath().compile(
					"/DCQLQuery/TargetObject").evaluate(doc,
					XPathConstants.NODE);
		}
		if (targetEl != null) {
			targetClassName = targetEl.getAttribute("name");
		}
		System.out.println("UMLClass: " + targetClassName);
	}

}
