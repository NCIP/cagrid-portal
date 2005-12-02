package gov.nih.nci.cagrid.gums.portal;

import gov.nih.nci.cagrid.gums.idp.portal.Login;

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
public class GumsPortalConf implements AbstractMobiusConfiguration {

	public static String RESOURCE = "GumsPortalConf";

	public List gumsServices;

	private Login idpLogin;

	private List idps;

	public GumsPortalConf() {
		this.idps = new ArrayList();
	}

	public List getGumsServiceList() {
		return gumsServices;
	}

	private static String SERVICES_ELEMENT = "gums-services";

	private static String SERVICE_ELEMENT = "gums-service";

	private static String IDPS_ELEMENT = "idps";

	private static String IDP_ELEMENT = "idp";

	public Login getIdPLogin() {
		if (idpLogin == null) {
			idpLogin = new Login();
		}
		return idpLogin;
	}

	public void parse(MobiusResourceManager resourceManager, Element config)
			throws MobiusException {
		this.gumsServices = new ArrayList();

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
				gumsServices.add(service);
			}
		}

	}

	public List getIdPs() {
		return idps;
	}

}
