package gov.nih.nci.cagrid.gridgrouper.grouper;

import edu.internet2.middleware.grouper.FieldType;
import edu.internet2.middleware.grouper.GroupType;
import edu.internet2.middleware.grouper.Privilege;

public interface FieldI {
	public GroupType getGroupType();
	  public FieldType getType();
	  public String getName();
	  public Privilege getReadPriv();
	  public boolean getRequired();
	  public Privilege getWritePriv();
}
