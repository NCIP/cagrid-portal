package gov.nih.nci.cagrid.data.upgrades;

import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.FileFilters;
import gov.nih.nci.cagrid.introduce.extension.utils.AxisJdomUtils;
import gov.nih.nci.cagrid.introduce.upgrade.ExtensionUpgraderBase;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

import org.apache.axis.message.MessageElement;
import org.jdom.Element;
import org.jdom.JDOMException;

/** 
 *  DataServiceUpgrade1pt0to1pt1
 *  Utility to upgrade a 1.0 data service to 1.1
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Feb 19, 2007 
 * @version $Id: DataServiceUpgrade1pt0to1pt1.java,v 1.1 2007-02-19 21:52:52 dervin Exp $ 
 */
public class DataServiceUpgrade1pt0to1pt1 extends ExtensionUpgraderBase {
	private ExtensionType dataExtension = null;
	
	public DataServiceUpgrade1pt0to1pt1(ServiceDescription serviceDescription,
		String fromVersion, String toVersion) {
		super(serviceDescription, fromVersion, toVersion);
	}
	

	protected void upgrade() throws Exception {
		// ensure we're upgrading appropriatly
		validateUpgrade();
		// get the extension data in raw form
		Element extensionData = getExtensionDataElement();
		// fix the cadsr information block
		setCadsrInformation(extensionData);
		// move the configuration for the CQL query processor into
		// the service properties and remove it from the extension data
		reconfigureCqlQueryProcessor(extensionData);
		// change the version number
		setCurrentExtensionVersion();
		// store the modified extension data back into the service model
		setExtensionDataElement(extensionData);
	}
	
	
	private void validateUpgrade() throws UpgradeException {
		if (!(getFromVersion() == null || getFromVersion().equals("1.0"))) {
			throw new UpgradeException(getClass().getName() + " upgrades FROM 1.0 TO 1.1, found FROM = " + getFromVersion());
		}
		if (!getToVersion().equals("1.1")) {
			throw new UpgradeException(getClass().getName() + " upgrades FROM 1.0 TO 1.1, found TO = " + getToVersion());
		}
		String currentVersion = getDataExtension().getVersion();
		if (!(currentVersion == null || currentVersion.equals("1.0"))) {
			throw new UpgradeException(getClass().getName() + " upgrades FROM 1.0 TO 1.1, current version found is " + currentVersion);
		}
	}
	
	
	private void setCurrentExtensionVersion() throws UpgradeException {
		getDataExtension().setVersion("1.1");
	}
	
	
	private void reconfigureCqlQueryProcessor(Element extensionData) throws UpgradeException {
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
		// remove all the processor config properties from the model
		extensionData.removeChild("CQLProcessorConfig", extensionData.getNamespace());
		
		// locate the query processor class property
		String queryProcessorClassName = null;
		try {
			queryProcessorClassName = CommonTools.getServicePropertyValue(getServiceDescription(), "queryProcessorClass");
		} catch (Exception ex) {
			throw new UpgradeException("Error getting query processor class name: " + ex.getMessage(), ex);
		}
		// load the query processor so we can ask it some questions
		// FIXME: must get the service's base directory!!!
		CQLQueryProcessor proc = loadQueryProcessorInstance(null, queryProcessorClassName);
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
			CommonTools.setServiceProperty(getServiceDescription(), key, value, false);
		}
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
		ClassLoader libLoader = new URLClassLoader(libUrls, Thread.currentThread().getContextClassLoader());
		CQLQueryProcessor proc = null;
		try {
			Class qpClass = libLoader.loadClass(queryProcessorClassName);
			proc = (CQLQueryProcessor) qpClass.newInstance();
		} catch (Exception ex) {
			throw new UpgradeException("Error instantiating query processor class: " + ex.getMessage(), ex);			
		}
		return proc;
	}
	
	
	private void setCadsrInformation(Element extensionData) {
		// additional libraries / jar names elements are unchanged
		// get cadsr information
		Element cadsrInfo = extensionData.getChild("CadsrInformation", extensionData.getNamespace());
		// now we have a noDomainModel boolean flag...
		boolean hasCadsrUrl = cadsrInfo.getAttributeValue("serviceUrl") != null;
		boolean usingSuppliedModel = cadsrInfo.getAttributeValue("useSuppliedModel").equals("true");
		boolean noDomainModel = (!hasCadsrUrl && !usingSuppliedModel);
		cadsrInfo.setAttribute("noDomainModel", String.valueOf(noDomainModel));
	}
	
	
	private void setExtensionDataElement(Element extensionData) throws UpgradeException {
		ExtensionTypeExtensionData ext = getDataExtension().getExtensionData();
		MessageElement rawExtensionData = null;
		try {
			rawExtensionData = AxisJdomUtils.fromElement(extensionData);
		} catch (JDOMException ex) {
			throw new UpgradeException("Error converting extension data to Axis MessageElement: " + ex.getMessage(), ex);
		}
		ext.set_any(new MessageElement[] {rawExtensionData});
	}
	
	
	private Element getExtensionDataElement() throws UpgradeException {
		MessageElement[] anys = getDataExtension().getExtensionData().get_any();
		MessageElement rawDataElement = null;
		for (int i = 0; anys != null && i < anys.length; i++) {
			if (anys[i].getQName().equals(Data.getTypeDesc().getXmlType())) {
				rawDataElement = anys[i];
				break;
			}
		}
		if (rawDataElement == null) {
			throw new UpgradeException("No extension data was found for the data service extension");
		}
		Element extensionDataElement = AxisJdomUtils.fromMessageElement(rawDataElement);
		return extensionDataElement;
	}
	
	
	private ExtensionType getDataExtension() throws UpgradeException {
		if (dataExtension == null) {
			ExtensionType[] existingExtensions = getServiceDescription()
				.getExtensions().getExtension();
			for (int i = 0; i < existingExtensions.length; i++) {
				if (existingExtensions[i].getName().equals("data")) {
					dataExtension = existingExtensions[i];
					break;
				}
			}
			// uhoh
			throw new UpgradeException("No data service extension found in service model");
		}
		return dataExtension;
	}
}
