package gov.nih.nci.cagrid.introduce.codegen.security;

import gov.nih.nci.cagrid.introduce.ServiceInformation;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.beans.security.CommunicationMethod;
import gov.nih.nci.cagrid.introduce.beans.security.MethodSecurity;
import gov.nih.nci.cagrid.introduce.beans.security.SecureConversation;
import gov.nih.nci.cagrid.introduce.beans.security.ServiceSecurity;

import org.projectmobius.common.XMLUtilities;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @created Jun 22, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class SecurityDescriptor {
	public static String writeSecurityDescriptor(ServiceInformation info) {
		StringBuffer xml = new StringBuffer();
		xml.append("<securityConfig xmlns=\"http://www.globus.org\">");
		MethodsType methods = info.getMethods();
		if (methods != null) {
			MethodType[] method = methods.getMethod();
			if (method != null) {
				for (int i = 0; i < method.length; i++) {
					xml.append(writeMethodSettings(info.getServiceSecurity(),
							method[i]));
				}
			}
		}
		xml.append(writeServiceSettings());
		xml.append("</securityConfig>");
		try {
			return XMLUtilities.formatXML(xml.toString());
		} catch (Exception e) {
			return xml.toString();
		}
	}

	private static String writeMethodSettings(ServiceSecurity service,
			MethodType method) {
		MethodSecurity ms = method.getMethodSecurity();
		StringBuffer xml = new StringBuffer();
		if (differsFromService(service, ms)) {
			xml.append("<method name=\"" + method.getName() + "\">");

			xml.append("</method>");
		}

		return xml.toString();
	}

	private static String getSecureConversationSettings(SecureConversation comm)
			throws Exception {
		StringBuffer xml = new StringBuffer();
		if (comm != null) {
			xml.append("<auth-method>");
			xml.append("<GSISecureConversation>");
			xml.append("<protection-level>");
			if (comm.getCommunicationMethod() == null) {
				throw new Exception(
						"Secure Conversation requires the specification of acceptable communication mechanism.");
			} else if (comm.getCommunicationMethod().equals(
					CommunicationMethod.Privacy)) {
				xml.append("<privacy/>");
			} else if (comm.getCommunicationMethod().equals(
					CommunicationMethod.Integrity)) {
				xml.append("<integrity/>");
			} else if (comm.getCommunicationMethod().equals(
					CommunicationMethod.Integrity_Or_Privacy)) {
				xml.append("<privacy/>");
				xml.append("<integrity/>");
			} else {
				throw new Exception(
						"Secure Conversation requires the specification of acceptable communication mechanism.");
			}
			xml.append("</protection-level>");
			xml.append("</GSISecureConversation>");
			xml.append("</auth-method>");
		}
		return xml.toString();
	}

	private static boolean differsFromService(ServiceSecurity service,
			MethodSecurity method) {
		if (service != null) {
			if (!objectEquals(service.getMethodSecuritySetting(), method
					.getMethodSecuritySetting())) {
				return true;
			}
			if (communicationDiffers(service, method)) {
				return true;
			}
			if (!objectEquals(service.getAnonymousClients(), method
					.getAnonymousClients())) {
				return true;
			}

			if (!objectEquals(service.getClientAuthorization(), method
					.getClientAuthorization())) {
				return true;
			}

			if (!objectEquals(service.getClientCommunication(), method
					.getClientCommunication())) {
				return true;
			}

			if (!objectEquals(service.getDelegationMode(), method
					.getDelegationMode())) {
				return true;
			}
			if (!objectEquals(service.getRunAsMode(), method.getRunAsMode())) {
				return true;
			}

			return false;

		} else {
			return true;
		}
	}

	private static boolean communicationDiffers(ServiceSecurity service,
			MethodSecurity method) {
		if (service != null) {
			if (!objectEquals(service.getTransportLevelSecurity(), method
					.getTransportLevelSecurity())) {
				return true;
			}
			if (!objectEquals(service.getSecureConversation(), method
					.getSecureConversation())) {
				return true;
			}
			if (!objectEquals(service.getSecureMessage(), method
					.getSecureMessage())) {
				return true;
			}
			return false;

		} else {
			return true;
		}
	}

	private static boolean objectEquals(Object o1, Object o2) {
		if ((o1 == null) && (o2 == null)) {
			return true;
		} else if ((o1 != null) && (o2 != null) && (o1.equals(o2))) {
			return true;
		} else {
			return false;
		}
	}

	private static String writeServiceSettings() {
		StringBuffer xml = new StringBuffer();
		xml.append("<authz value=\"none\"/>");
		return xml.toString();
	}

}
