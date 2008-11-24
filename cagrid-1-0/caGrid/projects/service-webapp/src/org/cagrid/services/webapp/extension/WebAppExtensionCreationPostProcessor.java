package org.cagrid.services.webapp.extension;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.XMLUtilities;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionException;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionPostProcessor;
import gov.nih.nci.cagrid.introduce.extension.utils.ExtensionUtilities;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;


public class WebAppExtensionCreationPostProcessor implements CreationExtensionPostProcessor {

    public static String EXTENSION_NAME = "service_webapp";


    public void postCreate(ServiceExtensionDescriptionType desc, ServiceInformation info)
        throws CreationExtensionException {
        try {
            Document doc = XMLUtilities.fileNameToDocument(info.getBaseDirectory() + File.separator
                + "server-config.wsdd");
            List servicesEls = doc.getRootElement().getChildren("service",
                Namespace.getNamespace("http://xml.apache.org/axis/wsdd/"));
            for (int serviceI = 0; serviceI < servicesEls.size(); serviceI++) {
                Element serviceEl = (Element) servicesEls.get(serviceI);
                ServiceType service = CommonTools.getService(info.getServices(), serviceEl.getAttributeValue("name")
                    .substring(serviceEl.getAttributeValue("name").lastIndexOf("/") + 1));

                // need to add the service name att and the etc path att for
                // each service
                Element servletEl = new Element("parameter", Namespace.getNamespace("http://xml.apache.org/axis/wsdd/"));
                servletEl.setAttribute("name", "servletClass");
                servletEl.setAttribute("value", "org.cagrid.services.webapp.servlet.DefaultServiceServlet");

                serviceEl.addContent(servletEl);

            }

            FileWriter fw = new FileWriter(info.getBaseDirectory() + File.separator + "server-config.wsdd");
            fw.write(XMLUtilities.formatXML(XMLUtilities.documentToString(doc)));
            fw.close();

            // copy over the jar containing the default servlet
            // copy in the required jars
            FileFilter filter = new FileFilter() {

                public boolean accept(File pathname) {
                    return !pathname.isDirectory() && pathname.getName().endsWith(".jar");
                }

            };

            File extensionLib = new File("extensions" + File.separator + EXTENSION_NAME + File.separator + "lib");
            File[] jars = extensionLib.listFiles(filter);
            for (int i = 0; i < jars.length; i++) {
                File in = jars[i];
                File out = new File(info.getBaseDirectory().getAbsolutePath() + File.separator + "lib" + File.separator
                    + in.getName());

                Utils.copyFile(in, out);

            }

            ExtensionUtilities.resyncWithLibDir(new File(info.getBaseDirectory().getAbsolutePath() + File.separator
                + ".classpath"));

        } catch (Exception e) {
            throw new CreationExtensionException(e.getMessage(), e);
        }

    }

}
