package gov.nih.nci.cagrid.introduce.codegen.methods;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;


/**
 * SyncSecurity TODO:DOCUMENT ME
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created Jun 14, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class SyncSecurity {

	public static final String SERVICE_NAMESPACE_PREFIX = "service";
	public static final String THIS_NAMESPACE = "http://www.globus.org";

	Document securityConfig;
	List secureMethods;
	Element securityElement;
	Properties deploymentProperties;
	File baseDir;


	public SyncSecurity(File baseDir, Properties deploymentProperties) {
		this.baseDir = baseDir;
		this.deploymentProperties = deploymentProperties;
	}


	public void sync(List methods) {
		SAXBuilder builder = new SAXBuilder(false);
		try {
			securityConfig = builder.build(new File(baseDir.getAbsolutePath() + File.separator + "src" + File.separator
				+ this.deploymentProperties.get("introduce.skeleton.package.dir")
				+ "/service/globus/custom-security-config.xml"));
			secureMethods = securityConfig.getRootElement().getChildren("method",
				securityConfig.getRootElement().getNamespace());
			securityElement = securityConfig.getRootElement();

			//compare methods to securityDocument
			for (int i = 0; i < methods.size(); i++) {
				checkMethod((Element) methods.get(i));
			}

			//compare securityDocument to methods
			for (int i = 0; i < secureMethods.size(); i++) {
				Element secureMethod = (Element) secureMethods.get(i);
				boolean found = false;
				for (int j = 0; j < methods.size(); j++) {
					Element method = (Element) methods.get(j);
					if (secureMethod.getAttributeValue("name").equals(
						SERVICE_NAMESPACE_PREFIX + ":" + method.getAttributeValue("name"))) {
						found = true;
						break;
					}
				}

				if (!found) {
					securityElement.removeContent(secureMethod);
				}
			}

		} catch (JDOMException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			FileWriter fw = new FileWriter(new File(baseDir.getAbsolutePath() + File.separator + "src" + File.separator
				+ this.deploymentProperties.get("introduce.skeleton.package.dir")
				+ "/service/globus/custom-security-config.xml"));
			XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
			out.output(this.securityConfig, fw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void checkMethod(Element method) {
		Attribute secureAtt = method.getAttribute("secure");
		String methodName = method.getAttributeValue("name");

		if (secureAtt != null && (!secureAtt.getValue().equals("NONE"))) {
			boolean found = false;
			//make sure the method is in there
			for (int i = 0; i < secureMethods.size(); i++) {
				Element secureMethod = (Element) secureMethods.get(i);
				if (secureMethod.getAttributeValue("name").equals(SERVICE_NAMESPACE_PREFIX + ":" + methodName)) {
					found = true;
					break;
				}
			}
			if (!found) {
				Element newMethod = new Element("method", this.securityElement.getNamespace());
				newMethod.setAttribute("name", SERVICE_NAMESPACE_PREFIX + ":" + methodName);
				Element authEl = new Element("auth-method", this.securityElement.getNamespace());
				Element gsiEl = new Element("gsi", this.securityElement.getNamespace());
				if (secureAtt.getValue().equals("PRIVACY") || secureAtt.getValue().equals("INTEGRITY")) {
					Element plEl = new Element("protection-level", this.securityElement.getNamespace());
					if (secureAtt.getValue().equals("INTEGRITY")) {
						Element integrityEl = new Element("integrity", this.securityElement.getNamespace());
						plEl.addContent(integrityEl);
					}
					if (secureAtt.getValue().equals("PRIVACY") || secureAtt.getValue().equals("EITHER")) {
						Element privacyEl = new Element("privacy", this.securityElement.getNamespace());
						plEl.addContent(privacyEl);
					}
					gsiEl.addContent(plEl);
				}
				authEl.addContent(gsiEl);
				newMethod.addContent(authEl);
				this.securityElement.addContent(0, newMethod);
			}
		} else {
			//make sure the method is not in there
			for (int i = 0; i < secureMethods.size(); i++) {
				Element secureMethod = (Element) secureMethods.get(i);
				if (secureMethod.getAttributeValue("name").equals(SERVICE_NAMESPACE_PREFIX + ":" + methodName)) {
					this.securityElement.removeContent(secureMethod);
					break;
				}
			}
		}

	}

}
