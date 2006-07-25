package gov.nih.nci.cagrid.gridgrouper.subject;
import java.util.HashSet;
import java.util.Set;

import edu.internet2.middleware.subject.Source;
import edu.internet2.middleware.subject.SourceUnavailableException;
import edu.internet2.middleware.subject.Subject;
import edu.internet2.middleware.subject.SubjectNotFoundException;
import edu.internet2.middleware.subject.SubjectNotUniqueException;
import edu.internet2.middleware.subject.provider.SubjectTypeEnum;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class GridUserSubjectSource implements Source {

	private String id;
	private Set subjectTypes;


	public GridUserSubjectSource(String id) {
		this.id = id;
		this.subjectTypes = new HashSet();
		this.subjectTypes.add(SubjectTypeEnum.APPLICATION);
	}


	public String getId() {
		return id;
	}


	public String getName() {
		return id;
	}


	public Subject getSubject(String arg0) throws SubjectNotFoundException, SubjectNotUniqueException {
		// TODO: Add Validation
		return new GridUserSubject(arg0, this);
	}


	public Subject getSubjectByIdentifier(String arg0) throws SubjectNotFoundException, SubjectNotUniqueException {
		// TODO: Add Validation
		return new GridUserSubject(arg0, this);
	}


	public Set getSubjectTypes() {
		return subjectTypes;
	}


	public void init() throws SourceUnavailableException {
		// TODO Auto-generated method stub

	}


	public Set search(String arg0) {
		return null;
	}


	public void setId(String id) {
		this.id = id;

	}


	public void setName(String name) {
		this.id = name;

	}
}
