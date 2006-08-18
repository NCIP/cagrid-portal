package gov.nih.nci.cagrid.gridgrouper.subject;

import java.util.Set;

import edu.internet2.middleware.subject.SourceUnavailableException;
import edu.internet2.middleware.subject.Subject;
import edu.internet2.middleware.subject.SubjectNotFoundException;
import edu.internet2.middleware.subject.provider.BaseSourceAdapter;
import edu.internet2.middleware.subject.provider.SubjectTypeEnum;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A href="mailto:ervin@bmi.osu.edu">David Ervin </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class NonGridSourceAdapter extends BaseSourceAdapter {

	public NonGridSourceAdapter() {
		super();
		this.addSubjectType(SubjectTypeEnum.PERSON.getName());
		this.addSubjectType(SubjectTypeEnum.APPLICATION.getName());
	}


	public NonGridSourceAdapter(String id, String name) {
		super(id, name);
		this.addSubjectType(SubjectTypeEnum.PERSON.getName());
		this.addSubjectType(SubjectTypeEnum.APPLICATION.getName());
	}


	public Subject getSubject(String id) throws SubjectNotFoundException {
		return createSubject(id);
	}


	public Subject getSubjectByIdentifier(String name) throws SubjectNotFoundException {
		return createSubject(id);
	}


	private Subject createSubject(String id) throws SubjectNotFoundException {
		return new NonGridSubject(id,this);
	}
	
	public void init() throws SourceUnavailableException {
		// Nothing
	} // public void init()


	public Set search(String searchValue) {
		return null;
	}

}
