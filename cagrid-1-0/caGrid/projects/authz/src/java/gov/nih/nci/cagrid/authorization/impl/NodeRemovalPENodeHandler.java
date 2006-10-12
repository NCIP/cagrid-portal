package gov.nih.nci.cagrid.authorization.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;

import gov.nih.nci.cagrid.authorization.PENode;
import gov.nih.nci.cagrid.authorization.PENodeHandler;

public class NodeRemovalPENodeHandler implements
		PENodeHandler {

	private static Log logger = LogFactory.getLog(NodeRemovalPENodeHandler.class.getName());
	
	public void handleNode(PENode peNode) {
		Node child = peNode.getNode();
		Node parent = child.getParentNode();
		if(parent != null){
			logger.debug("removing child");
			parent.removeChild(child);
		}else{
			logger.debug("parent is null");
		}
	}

}
