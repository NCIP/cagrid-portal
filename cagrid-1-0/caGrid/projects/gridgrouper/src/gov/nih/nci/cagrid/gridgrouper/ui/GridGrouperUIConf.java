package gov.nih.nci.cagrid.gridgrouper.ui;

import java.util.ArrayList;
import java.util.List;

import org.jdom.Element;
import org.projectmobius.common.AbstractMobiusConfiguration;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.MobiusResourceManager;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella</A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @version $Id: GridGrouperBaseTreeNode.java,v 1.1 2006/08/04 03:49:26 langella
 *          Exp $
 */
public class GridGrouperUIConf implements AbstractMobiusConfiguration {

	public static String RESOURCE = "GridGrouperUI";

	public List gridGrouperServices;

	public GridGrouperUIConf() {

	}

	public List getGridGrouperServices() {
		return gridGrouperServices;
	}

	private static String SERVICES_ELEMENT = "grid-groupers";

	private static String SERVICE_ELEMENT = "grid-grouper";

	public void parse(MobiusResourceManager resourceManager, Element config)
			throws MobiusException {
		this.gridGrouperServices = new ArrayList();

		Element services = config.getChild(SERVICES_ELEMENT, config
				.getNamespace());
		List serviceList = services.getChildren(SERVICE_ELEMENT, config
				.getNamespace());
		for (int i = 0; i < serviceList.size(); i++) {
			String service = ((Element) serviceList.get(i)).getText();
			if (service != null) {
				gridGrouperServices.add(service);
			}
		}

	}

}
