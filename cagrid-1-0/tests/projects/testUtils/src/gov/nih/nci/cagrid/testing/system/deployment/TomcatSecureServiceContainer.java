package gov.nih.nci.cagrid.testing.system.deployment;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.XMLUtilities;

import java.io.File;

import org.jdom.Document;
import org.jdom.Element;


/**
 * TomcatServiceContainer 
 * Service container implementation for tomcat with security
 * 
 * @author David Ervin
 * @created Oct 19, 2007 12:01:22 PM
 * @version $Id: TomcatServiceContainer.java,v 1.4 2007/11/05 16:19:58 dervin
 *          Exp $
 */
public class TomcatSecureServiceContainer extends TomcatServiceContainer implements SecureContainer {
    public static final String SECURITY_DESCRIPTOR_PLACEHOLDER = "<!-- @CONTAINER_SECURITY_DESCRIPTOR@ -->";
    public static final String DESCRIPTOR_FILE_PLACEHOLDER = "@@LOCATION@@";
    public static final String SECURITY_DESCRIPTOR_PARAMETER_TEMPLATE = "" +
            "<parameter name=\"containerSecDesc\"\n" +
            "\t\tvalue=\"" + DESCRIPTOR_FILE_PLACEHOLDER + "\"/>";
    
    public TomcatSecureServiceContainer(ContainerProperties properties) {
        super(properties);
    }
    
    
    public void unpackContainer() throws ContainerException {
        super.unpackContainer();
        File wsrfCoreDir = new File(getProperties().getContainerDirectory(), 
            "webapps/wsrf/WEB-INF/etc/globus_wsrf_core/");
        // locate the security descriptor file
        File globalSecurityDescriptorFile = new File(wsrfCoreDir, "global_security_descriptor.xml");
        Document descriptorDocument = null;
        try {
            descriptorDocument = XMLUtilities.fileNameToDocument(
                globalSecurityDescriptorFile.getCanonicalPath());
        } catch (Exception ex) {
            throw new ContainerException("Error loading global security descriptor: " + ex.getMessage(), ex);
        }
        // fix the security configuration
        Element securityConfigElement = descriptorDocument.getRootElement();
        Element credentialElement = securityConfigElement.getChild("credential", securityConfigElement.getNamespace());
        Element keyFileElement = credentialElement.getChild("key-file", credentialElement.getNamespace());
        keyFileElement.setAttribute("value", getCertificatesDirectory().getAbsolutePath() + File.separator + "localhost_key.pem");
        Element certFileElement = credentialElement.getChild("cert-file", credentialElement.getNamespace());
        certFileElement.setAttribute("value", getCertificatesDirectory().getAbsolutePath() + File.separator + "localhost_cert.pem");
        // write the config back to disk
        try {
            String fixedConfig = XMLUtilities.formatXML(XMLUtilities.documentToString(descriptorDocument));
            Utils.stringBufferToFile(new StringBuffer(fixedConfig), globalSecurityDescriptorFile.getCanonicalPath());
        } catch (Exception ex) {
            throw new ContainerException("Error writting edited global security descriptor: " + ex.getMessage(), ex);
        }
        
        // edit the server-config.wsdd to use the modified global security descriptor.
        File serverWsdd = new File(wsrfCoreDir, "server-config.wsdd");
        StringBuffer wsddContents = null;
        try {
            wsddContents = Utils.fileToStringBuffer(serverWsdd);
            String parameter = SECURITY_DESCRIPTOR_PARAMETER_TEMPLATE.replace(
                DESCRIPTOR_FILE_PLACEHOLDER, globalSecurityDescriptorFile.getCanonicalPath());
            int placeholderIndex = wsddContents.indexOf(SECURITY_DESCRIPTOR_PLACEHOLDER);
            wsddContents.replace(placeholderIndex, placeholderIndex + SECURITY_DESCRIPTOR_PLACEHOLDER.length(), parameter);
            Utils.stringBufferToFile(wsddContents, serverWsdd.getCanonicalPath());
        } catch (Exception ex) {
            throw new ContainerException("Error editing server-config.wsdd for editing");
        }
    }


    public File getCertificatesDirectory() {
        return new File(this.getProperties().getContainerDirectory() + File.separator + "certificates");
    }

}
