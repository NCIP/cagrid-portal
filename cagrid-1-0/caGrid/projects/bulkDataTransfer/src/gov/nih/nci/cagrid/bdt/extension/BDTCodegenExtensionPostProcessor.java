package gov.nih.nci.cagrid.bdt.extension;

import gov.nih.nci.cagrid.bdt.templates.JNDIConfigResourceTemplate;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionException;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionPostProcessor;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.info.SpecificServiceInformation;

import java.io.File;
import java.io.FileWriter;
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

		ServiceType mainService = info.getServices().getService(0);

		// change the resource in the jndi file
		File jndiConfigF = new File(info.getBaseDirectory().getAbsolutePath() + File.separator + "jndi-config.xml");

		Document serverConfigJNDIDoc = null;
		try {
			serverConfigJNDIDoc = XMLUtilities.fileNameToDocument(jndiConfigF.getAbsolutePath());
		} catch (MobiusException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
						JNDIConfigResourceTemplate resourceT = new JNDIConfigResourceTemplate();
						String resourceS = resourceT.generate(new SpecificServiceInformation(info, info.getServices()
							.getService(0)));
						Element newResourceEl;
						try {
							newResourceEl = XMLUtilities.stringToDocument(resourceS).getRootElement();
						} catch (MobiusException e) {
							throw new CodegenExtensionException(e.getMessage());
						}
						serviceEl.addContent(newResourceEl.detach());
						FileWriter resourceFW;
						try {
							resourceFW = new FileWriter(jndiConfigF);
							resourceFW.write(XMLUtilities.formatXML(XMLUtilities.documentToString(serverConfigJNDIDoc)));
							resourceFW.close();
						} catch (Exception e) {
							throw new CodegenExtensionException(e.getMessage());
						}

						break;
					}
				}
				break;
			}

		}

	}

}
