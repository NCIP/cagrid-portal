package gov.nih.nci.cagrid.dorian.events;

import gov.nih.nci.cagrid.common.Utils;

import java.lang.reflect.Constructor;
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


	public void registerEventHandlingPolicy(EventHandlingPolicy policy) throws InvalidHandlerException {
		if (policy != null) {
			EventHandlers eh = policy.getEventHandlers();
			if (eh != null) {
				EventHandlerDescription[] handlers = eh.getEventHandlerDescription();
				if (handlers != null) {
					Class[] types = new Class[2];
					types[0] = String.class;
					types[1] = EventHandlerConfiguration.class;
					EventHandler handler = null;
					for (int i = 0; i < handlers.length; i++) {
						try {
							Constructor c = Class.forName(handlers[i].getClassName()).getConstructor(types);
							handler = (EventHandler) c.newInstance(new Object[]{handlers[i].getName(),
									handlers[i].getEventHandlerConfiguration()});

						} catch (Exception e) {
							throw new InvalidHandlerException("Could not create an instance of the "
								+ handlers[i].getName() + " event handler.", e);
						}
						registerHandler(handler);
					}

				}
			}
			EventMappings em = policy.getEventMappings();
			if (em != null) {
				EventToHandlersMapping[] mappings = em.getEventToHandlersMapping();
				if (mappings != null) {
					for (int i = 0; i < mappings.length; i++) {
						String[] handlers = mappings[i].getHandlerName();
						if (handlers != null) {
							for (int j = 0; j < handlers.length; j++) {
								this.registerEventWithHandler(mappings[i].getEventName(), handlers[j]);
							}
						}
					}
				}
			}
		}
	}


	public void registerEventWithHandler(String eventName, String handlerName) throws InvalidHandlerException {
		if (!handlers.containsKey(handlerName)) {
			throw new InvalidHandlerException("Cannot register the event " + eventName + " with the handler "
				+ handlerName + ", no such handler is registered.");
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


	protected EventHandler getHandler(String name) throws InvalidHandlerException {

		if (!handlers.containsKey(name)) {
			throw new InvalidHandlerException("Cannot get the handler " + name + ", no such handler is registered.");
		} else {
			return this.handlers.get(name);
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


	public void registerHandler(EventHandler handler) throws InvalidHandlerException {
		if (Utils.clean(handler.getName()) == null) {
			throw new InvalidHandlerException("Invalid name provided for handler.");
		}

		if (!handlers.containsKey(handler.getName())) {
			handler.setSubjectResolver(resolver);
			handlers.put(handler.getName(), handler);
		} else {
			throw new InvalidHandlerException("The handler " + handler.getName() + " is already registered.");
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
