package gov.nih.nci.cagrid.data.upgrades;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.introduce.common.FileFilters;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.filter.Filter;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.XMLUtilities;

/** 
 *  Upgrade1pt0to1pt1
 *  Utility to upgrade a caGrid 1.0 data service to a 1.1 data service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Feb 8, 2007 
 * @version $Id: Upgrade1pt0to1pt1.java,v 1.2 2007-02-16 16:39:51 dervin Exp $ 
 */
public class Upgrade1pt0to1pt1 implements DataServiceUpgrade {
	public static final String EXTENSIONS_NAMESPACE = "gme://gov.nih.nci.cagrid.introduce/1/Extension";
	public static final String DATA_NAMESPACE = "http://CQL.caBIG/1/gov.nih.nci.cagrid.data.extension";
	
	public void upgrade(File inServiceDir, File outServiceDir) throws UpgradeException {
		Element upgradedModel = convertModel(inServiceDir);
		// write the new model to the output directory
		String xmlText = XMLUtilities.elementToString(upgradedModel);
		try {
			Utils.stringBufferToFile(new StringBuffer(xmlText), outServiceDir.getAbsolutePath() + File.separator + "introduce.xml");
		} catch (IOException ex) {
			throw new UpgradeException("Error saving updated model: " + ex.getMessage(), ex);
		}
		// fire off introduce to build the new model
	}
	
	
	private Element convertModel(File inServiceDir) throws UpgradeException {
		// load the introduce.xml from the input service
		Element inputModel = null;
		try {
			inputModel = XMLUtilities.fileNameToDocument(
				inServiceDir.getAbsolutePath() + File.separator + "introduce.xml").getRootElement();
		} catch (MobiusException ex) {
			throw new UpgradeException(ex);
		}
		// start with the old model
		Element outputModel = (Element) inputModel.clone();
		// locate and remove the data service extension data element
		Element extensionsElement = outputModel.getChild("Extensions", Namespace.getNamespace(EXTENSIONS_NAMESPACE));
		Element dataExtensionElement = extensionsElement.getChild("data", extensionsElement.getNamespace());
		if (dataExtensionElement == null) {
			throw new UpgradeException("No data service extension found in service model");
		}
		// get the extension data
		Element extensionDataContainer = dataExtensionElement.getChild("ExtensionData");
		Element extensionData = extensionDataContainer.getChild("Data", Namespace.getNamespace(DATA_NAMESPACE));
		
		// additional libraries / jar names elements are unchanged
		// get cadsr information
		Element cadsrInfo = extensionData.getChild("CadsrInformation", extensionData.getNamespace());
		// now we have a noDomainModel boolean flag...
		boolean hasCadsrUrl = cadsrInfo.getAttributeValue("serviceUrl") != null;
		boolean usingSuppliedModel = cadsrInfo.getAttributeValue("useSuppliedModel").equals("true");
		boolean noDomainModel = (!hasCadsrUrl && !usingSuppliedModel);
		cadsrInfo.setAttribute("noDomainModel", String.valueOf(noDomainModel));
		
		// service properties now contain CQL Query Processor configuration
		// get the current config properties out of the data element
		Element procConfig = extensionData.getChild("CQLProcessorConfig", extensionData.getNamespace());
		Properties configuredProps = new Properties();
		Iterator configuredPropElemIter = procConfig.getChildren("Property", procConfig.getNamespace()).iterator();
		while (configuredPropElemIter.hasNext()) {
			Element propElem = (Element) configuredPropElemIter.next();
			String key = propElem.getAttributeValue("name");
			String value = propElem.getAttributeValue("value");
			configuredProps.setProperty(key, value);
		}
		
		// now those properties must be removed
		extensionData.removeChild("CQLProcessorConfig", extensionData.getNamespace());
		
		// load up service properties to find the query processor class name
		Element servicePropsElem = outputModel.getChild("ServiceProperties", outputModel.getNamespace());
		Iterator propsElemIter = servicePropsElem.getChildren("Property", servicePropsElem.getNamespace()).iterator();
		String queryProcessorClassName = null;
		while (propsElemIter.hasNext()) {
			Element propElem = (Element) propsElemIter.next();
			String key = propElem.getAttributeValue("key");
			if (key.equals("queryProcessorClass")) {
				queryProcessorClassName = propElem.getAttributeValue("value");
				break;
			}
		}
		// remove the old cql query processor config service properties
		Filter oldPropertyFilter = new Filter() {
			public boolean matches(Object o) {
				if (o instanceof Element) {
					Element e = (Element) o;
					return (e.getAttribute("key") != null)
						&& e.getAttributeValue("key").startsWith("cqlQueryProcessorConfig_");
				}
				return false;
			}
		};
		servicePropsElem.removeContent(oldPropertyFilter);
		if (queryProcessorClassName == null) {
			throw new UpgradeException("No query processor class property found in service");
		}
		// load the query processor so we can ask it some questions
		CQLQueryProcessor proc = loadQueryProcessorInstance(inServiceDir, queryProcessorClassName);
		// get the properties for the query processor
		Properties qpProps = proc.getRequiredParameters();
		// set the user configured properties
		Enumeration keyEnum = configuredProps.keys();
		while (keyEnum.hasMoreElements()) {
			String key = (String) keyEnum.nextElement();
			String value = qpProps.getProperty(key);
			if (configuredProps.containsKey(key)) {
				value = configuredProps.getProperty(key);
			}
			// add the property to the service properties
			String expanededKey = "cqlQueryProcessorConfig_" + key;
			Element propertyElement = new Element("Property", servicePropsElem.getNamespace());
			propertyElement.setAttribute("isFromETC", "false");
			propertyElement.setAttribute("key", expanededKey);
			propertyElement.setAttribute("value", value);
			servicePropsElem.addContent(propertyElement);
		}
		
		return outputModel;
	}
	
	
	private CQLQueryProcessor loadQueryProcessorInstance(File inServiceDir, String queryProcessorClassName)
		throws UpgradeException {
		// reflect load the query processor (this should live in the service's lib dir)
		File libDir = new File(inServiceDir.getAbsolutePath() + File.separator + "lib");
		File[] libs = libDir.listFiles(new FileFilters.JarFileFilter());
		URL[] libUrls = new URL[libs.length];
		try {
			for (int i = 0; i < libs.length; i++) {
				libUrls[i] = libs[i].toURL();
			}
		} catch (MalformedURLException ex) {
			throw new UpgradeException("Error converting library path to URL: " + ex.getMessage(), ex);
		}
		ClassLoader libLoader = new URLClassLoader(libUrls);
		CQLQueryProcessor proc = null;
		try {
			Class qpClass = libLoader.loadClass(queryProcessorClassName);
			proc = (CQLQueryProcessor) qpClass.newInstance();
		} catch (Exception ex) {
			throw new UpgradeException("Error instantiating query processor class: " + ex.getMessage(), ex);			
		}
		return proc;
	}
	

	public static void main(String[] args) {
		if (args.length != 2) {
			usage();
			System.exit(1);
		}
		File inputServiceDir = new File(args[0]);
		File outputServiceDir = new File(args[1]);
		if (!inputServiceDir.isDirectory() || !inputServiceDir.exists()) {
			System.err.println("Error: Input directory is not valid");
		}
		if (!outputServiceDir.exists()) {
			System.out.println("Output directory will be created");
			outputServiceDir.mkdirs();
		}
		if (!outputServiceDir.isDirectory()) {
			System.err.println("Error: Output directory is not valid");
		}
		
		DataServiceUpgrade upgrade = new Upgrade1pt0to1pt1();
		try {
			upgrade.upgrade(inputServiceDir, outputServiceDir);
		} catch (UpgradeException ex) {
			System.err.println("Error performing upgrade:");
			System.err.println(ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	
	private static void usage() {
		System.out.println("Usage: ");
		System.out.println(Upgrade1pt0to1pt1.class.getName() + " <oldServiceDir> <newServiceDir>");
	}
}
