package gov.nih.nci.cagrid.gridgrouper.grouper;

import edu.internet2.middleware.grouper.GroupNotFoundException;
import edu.internet2.middleware.grouper.MemberNotFoundException;

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
