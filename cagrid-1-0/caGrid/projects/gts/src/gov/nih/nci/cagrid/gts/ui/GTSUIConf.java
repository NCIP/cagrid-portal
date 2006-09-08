package gov.nih.nci.cagrid.gts.ui;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;
import org.projectmobius.common.AbstractMobiusConfiguration;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.MobiusResourceManager;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class GTSUIConf implements AbstractMobiusConfiguration {

	public static String RESOURCE = "GTSPortalConf";

	private static String SERVICES_ELEMENT = "gts-services";

	private static String SERVICE_ELEMENT = "gts-service";

	public List gtsServices;

	public GTSUIConf() {

	}

	public List getGTSServiceList() {
		return gtsServices;
	}

	public void parse(MobiusResourceManager resourceManager, Element config)
			throws MobiusException {
		this.gtsServices = new ArrayList();

		Element services = config.getChild(SERVICES_ELEMENT, config
				.getNamespace());
		List serviceList = services.getChildren(SERVICE_ELEMENT, config
				.getNamespace());
		for (int i = 0; i < serviceList.size(); i++) {
			String service = ((Element) serviceList.get(i)).getText();
			if (service != null) {
				gtsServices.add(service);
			}
		}

	}
}
