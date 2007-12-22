package org.cagrid.tools.events;

import java.util.HashSet;
import java.util.Set;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A href="mailto:ervin@bmi.osu.edu">David Ervin</A>
 */
public class SimpleEventHandler extends BaseEventHandler {

	private Set<String> eventsRecord;


	public SimpleEventHandler(String name) {
		super(name);
		eventsRecord = new HashSet<String>();
	}


	public void reset() {
		eventsRecord.clear();
	}


	public Set<String> getEventsRecord() {
		return eventsRecord;
	}


	public void handleEvent(Event event) {
		eventsRecord.add(event.getEventType());
	}

}
