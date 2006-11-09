package gov.nih.nci.cagrid.data;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.extension.CQLProcessorConfig;
import gov.nih.nci.cagrid.data.extension.CQLProcessorConfigProperty;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.io.StringReader;
import java.io.StringWriter;

import org.apache.axis.message.MessageElement;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.xml.sax.InputSource;

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
	
	public static Data getExtensionData(ExtensionTypeExtensionData data) throws Exception {
		MessageElement[] anys = data.get_any();
		MessageElement dataElement = null;
		for (int i = 0; anys != null && i < anys.length; i++) {
			if (anys[i].getQName().equals(Data.getTypeDesc().getXmlType())) {
				dataElement = anys[i];
				break;
			}
		}
		if (dataElement == null) {
			dataElement = new MessageElement(Data.getTypeDesc().getXmlType(), new Data());
			MessageElement[] newAnys = null;
			if (anys == null) {
				newAnys = new MessageElement[] {dataElement};
			} else {
				newAnys = new MessageElement[anys.length + 1];
				System.arraycopy(anys, 0, newAnys, 0, anys.length);
				newAnys[newAnys.length - 1] = dataElement;
			}
			data.set_any(newAnys);
		}
		StringWriter dataXml = new StringWriter();
		Utils.serializeObject(dataElement, dataElement.getQName(), dataXml);
		Data value = (Data) ObjectDeserializer.deserialize(
			new InputSource(new StringReader(dataXml.getBuffer().toString())), Data.class);
		return value;
	}
	
	
	public static void storeExtensionData(ExtensionTypeExtensionData extData, Data data) throws Exception {
		MessageElement element = new MessageElement(Data.getTypeDesc().getXmlType(), data);
		ExtensionTools.updateExtensionDataElement(extData, element);
	}
	
	
	public static String getQueryProcessorStubClassName(ServiceInformation info) {
		ServiceType mainService = CommonTools.getService(info.getServices(), 
			info.getIntroduceServiceProperties().getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME));
		String basePackage = mainService.getPackageName();
		basePackage += ".stubs.cql";
		return basePackage + "." + DataServiceConstants.QUERY_PROCESSOR_STUB_NAME;		
	}
	
	
	public static void setCQLProcessorProperty(Data data, String key, String value) {
		CQLProcessorConfig config = data.getCQLProcessorConfig();
		if (config == null) {
			config = new CQLProcessorConfig();
			data.setCQLProcessorConfig(config);
		}
		CQLProcessorConfigProperty[] props = config.getProperty();
		if (props == null) {
			props = new CQLProcessorConfigProperty[] {
				new CQLProcessorConfigProperty(key, value)
			};
		} else {
			boolean found = false;
			for (int i = 0; i < props.length; i++) {
				if (props[i].getName().equals(key)) {
					props[i].setValue(value);
					found = true;
					break;
				}
			}
			if (!found) {
				props = (CQLProcessorConfigProperty[]) Utils.appendToArray(
					props, new CQLProcessorConfigProperty(key, value));
			}
		}
		config.setProperty(props);
	}
}
