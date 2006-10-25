package gov.nih.nci.cagrid.data.utilities;

import java.io.FileWriter;
import java.util.Iterator;

import org.jdom.Element;
import org.projectmobius.common.XMLUtilities;

/** 
 *  WsddUtil
 *  Utility for making small changes to a wsdd
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Oct 25, 2006 
 * @version $Id$ 
 */
public class WsddUtil {

	public static void setGlobalClientParameter(String clientWsddFile, String key, String value) throws Exception {
		Element wsddRoot = XMLUtilities.fileNameToDocument(clientWsddFile).getRootElement();
		Element configElement = wsddRoot.getChild("globalConfiguration", wsddRoot.getNamespace());
		setParameter(configElement, key, value);
		String editedWsdd = XMLUtilities.elementToString(wsddRoot);
		FileWriter writer = new FileWriter(clientWsddFile);
		writer.write(editedWsdd);
		writer.flush();
		writer.close();
	}
	
	
	public static void setServiceParameter(String serverWsddFile, String serviceName, String key, String value) throws Exception {
		Element wsddRoot = XMLUtilities.fileNameToDocument(serverWsddFile).getRootElement();
		Iterator serviceElemIter = wsddRoot.getChildren("service", wsddRoot.getNamespace()).iterator();
		while (serviceElemIter.hasNext()) {
			Element serviceElement = (Element) serviceElemIter.next();
			String name = serviceElement.getAttributeValue("name");
			if (name.endsWith("/" + serviceName)) {
				setParameter(serviceElement, key, value);
				break;
			}
		}
		String editedWsdd = XMLUtilities.elementToString(wsddRoot);
		FileWriter writer = new FileWriter(serverWsddFile);
		writer.write(editedWsdd);
		writer.flush();
		writer.close();
	}
	
	
	private static void setParameter(Element parentElem, String key, String value) {
		Iterator parameterElemIter = parentElem.getChildren("parameter", parentElem.getNamespace()).iterator();
		boolean parameterFound = false;
		while (parameterElemIter.hasNext()) {
			Element paramElement = (Element) parameterElemIter.next();
			if (paramElement.getAttributeValue("name").equals(key)) {
				paramElement.setAttribute("value", value);
				parameterFound = true;
				System.out.println("Parameter found and changed");
				break;
			}
		}
		if (!parameterFound) {
			System.out.println("Parameter was new!");
			Element paramElement = new Element("parameter", parentElem.getNamespace());
			paramElement.setAttribute("name", key);
			paramElement.setAttribute("value", value);
			parentElem.addContent(paramElement);
		}
	}
}
