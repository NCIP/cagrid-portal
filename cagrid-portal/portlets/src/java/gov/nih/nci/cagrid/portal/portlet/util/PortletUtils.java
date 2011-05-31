/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.util;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.fqp.common.DCQLConstants;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRelationshipInstance;
import gov.nih.nci.cagrid.portal.portlet.query.results.QueryResultToTableHandler;
import gov.nih.nci.cagrid.portal.portlet.query.results.QueryResultToWorkbookHandler;
import gov.nih.nci.cagrid.portal.util.PortalUtils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.portlet.PortletRequest;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.model.ResourceConstants;
import com.liferay.portal.service.ResourceLocalServiceUtil;

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

	public static boolean isCountQuery(String cqlQuery) {
		boolean isCountQuery = false;
		try {
			Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(
							new ByteArrayInputStream(cqlQuery.getBytes()));
			XPathFactory xpFact = XPathFactory.newInstance();
			String countOnly = (String) xpFact.newXPath().compile(
					"/CQLQuery/QueryModifier/@countOnly").evaluate(doc,
					XPathConstants.STRING);

			isCountQuery = "true".equals(countOnly);

		} catch (Exception ex) {
			String msg = "Error checking count query: " + ex.getMessage();
			logger.error(msg, ex);
			throw new RuntimeException(msg, ex);
		}
		return isCountQuery;
	}

	public static List<Class> getSubclasses(String packageName,
			final Class superclass) {
		String pkgName = packageName;
		if (pkgName == null) {
			pkgName = superclass.getPackage().getName();
		}
		List<Class> subclasses = new ArrayList<Class>();
		ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(
				false);
		provider.addIncludeFilter(new TypeFilter() {
			public boolean match(MetadataReader reader,
					MetadataReaderFactory factory) throws IOException {
				boolean match = false;
				try {
					Class klass = Class.forName(reader.getClassMetadata()
							.getClassName());
					match = superclass.isAssignableFrom(klass);
				} catch (Exception ex) {
					throw new RuntimeException("Error matching: "
							+ ex.getMessage(), ex);
				}
				return match;
			}
		});
		try {
			for (BeanDefinition def : provider.findCandidateComponents(pkgName)) {
				subclasses.add(Class.forName(def.getBeanClassName()));
			}
		} catch (Exception ex) {
			throw new RuntimeException("Error loading classes: "
					+ ex.getMessage(), ex);
		}
		return subclasses;
	}

	public static void main(String[] args) throws Exception {

		// for(Class klass :
		// getSubclasses("gov.nih.nci.cagrid.portal.domain.catalog",
		// CatalogEntry.class)){
		// System.out.println(klass.getName());
		// }
		String inFilePath = "/Users/joshua/Desktop/person_placeholder_180px.png";
		String outFilePath = "/Users/joshua/Desktop/person_placeholder_50px.png";
		BufferedImage image = ImageIO.read(new FileInputStream(inFilePath));
		System.out.println("Height: " + image.getHeight() + ", Width: "
				+ image.getWidth());
		Image thumb = image.getScaledInstance(50, -1, -1);

		ImageIO.write(ImageUtils.toBufferedImage(thumb), "png", new File(
				outFilePath));
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

	public static Object getMapValueForType(Class klass, Map map) {
		Object value = null;
		Class superclass = klass;
		while (true) {
			value = map.get(superclass.getName());
			if (value != null) {
				break;
			}
			superclass = superclass.getSuperclass();
			if (superclass == null) {
				break;
			}
		}
		return value;
	}

	public static void addResource(PortalUser portalUser, Class klass,
			Integer id) {
		String[] portalId = portalUser.getPortalId().split(":");
		try {
			ResourceLocalServiceUtil.addResources(Long.parseLong(portalId[0]),
					0, Long.parseLong(portalId[1]), klass.getName(), String
							.valueOf(id), false, false, false);
		} catch (Exception ex) {
			throw new RuntimeException("Error creating resource: "
					+ ex.getMessage(), ex);
		}

	}

	public static void deleteResource(PortalUser portalUser, Class klass,
			Integer id) {
		String[] portalId = portalUser.getPortalId().split(":");
		try {
			ResourceLocalServiceUtil.deleteResource(
					Long.parseLong(portalId[0]), klass.getName(),
					ResourceConstants.SCOPE_INDIVIDUAL, String.valueOf(id));
		} catch (Exception ex) {
			throw new RuntimeException("Error creating resource: "
					+ ex.getMessage(), ex);
		}
	}

	public static gov.nih.nci.cagrid.cqlquery.CQLQuery parseCQL(String queryXML) {
		try {
			return (gov.nih.nci.cagrid.cqlquery.CQLQuery) Utils
					.deserializeObject(new StringReader(queryXML),
							gov.nih.nci.cagrid.cqlquery.CQLQuery.class);
		} catch (Exception ex) {
			throw new RuntimeException("Error parsing CQL: " + ex.getMessage(),
					ex);
		}

	}

	public static String normalizeCQL(String queryXML) {
		StringWriter w = new StringWriter();
		try {
			Utils.serializeObject(parseCQL(queryXML),
					DataServiceConstants.CQL_QUERY_QNAME, w);
		} catch (Exception ex) {
			throw new RuntimeException("Error normalizing CQL: "
					+ ex.getMessage(), ex);
		}
		return w.toString();
	}

	public static gov.nih.nci.cagrid.dcql.DCQLQuery parseDCQL(String queryXML) {
		try {
			return (gov.nih.nci.cagrid.dcql.DCQLQuery) Utils.deserializeObject(
					new StringReader(queryXML),
					gov.nih.nci.cagrid.dcql.DCQLQuery.class);
		} catch (Exception ex) {
			throw new RuntimeException(
					"Error parsing DCQL: " + ex.getMessage(), ex);
		}

	}

	public static String normalizeDCQL(String queryXML) {
		StringWriter w = new StringWriter();
		try {
			Utils.serializeObject(parseDCQL(queryXML),
					DCQLConstants.DCQL_QUERY_QNAME, w);
		} catch (Exception ex) {
			throw new RuntimeException("Error normalizing DCQL: "
					+ ex.getMessage(), ex);
		}
		return w.toString();
	}

	public static boolean isDCQL(String queryXML) {
		boolean isDCQL = false;
		try {
			parseDCQL(queryXML);
			isDCQL = true;
		} catch (Exception ex) {

		}
		return isDCQL;
	}

	public static boolean isCQL(String queryXML) {
		boolean isCQL = false;
		try {
			parseCQL(queryXML);
			isCQL = true;
		} catch (Exception ex) {

		}
		return isCQL;
	}
}
