package org.cagrid.tools.events;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A href="mailto:ervin@bmi.osu.edu">David Ervin</A>
 */
public abstract class EventHandler {

	private SubjectResolver subjectResolver;

	private Map<String, String> properties;

	private String name;

	private Log log;


	public EventHandler(String name, EventHandlerConfiguration conf) {
		this.name = name;
		this.log = LogFactory.getLog(this.getClass().getName());
		properties = new HashMap<String, String>();
		if (conf != null) {
			Property[] props = conf.getProperty();
			if (props != null) {
				for (int i = 0; i < props.length; i++) {
					properties.put(props[i].getName(), props[i].getValue());
				}
			}
		}
	}


	public Log getLog() {
		return log;
	}


	public String getPropertyValue(String name) throws InvalidPropertyException {
		String prop = this.properties.get(name);
		if (prop == null) {
			throw new InvalidPropertyException("The property " + name + " for the event handler " + getName()
				+ " does not exist.");
		}
		return prop;
	}


	public String getName() {
		return name;
	}


	public SubjectResolver getSubjectResolver() {
		return subjectResolver;
	}


	protected void setSubjectResolver(SubjectResolver subjectResolver) {
		this.subjectResolver = subjectResolver;
	}


	public abstract void handleEvent(Event event) throws EventHandlingException;

}
