package gov.nih.nci.cagrid.data;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;

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
		System.out.println("Getting extension data");
		System.out.println("Getting extension data");
		System.out.println("Getting extension data");
		System.out.println("Data has QName " + Data.getTypeDesc().getXmlType());
		MessageElement[] anys = data.get_any();
		if (anys != null) {
			System.out.println("There are " + anys.length + " message elements");
			for (int i = 0; i < anys.length; i++) {
				System.out.println("Message Element " + i);
				System.out.println("QName: " + anys[i].getQName().toString());
				System.out.println(anys[i].getAsString());
			}
		} else {
			System.out.println("Message Element array is NULL!!!!");
		}
		MessageElement dataElement = null;
		for (int i = 0; anys != null && i < anys.length; i++) {
			if (anys[i].getQName().equals(Data.getTypeDesc().getXmlType())) {
				System.out.println("Found data message element");
				dataElement = anys[i];
				break;
			}
		}
		if (dataElement == null) {
			System.out.println("Creating new message element for Data");
			dataElement = new MessageElement(Data.getTypeDesc().getXmlType(), new Data());
			MessageElement[] newAnys = null;
			if (anys == null) {
				newAnys = new MessageElement[] {dataElement};
			} else {
				newAnys = new MessageElement[anys.length + 1];
				System.arraycopy(anys, 0, newAnys, 0, anys.length);
				newAnys[newAnys.length - 1] = dataElement;
			}
			System.out.println("Adding message element array to extension data");
			data.set_any(newAnys);
		}
		StringWriter dataXml = new StringWriter();
		Utils.serializeObject(dataElement, dataElement.getQName(), dataXml);
		/*
		Data value = (Data) Utils.deserializeObject(
			new StringReader(dataXml.getBuffer().toString()), Data.class, null);
		*/
		Data value = (Data) ObjectDeserializer.deserialize(
			new InputSource(new StringReader(dataXml.getBuffer().toString())), Data.class);
		return value;
	}
	
	
	public static void storeExtensionData(ExtensionTypeExtensionData extData, Data data) throws Exception {
		MessageElement element = new MessageElement(Data.getTypeDesc().getXmlType(), data);
		ExtensionTools.updateExtensionDataElement(extData, element);
	}
}
