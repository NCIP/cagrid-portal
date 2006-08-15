package gov.nih.nci.cagrid.gridgrouper.common;

import edu.internet2.middleware.subject.Subject;
import edu.internet2.middleware.subject.SubjectNotFoundException;
import gov.nih.nci.cagrid.gridgrouper.subject.GridSourceAdapter;
import gov.nih.nci.cagrid.gridgrouper.subject.NonGridSourceAdapter;

public class SubjectUtils {

	public static final GridSourceAdapter GRID_SOURCE = new GridSourceAdapter(
			"grid", "Grid Grouper: Grid Source Adapter");

	public static final NonGridSourceAdapter NON_GRID_SOURCE = new NonGridSourceAdapter(
			"Unknown", "Grid Grouper: Unknown Source Adapter");

	public static Subject getSubject(String id) throws SubjectNotFoundException {
		return getSubject(id, false);
	}

	public static Subject getSubject(String id, boolean allowNonGridSubject)
			throws SubjectNotFoundException {
		try {
			return GRID_SOURCE.getSubject(id);

		} catch (SubjectNotFoundException e) {
			if (allowNonGridSubject) {
				return NON_GRID_SOURCE.getSubject(id);
			} else {
				throw e;
			}
		}

	}

}
