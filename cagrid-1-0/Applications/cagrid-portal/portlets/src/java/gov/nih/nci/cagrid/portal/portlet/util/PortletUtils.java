/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.util;

import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import gov.nih.nci.cagrid.portal.portlet.query.results.QueryResultToTableHandler;
import gov.nih.nci.cagrid.portal.portlet.query.results.QueryResultToWorkbookHandler;
import gov.nih.nci.cagrid.portal.util.PortalUtils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.w3c.dom.NodeList;

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
		Set<String> urls = new HashSet<String>();
		Document doc = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder().parse(
						new FileInputStream("tissueQuery.xml"));
		XPathFactory xpFact = XPathFactory.newInstance();
		NodeList urlEls = (NodeList) xpFact.newXPath().compile(
				"/DCQLQuery/targetServiceURL").evaluate(doc,
				XPathConstants.NODESET);
		for (int i = 0; i < urlEls.getLength(); i++) {
			Element el = (Element) urlEls.item(i);
			urls.add(el.getTextContent());
		}
		System.out.println(urls);
	}

	public static Set<String> getTargetServiceUrls(String dcql)
			throws Exception {
		Set<String> urls = new HashSet<String>();
		Document doc = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder().parse(
						new ByteArrayInputStream(dcql.getBytes()));
		XPathFactory xpFact = XPathFactory.newInstance();
		NodeList urlEls = (NodeList) xpFact.newXPath().compile(
				"/DCQLQuery/targetServiceURL").evaluate(doc,
				XPathConstants.NODESET);
		for (int i = 0; i < urlEls.getLength(); i++) {
			Element el = (Element) urlEls.item(i);
			urls.add(el.getTextContent());
		}
		return urls;
	}
	


}
