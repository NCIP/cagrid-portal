package gov.nih.nci.cagrid.authorization.impl;

import gov.nih.nci.cagrid.authorization.ObjectIdGenerator;
import gov.nih.nci.cagrid.authorization.PENodeSelector;
import gov.nih.nci.security.AuthorizationManager;

import java.util.Map;

import javax.xml.soap.SOAPException;

import org.apache.axis.MessageContext;
import org.apache.axis.message.SOAPBody;

public abstract class AbstractCSMAuthz {
	
	
	private AuthorizationManager authorizationManager;

	private PENodeSelector protectionElementLocator;

	private Map privilegeMap;

	private ObjectIdGenerator objectIdGenerator;
	
	public AuthorizationManager getAuthorizationManager() {
		return authorizationManager;
	}

	public void setAuthorizationManager(
			AuthorizationManager authorizationManager) {
		this.authorizationManager = authorizationManager;
	}

	public Map getPrivilegeMap() {
		return privilegeMap;
	}

	public void setPrivilegeMap(Map privilegeMap) {
		this.privilegeMap = privilegeMap;
	}

	public PENodeSelector getProtectionElementLocator() {
		return protectionElementLocator;
	}

	public void setProtectionElementLocator(
			PENodeSelector protectionElementLocator) {
		this.protectionElementLocator = protectionElementLocator;
	}

	public ObjectIdGenerator getObjectIdGenerator() {
		return objectIdGenerator;
	}

	public void setObjectIdGenerator(ObjectIdGenerator objectIdGenerator) {
		this.objectIdGenerator = objectIdGenerator;
	}
	
	public SOAPBody getSOAPBody(MessageContext context) throws SOAPException{
		return (SOAPBody) context.getCurrentMessage().getSOAPBody();
	}

}
