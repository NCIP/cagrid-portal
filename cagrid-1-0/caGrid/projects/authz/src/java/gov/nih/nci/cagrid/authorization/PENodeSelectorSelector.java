package gov.nih.nci.cagrid.authorization;

import org.apache.axis.MessageContext;

public interface PENodeSelectorSelector {
	
	PENodeSelector select(MessageContext context);

}
