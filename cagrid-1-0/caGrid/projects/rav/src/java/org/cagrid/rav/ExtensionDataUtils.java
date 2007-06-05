package org.cagrid.rav;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

import org.apache.axis.message.MessageElement;
import org.ggf.schemas.jsdl._2005._11.jsdl.Application_Type;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.xml.sax.InputSource;

/**
 * @author madduri
 *
 */
public class ExtensionDataUtils {
	
	public  static Application_Type getExtensionData(ExtensionTypeExtensionData data) throws Exception {
		MessageElement[] anys = data.get_any();
		MessageElement dataElement = null;
		for (int i = 0; anys != null && i < anys.length; i++) {
			if (anys[i].getQName().equals(Application_Type.getTypeDesc().getXmlType())) {
				dataElement = anys[i];
				break;
			}
		}
		if (dataElement == null) {
			dataElement = new MessageElement(Application_Type.getTypeDesc().getXmlType(), new Application_Type());
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
		Application_Type value = (Application_Type) ObjectDeserializer.deserialize(
			new InputSource(new StringReader(dataXml.getBuffer().toString())), Application_Type.class);
		return value;
	}
	
	public static String getMethodName(String applicationPath){
		System.out.println("Appname " + applicationPath);
		String result = applicationPath;
		
		int sepindex = applicationPath.lastIndexOf(File.separator);
		int dotindex = applicationPath.lastIndexOf(".");
		
		if (sepindex >0 && dotindex > 0){
			result = applicationPath.substring(sepindex + 1, dotindex);
		}else if (sepindex >0){
			result = applicationPath.substring(sepindex + 1);
		}else if (dotindex > 0){
			result = applicationPath.substring(0, dotindex);
		}
		System.out.println("Appname " + result);
		return result;
	}
	public static String delimitSlashes(String applicationPath){
		System.out.println(applicationPath);
		 
		applicationPath = applicationPath.replaceAll("\\\\", "\\\\\\\\");
		//applicationPath = applicationPath.replaceAll(File.separator, "asd");
		System.out.println(applicationPath);
		return applicationPath;
	}
	public static void main(String []args){
		/*System.out.println(getMethodName("C:\\asd\\solit"));
		System.out.println(getMethodName("C:\\asd\\solit.exe"));
		System.out.println(getMethodName("solit"));
		System.out.println(getMethodName("solit.exe"));*/
		
		System.out.println(delimitSlashes("C:" + File.separator + "asd" + File.separator + "solit.exe"));
		//System.out.println(delimitSlashes("C:/asd/solit.exe"));
		//System.out.println(File.pathSeparator);
	}

}
