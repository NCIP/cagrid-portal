package gov.nih.nci.cagrid.introduce.common;

import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeImportInformation;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeProviderInformation;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertyType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.namespace.QName;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.XMLUtilities;


public final class ProviderTools {
    
    public ProviderTools(){
        
    }

    public static void addLifetimeResourceProvider(ServiceType service, ServiceInformation info) {
        // create the two lifetime methods to add

        MethodType destroyMethod = new MethodType();
        destroyMethod.setName("Destroy");
        MethodTypeOutput destroyOutput = new MethodTypeOutput();
        destroyOutput.setIsArray(false);
        destroyOutput.setQName(new QName("", "void"));
        destroyMethod.setOutput(destroyOutput);
        MethodTypeImportInformation ii = new MethodTypeImportInformation();
        ii.setFromIntroduce(Boolean.FALSE);
        ii.setInputMessage(new QName(
            "http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.wsdl", "DestroyRequest"));
        ii.setOutputMessage(new QName(
            "http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.wsdl", "DestroyResponse"));
        ii.setNamespace("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.wsdl");
        ii.setPackageName("org.oasis.wsrf.lifetime");
        ii.setPortTypeName("ImmediateResourceTermination");
        ii.setWsdlFile("../wsrf/lifetime/WS-ResourceLifetime.wsdl");
        destroyMethod.setIsImported(true);
        destroyMethod.setImportInformation(ii);
        MethodTypeProviderInformation pi = new MethodTypeProviderInformation();
        pi.setProviderClass("DestroyProvider");
        destroyMethod.setIsProvided(true);
        destroyMethod.setProviderInformation(pi);

        MethodType sttMethod = new MethodType();
        sttMethod.setName("SetTerminationTime");
        MethodTypeOutput sttOutput = new MethodTypeOutput();
        sttOutput.setIsArray(false);
        sttOutput.setQName(new QName("", "void"));
        sttMethod.setOutput(sttOutput);
        ii = new MethodTypeImportInformation();
        ii.setFromIntroduce(new Boolean(false));
        ii.setInputMessage(new QName(
            "http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.wsdl",
            "SetTerminationTimeRequest"));
        ii.setOutputMessage(new QName(
            "http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.wsdl",
            "SetTerminationTimeResponse"));
        ii.setNamespace("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.wsdl");
        ii.setPackageName("org.oasis.wsrf.lifetime");
        ii.setPortTypeName("ScheduledResourceTermination");
        ii.setWsdlFile("../wsrf/lifetime/WS-ResourceLifetime.wsdl");
        sttMethod.setIsImported(true);
        sttMethod.setImportInformation(ii);
        pi = new MethodTypeProviderInformation();
        pi.setProviderClass("SetTerminationTimeProvider");
        sttMethod.setIsProvided(true);
        sttMethod.setProviderInformation(pi);

        // add the two lifetime methods
        CommonTools.addMethod(service, destroyMethod);
        CommonTools.addMethod(service, sttMethod);

        // need add the lifetime resource properties
        NamespaceType nsType = new NamespaceType();
        nsType.setNamespace("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd");
        nsType.setLocation("../wsrf/lifetime/WS-ResourceLifetime.xsd");
        nsType.setPackageName("org.oasis.wsrf.lifetime");
        SchemaElementType ctel = new SchemaElementType();
        ctel.setType("CurrentTime");
        SchemaElementType ttel = new SchemaElementType();
        ttel.setType("TerminationTime");
        SchemaElementType tnel = new SchemaElementType();
        tnel.setType("TerminationNotification");
        SchemaElementType[] types = new SchemaElementType[3];
        types[0] = ctel;
        types[1] = ttel;
        types[2] = tnel;
        nsType.setSchemaElement(types);

        CommonTools.addNamespace(info.getServiceDescriptor(), nsType);

        ResourcePropertyType currentTime = new ResourcePropertyType();
        currentTime.setQName(new QName(
            "http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", "CurrentTime"));
        currentTime.setRegister(false);
        currentTime.setPopulateFromFile(false);
        ResourcePropertyType terminationTime = new ResourcePropertyType();
        terminationTime.setQName(new QName(
            "http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceLifetime-1.2-draft-01.xsd", "TerminationTime"));
        terminationTime.setRegister(false);
        terminationTime.setPopulateFromFile(false);

        CommonTools.addResourcePropety(service, currentTime);
        CommonTools.addResourcePropety(service, terminationTime);

    }

//
//    public static void removeLifetimeResourceProvider(ServiceType service, ServiceInformation info) {
//        CommonTools.removeMethod(service.getMethods(), CommonTools.getMethod(service.getMethods(), "Destroy"));
//        CommonTools.removeMethod(service.getMethods(), CommonTools
//            .getMethod(service.getMethods(), "SetTerminationTime"));
//        CommonTools.removeMethod(service.getMethods(), CommonTools
//            .getMethod(service.getMethods(), "SetTerminationTime"));
//        
//    }
//

    public static void addSubscribeResourceProvider(ServiceType service, ServiceInformation info) {
        MethodType subscribeMethod = new MethodType();
        subscribeMethod.setName("Subscribe");
        subscribeMethod.setOutput(new MethodTypeOutput());
        subscribeMethod.getOutput().setIsArray(false);
        subscribeMethod.getOutput().setQName(new QName("", "void"));

        MethodTypeImportInformation ii = new MethodTypeImportInformation();
        ii.setFromIntroduce(Boolean.FALSE);
        ii.setInputMessage(new QName(
            "http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.wsdl", "SubscribeRequest"));
        ii.setOutputMessage(new QName(
            "http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.wsdl", "SubscribeResponse"));
        ii.setNamespace("http://docs.oasis-open.org/wsn/2004/06/wsn-WS-BaseNotification-1.2-draft-01.wsdl");
        ii.setPackageName("org.oasis.wsn");
        ii.setPortTypeName("NotificationProducer");
        ii.setWsdlFile("../wsrf/notification/WS-BaseN.wsdl");
        subscribeMethod.setImportInformation(ii);
        subscribeMethod.setIsImported(true);

        MethodTypeProviderInformation pi = new MethodTypeProviderInformation();
        pi.setProviderClass("SubscribeProvider");
        subscribeMethod.setProviderInformation(pi);
        subscribeMethod.setIsProvided(true);

        CommonTools.addMethod(service, subscribeMethod);
    }


    public static void removeSubscribeResourceProvider(ServiceType service, ServiceInformation info) {
        CommonTools.removeMethod(service.getMethods(), CommonTools.getMethod(service.getMethods(), "Subscribe"));
    }


    private static void addGetResourcePropertyResourceProvider(ServiceType service, ServiceInformation info) {
        MethodType rpMethod = new MethodType();
        rpMethod.setName("GetResourceProperty");
        rpMethod.setOutput(new MethodTypeOutput());
        rpMethod.getOutput().setIsArray(false);
        rpMethod.getOutput().setQName(new QName("", "void"));

        MethodTypeImportInformation ii = new MethodTypeImportInformation();
        ii.setFromIntroduce(Boolean.FALSE);
        ii.setInputMessage(new QName(
            "http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.wsdl",
            "GetResourcePropertyRequest"));
        ii.setOutputMessage(new QName(
            "http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.wsdl",
            "GetResourcePropertyResponse"));
        ii.setNamespace("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.wsdl");
        ii.setPackageName("org.oasis.wsrf.properties");
        ii.setPortTypeName("GetResourceProperty");
        ii.setWsdlFile("../wsrf/properties/WS-ResourceProperties.wsdl");
        rpMethod.setImportInformation(ii);
        rpMethod.setIsImported(true);

        MethodTypeProviderInformation pi = new MethodTypeProviderInformation();
        pi.setProviderClass("GetRPProvider");
        rpMethod.setProviderInformation(pi);
        rpMethod.setIsProvided(true);

        CommonTools.addMethod(service, rpMethod);
    }


    public static void removeGetResourcePropertyResourceProvider(ServiceType service, ServiceInformation info) {
        CommonTools.removeMethod(service.getMethods(), CommonTools.getMethod(service.getMethods(),
            "GetResourceProperty"));
    }


    private static void addGetMultipeResourcePropertiesResourceProvider(ServiceType service, ServiceInformation info) {
        MethodType rpMethod = new MethodType();
        rpMethod.setName("GetMultipleResourceProperties");
        rpMethod.setOutput(new MethodTypeOutput());
        rpMethod.getOutput().setIsArray(false);
        rpMethod.getOutput().setQName(new QName("", "void"));

        MethodTypeImportInformation ii = new MethodTypeImportInformation();
        ii.setFromIntroduce(Boolean.FALSE);
        ii.setInputMessage(new QName(
            "http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.wsdl",
            "GetMultipleResourcePropertiesRequest"));
        ii.setOutputMessage(new QName(
            "http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.wsdl",
            "GetMultipleResourcePropertiesResponse"));
        ii.setNamespace("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.wsdl");
        ii.setPackageName("org.oasis.wsrf.properties");
        ii.setPortTypeName("GetMultipleResourceProperties");
        ii.setWsdlFile("../wsrf/properties/WS-ResourceProperties.wsdl");
        rpMethod.setImportInformation(ii);
        rpMethod.setIsImported(true);

        MethodTypeProviderInformation pi = new MethodTypeProviderInformation();
        pi.setProviderClass("GetMRPProvider");
        rpMethod.setProviderInformation(pi);
        rpMethod.setIsProvided(true);

        CommonTools.addMethod(service, rpMethod);
    }


    public static void removeGetMultipeResourcePropertiesResourceProvider(ServiceType service, ServiceInformation info) {
        CommonTools.removeMethod(service.getMethods(), CommonTools.getMethod(service.getMethods(),
            "GetMultipleResourceProperties"));
    }


    private static void addQueryResourcePropertiesResourceProvider(ServiceType service, ServiceInformation info) {
        MethodType rpMethod = new MethodType();
        rpMethod.setName("QueryResourceProperties");
        rpMethod.setOutput(new MethodTypeOutput());
        rpMethod.getOutput().setIsArray(false);
        rpMethod.getOutput().setQName(new QName("", "void"));

        MethodTypeImportInformation ii = new MethodTypeImportInformation();
        ii.setFromIntroduce(Boolean.FALSE);
        ii.setInputMessage(new QName(
            "http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.wsdl",
            "QueryResourcePropertiesRequest"));
        ii.setOutputMessage(new QName(
            "http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.wsdl",
            "QueryResourcePropertiesResponse"));
        ii.setNamespace("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.wsdl");
        ii.setPackageName("org.oasis.wsrf.properties");
        ii.setPortTypeName("QueryResourceProperties");
        ii.setWsdlFile("../wsrf/properties/WS-ResourceProperties.wsdl");
        rpMethod.setImportInformation(ii);
        rpMethod.setIsImported(true);

        MethodTypeProviderInformation pi = new MethodTypeProviderInformation();
        pi.setProviderClass("QueryRPProvider");
        rpMethod.setProviderInformation(pi);
        rpMethod.setIsProvided(true);

        CommonTools.addMethod(service, rpMethod);
    }


    public static void removeQueryResourcePropertiesResourceProvider(ServiceType service, ServiceInformation info) {
        CommonTools.removeMethod(service.getMethods(), CommonTools.getMethod(service.getMethods(),
            "QueryResourceProperties"));
    }


    private static void addSetResourcePropertyResourceProvider(ServiceType service, ServiceInformation info) {
        MethodType rpMethod = new MethodType();
        rpMethod.setName("SetResourceProperties");
        rpMethod.setOutput(new MethodTypeOutput());
        rpMethod.getOutput().setIsArray(false);
        rpMethod.getOutput().setQName(new QName("", "void"));

        MethodTypeImportInformation ii = new MethodTypeImportInformation();
        ii.setFromIntroduce(Boolean.FALSE);
        ii.setInputMessage(new QName(
            "http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.wsdl",
            "SetResourcePropertiesRequest"));
        ii.setOutputMessage(new QName(
            "http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.wsdl",
            "SetResourcePropertiesResponse"));
        ii.setNamespace("http://docs.oasis-open.org/wsrf/2004/06/wsrf-WS-ResourceProperties-1.2-draft-01.wsdl");
        ii.setPackageName("org.oasis.wsrf.properties");
        ii.setPortTypeName("SetResourceProperties");
        ii.setWsdlFile("../wsrf/properties/WS-ResourceProperties.wsdl");
        rpMethod.setImportInformation(ii);
        rpMethod.setIsImported(true);

        MethodTypeProviderInformation pi = new MethodTypeProviderInformation();
        pi.setProviderClass("SetRPProvider");
        rpMethod.setProviderInformation(pi);
        rpMethod.setIsProvided(true);

        CommonTools.addMethod(service, rpMethod);
    }


    public static void removeSetResourcePropertyResourceProvider(ServiceType service, ServiceInformation info) {
        CommonTools.removeMethod(service.getMethods(), CommonTools.getMethod(service.getMethods(),
            "SetResourceProperties"));
    }


    public static void removeProviderFromServiceConfig(ServiceType service, String providerClass,
        ServiceInformation info) throws MobiusException, IOException {

        Document doc = XMLUtilities.fileNameToDocument(info.getBaseDirectory() + File.separator + "server-config.wsdd");
        List servicesEls = doc.getRootElement().getChildren("service",
            Namespace.getNamespace("http://xml.apache.org/axis/wsdd/"));
        for (int serviceI = 0; serviceI < servicesEls.size(); serviceI++) {
            Element serviceEl = (Element) servicesEls.get(serviceI);
            if (serviceEl.getAttribute("name").getValue().equals("SERVICE-INSTANCE-PREFIX/" + service.getName())) {
                List paramsEls = serviceEl.getChildren("parameter", Namespace
                    .getNamespace("http://xml.apache.org/axis/wsdd/"));
                for (int paramsI = 0; paramsI < paramsEls.size(); paramsI++) {
                    Element paramEl = (Element) paramsEls.get(paramsI);
                    if (paramEl.getAttributeValue("name").equals("providers")) {
                        String value = paramEl.getAttributeValue("value");
                        String newValue = "";
                        StringTokenizer strtok = new StringTokenizer(value, " ", false);
                        while (strtok.hasMoreElements()) {
                            String nextTok = strtok.nextToken();
                            if (!nextTok.equals(providerClass)) {
                                newValue = newValue + " " + nextTok;
                            }
                        }
                        paramEl.setAttribute("value", newValue);
                    }
                }

            }
        }

        FileWriter fw = new FileWriter(info.getBaseDirectory() + File.separator + "server-config.wsdd");
        fw.write(XMLUtilities.formatXML(XMLUtilities.documentToString(doc)));
        fw.close();
    }
    
    public static void addResourcePropertiesManagementResourceFrameworkOption(ServiceType service, ServiceInformation info){
        addGetMultipeResourcePropertiesResourceProvider(service, info);
        addGetResourcePropertyResourceProvider(service, info);
        addSetResourcePropertyResourceProvider(service, info);
        addQueryResourcePropertiesResourceProvider(service, info);
    }

}
