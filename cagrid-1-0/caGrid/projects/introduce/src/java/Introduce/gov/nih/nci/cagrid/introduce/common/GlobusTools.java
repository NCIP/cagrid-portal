package gov.nih.nci.cagrid.introduce.common;

import gov.nih.nci.cagrid.introduce.portal.IntroducePortalConf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.projectmobius.portal.PortalResourceManager;

public class GlobusTools {

	public static List getGlobusProvidedNamespaces() {
		List globusNamespaces = new ArrayList();
		File schemasDir = new File(getGlobusLocation() + File.separator
				+ "share" + File.separator + "schema");

		CommonTools.getTargetNamespaces(globusNamespaces, schemasDir);
		return globusNamespaces;
	}

	public static String getGlobusLocation() {
		IntroducePortalConf conf = (IntroducePortalConf) PortalResourceManager
				.getInstance().getResource(IntroducePortalConf.RESOURCE);
		return conf.getGlobusLocation();
	}

}
