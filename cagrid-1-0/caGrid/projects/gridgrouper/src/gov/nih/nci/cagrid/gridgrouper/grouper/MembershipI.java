package gov.nih.nci.cagrid.gridgrouper.grouper;

import edu.internet2.middleware.grouper.GroupNotFoundException;
import edu.internet2.middleware.grouper.MemberNotFoundException;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A href="mailto:ervin@bmi.osu.edu">David Ervin </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public interface MembershipI {
	// public Set getChildMemberships();

	public GroupI getGroup() throws GroupNotFoundException;

	// public FieldI getList();

	public MemberI getMember() throws MemberNotFoundException;

	// public MembershipI getParentMembership() throws
	// MembershipNotFoundException;

	// public Owner getVia() throws OwnerNotFoundException;

	public GroupI getViaGroup() throws GroupNotFoundException;

	public int getDepth();
}
