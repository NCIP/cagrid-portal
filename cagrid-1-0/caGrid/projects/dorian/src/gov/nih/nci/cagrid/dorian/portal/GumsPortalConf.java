package gov.nih.nci.cagrid.gums.portal;

import gov.nih.nci.cagrid.gums.ifs.bean.AttributeDescriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Element;
import org.projectmobius.common.AbstractMobiusConfiguration;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.MobiusResourceManager;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: GumsPortalConf.java,v 1.3 2005-10-18 23:23:48 langella Exp $
 */
public class GumsPortalConf implements AbstractMobiusConfiguration {

	public static String RESOURCE = "GumsPortalConf";

	public Map attributeViewers;

	public List gumsServices;

	public GumsPortalConf() {
		this.attributeViewers = new HashMap();
		AttributeDescriptor rid = new AttributeDescriptor();
		rid.setNamespace("cagrid.nci.nih.gov/1/person");
		rid.setName("person");
		this.attributeViewers
				.put(rid,
						"gov.nih.nci.cagrid.gums.portal.attributes.PersonAttributeViewer");
	}

	public List getGumsServiceList() {
		return gumsServices;
	}

	public AttributeViewer getAttributeViewer(AttributeDescriptor rid)
			throws Exception {
		String cs = (String) this.attributeViewers.get(rid);
		if (cs == null) {
			throw new Exception(
					"No Attribute viewer could be found for the attribute "
							+ rid.getNamespace() + "/" + rid.getName()
							+ ", make sure the portal is properly configured.");
		}
		return (AttributeViewer) Class.forName(cs).newInstance();
	}

	private static String SERVICES_ELEMENT = "gums-services";

	private static String SERVICE_ELEMENT = "gums-service";

	private static String ATTRIBUTE_VIEWERS_ELEMENT = "attribute-viewers";

	private static String ATTRIBUTE_VIEWER_ELEMENT = "attribute-viewer";

	private static String ATTRIBUTE_VIEWER_NAMESPACE_ELEMENT = "namespace";

	private static String ATTRIBUTE_VIEWER_NAME_ELEMENT = "name";

	private static String ATTRIBUTE_VIEWER_CLASS_ELEMENT = "class";

	public void parse(MobiusResourceManager resourceManager, Element config)
			throws MobiusException {
		this.gumsServices = new ArrayList();
		Element services = config.getChild(SERVICES_ELEMENT, config
				.getNamespace());
		List serviceList = services.getChildren(SERVICE_ELEMENT, config
				.getNamespace());
		for (int i = 0; i < serviceList.size(); i++) {
			String service = ((Element)serviceList.get(i)).getText();
			if (service != null) {
				gumsServices.add(service);
			} 
		}
		
		Element viewers = config.getChild(ATTRIBUTE_VIEWERS_ELEMENT, config
				.getNamespace());
		if (viewers == null) {
			throw new MobiusException(
					"Error configuring the GUMS Portal, "
							+ ATTRIBUTE_VIEWERS_ELEMENT
							+ " element of the GridPortal configuration was not specified.");
		}
		List l = viewers.getChildren(ATTRIBUTE_VIEWER_ELEMENT, config
				.getNamespace());
		for (int i = 0; i < l.size(); i++) {
			Element view = (Element) l.get(i);
			AttributeDescriptor des = new AttributeDescriptor();

			String name = view.getChildText(ATTRIBUTE_VIEWER_NAME_ELEMENT,
					config.getNamespace());
			if (name == null) {
				throw new MobiusException(
						"In one of the attribute viewers, the "
								+ ATTRIBUTE_VIEWER_NAME_ELEMENT
								+ " element of the GridPortal configuration was not specified.");
			}
			des.setName(name);

			String ns = view.getChildText(ATTRIBUTE_VIEWER_NAMESPACE_ELEMENT,
					config.getNamespace());
			if (ns == null) {
				throw new MobiusException(
						"In one of the attribute viewers, the "
								+ ATTRIBUTE_VIEWER_NAMESPACE_ELEMENT
								+ " element of the GridPortal configuration was not specified.");
			}
			des.setNamespace(ns);

			String className = view.getChildText(
					ATTRIBUTE_VIEWER_CLASS_ELEMENT, config.getNamespace());
			if (className == null) {
				throw new MobiusException(
						"In one of the components, the "
								+ ATTRIBUTE_VIEWER_CLASS_ELEMENT
								+ " element of the GridPortal configuration was not specified.");
			}
			try {
				Class theclass = Class.forName(className);
				if (!AttributeViewer.class.isAssignableFrom(theclass)) {
					throw new MobiusException("The class " + className
							+ " for the attribute " + des.getNamespace() + ":"
							+ des.getName() + " must be of type, "
							+ AttributeViewer.class.getName());
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new MobiusException(
						"The following error occurred in configuring the Grid Portal "
								+ e.getMessage());
			}
			this.attributeViewers.put(des, className);
		}
	}
}
