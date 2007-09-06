package gov.nih.nci.cagrid.dorian.events;

public abstract class EventHandler {

	private SubjectResolver subjectResolver;


	public SubjectResolver getSubjectResolver() {
		return subjectResolver;
	}


	protected void setSubjectResolver(SubjectResolver subjectResolver) {
		this.subjectResolver = subjectResolver;
	}


	public abstract void handleEvent(Event event);

}
