package gov.nih.nci.cagrid.data.common;

import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;

import java.io.File;
import java.util.Iterator;

import org.jdom.Element;
import org.jdom.Namespace;
import org.projectmobius.common.XMLUtilities;

/** 
 *  CastorMappingUtil
 *  Utility for making edits to a castor mapping xml document
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Oct 26, 2006 
 * @version $Id: CastorMappingUtil.java,v 1.2 2007-08-15 18:10:21 dervin Exp $ 
 */
public class CastorMappingUtil {

	/**
	 * Edits a castor mapping XML file to change the namespace of all classes in a package
	 * 
	 * @param mapping
	 * 		The text of the castor mapping file
	 * @param packageName
	 * 		The name of the package to change the namespace mapping of
	 * @param namespace
	 * 		The namespace to remap the package's classes to
	 * @return
	 * 		The modified text of the castor mapping file
	 * @throws Exception
	 */
	public static String changeNamespaceOfPackage(String mapping, String packageName, String namespace) throws Exception {
		Element mappingRoot = XMLUtilities.stringToDocument(mapping).getRootElement();
		// get class elements
		String oldNamespace = null;
		String oldPrefix = null;
		Iterator classElemIter = mappingRoot.getChildren("class", mappingRoot.getNamespace()).iterator();
		while (classElemIter.hasNext()) {
			Element classElem = (Element) classElemIter.next();
			String className = classElem.getAttributeValue("name");
			int dotIndex = className.lastIndexOf('.');
			String classPackage = className.substring(0, dotIndex);
			if (classPackage.equals(packageName)) {
				Element mapToElem = classElem.getChild("map-to", classElem.getNamespace());
				if (oldNamespace == null) {
					// keep a record of the old namespace for the package
					oldNamespace = mapToElem.getAttributeValue("ns-uri");
					oldPrefix = mapToElem.getAttributeValue("ns-prefix");
				}
				// change the namespace in the map-to element
				mapToElem.setAttribute("ns-uri", namespace);
			}
		}
		if (oldNamespace != null) {
			// re-walk every class in the mapping, this time looking for attributes
			// of those classes which bind to the old namespace
			classElemIter = mappingRoot.getChildren("class", mappingRoot.getNamespace()).iterator();
			while (classElemIter.hasNext()) {
				Element classElem = (Element) classElemIter.next();
				Iterator fieldElemIter = classElem.getChildren("field", classElem.getNamespace()).iterator();
				while (fieldElemIter.hasNext()) {
					Element fieldElem = (Element) fieldElemIter.next();
					Element bindXmlElement = fieldElem.getChild("bind-xml", fieldElem.getNamespace());
					Namespace elemNamespace = bindXmlElement.getNamespace(oldPrefix);
					if (elemNamespace != null && elemNamespace.getURI().equals(oldNamespace)) {
						// TODO: This is probably horribly inefficient, see if it can be improved
						String elementString = XMLUtilities.elementToString(bindXmlElement);
						int nsStart = elementString.indexOf(oldNamespace);
						int nsEnd = nsStart + oldNamespace.length();
						String changedString = elementString.substring(0, nsStart) + namespace + elementString.substring(nsEnd);
						Element changedElement = XMLUtilities.stringToDocument(changedString).detachRootElement();
						fieldElem.removeContent(bindXmlElement);
						fieldElem.addContent(changedElement);
					}
				}
			}
		}
		return XMLUtilities.elementToString(mappingRoot);
	}
	
	
	public static String getCustomCastorMappingFileName(ServiceInformation serviceInfo) {
		String mappingOut = serviceInfo.getBaseDirectory().getAbsolutePath() 
			+ File.separator + "src" + File.separator 
			+ getCustomCastorMappingName(serviceInfo);
		return mappingOut;
	}
	
	
	public static String getCustomCastorMappingName(ServiceInformation serviceInfo) {
		String mappingName = serviceInfo.getServices().getService(0)
				.getPackageName().replace('.', '/')
			+ '/' + serviceInfo.getServices().getService(0).getName() 
			+ "-" + DataServiceConstants.CACORE_CASTOR_MAPPING_FILE;
		return mappingName;
	}
}
