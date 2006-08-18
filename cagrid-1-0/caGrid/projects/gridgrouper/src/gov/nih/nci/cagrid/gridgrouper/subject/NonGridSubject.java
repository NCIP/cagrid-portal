package gov.nih.nci.cagrid.gridgrouper.subject;

import java.util.Map;
import java.util.Set;

import edu.internet2.middleware.subject.Source;
import edu.internet2.middleware.subject.Subject;
import edu.internet2.middleware.subject.SubjectType;
import edu.internet2.middleware.subject.provider.SubjectTypeEnum;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A href="mailto:ervin@bmi.osu.edu">David Ervin </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class NonGridSubject implements Subject {

	private String id;
	private SubjectType type;
	private Source source;


	protected NonGridSubject(String id, Source source) {
		this.id = id;
		this.type = SubjectTypeEnum.PERSON;
		this.source = source;
	}


	public String getAttributeValue(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}


	public Set getAttributeValues(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}


	public Map getAttributes() {
		// TODO Auto-generated method stub
		return null;
	}


	public String getDescription() {
		return id;
	}


	public String getId() {
		return id;
	}


	public String getName() {
		return id;
	}


	public Source getSource() {
		// TODO Auto-generated method stub
		return source;
	}


	public SubjectType getType() {
		// TODO Auto-generated method stub
		return type;
	}

}
