package gov.nih.nci.cagrid.dorian.events;

import java.util.HashSet;
import java.util.Set;


public class SimpleEventHandler extends EventHandler {

	private Set<String> eventsRecord;


	public SimpleEventHandler(String name, EventHandlerConfiguration conf) {
		super(name, conf);
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
