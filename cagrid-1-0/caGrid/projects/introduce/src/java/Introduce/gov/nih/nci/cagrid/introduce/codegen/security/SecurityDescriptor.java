package gov.nih.nci.cagrid.introduce.codegen.security;

import gov.nih.nci.cagrid.introduce.ServiceInformation;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.beans.security.CommunicationMethod;
import gov.nih.nci.cagrid.introduce.beans.security.MethodSecurity;
import gov.nih.nci.cagrid.introduce.beans.security.MethodSecurityType;
import gov.nih.nci.cagrid.introduce.beans.security.RunAsMode;
import gov.nih.nci.cagrid.introduce.beans.security.SecureConversation;
import gov.nih.nci.cagrid.introduce.beans.security.SecureMessage;
import gov.nih.nci.cagrid.introduce.beans.security.ServiceSecurity;
import gov.nih.nci.cagrid.introduce.beans.security.TransportLevelSecurity;

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
		try {
			StringBuffer xml = new StringBuffer();
			xml.append("<securityConfig xmlns=\"http://www.globus.org\">");
			MethodsType methods = info.getMethods();
			if (methods != null) {
				MethodType[] method = methods.getMethod();
				if (method != null) {
					for (int i = 0; i < method.length; i++) {
						xml.append(writeMethodSettings(info
								.getServiceSecurity(), method[i]));
					}
				}
			}
			xml.append(writeServiceSettings(info.getServiceSecurity()));
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
	
	private static String writeServiceSettings(ServiceSecurity ss) throws Exception{
		StringBuffer xml = new StringBuffer();
		if(ss!=null){
		if ((ss.getMethodSecuritySetting() != null)
				&& (ss.getMethodSecuritySetting()
						.equals(MethodSecurityType.Custom))) {
			xml.append("<auth-method>");
			xml.append(getSecureConversationSettings(ss
					.getSecureConversation()));
			xml.append(getSecureMessageSettings(ss.getSecureMessage()));
			xml.append(getTransportLayerSecuritySettings(ss
					.getTransportLevelSecurity()));
			xml.append("</auth-method>");
			xml.append(getSecureConversationExtras(ss
					.getSecureConversation()));
			xml.append(getSecureMessageExtras(ss.getSecureMessage()));
			xml.append(getRunAsMode(ss.getRunAsMode()));			
		} else {
			xml.append("<auth-method>");
			xml.append("<none/>");
			xml.append("</auth-method>");
		}
		
		xml.append("<authz value=\"none\"/>");
		return xml.toString();
		}else{
			return "";
		}
	}

	private static String writeMethodSettings(ServiceSecurity service,
			MethodType method) throws Exception {
		try {
			MethodSecurity ms = method.getMethodSecurity();
			StringBuffer xml = new StringBuffer();
			if (determineWriteMethod(service, ms)) {
				xml.append("<method name=\"" + method.getName() + "\">");
				if ((ms.getMethodSecuritySetting() != null)
						&& (ms.getMethodSecuritySetting()
								.equals(MethodSecurityType.Custom))) {
					xml.append("<auth-method>");
					xml.append(getSecureConversationSettings(ms
							.getSecureConversation()));
					xml.append(getSecureMessageSettings(ms.getSecureMessage()));
					xml.append(getTransportLayerSecuritySettings(ms
							.getTransportLevelSecurity()));
					xml.append("</auth-method>");
					xml.append(getSecureConversationExtras(ms
							.getSecureConversation()));
					xml.append(getSecureMessageExtras(ms.getSecureMessage()));
					xml.append(getRunAsMode(ms.getRunAsMode()));			
				} else {
					xml.append("<auth-method>");
					xml.append("<none/>");
					xml.append("</auth-method>");
				}
				xml.append("</method>");
			}
			return xml.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
					"Error configuring the security descriptor for the method "
							+ method.getName() + ": "+e.getMessage());
		}
	}

	private static String getRunAsMode(RunAsMode mode) throws Exception {
		StringBuffer xml = new StringBuffer();
		if (mode != null) {
			xml.append("<run-as>");
			if (mode.equals(RunAsMode.System)) {
				xml.append("<system-identity/>");
			} else if (mode.equals(RunAsMode.Service)) {
				xml.append("<service-identity/>");
			} else if (mode.equals(RunAsMode.Resource)) {
				xml.append("<resource-identity/>");
			} else if (mode.equals(RunAsMode.Caller)) {
				xml.append("<caller-identity/>");
			} else {
				throw new Exception("Unsupported run as provided.");
			}
			xml.append("</run-as>");
		}
		return xml.toString();
	}

	private static String getSecureConversationExtras(SecureConversation comm)
			throws Exception {
		StringBuffer xml = new StringBuffer();
		if (comm != null) {
			if (comm.getContextLifetime() != null) {
				xml.append("<context-lifetime value=\""
						+ comm.getContextLifetime().intValue() + "\"/>");
			}
		}
		return xml.toString();
	}

	private static String getSecureMessageExtras(SecureMessage comm)
			throws Exception {
		StringBuffer xml = new StringBuffer();
		if (comm != null) {
			if (comm.getReplayAttackInterval() != null) {
				xml.append("<replay-attack-interval value=\""
						+ comm.getReplayAttackInterval().intValue() + "\"/>");
			}
		}
		return xml.toString();
	}

	private static String getSecureConversationSettings(SecureConversation comm)
			throws Exception {
		StringBuffer xml = new StringBuffer();
		if (comm != null) {
			xml.append("<GSISecureConversation>");
			xml.append(getProtectionLevel("Secure Conversation", comm
					.getCommunicationMethod()));
			xml.append("</GSISecureConversation>");
		}
		return xml.toString();
	}

	private static String getSecureMessageSettings(SecureMessage comm)
			throws Exception {
		StringBuffer xml = new StringBuffer();
		if (comm != null) {
			xml.append("<GSISecureMessage>");
			xml.append(getProtectionLevel("Secure Message", comm
					.getCommunicationMethod()));
			xml.append("</GSISecureMessage>");
		}
		return xml.toString();
	}

	private static String getTransportLayerSecuritySettings(
			TransportLevelSecurity comm) throws Exception {
		StringBuffer xml = new StringBuffer();
		if (comm != null) {
			xml.append("<GSITransport>");
			xml.append(getProtectionLevel("Transport Layer Security", comm
					.getCommunicationMethod()));
			xml.append("</GSITransport>");
		}
		return xml.toString();
	}

	private static String getProtectionLevel(String type,
			CommunicationMethod comm) throws Exception {
		StringBuffer xml = new StringBuffer();
		xml.append("<protection-level>");
		if (comm == null) {
			throw new Exception(
					type
							+ " requires the specification of acceptable communication mechanism.");
		} else if (comm.equals(CommunicationMethod.Privacy)) {
			xml.append("<privacy/>");
		} else if (comm.equals(CommunicationMethod.Integrity)) {
			xml.append("<integrity/>");
		} else if (comm.equals(CommunicationMethod.Integrity_Or_Privacy)) {
			xml.append("<privacy/>");
			xml.append("<integrity/>");
		} else {
			throw new Exception(
					type
							+ " requires the specification of acceptable communication mechanism.");
		}
		xml.append("</protection-level>");
		return xml.toString();
	}

	private static boolean determineWriteMethod(ServiceSecurity service,
			MethodSecurity method) {
		if (service != null) {
			if(method == null){
				return false;
			}
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
}
