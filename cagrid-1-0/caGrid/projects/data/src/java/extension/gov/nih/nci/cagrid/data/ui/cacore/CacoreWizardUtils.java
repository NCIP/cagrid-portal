package gov.nih.nci.cagrid.data.ui.cacore;

import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.awt.Component;
import java.awt.Container;
import java.io.File;

/** 
 *  CacoreWizardConstants
 *  TODO:DOCUMENT ME
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Sep 25, 2006 
 * @version $Id$ 
 */
public class CacoreWizardUtils {

	public static final String LAST_DIRECTORY_KEY = "LastDirectory";
	
	
	public static void setContainerEnabled(Container con, boolean enable) {
		for (int i = 0; i < con.getComponentCount(); i++) {
			Component comp = con.getComponent(i);
			comp.setEnabled(enable);
			if (comp instanceof Container) {
				setContainerEnabled((Container) comp, enable);
			}
		}
	}
	
	
	public static String getServiceLibDir(ServiceInformation info) {
		String serviceDir = info.getIntroduceServiceProperties()
			.getProperty(IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR);
		return serviceDir + File.separator + "lib";
	}
}
