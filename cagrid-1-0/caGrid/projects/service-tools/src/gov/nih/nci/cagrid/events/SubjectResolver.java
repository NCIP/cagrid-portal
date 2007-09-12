package gov.nih.nci.cagrid.events;

import java.util.List;

public interface SubjectResolver {
	public List<String> resolveSubjects(String targetGroup);
	public String lookupAttribute(String targetId, String att);
}
