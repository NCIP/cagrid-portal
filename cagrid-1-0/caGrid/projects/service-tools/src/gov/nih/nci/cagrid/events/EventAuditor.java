package gov.nih.nci.cagrid.events;

import gov.nih.nci.cagrid.database.Database;


public class EventAuditor extends EventHandler {

	private Database db;


	public EventAuditor(String name, EventHandlerConfiguration conf, Database db) {
		super(name, conf);
		this.db = db;
	}


	public void handleEvent(Event event) {
		// TODO Auto-generated method stub
	}
}
