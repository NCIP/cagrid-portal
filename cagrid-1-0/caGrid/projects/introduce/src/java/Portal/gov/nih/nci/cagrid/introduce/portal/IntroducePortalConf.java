package gov.nih.nci.cagrid.introduce.portal;

import org.jdom.Element;
import org.projectmobius.common.AbstractMobiusConfiguration;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.MobiusResourceManager;


/**
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class IntroducePortalConf implements AbstractMobiusConfiguration {

	public static String RESOURCE = "IntroducePortalConf";

	public static final String GME_EL = "gme";
	public static final String CADSR_EL = "cadsr";
	public static final String GME_DISCOVERY = "GME";
	public static final String CADSR_DISCOVERY = "CADSR";
	

	public String gme = null;

	public String cadsr = null;
	
	public String discoveryType = null;


	public IntroducePortalConf() {
	}
	
	public String getDiscoveryType(){
		return this.discoveryType;
	}


	public String getGME() {
		return gme;
	}


	public String getCADSR() {
		return cadsr;
	}


	public void setGme(String gme) {
		this.gme = gme;
	}


	public void parse(MobiusResourceManager resourceManager, Element config) throws MobiusException {
		this.discoveryType = config.getAttributeValue("discoveryType");
		Element gmeEl = config.getChild(GME_EL, config.getNamespace());
		if (gmeEl != null) {
			this.gme = gmeEl.getText();
		}
		Element cadsrEl = config.getChild(CADSR_EL, config.getNamespace());
		if (cadsrEl != null) {
			this.cadsr = cadsrEl.getText();
		}

	}

}
