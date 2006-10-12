package gov.nih.nci.cagrid.authorization.impl;

import gov.nih.nci.cagrid.authorization.PENode;
import gov.nih.nci.cagrid.authorization.PENodeSelector;
import gov.nih.nci.cagrid.authorization.PENodeSelectorSelector;
import gov.nih.nci.cagrid.gridgrouper.grouper.GroupI;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.exceptions.CSException;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.security.auth.Subject;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.soap.SOAPException;

import org.apache.axis.message.MessageElement;
import org.apache.axis.message.SOAPBody;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.wsrf.impl.security.authorization.exceptions.AuthorizationException;
import org.globus.wsrf.impl.security.authorization.exceptions.CloseException;
import org.globus.wsrf.impl.security.authorization.exceptions.InitializeException;
import org.globus.wsrf.impl.security.authorization.exceptions.InvalidPolicyException;
import org.globus.wsrf.security.authorization.PDP;
import org.globus.wsrf.security.authorization.PDPConfig;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class CSMPDP implements PDP {

	private static Log logger = LogFactory.getLog(CSMPDP.class.getName());

	private AuthorizationManager authorizationManager;

	private PENodeSelectorSelector selectorSelector;

	public Node getPolicy(Node arg0) throws InvalidPolicyException {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getPolicyNames() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isPermitted(Subject subject, MessageContext context,
			QName operation) throws AuthorizationException {

		boolean permitted = false;

		if (!(context instanceof org.apache.axis.MessageContext)) {
			throw new IllegalArgumentException(
					"Expected instance of org.apache.axis.MessageContext. Got "
							+ (context == null ? "null" : context.getClass()
									.getName()));
		}
		org.apache.axis.MessageContext apacheCtx = (org.apache.axis.MessageContext) context;

		Document doc = null;
		try {
			SOAPBody body = (SOAPBody) apacheCtx.getCurrentMessage()
					.getSOAPBody();
			MessageElement mel = (MessageElement) body.getChildElements().next();
			doc = mel.getAsDocument();
		} catch (Exception ex) {
			throw new RuntimeException("Error getting body document: "
					+ ex.getMessage(), ex);
		}

		PENodeSelector selector = getSelectorSelector().select(apacheCtx);
		if (selector != null) {
			PENode[] peNodes = selector.selectPENodes(doc);
			Set groups = AuthzUtils.getGroups(subject);
			logger.debug("Found " + peNodes.length + " nodes.");
			logger.debug("Found " + groups.size() + " groups.");

			for (int i = 0; i < peNodes.length; i++) {
				boolean authorizedAnyGroup = false;
				for (Iterator j = groups.iterator(); j.hasNext()
						&& !authorizedAnyGroup;) {
					GroupI group = (GroupI) j.next();
					String groupIdent = group.getName();
					logger.debug("Checking authorization for '" + groupIdent
							+ "', '" + peNodes[i].getObjectId() + "', '"
							+ selector.getPrivilege());
					try {
						authorizedAnyGroup = getAuthorizationManager()
								.checkPermission(groupIdent,
										peNodes[i].getObjectId(),
										selector.getPrivilege());
					} catch (CSException ex) {
						throw new AuthorizationException(
								"Error checking permission: " + ex.getMessage(),
								ex);
					}
				}
				if(!authorizedAnyGroup){
					permitted = false;
					break;
				}else{
					permitted = true;
				}
			}
		}

		return permitted;
	}

	public Node setPolicy(Node arg0) throws InvalidPolicyException {
		// TODO Auto-generated method stub
		return null;
	}

	public void close() throws CloseException {
		// TODO Auto-generated method stub

	}

	public void initialize(PDPConfig arg0, String arg1, String arg2)
			throws InitializeException {
		// TODO Auto-generated method stub

	}

	public AuthorizationManager getAuthorizationManager() {
		return authorizationManager;
	}

	public void setAuthorizationManager(
			AuthorizationManager authorizationManager) {
		this.authorizationManager = authorizationManager;
	}

	public PENodeSelectorSelector getSelectorSelector() {
		return selectorSelector;
	}

	public void setSelectorSelector(PENodeSelectorSelector selectorSelector) {
		this.selectorSelector = selectorSelector;
	}

}
