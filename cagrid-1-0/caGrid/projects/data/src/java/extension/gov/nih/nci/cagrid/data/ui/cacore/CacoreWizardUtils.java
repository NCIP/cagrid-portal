package gov.nih.nci.cagrid.data.ui.cacore;

import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.awt.Component;
import java.awt.Container;

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
	
	
	public static String getServiceBaseDir(ServiceInformation info) {
		/* You'd think this would be right, but no...
		String serviceDir = info.getIntroduceServiceProperties()
			.getProperty(IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR);
		*/
		String serviceDir = info.getBaseDirectory().getAbsolutePath();
		return serviceDir;
	}
	
	
	public static void setSdkSerialization(NamespaceType nsType) {
		if (nsType.getSchemaElement() != null) {
			for (int i = 0; i < nsType.getSchemaElement().length; i++) {
				SchemaElementType elem = nsType.getSchemaElement(i);
				elem.setClassName(elem.getType());
				elem.setSerializer(DataServiceConstants.SDK_SERIALIZER);
				elem.setDeserializer(DataServiceConstants.SDK_DESERIALIZER);
			}
		}
	}
}
