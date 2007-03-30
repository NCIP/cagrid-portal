package gov.nih.nci.cagrid.bdt.extension;

import gov.nih.nci.cagrid.bdt.templates.BDTResourceCreatorTemplate;
import gov.nih.nci.cagrid.bdt.templates.JNDIConfigResourceTemplate;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.codegen.services.methods.SyncSource;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionException;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionPostProcessor;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.info.SpecificServiceInformation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.xml.namespace.QName;

import org.jdom.Document;
import org.jdom.Element;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.XMLUtilities;


public class BDTCodegenExtensionPostProcessor implements CodegenExtensionPostProcessor {

    public static final QName BDT_HANDLER_REFERENCE_QNAME = new QName(
        "http://cagrid.nci.nih.gov/BulkDataHandlerReference", "BulkDataHandlerReference");


    public void postCodegen(ServiceExtensionDescriptionType desc, ServiceInformation info)
        throws CodegenExtensionException {
        ServiceType mainService = info.getServices().getService(0);

        // 1. change the resource in the jndi file
        File jndiConfigF = new File(info.getBaseDirectory().getAbsolutePath() + File.separator + "jndi-config.xml");

        Document serverConfigJNDIDoc = null;
        try {
            serverConfigJNDIDoc = XMLUtilities.fileNameToDocument(jndiConfigF.getAbsolutePath());
        } catch (MobiusException ex) {
            throw new CodegenExtensionException("Error parsing jndi-config.xml: " + ex.getMessage(), ex);
        }

        List serviceEls = serverConfigJNDIDoc.getRootElement().getChildren();
        for (int i = 0; i < serviceEls.size(); i++) {
            Element serviceEl = (Element) serviceEls.get(i);
            if (serviceEl.getName().equals("service")
                && serviceEl.getAttributeValue("name").equals(
                    "SERVICE-INSTANCE-PREFIX/" + mainService.getName() + "BulkDataHandler")) {
                List resourceEls = serviceEl.getChildren();
                for (int j = 0; j < resourceEls.size(); j++) {
                    Element resourceEl = (Element) resourceEls.get(j);
                    if (resourceEl.getName().equals("resource") && resourceEl.getAttributeValue("name").equals("home")) {
                        serviceEl.removeContent(resourceEl);
                        break;
                    }
                }
                JNDIConfigResourceTemplate resourceTemplate = new JNDIConfigResourceTemplate();
                String generatedResource = resourceTemplate.generate(new SpecificServiceInformation(
                    info, info.getServices().getService(0)));
                Element newResourceEl;
                try {
                    newResourceEl = XMLUtilities.stringToDocument(generatedResource).getRootElement();
                } catch (MobiusException ex) {
                    throw new CodegenExtensionException(ex.getMessage(), ex);
                }
                serviceEl.addContent(newResourceEl.detach());
                FileWriter resourceFW;
                try {
                    resourceFW = new FileWriter(jndiConfigF);
                    resourceFW.write(XMLUtilities.formatXML(XMLUtilities.documentToString(serverConfigJNDIDoc)));
                    resourceFW.close();
                } catch (Exception ex) {
                    throw new CodegenExtensionException(ex.getMessage(), ex);
                }
            }
        }

        // 2. add any impls that need to be added if the method 
        // returns a BDTHandle
        if (mainService.getMethods() != null && mainService.getMethods().getMethod() != null) {
            for (int i = 0; i < mainService.getMethods().getMethod().length; i++) {
                MethodType method = mainService.getMethods().getMethod(i);
                if (methodReturnsBdtHandle(mainService, method)) {
                    // need to see if i have added the base impl to this method
                    File implSourceFile = new File(info.getBaseDirectory() + File.separator + "src" + File.separator
                        + CommonTools.getPackageDir(mainService) + File.separator + "service" + File.separator
                        + mainService.getName() + "Impl.java");
                    System.out.println("Editing source file: " + implSourceFile);

                    StringBuffer fileContent = null;
                    try {
                        fileContent = Utils.fileToStringBuffer(implSourceFile);
                    } catch (IOException ex) {
                        throw new CodegenExtensionException("Error loading service implementaton: " 
                            + ex.getMessage(), ex);
                    }

                    // perform the implementation
                    implementBdtMethod(fileContent, info, method);

                    // write out the edited service implementation
                    try {
                        String fileContentString = fileContent.toString();
                        FileWriter fw = new FileWriter(implSourceFile);
                        fw.write(fileContentString);
                        fw.close();
                    } catch (IOException ex) {
                        throw new CodegenExtensionException("Error saving service implementation: " +
                            ex.getMessage(), ex);
                    }
                }
            }
        }
        
        
        // 3. edit the bdt client to implement the WS-Enumeration DataSource interface
        // org.xmlsoap.schemas.ws._2004._09.enumeration.DataSource
        String bulkDataServiceName = mainService.getName() + "BulkDataHandler";
        File bdtClientSourceFile = new File(info.getBaseDirectory().getAbsolutePath() 
            + File.separator + "src" + File.separator + CommonTools.getPackageDir(mainService) 
            + File.separator + "bdt" + File.separator + "client" + File.separator
            + bulkDataServiceName + "Client.java");
        System.out.println("Editing BDT client: " + bdtClientSourceFile);
        
        StringBuffer clientImpl = null;
        try {
            clientImpl = Utils.fileToStringBuffer(bdtClientSourceFile);
        } catch (IOException ex) {
            throw new CodegenExtensionException("Error loading BDT client source: " + ex.getMessage(), ex);
        }
        
        // public class <%=info.getService().getName()%>Client extends ServiceSecurityClient implements <%=serviceName%>I {
        String base = "public class " + bulkDataServiceName 
            + "Client extends ServiceSecurityClient implements " + bulkDataServiceName + "I";
        String uneditedClassDeclaration = base + " {";
        int classDeclStart = clientImpl.indexOf(uneditedClassDeclaration);
        if (classDeclStart != -1) {
            // edit the impl
            int classDeclEnd = classDeclStart + uneditedClassDeclaration.length();
            String editedClassDeclaration = base + ", org.xmlsoap.schemas.ws._2004._09.enumeration.DataSource {";
            clientImpl.delete(classDeclStart, classDeclEnd);
            clientImpl.insert(classDeclStart, editedClassDeclaration);
            // save the impl to disk
            try {
                FileWriter clientImplWriter = new FileWriter(bdtClientSourceFile);
                clientImplWriter.write(clientImpl.toString());
                clientImplWriter.flush();
                clientImplWriter.close();
            } catch (IOException ex) {
                throw new CodegenExtensionException("Error writing edited BDT client: " + ex.getMessage(), ex);
            }
        }        
    }


    private boolean methodReturnsBdtHandle(ServiceType service, MethodType method) {
        MethodTypeOutput methodOutput = method.getOutput();
        if (methodOutput != null) {
            if (methodOutput.getIsClientHandle() != null && methodOutput.getIsClientHandle().booleanValue()) {
                // method returns a client handle
                String specificServiceClientHandleClass = service.getPackageName() + ".bdt.client." + service.getName()
                    + "BulkDataHandlerClient";
                return methodOutput.getClientHandleClass().equals(specificServiceClientHandleClass);
            } else {
                // check the output QName
                QName outputName = methodOutput.getQName();
                return outputName.getLocalPart().equals(service.getName() + "BulkDataHandlerReference")
                    || outputName.equals(BDT_HANDLER_REFERENCE_QNAME);
            }
        }
        return false;
    }


    private void implementBdtMethod(StringBuffer implSource, ServiceInformation info, MethodType method)
        throws CodegenExtensionException {
        try {
            String methodSignature = SyncSource.createUnBoxedSignatureStringFromMethod(method, info);
            int startOfMethod = SyncSource.startOfSignature(implSource, methodSignature);
            int endOfMethod = SyncSource.bracketMatch(implSource, startOfMethod);

            if (startOfMethod == -1 || endOfMethod == -1) {
                System.err.println("WARNING: Unable to locate method in Impl : " + method.getName());
                return;
            }
            String serviceMethod = implSource.substring(startOfMethod, endOfMethod);
            // the stub method body
            String oldclientMethod = "";
            oldclientMethod += "//TODO: Implement this autogenerated method";
            if (serviceMethod.indexOf(oldclientMethod) < 0) {
                System.out.println("Method no longer stubbed, implementation will not be added");
                return;
            }
            // remove the stub method
            implSource.delete(startOfMethod, endOfMethod);

            // insert the new client method
            ServiceType mainService = info.getServices().getService(0);
            // create the new signature, including exceptions
            String bdtMethodImpl = "\n\t" + SyncSource.createUnBoxedSignatureStringFromMethod(method, info) + " "
                + SyncSource.createExceptions(method, info, mainService);
            // open the method body
            bdtMethodImpl += "{\n";
            // generate the method body from the template
            BDTResourceCreatorTemplate template = new BDTResourceCreatorTemplate();
            String methodTemplate = template.generate(new SpecificServiceInformation(info, mainService));
            bdtMethodImpl += methodTemplate + "\t}\n";

            // append the method to the end of the class
            int endOfClass = implSource.lastIndexOf("}");
            implSource.insert(endOfClass - 1, bdtMethodImpl);
        } catch (Exception ex) {
            throw new CodegenExtensionException("Error implementing BDT method: " + ex.getMessage(), ex);
        }
    }
}
