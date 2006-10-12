package gov.nih.nci.cagrid.authorization.impl;

import java.util.Iterator;
import java.util.Set;

import javax.security.auth.Subject;

import gov.nih.nci.cagrid.authorization.PENode;
import gov.nih.nci.cagrid.authorization.PENodeHandler;
import gov.nih.nci.cagrid.authorization.PENodeSelector;
import gov.nih.nci.cagrid.authorization.PENodeSelectorSelector;
import gov.nih.nci.cagrid.gridgrouper.grouper.GroupI;
import gov.nih.nci.security.AuthorizationManager;
import gov.nih.nci.security.exceptions.CSException;

import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.message.MessageElement;
import org.apache.axis.message.SOAPBody;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.wsrf.impl.security.authorization.exceptions.AuthorizationException;
import org.w3c.dom.Document;

public class CSMNodeFilter extends BasicHandler {

	private static Log logger = LogFactory.getLog(CSMNodeFilter.class
			.getName());

	private AuthorizationManager authorizationManager;

	private PENodeSelectorSelector selectorSelector;

	private PENodeHandler nodeHandler;

	public PENodeHandler getNodeHandler() {
		return nodeHandler;
	}

	public void setNodeHandler(PENodeHandler nodeHandler) {
		this.nodeHandler = nodeHandler;
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

	public void invoke(MessageContext context) throws AxisFault {

		SOAPBody body = null;
		Document doc = null;
		try {
			body = (SOAPBody) context.getCurrentMessage()
					.getSOAPBody();
			MessageElement mel = (MessageElement) body.getChildElements().next();
			doc = mel.getAsDocument();
			body.removeChild(mel);
		} catch (Exception ex) {
			throw new RuntimeException("Error getting body document: "
					+ ex.getMessage(), ex);
		}

		// Pull out the subject
		Subject subject = null;
		try {
			subject = (Subject) context
					.getProperty(org.globus.wsrf.impl.security.authentication.Constants.PEER_SUBJECT);
		} catch (Exception ex) {
			throw new AxisFault("Error getting subject from context: "
					+ ex.getMessage(), ex);
		}
		if (subject == null) {
			throw new AxisFault("No subject found in context.");
		}

		PENodeSelector selector = getSelectorSelector().select(context);
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
						throw new AxisFault("Error checking permission: "
								+ ex.getMessage(), ex);
					}
				}
				if (!authorizedAnyGroup) {
					getNodeHandler().handleNode(peNodes[i]);
				}
			}
		}
		try{
			body.addDocument(doc);
		}catch(Exception ex){
			throw new AxisFault("Error adding body document: " + ex.getMessage(), ex);
		}
	}

}
