package gov.nih.nci.cagrid.authorization;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public interface PENodeSelector {
	
	String getPrivilege();
	
	PENode[] selectPENodes(Document doc);

}
