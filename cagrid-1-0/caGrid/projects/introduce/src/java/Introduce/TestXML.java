import java.io.File;
import java.util.List;

import javax.xml.namespace.QName;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.projectmobius.common.XMLUtilities;


public class TestXML {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String baseDir = "c:\\HelloWorld";
		String serviceName = "HelloWorld";
		String portTypeName= "NewServicePortType";
		String operationName = "newMethod";
		String fromServiceName= "NewService";
		String toServiceName = "HelloWorld";
		
		// parse the wsdl and get the operation text.....
		Document fromDoc = null;
		Document toDoc = null;
		try {
			fromDoc = XMLUtilities.fileNameToDocument(baseDir
				+ File.separator
				+ "schema"
				+ File.separator
				+ serviceName + File.separator
				+ fromServiceName + ".wsdl");
			toDoc = XMLUtilities.fileNameToDocument(baseDir
				+ File.separator
				+ "schema"
				+ File.separator
				+ serviceName + File.separator
				+ toServiceName + ".wsdl");
			List portTypes = fromDoc.getRootElement().getChildren("portType", fromDoc.getRootElement().getNamespace());
			for (int i = 0; i < portTypes.size(); i++) {
				Element el = (Element) portTypes.get(i);
				if (el.getAttributeValue("name").equals(portTypeName)) {
					List operations = el.getChildren("operation", fromDoc.getRootElement().getNamespace());
					for (int j = 0; j < operations.size(); j++) {
						Element opEl = (Element) operations.get(j);
						if (opEl.getAttributeValue("name").equals(operationName)) {
							// need to detach the el and add it to the service
							// which will be using it...
							List toportTypes = toDoc.getRootElement().getChildren("portType",
								toDoc.getRootElement().getNamespace());
							for (int i2 = 0; i2 < toportTypes.size(); i2++) {
								Element el2 = (Element) toportTypes.get(i2);
								if (el2.getAttributeValue("name").equals(
									toServiceName + "PortType")) {
									// found the right one... add to here
									Element copEl = (Element)opEl.clone();
									List copElChildren = copEl.getChildren();
									for(int childi = 0; childi < copElChildren.size(); childi++){
										Element copElChild = (Element)copElChildren.get(childi);
										String messageString = copElChild.getAttributeValue("message");
										Namespace ns = null;
										String prefix = "";
										String message = "";
										if(messageString.indexOf(":")>=0){
											prefix = messageString.substring(0,messageString.indexOf(":"));
											message = messageString.substring(messageString.indexOf(":")+1);
											ns = fromDoc.getRootElement().getNamespace(prefix);
											
										} else {
											message = messageString;
											ns = fromDoc.getRootElement().getNamespace();
										}
										List nslist = toDoc.getRootElement().getAdditionalNamespaces();
										for(int nsli = 0; nsli < nslist.size(); nsli++){
											Namespace tempns = (Namespace)nslist.get(nsli);
											System.out.println("Looking at prefix: " + tempns.getURI());
											if(tempns.getURI().equals(ns.getURI())){
												copElChild.setAttribute("message",tempns.getPrefix() + ":" + message);
											}
										}
									}
									el2.addContent(copEl);
									break;
								}
							}
							break;
						}
					}
					break;
				}
			}
			System.out.println(XMLUtilities.formatXML(XMLUtilities.documentToString(toDoc)));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
