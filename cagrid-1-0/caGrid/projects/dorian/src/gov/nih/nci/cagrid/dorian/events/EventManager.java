package gov.nih.nci.cagrid.dorian.events;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class EventManager {

	private Map<String, EventHandler> handlers;
	private Map<String, Set<String>> events;
	private SubjectResolver resolver;


	public EventManager(SubjectResolver resolver) {
		this.resolver = resolver;
		handlers = new HashMap<String, EventHandler>();
		events = new HashMap<String, Set<String>>();
	}


	public void logEvent(String targetId, String reportingPartyId, String eventType, String message) {
		// TODO: Thread this out.
		Event e = new Event();
		e.setTargetId(targetId);
		e.setReportingPartyId(reportingPartyId);
		e.setEventType(eventType);
		e.setMessage(message);
		e.setOccurredAt(new Date());
		Set<EventHandler> s = getHandlers(eventType);
		Iterator<EventHandler> itr = s.iterator();
		while (itr.hasNext()) {
			itr.next().handleEvent(e);
		}
	}


	public void registerEventWithHandler(String eventName, String handlerName) throws InvalidHandlerException {
		if (!handlers.containsKey(handlerName)) {
			throw new InvalidHandlerException("Cannot register the event " + eventName + " with the handler "
				+ handlerName + ", no such handler is registerd.");
		}
		Set set = events.get(eventName);
		if (set == null) {
			set = new HashSet<String>();
			events.put(eventName, set);
		}
		if (!set.contains(handlerName)) {
			set.add(handlerName);
		}
	}


	protected Set<EventHandler> getHandlers(String event) {
		Set<EventHandler> set = new HashSet<EventHandler>();
		Set<String> s = events.get(event);
		if (s != null) {
			Iterator<String> itr = s.iterator();
			while (itr.hasNext()) {
				set.add(handlers.get(itr.next()));
			}

		}
		return set;
	}


	public void registerHandler(String name, EventHandler handler) throws InvalidHandlerException {
		if (!handlers.containsKey(name)) {
			handler.setSubjectResolver(resolver);
			handlers.put(name, handler);
		} else {
			throw new InvalidHandlerException("The handler " + name + " is already registered.");
		}
	}


	public void unregisterHandler(String name) {
		handlers.remove(name);
		Iterator<Set<String>> itr = events.values().iterator();
		while (itr.hasNext()) {
			itr.next().remove(name);
		}
	}


	public void unregisterEventWithHandler(String eventName, String handlerName) {
		Set set = events.get(eventName);
		if (set != null) {
			set = new HashSet<String>();
			set.remove(handlerName);
		}
	}

}
