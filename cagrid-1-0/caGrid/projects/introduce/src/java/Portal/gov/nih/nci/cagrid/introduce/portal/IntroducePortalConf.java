package gov.nih.nci.cagrid.introduce.portal;

import java.util.HashMap;
import java.util.Map;

import javax.swing.ToolTipManager;

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


	public IntroducePortalConf() {
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
	}


	public void parse(MobiusResourceManager resourceManager, Element config) throws MobiusException {
	}
}
