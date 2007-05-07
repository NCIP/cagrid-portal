package gov.nih.nci.cagrid.introduce.extensions.wsenum.codegen;

import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionException;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionPostProcessor;

import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;

import org.jdom.Element;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.XMLUtilities;


/**
 * WsEnumCreationPostProcessor Post-creation extension to Introduce to add
 * WS-Enumeration support to a Grid Service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A> *
 * @created Nov 16, 2006
 * @version $Id: WsEnumCodegenPostProcessor.java,v 1.2 2007-05-07 19:41:50 dervin Exp $
 */
public class WsEnumCodegenPostProcessor implements CodegenExtensionPostProcessor {

    public WsEnumCodegenPostProcessor() {

    }


    public void postCodegen(ServiceExtensionDescriptionType desc, ServiceInformation info)
        throws CodegenExtensionException {
         editJNDI(info);
    }


    private void editJNDI(ServiceInformation info) throws CodegenExtensionException {
        boolean jndiEdited = false;
        File serviceJndiFile = new File(info.getBaseDirectory().getAbsolutePath()
            + File.separator + "jndi-config.xml");
        Element jndiRoot = null;
        try {
            jndiRoot = XMLUtilities.fileNameToDocument(serviceJndiFile.getAbsolutePath()).getRootElement();
        } catch (MobiusException ex) {
            throw new CodegenExtensionException("Error loading service's JNDI file: " + ex.getMessage(), ex);
        }
        // locate the enumeration service context's stuff in the JNDI
        Iterator serviceElementIter = jndiRoot.getChildren("service", jndiRoot.getNamespace()).iterator();
        String serviceName = "SERVICE-INSTANCE-PREFIX/" 
            + info.getServices().getService(0).getName() + "Enumeration";
        System.out.println("JNDIEDIT: Looking for service named " + serviceName);
        while (serviceElementIter.hasNext()) {
            Element serviceElement = (Element) serviceElementIter.next();
            System.out.println("JNDIEDIT: Service " + serviceElement.getAttributeValue("name"));
            if (serviceElement.getAttributeValue("name").equals(serviceName)) {
                System.out.println("JNDIEDIT: Editing service's resource");
                // see if the service already has the resource definition
                if (serviceElement.getChild("resource", jndiRoot.getNamespace()) == null) {
                    System.out.println("JNDIEDIT: No resource found, adding new");
                    // add the enumeration resource description
                    serviceElement.addContent(getEnumerationResourceDescription());
                    jndiEdited = true;
                }
                break;
            }
        }
        if (jndiEdited) {
            System.out.println("JNDIEDIT: jndi was edited");
            // write the JNDI with edits back out
            try {
                FileWriter jndiWriter = new FileWriter(serviceJndiFile);
                String xml = XMLUtilities.formatXML(XMLUtilities.elementToString(jndiRoot));
                System.out.println("JNDIEDIT: WRITING EDITED JNDI");
                System.out.println(xml);
                jndiWriter.write(xml);
                jndiWriter.flush();
                jndiWriter.close();
            } catch (Exception ex) {
                throw new CodegenExtensionException("Error writing edited JNDI file: " + ex.getMessage(), ex);
            }
        }
    }
    
    
    private Element getEnumerationResourceDescription() throws CodegenExtensionException {
        String globusLocation = System.getenv("GLOBUS_LOCATION");
        File coreJndiConfigFile = new File(new File(globusLocation).getAbsolutePath()
            + File.separator + "etc" + File.separator + "globus_wsrf_core" 
            + File.separator + "jndi-config.xml");
        System.out.println("JNDIEDIT: core jndi file is " + coreJndiConfigFile.getAbsolutePath());
        Element coreJndiRoot = null;
        try {
            coreJndiRoot = XMLUtilities.fileNameToDocument(
                coreJndiConfigFile.getAbsolutePath()).getRootElement();
        } catch (MobiusException ex) {
            throw new CodegenExtensionException("Error loading Globus core JNDI file: " 
                + ex.getMessage(), ex);
        }
        Element globalElement = coreJndiRoot.getChild("global", coreJndiRoot.getNamespace());
        Iterator resourceElementIter = globalElement.getChildren("resource", coreJndiRoot.getNamespace()).iterator();
        while (resourceElementIter.hasNext()) {
            Element resourceElement = (Element) resourceElementIter.next();
            System.out.println("JNDIEDIT: Looking at resource " + resourceElement.getAttributeValue("name"));
            if (resourceElement.getAttributeValue("name").equals("enumeration/EnumerationHome")) {
                Element resourceDescription = (Element) resourceElement.clone();
                System.out.println("JNDIEDIT: found resource");
                return (Element) resourceDescription.detach();
            }
        }
        System.out.println("JNDIEDIT: NEVER FOUND THE RESOURCE DESCRIPTION");
        return null;
    }
}
