package gov.nih.nci.cagrid.gridgrouper.client;

import edu.internet2.middleware.grouper.GroupNotFoundException;
import edu.internet2.middleware.grouper.MemberNotFoundException;
import edu.internet2.middleware.subject.SubjectNotFoundException;
import gov.nih.nci.cagrid.gridgrouper.bean.MembershipDescriptor;
import gov.nih.nci.cagrid.gridgrouper.grouper.GroupI;
import gov.nih.nci.cagrid.gridgrouper.grouper.MemberI;
import gov.nih.nci.cagrid.gridgrouper.grouper.MembershipI;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class Membership extends GridGrouperObject implements MembershipI {

	private MembershipDescriptor des;

	private GroupI group;

	private GroupI viaGroup;

	private Member member;

	private GridGrouper gridGrouper;

	protected Membership(GridGrouper gridGrouper, MembershipDescriptor des)
			throws SubjectNotFoundException {
		this.gridGrouper = gridGrouper;
		this.des = des;
		this.member = new Member(des.getMember());
		this.group = new Group(this.gridGrouper, des.getGroup());
		if (this.des.getViaGroup() != null) {
			this.viaGroup = new Group(this.gridGrouper, des.getViaGroup());
		}

	}

	public int getDepth() {
		return des.getDepth();
	}

	public GroupI getGroup() throws GroupNotFoundException {
		return group;
	}


	public MemberI getMember() throws MemberNotFoundException {
		return member;
	}

	public GroupI getViaGroup() throws GroupNotFoundException {
		if(viaGroup!=null){
			return viaGroup;
		}else{
			throw new GroupNotFoundException("No via group found for this membership!!!");
		}
	}

}
