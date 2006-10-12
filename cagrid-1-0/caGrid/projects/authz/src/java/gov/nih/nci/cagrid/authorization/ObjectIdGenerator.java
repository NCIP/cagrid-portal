package gov.nih.nci.cagrid.authorization;

import org.w3c.dom.Node;

public interface ObjectIdGenerator {
	
	String generateId(Node node);

}
