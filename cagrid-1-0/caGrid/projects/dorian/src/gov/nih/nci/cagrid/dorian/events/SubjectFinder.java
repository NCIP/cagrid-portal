package gov.nih.nci.cagrid.dorian.events;

import java.util.List;

public interface SubjectFinder {
	public List<String> resolveSubjects(String targetId);
	public String lookupFirstName(String targetId);
	public String lookupLastName(String targetId);
	public String lookupEmail(String targetId);
}
