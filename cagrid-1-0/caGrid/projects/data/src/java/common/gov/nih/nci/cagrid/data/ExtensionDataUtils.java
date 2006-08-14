package gov.nih.nci.cagrid.data;

import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.extension.utils.AxisJdomUtils;

import org.apache.axis.message.MessageElement;
import org.jdom.Element;

/** 
 *  ExtensionDataUtils
 *  Utilities for extension data management
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Aug 14, 2006 
 * @version $Id$ 
 */
public class ExtensionDataUtils {
	
	private static Element getFeaturesElement(ExtensionTypeExtensionData data) {
		MessageElement me = ExtensionTools.getExtensionDataElement(data, DataServiceConstants.DS_FEATURES);
		return AxisJdomUtils.fromMessageElement(me);
	}
	

	public static boolean getWsEnumFeature(ExtensionTypeExtensionData data) {
		Element features = getFeaturesElement(data);
		if (features != null && features.getAttribute(DataServiceConstants.USE_WS_ENUM) != null) {
			return Boolean.valueOf(features.getAttributeValue(
				DataServiceConstants.USE_WS_ENUM)).booleanValue();
		}
		return false;
	}
	
	
	public static boolean getGridIdentifiersFeature(ExtensionTypeExtensionData data) {
		Element features = getFeaturesElement(data);
		if (features != null && features.getAttribute(DataServiceConstants.USE_GRID_IDENTIFIERS) != null) {
			return Boolean.valueOf(features.getAttributeValue(
				DataServiceConstants.USE_GRID_IDENTIFIERS)).booleanValue();
		}
		return false;
	}
	
	
	public static boolean getSdkDataSourceFeature(ExtensionTypeExtensionData data) {
		Element features = getFeaturesElement(data);
		if (features != null && features.getAttribute(DataServiceConstants.USE_SDK_DATA_SOURCE) != null) {
			return Boolean.valueOf(features.getAttributeValue(
				DataServiceConstants.USE_SDK_DATA_SOURCE)).booleanValue();
		}
		return false;
	}
	
	
	public static boolean getCustomDataSourceFeature(ExtensionTypeExtensionData data) {
		Element features = getFeaturesElement(data);
		if (features != null && features.getAttribute(DataServiceConstants.USE_CUSTOM_DATA_SORUCE) != null) {
			return Boolean.valueOf(features.getAttributeValue(
				DataServiceConstants.USE_CUSTOM_DATA_SORUCE)).booleanValue();
		}
		return false;
	}
}
