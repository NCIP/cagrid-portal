package gov.nih.nci.cagrid.introduce.codegen.security;

import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.info.SpecificServiceInformation;

import org.projectmobius.common.XMLUtilities;

public class SecurityMetadataGenerator {
/*
	public static Service getSecurityMetadata(SpecificServiceInformation info) {
		try {
			StringBuffer xml = new StringBuffer();
			xml.append("<securityConfig xmlns=\"http://www.globus.org\">");
			
			xml.append("<method name=\"queryResourceProperties\">\n");
			xml.append("    <auth-method>\n");
			xml.append("      <none/>\n");
		    xml.append("    </auth-method>\n");
		    xml.append("</method>\n");
		    
		    xml.append("<method name=\"getMultipleResourceProperties\">\n");
			xml.append("    <auth-method>\n");
			xml.append("      <none/>\n");
		    xml.append("    </auth-method>\n");
		    xml.append("</method>\n");
		    
		    xml.append("<method name=\"getResourceProperty\">\n");
			xml.append("    <auth-method>\n");
			xml.append("      <none/>\n");
		    xml.append("    </auth-method>\n");
		    xml.append("</method>\n");
			 
			xml.append(writeServiceSettings(info.getService().getServiceSecurity()));
			
				ServiceType service = info.getService();
				MethodsType methods = service.getMethods();
				if (methods != null) {
					MethodType[] method = methods.getMethod();
					if (method != null) {
						for (int i = 0; i < method.length; i++) {
							xml.append(writeMethodSettings(info.getService().getServiceSecurity(), method[i]));
						}
					}
				}
			
			xml.append("</securityConfig>");
			try {
				return XMLUtilities.formatXML(xml.toString());
			} catch (Exception e) {
				return xml.toString();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}
*/
}
