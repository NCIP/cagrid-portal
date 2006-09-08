package gov.nih.nci.cagrid.dorian.ui;

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
public class DorianPortalConf implements AbstractMobiusConfiguration {

	public static String RESOURCE = "DorianPortalConf";

	public List dorianServices;

	private List idps;

	public DorianPortalConf() {
		this.idps = new ArrayList();
	}

	public List getDorianServiceList() {
		return dorianServices;
	}

	private static String SERVICES_ELEMENT = "dorian-services";

	private static String SERVICE_ELEMENT = "dorian-service";

	private static String IDPS_ELEMENT = "idps";

	private static String IDP_ELEMENT = "idp";

	public void parse(MobiusResourceManager resourceManager, Element config)
			throws MobiusException {
		this.dorianServices = new ArrayList();

		Element idpsDOM = config.getChild(IDPS_ELEMENT, config.getNamespace());
		List idpList = idpsDOM.getChildren(IDP_ELEMENT, config.getNamespace());
		for (int i = 0; i < idpList.size(); i++) {
			try {
				IdPConf conf = new IdPConf((Element) idpList.get(i));
				idps.add(conf);
			} catch (Exception e) {
				throw new MobiusException(e.getMessage(), e);
			}
		}

		Element services = config.getChild(SERVICES_ELEMENT, config
				.getNamespace());
		List serviceList = services.getChildren(SERVICE_ELEMENT, config
				.getNamespace());
		for (int i = 0; i < serviceList.size(); i++) {
			String service = ((Element) serviceList.get(i)).getText();
			if (service != null) {
				dorianServices.add(service);
			}
		}

	}

	public List getIdPs() {
		return idps;
	}

}
