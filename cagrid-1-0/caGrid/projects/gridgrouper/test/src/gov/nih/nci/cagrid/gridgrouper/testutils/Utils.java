package gov.nih.nci.cagrid.gridgrouper.testutils;

import java.util.Iterator;
import java.util.Set;

import edu.internet2.middleware.grouper.Group;
import edu.internet2.middleware.grouper.GroupFinder;
import edu.internet2.middleware.grouper.GroupNotFoundException;
import edu.internet2.middleware.grouper.GrouperSession;
import edu.internet2.middleware.grouper.Membership;
import edu.internet2.middleware.grouper.OwnerNotFoundException;
import edu.internet2.middleware.subject.Subject;
import gov.nih.nci.cagrid.gridgrouper.bean.GroupDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.GroupIdentifier;
import gov.nih.nci.cagrid.gridgrouper.bean.StemDescriptor;
import gov.nih.nci.cagrid.gridgrouper.bean.StemIdentifier;
import gov.nih.nci.cagrid.gridgrouper.common.SubjectUtils;
import gov.nih.nci.cagrid.gridgrouper.subject.AnonymousGridUserSubject;

public class Utils {

	public static StemIdentifier getStemIdentifier(StemDescriptor des) {
		StemIdentifier id = new StemIdentifier();
		id.setStemName(des.getName());
		return id;
	}

	public static StemIdentifier getRootStemIdentifier() {
		StemIdentifier id = new StemIdentifier();
		id
				.setStemName(gov.nih.nci.cagrid.gridgrouper.client.GridGrouper.ROOT_STEM);
		return id;
	}

	public static GroupIdentifier getGroupIdentifier(GroupDescriptor des) {
		GroupIdentifier id = new GroupIdentifier();
		id.setGroupName(des.getName());
		return id;
	}

	public static void printMemberships(GroupDescriptor grp) throws Exception {
		Subject subject = SubjectUtils
				.getSubject(AnonymousGridUserSubject.ANONYMOUS_GRID_USER_ID);
		GrouperSession session = GrouperSession.start(subject);
		Group group = GroupFinder.findByName(session, grp.getName());
		System.out.println();
		System.out.println("All Memberships of " + group.getName());
		System.out.println("-------------------------------------------------");
		System.out.println();
		Set set = group.getMemberships();
		Iterator itr = set.iterator();
		while (itr.hasNext()) {
			Membership m = (Membership) itr.next();
			System.out.println("Member Name: "
					+ m.getMember().getSubject().getId());
			System.out.println("Depth: " + m.getDepth());
			System.out.println("Group: " + m.getGroup().getName());
			try {
				System.out.println("Via Group: " + m.getViaGroup().getName());
			} catch (GroupNotFoundException e) {
				System.out.println("Via Group: NONE");
			}

			try {
				System.out.println("Via Owner: " + m.getVia().getUuid());
			} catch (OwnerNotFoundException e) {
				System.out.println("Via Owner: NONE");
			}
			System.out.println();
		}
	}

}
