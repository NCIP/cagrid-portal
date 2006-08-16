package gov.nih.nci.cagrid.gridgrouper.grouper;

import edu.internet2.middleware.grouper.FieldType;
import edu.internet2.middleware.grouper.GroupType;
import edu.internet2.middleware.grouper.Privilege;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public interface FieldI {
	  public GroupType getGroupType();
	  public FieldType getType();
	  public String getName();
	  public Privilege getReadPriv();
	  public boolean getRequired();
	  public Privilege getWritePriv();
}
