package gov.nih.nci.cagrid.authorization.impl;

import org.w3c.dom.Node;

import gov.nih.nci.cagrid.authorization.PENode;

public class PENodeImpl implements PENode {
	
	private Node node;
	private String objectId;
	
	public PENodeImpl(Node node, String objectId){
		this.node = node;
		this.objectId = objectId;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public Node getNode() {
		return this.node;
	}

	public String getObjectId() {
		return this.objectId;
	}

}
