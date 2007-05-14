package gov.nih.nci.cagrid.data.upgrades;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.data.utilities.CastorMappingUtil;
import gov.nih.nci.cagrid.data.utilities.WsddUtil;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptionsException;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.FileFilters;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;
import gov.nih.nci.cagrid.introduce.extension.utils.AxisJdomUtils;
import gov.nih.nci.cagrid.introduce.extension.utils.ExtensionUtilities;
import gov.nih.nci.cagrid.introduce.upgrade.common.ExtensionUpgradeStatus;
import gov.nih.nci.cagrid.introduce.upgrade.one.one.ExtensionUpgraderBase;
import gov.nih.nci.cagrid.wsenum.utils.IterImplType;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.axis.message.MessageElement;
import org.jdom.Element;
import org.jdom.JDOMException;


/**
 * DataServiceUpgrade1pt0to1pt1 Utility to upgrade a 1.0 data service to 1.1
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A> *
 * @created Feb 19, 2007
 * @version $Id: DataServiceUpgrade1pt0to1pt1.java,v 1.1 2007/02/19 21:52:52
 *          dervin Exp $
 */
public class DataServiceUpgrade1pt0to1pt1 extends ExtensionUpgraderBase {

    public DataServiceUpgrade1pt0to1pt1(ExtensionType extensionType, ServiceInformation serviceInformation,
        String servicePath, String fromVersion, String toVersion) {
        super("DataServiceUpgrade1pt0to1pt1",extensionType, serviceInformation, servicePath, fromVersion, toVersion);
    }


    protected void upgrade() throws Exception {
        // ensure we're upgrading appropriatly
        validateUpgrade();
        // get the extension data in raw form
        Element extensionData = getExtensionDataElement();
        // update the data service libraries
        updateLibraries();
        // fix the cadsr information block
        setCadsrInformation(extensionData);
        // move the configuration for the CQL query processor into
        // the service properties and remove it from the extension data
        reconfigureCqlQueryProcessor(extensionData);
        // add selected enum iterator
        setEnumIteratorSelection();
        // update schemas
        updateDataSchemas();
        // change the version number
        setCurrentExtensionVersion();
        // set the method documentation strings
        setDescriptionStrings();
        // add new attributes to extension data's Service Features
        updateServiceFeatures();
        // fix up the castor mapping location
        moveCastorMappingFile();
        // store the modified extension data back into the service model
        setExtensionDataElement(extensionData);

    }


    private void validateUpgrade() throws UpgradeException {
        if (!((getFromVersion() == null) || getFromVersion().equals("1.0"))) {
            throw new UpgradeException(getClass().getName() + " upgrades FROM 1.0 TO 1.1, found FROM = "
                + getFromVersion());
        }
        if (!getToVersion().equals("1.1")) {
            throw new UpgradeException(getClass().getName() + " upgrades FROM 1.0 TO 1.1, found TO = " + getToVersion());
        }
        String currentVersion = getExtensionType().getVersion();
        if (!((currentVersion == null) || currentVersion.equals("1.0"))) {
            throw new UpgradeException(getClass().getName() + " upgrades FROM 1.0 TO 1.1, current version found is "
                + currentVersion);
        }
    }


    private void updateLibraries() throws UpgradeException {
        updateDataLibraries();
        if (serviceIsUsingEnumeration()) {
            updateEnumerationLibraries();
        }
        if (serviceIsUsingSdkDataSource()) {
            updateSdkQueryLibraries();
        }
    }
    

    private void updateDataLibraries() throws UpgradeException {
        FileFilter dataLibFilter = new FileFilter() {
            public boolean accept(File pathname) {
                String name = pathname.getName();
                return (name.endsWith(".jar") && (name.startsWith("caGrid-1.0-data")
                    || name.startsWith("caGrid-1.0-core") || name.startsWith("caGrid-1.0-caDSR")
                    || name.startsWith("caGrid-1.0-metadata")));
            }
        };
        FileFilter newDataLibFilter =new FileFilter() {
            public boolean accept(File pathname) {
                String name = pathname.getName();
                return (name.endsWith(".jar") && (name.startsWith("caGrid-1.1-data")
                    || name.startsWith("caGrid-1.1-core") || name.startsWith("caGrid-1.1-caDSR")
                    || name.startsWith("caGrid-1.1-metadata")));
            }
        };
        // locate the old data service libs in the service
        File serviceLibDir = new File(getServicePath() + File.separator + "lib");
        File[] serviceDataLibs = serviceLibDir.listFiles(dataLibFilter);
        // delete the old libraries
        for (int i = 0; i < serviceDataLibs.length; i++) {
            serviceDataLibs[i].delete();
        }
        // copy new libraries in
        File extLibDir = new File(ExtensionsLoader.EXTENSIONS_DIRECTORY + File.separator + "lib");
        File[] dataLibs = extLibDir.listFiles(newDataLibFilter);
        File[] outLibs = new File[dataLibs.length];
        for (int i = 0; i < dataLibs.length; i++) {
            File out = new File(serviceLibDir.getAbsolutePath() + File.separator + dataLibs[i].getName());
            try {
                Utils.copyFile(dataLibs[i], out);
            } catch (IOException ex) {
                throw new UpgradeException("Error copying new data service library: " + ex.getMessage(), ex);
            }
            outLibs[i] = out;
        }
        // update the Eclipse .classpath file
        File classpathFile = new File(getServicePath() + File.separator + ".classpath");
        try {
            ExtensionUtilities.syncEclipseClasspath(classpathFile, outLibs);
        } catch (Exception ex) {
            throw new UpgradeException("Error updating Eclipse .classpath file: " + ex.getMessage(), ex);
        }
    }


    private void updateEnumerationLibraries() throws UpgradeException {
        FileFilter enumLibFilter = new FileFilter() {
            public boolean accept(File name) {
                String filename = name.getName();
                return filename.equals("caGrid-1.0-wsEnum.jar");
            }
        };
        FileFilter newEnumLibFilter = new FileFilter() {
            public boolean accept(File name) {
                String filename = name.getName();
                return filename.equals("caGrid-1.1-wsEnum.jar");
            }
        };
        // locate old enumeration libraries in the service
        File serviceLibDir = new File(getServicePath() + File.separator + "lib");
        File[] oldEnumLibs = serviceLibDir.listFiles(enumLibFilter);
        for (int i = 0; i < oldEnumLibs.length; i++) {
            oldEnumLibs[i].delete();
        }
        // copy in new libraries
        File extLibDir = new File(ExtensionsLoader.EXTENSIONS_DIRECTORY + File.separator + "lib");
        File[] newEnumLibs = extLibDir.listFiles(newEnumLibFilter);
        File[] outLibs = new File[newEnumLibs.length];
        for (int i = 0; i < newEnumLibs.length; i++) {
            File outFile = new File(serviceLibDir.getAbsolutePath() + File.separator + newEnumLibs[i].getName());
            try {
                Utils.copyFile(newEnumLibs[i], outFile);
            } catch (IOException ex) {
                throw new UpgradeException("Error copying new enumeration library: " + ex.getMessage(), ex);
            }
            outLibs[i] = outFile;
        }
        // update the Eclipse .classpath file
        File classpathFile = new File(getServicePath() + File.separator + ".classpath");
        try {
            ExtensionUtilities.syncEclipseClasspath(classpathFile, outLibs);
        } catch (Exception ex) {
            throw new UpgradeException("Error updating Eclipse .classpath file: " + ex.getMessage(), ex);
        }
    }


    private void updateSdkQueryLibraries() throws UpgradeException {
        FileFilter sdkLibFilter = new FileFilter() {
            public boolean accept(File name) {
                String filename = name.getName();
                return (filename.startsWith("caGrid-1.0-sdkQuery") || filename.startsWith("caGrid-1.0-sdkQuery32"))
                    && filename.endsWith(".jar");
            }
        };
        FileFilter newSdkLibFilter = new FileFilter() {
            public boolean accept(File name) {
                String filename = name.getName();
                return (filename.startsWith("caGrid-1.1-sdkQuery") || filename.startsWith("caGrid-1.1-sdkQuery32"))
                    && filename.endsWith(".jar");
            }
        };
        // locate old libraries in the service
        File serviceLibDir = new File(getServicePath() + File.separator + "lib");
        boolean isSdk31 = false;
        boolean isSdk32 = false;
        File[] oldLibs = serviceLibDir.listFiles(sdkLibFilter);
        // first must see which version of SDK we're using
        for (int i = 0; i < oldLibs.length; i++) {
            if (oldLibs[i].getName().indexOf("caGrid-1.0-sdkQuery32") != -1) {
                isSdk32 = true;
            } else {
                if (oldLibs[i].getName().indexOf("caGrid-1.0-sdkQuery") != -1) {
                    isSdk31 = true;
                }
            }
        }
        if ((!isSdk31 && !isSdk32) || (isSdk31 && isSdk32)) {
            throw new UpgradeException("Unable to determine SDK version to upgrade");
        }
        // delete old libs
        for (int i = 0; i < oldLibs.length; i++) {
            oldLibs[i].delete();
        }
        // locate new libraries
        File[] newLibs = null;
        if (isSdk31) {
            File sdk31LibDir = new File(ExtensionsLoader.EXTENSIONS_DIRECTORY + File.separator + "data"  + File.separator + "sdk31" + File.separator + "lib");
            newLibs = sdk31LibDir.listFiles(newSdkLibFilter);
        } else if (isSdk32) {
            File sdk32LibDir = new File(ExtensionsLoader.EXTENSIONS_DIRECTORY + File.separator + "data" + File.separator + "sdk32" + File.separator + "lib");
            newLibs = sdk32LibDir.listFiles(newSdkLibFilter);
        }
        // copy the libraries in
        File[] outLibs = new File[newLibs.length];
        for (int i = 0; i < newLibs.length; i++) {
            File output = new File(getServicePath() + File.separator + "lib" + File.separator + newLibs[i].getName());
            try {
                Utils.copyFile(newLibs[i], output);
            } catch (IOException ex) {
                throw new UpgradeException("Error copying SDK Query Processor library: " + ex.getMessage(), ex);
            }
            outLibs[i] = output;
        }
        // sync up the Eclipse .classpath file
        File classpathFile = new File(getServicePath() + File.separator + ".classpath");
        try {
            ExtensionUtilities.syncEclipseClasspath(classpathFile, outLibs);
        } catch (Exception ex) {
            throw new UpgradeException("Error updating Eclipse .classpath file: " + ex.getMessage(), ex);
        }
        
    }


    private void moveCastorMappingFile() throws UpgradeException {
        File oldCastorMapping = new File(getServicePath() + File.separator + "xml-mapping.xml");
        if (oldCastorMapping.exists()) {
            Properties introduceProperties = new Properties();
            try {
                introduceProperties
                    .load(new FileInputStream(getServicePath() + File.separator + "introduce.properties"));
            } catch (IOException ex) {
                throw new UpgradeException("Error loading introduce properties for this service: " + ex.getMessage(),
                    ex);
            }

            File newCastorMapping = new File(CastorMappingUtil.getCustomCastorMappingFileName(getServiceInformation()));
            try {
                Utils.copyFile(oldCastorMapping, newCastorMapping);
            } catch (IOException ex) {
                throw new UpgradeException("Error moving castor mapping file: " + ex.getMessage(), ex);
            }
            // fix the server-config.wsdd file's castrorMapping parameter
            File serverConfigFile = new File(getServicePath() + File.separator + "server-config.wsdd");
            try {
                WsddUtil.setServiceParameter(serverConfigFile.getAbsolutePath(), getServiceInformation().getServices()
                    .getService(0).getName(), DataServiceConstants.CASTOR_MAPPING_WSDD_PARAMETER, CastorMappingUtil
                    .getCustomCastorMappingName(getServiceInformation()));
            } catch (Exception ex) {
                throw new UpgradeException("Error setting castor mapping parameter in server-config.wsdd: "
                    + ex.getMessage(), ex);
            }
            // fix the client config file
            String mainServiceName = getServiceInformation().getIntroduceServiceProperties().getProperty(
                IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME);
            ServiceType mainService = CommonTools.getService(getServiceInformation().getServices(), mainServiceName);
            String servicePackageName = mainService.getPackageName();
            String packageDir = servicePackageName.replace('.', File.separatorChar);
            File clientConfigFile = new File(getServicePath() + File.separator + "src" + File.separator + packageDir
                + File.separator + "client" + File.separator + "client-config.wsdd");
            try {
                WsddUtil.setGlobalClientParameter(clientConfigFile.getAbsolutePath(),
                    DataServiceConstants.CASTOR_MAPPING_WSDD_PARAMETER, CastorMappingUtil
                        .getCustomCastorMappingName(getServiceInformation()));
            } catch (Exception ex) {
                throw new UpgradeException("Error setting castor mapping parameter in client-config.wsdd: "
                    + ex.getMessage(), ex);
            }
            oldCastorMapping.delete();
        }
    }


    private void updateDataSchemas() throws UpgradeException {
        String serviceName = getServiceInformation().getServices().getService(0).getName();
        // extension data has been updated
        File serviceExtensionDataSchema = new File(getServicePath() + File.separator + "schema" + File.separator
            + serviceName + "DataServiceExtensionData.xsd");
        File newExtensionDataSchema = new File(ExtensionsLoader.getInstance().getExtensionsDir() + File.separator
            + "data" + File.separator + "schema" + File.separator + "Data" + File.separator
            + "DataServiceExtensionData.xsd");
        try {
            Utils.copyFile(newExtensionDataSchema, serviceExtensionDataSchema);
        } catch (IOException ex) {
            throw new UpgradeException("Error upgrading extension data schema: " + ex.getMessage(), ex);
        }

        // if the CQL schema changes, upgrade it here
    }


    private void updateServiceFeatures() throws UpgradeException {
        Element extDataElement = getExtensionDataElement();
        Element serviceFeaturesElement = extDataElement.getChild("ServiceFeatures", extDataElement.getNamespace());
        serviceFeaturesElement.setAttribute("useBdt", String.valueOf(false));
        setExtensionDataElement(extDataElement);
    }


    private void setCurrentExtensionVersion() throws UpgradeException {
        getExtensionType().setVersion("1.1");
    }


    private void reconfigureCqlQueryProcessor(Element extensionData) throws UpgradeException {
        // make sure to replace the processor jar with the new one in the libs listing
        Element additionalLibraries = extensionData.getChild("AdditionalLibraries",extensionData.getNamespace());
        List libsEls = additionalLibraries.getChildren();
        Iterator libsElsIt = libsEls.iterator();
        while(libsElsIt.hasNext()){
            Element nextLib = (Element)libsElsIt.next();
            String jarName = nextLib.getText();
            if(jarName.equals("caGrid-1.0-sdkQuery.jar")){
                nextLib.setText("caGrid-1.1-sdkQuery-core.jar");
            } else if(jarName.equals("caGrid-1.0-sdkQuery32.jar")){
                nextLib.setText("caGrid-1.1-sdkQuery32-core.jar");
            }
        }
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
            queryProcessorClassName = CommonTools.getServicePropertyValue(getServiceInformation()
                .getServiceDescriptor(), "queryProcessorClass");
        } catch (Exception ex) {
            throw new UpgradeException("Error getting query processor class name: " + ex.getMessage(), ex);
        }
        // load the query processor so we can ask it some questions
        CQLQueryProcessor proc = loadQueryProcessorInstance(queryProcessorClassName);
        // get the properties for the query processor
        Properties qpProps = proc.getRequiredParameters();
        // set the user configured properties
        Enumeration keyEnum = qpProps.keys();
        while (keyEnum.hasMoreElements()) {
            String key = (String) keyEnum.nextElement();
            String value = qpProps.getProperty(key);
            if (configuredProps.containsKey(key)) {
                value = configuredProps.getProperty(key);
            }
            // add the property to the service properties
            String extendedKey = "cqlQueryProcessorConfig_" + key;
            CommonTools.setServiceProperty(getServiceInformation().getServiceDescriptor(), extendedKey, value, false);
        }
    }


    private void setEnumIteratorSelection() throws UpgradeException {
        if (serviceIsUsingEnumeration()) {
            CommonTools.setServiceProperty(getServiceInformation().getServiceDescriptor(),
                DataServiceConstants.ENUMERATION_ITERATOR_TYPE_PROPERTY, IterImplType.CAGRID_CONCURRENT_COMPLETE
                    .toString(), false);
        }
    }


    private CQLQueryProcessor loadQueryProcessorInstance(String queryProcessorClassName) throws UpgradeException {
        // reflect load the query processor (this should live in the service's
        // lib dir)
        File libDir = new File(getServicePath() + File.separator + "lib");
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
        boolean usingSuppliedModel = (cadsrInfo.getAttributeValue("useSuppliedModel") != null)
            && cadsrInfo.getAttributeValue("useSuppliedModel").equals("true");
        boolean noDomainModel = (!hasCadsrUrl && !usingSuppliedModel);
        cadsrInfo.setAttribute("noDomainModel", String.valueOf(noDomainModel));
    }


    private void setDescriptionStrings() throws UpgradeException {
        // get the query method
        MethodType queryMethod = null;
        MethodType enumerationMethod = null;
        MethodType[] allMethods = getServiceInformation().getServices().getService(0).getMethods().getMethod();
        for (int i = 0; i < allMethods.length; i++) {
            if (allMethods[i].getName().equals(DataServiceConstants.QUERY_METHOD_NAME)) {
                queryMethod = allMethods[i];
            } else if (allMethods[i].getName().equals(DataServiceConstants.ENUMERATION_QUERY_METHOD_NAME)) {
                enumerationMethod = allMethods[i];
            }
        }

        // query method is REQUIRED
        if (queryMethod == null) {
            throw new UpgradeException("No standard query method found in the data service!");
        }

        // set descriptions for query method
        queryMethod.setDescription(DataServiceConstants.QUERY_METHOD_DESCRIPTION);
        queryMethod.getInputs().getInput(0).setDescription(DataServiceConstants.QUERY_METHOD_PARAMETER_DESCRIPTION);
        queryMethod.getOutput().setDescription(DataServiceConstants.QUERY_METHOD_OUTPUT_DESCRIPTION);
        MethodTypeExceptionsException[] queryExceptions = queryMethod.getExceptions().getException();
        for (int i = 0; i < queryExceptions.length; i++) {
            if (queryExceptions[i].getQname().equals(DataServiceConstants.QUERY_PROCESSING_EXCEPTION_QNAME)) {
                queryExceptions[i].setDescription(DataServiceConstants.QUERY_PROCESSING_EXCEPTION_DESCRIPTION);
            } else if (queryExceptions[i].getQname().equals(DataServiceConstants.MALFORMED_QUERY_EXCEPTION_QNAME)) {
                queryExceptions[i].setDescription(DataServiceConstants.MALFORMED_QUERY_EXCEPTION_DESCRIPTION);
            }
        }

        // enumeration query method is optional
        if (serviceIsUsingEnumeration()) {
            if (enumerationMethod == null) {
                throw new UpgradeException("No enumeration query method found in the data service!");
            }
            enumerationMethod.setDescription(DataServiceConstants.ENUMERATION_QUERY_METHOD_DESCRIPTION);
            enumerationMethod.getInputs().getInput(0).setDescription(
                DataServiceConstants.QUERY_METHOD_PARAMETER_DESCRIPTION);
            enumerationMethod.getOutput().setDescription(
                DataServiceConstants.ENUMERATION_QUERY_METHOD_OUTPUT_DESCRIPTION);
            MethodTypeExceptionsException[] enumExceptions = enumerationMethod.getExceptions().getException();
            for (int i = 0; i < enumExceptions.length; i++) {
                if (enumExceptions[i].getQname().equals(DataServiceConstants.QUERY_PROCESSING_EXCEPTION_QNAME)) {
                    enumExceptions[i].setDescription(DataServiceConstants.QUERY_PROCESSING_EXCEPTION_DESCRIPTION);
                } else if (enumExceptions[i].getQname().equals(DataServiceConstants.MALFORMED_QUERY_EXCEPTION_QNAME)) {
                    enumExceptions[i].setDescription(DataServiceConstants.MALFORMED_QUERY_EXCEPTION_DESCRIPTION);
                }
            }
        }
    }


    private boolean serviceIsUsingEnumeration() throws UpgradeException {
        Element extDataElement = getExtensionDataElement();
        Element serviceFeaturesElement = extDataElement.getChild("ServiceFeatures", extDataElement.getNamespace());
        String useEnumValue = serviceFeaturesElement.getAttributeValue("useWsEnumeration");
        return Boolean.valueOf(useEnumValue).booleanValue();
    }


    private boolean serviceIsUsingSdkDataSource() throws UpgradeException {
        Element extDataElement = getExtensionDataElement();
        Element serviceFeaturesElement = extDataElement.getChild("ServiceFeatures", extDataElement.getNamespace());
        String useSdkValue = serviceFeaturesElement.getAttributeValue("useSdkDataSource");
        return Boolean.valueOf(useSdkValue).booleanValue();
    }


    private void setExtensionDataElement(Element extensionData) throws UpgradeException {
        ExtensionTypeExtensionData ext = getExtensionType().getExtensionData();
        MessageElement rawExtensionData = null;
        try {
            rawExtensionData = AxisJdomUtils.fromElement(extensionData);
        } catch (JDOMException ex) {
            throw new UpgradeException("Error converting extension data to Axis MessageElement: " + ex.getMessage(), ex);
        }
        ext.set_any(new MessageElement[]{rawExtensionData});
    }


    private Element getExtensionDataElement() throws UpgradeException {
        MessageElement[] anys = getExtensionType().getExtensionData().get_any();
        MessageElement rawDataElement = null;
        for (int i = 0; (anys != null) && (i < anys.length); i++) {
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
}
