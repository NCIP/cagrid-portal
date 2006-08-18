package gov.nih.nci.cagrid.gridgrouper.client;

import edu.internet2.middleware.grouper.GrouperRuntimeException;
import edu.internet2.middleware.subject.Source;
import edu.internet2.middleware.subject.Subject;
import edu.internet2.middleware.subject.SubjectNotFoundException;
import edu.internet2.middleware.subject.SubjectType;
import gov.nih.nci.cagrid.gridgrouper.bean.MemberDescriptor;
import gov.nih.nci.cagrid.gridgrouper.common.SubjectUtils;
import gov.nih.nci.cagrid.gridgrouper.grouper.MemberI;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A href="mailto:ervin@bmi.osu.edu">David Ervin </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class Member implements MemberI {

	private MemberDescriptor des;

	private Subject subject;

	public Member(MemberDescriptor des) throws SubjectNotFoundException {
		this.des = des;
		subject = SubjectUtils.getSubject(des);

	}

	public String getSubjectId() {
		return subject.getId();
	}

	public Source getSubjectSource() throws GrouperRuntimeException {
		return subject.getSource();
	}

	public String getSubjectSourceId() {
		if (subject.getSource() == null) {
			return null;
		} else {
			return subject.getSource().getId();
		}
	}

	public SubjectType getSubjectType() {
		return subject.getType();
	}

	public String getSubjectTypeId() {
		return subject.getType().getName();
	}

	public String getUuid() {
		return des.getUUID();
	}

	public Subject getSubject() {
		return subject;
	}
	
}
