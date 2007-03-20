package gov.nih.nci.cagrid.bdt.extension;

import gov.nih.nci.cagrid.bdt.client.BulkDataHandlerClient;
import gov.nih.nci.cagrid.bdt.templates.BDTResourceCreatorTemplate;
import gov.nih.nci.cagrid.bdt.templates.JNDIConfigResourceTemplate;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
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

import org.jdom.Document;
import org.jdom.Element;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.XMLUtilities;


public class BDTCodegenExtensionPostProcessor implements CodegenExtensionPostProcessor {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }


    public void postCodegen(ServiceExtensionDescriptionType desc, ServiceInformation info)
        throws CodegenExtensionException {

        // 1. change the resource in the jndi file
        File jndiConfigF = new File(info.getBaseDirectory().getAbsolutePath() + File.separator + "jndi-config.xml");

        Document serverConfigJNDIDoc = null;
        try {
            serverConfigJNDIDoc = XMLUtilities.fileNameToDocument(jndiConfigF.getAbsolutePath());
        } catch (MobiusException ex) {
            throw new CodegenExtensionException("Error parsing jndi-config.xml: " + ex.getMessage(), ex);
        }

        ServiceType mainService = info.getServices().getService(0);

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
                JNDIConfigResourceTemplate resourceT = new JNDIConfigResourceTemplate();
                String resourceS = resourceT.generate(new SpecificServiceInformation(info, info.getServices()
                    .getService(0)));
                Element newResourceEl;
                try {
                    newResourceEl = XMLUtilities.stringToDocument(resourceS).getRootElement();
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
        // return a BDTHandle
        if (mainService.getMethods() != null && mainService.getMethods().getMethod() != null) {
            for (int i = 0; i < mainService.getMethods().getMethod().length; i++) {
                MethodType method = mainService.getMethods().getMethod(i);
                if (method.getOutput().getIsClientHandle() != null
                    && method.getOutput().getIsClientHandle().booleanValue()
                    && method.getOutput().getClientHandleClass().equals(BulkDataHandlerClient.class.getName())) {
                    // need to see if i have added the base impl to this method
                    boolean needToAdd = false;
                    File implSourceFile = new File(info.getBaseDirectory() + File.separator + "src" + File.separator
                        + CommonTools.getPackageDir(mainService) + File.separator + "service" + File.separator
                        + mainService.getName() + "Impl.java");
                    StringBuffer fileContent = null;
                    try {
                        fileContent = Utils.fileToStringBuffer(implSourceFile);
                        String clientMethod = SyncSource.createUnBoxedSignatureStringFromMethod(method, info);
                        int startOfMethod = SyncSource.startOfSignature(fileContent, clientMethod);
                        int endOfMethod = SyncSource.bracketMatch(fileContent, startOfMethod);

                        if (startOfMethod == -1 || endOfMethod == -1) {
                            System.err.println("WARNING: Unable to locate method in Impl : " + method.getName());
                            return;
                        }
                        String serviceMethod = fileContent.substring(startOfMethod, endOfMethod);
                        String oldclientMethod = "";
                        oldclientMethod += "//TODO: Implement this autogenerated method";
                        if (serviceMethod.indexOf(oldclientMethod) >= 0) {
                            needToAdd = true;
                        }
                    } catch (Exception e) {
                        throw new CodegenExtensionException(
                            "Error editing service implementation class: " + e.getMessage(), e);
                    }
                    System.out.println(implSourceFile);

                    if (needToAdd) {
                        // now that i know i will remove it and replace it
                        fileContent = null;
                        try {
                            fileContent = Utils.fileToStringBuffer(implSourceFile);
                        } catch (Exception ex) {
                            throw new CodegenExtensionException(
                                "Error loading implementation source: " + ex.getMessage(), ex);
                        }

                        // remove the method
                        String clientMethod = SyncSource.createUnBoxedSignatureStringFromMethod(method, info);
                        int startOfMethod = SyncSource.startOfSignature(fileContent, clientMethod);
                        int endOfMethod = SyncSource.bracketMatch(fileContent, startOfMethod);

                        if (startOfMethod == -1 || endOfMethod == -1) {
                            System.err.println("WARNING: Unable to locate method in Impl : " + method.getName());
                            return;
                        }

                        fileContent.delete(startOfMethod, endOfMethod);

                        try {
                            FileWriter fw = new FileWriter(implSourceFile);
                            fw.write(SyncSource.removeMultiNewLines(fileContent.toString()));
                            fw.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }

                        // now add the new method.....
                        fileContent = null;
                        try {
                            fileContent = Utils.fileToStringBuffer(implSourceFile);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // insert the new client method
                        int endOfClass = fileContent.lastIndexOf("}");
                        clientMethod = "\n\t" + SyncSource.createUnBoxedSignatureStringFromMethod(method, info) + " "
                            + SyncSource.createExceptions(method, info, mainService);

                        clientMethod += "{\n";
                        BDTResourceCreatorTemplate rt = new BDTResourceCreatorTemplate();
                        String methodTemplate = rt.generate(new SpecificServiceInformation(info, mainService));
                        clientMethod += methodTemplate + "\t}\n";

                        fileContent.insert(endOfClass - 1, clientMethod);
                        try {
                            String fileContentString = fileContent.toString();
                            FileWriter fw = new FileWriter(implSourceFile);
                            fw.write(fileContentString);
                            fw.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
