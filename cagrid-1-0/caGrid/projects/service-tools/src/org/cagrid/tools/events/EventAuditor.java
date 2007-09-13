package org.cagrid.tools.events;

import gov.nih.nci.cagrid.common.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import org.cagrid.tools.database.Database;
import org.cagrid.tools.database.DatabaseConfiguration;
import org.cagrid.tools.database.DatabaseException;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A href="mailto:ervin@bmi.osu.edu">David Ervin</A>
 */
public class EventAuditor extends EventHandler {

	private Database db;

	private boolean dbBuilt = false;

	private final static String EVENT_ID = "EVENT_ID";
	private final static String TARGET_ID = "TARGET_ID";
	private final static String REPORTING_PARTY_ID = "REPORTING_PARTY_ID";
	private final static String EVENT_TYPE = "EVENT_TYPE";
	private final static String OCCURRED_AT = "OCCURRED_AT";
	private final static String MESSAGE = "MESSAGE";

	private final static String DB_NAME_PROPERTY = "db-name";
	private final static String DB_TABLE_PROPERTY = "db-table";
	private final static String DB_HOST_PROPERTY = "db-host";
	private final static String DB_PORT_PROPERTY = "db-port";
	private final static String DB_USERNAME_PROPERTY = "db-username";
	private final static String DB_PASSWORD_PROPERTY = "db-password";

	private String table;


	public EventAuditor(String name, EventHandlerConfiguration conf) throws EventHandlerInitializationException {
		super(name, conf);
		try {
			DatabaseConfiguration dc = new DatabaseConfiguration();
			dc.setHost(this.getPropertyValue(DB_HOST_PROPERTY));
			dc.setPort(Integer.valueOf(this.getPropertyValue(DB_PORT_PROPERTY)).intValue());
			dc.setUsername(this.getPropertyValue(DB_USERNAME_PROPERTY));
			dc.setPassword(this.getPropertyValue(DB_PASSWORD_PROPERTY));
			this.db = new Database(dc, this.getPropertyValue(DB_NAME_PROPERTY));
			this.table = this.getPropertyValue(DB_TABLE_PROPERTY);
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new EventHandlerInitializationException("Error initializing the event handler, " + name + ": "
				+ e.getMessage(), e);
		}
	}


	public EventAuditor(String name, Database db, String table) {
		super(name, null);
		this.db = db;
		this.table = table;
	}


	public void handleEvent(Event event) throws EventHandlingException {
		try {
			insertEvent(event);
		} catch (DatabaseException e) {
			getLog().error(e.getMessage(), e);
			throw new EventHandlingException("An unexpected database error occurred.", e);
		}
	}


	public boolean eventExists(long eventId) throws DatabaseException {
		return db.exists(table, EVENT_ID, eventId);
	}


	public Event getEvent(long eventId) throws DatabaseException {
		Event event = null;
		buildDatabase();
		Connection c = null;

		try {
			c = db.getConnection();
			PreparedStatement s = c.prepareStatement("select * from " + table + " WHERE " + EVENT_ID + "= ?");
			s.setLong(1, eventId);
			ResultSet rs = s.executeQuery();
			if (rs.next()) {
				event = new Event();
				event.setEventId(rs.getLong(EVENT_ID));
				event.setTargetId(rs.getString(TARGET_ID));
				event.setReportingPartyId(rs.getString(REPORTING_PARTY_ID));
				event.setEventType(rs.getString(EVENT_TYPE));
				event.setOccurredAt(new Date(rs.getLong(OCCURRED_AT)));
				event.setMessage(rs.getString(MESSAGE));
			}
			rs.close();
			s.close();
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new DatabaseException("An unexpected database error occurred.", e);
		} finally {
			db.releaseConnection(c);
		}

		return event;
	}


	public void deleteEvent(long eventId) throws DatabaseException {
		buildDatabase();
		try {
			db.update("delete from " + this.table + " where " + EVENT_ID + "=" + eventId);
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new DatabaseException("An unexpected database error occurred.", e);
		}
	}


	private Event insertEvent(Event event) throws DatabaseException, EventHandlingException {
		buildDatabase();
		Connection c = null;
		try {

			if (Utils.clean(event.getTargetId()) == null) {
				throw new EventHandlingException("Could not audit event, no target id was specified.");
			}

			if (Utils.clean(event.getReportingPartyId()) == null) {
				throw new EventHandlingException("Could not audit event, no reporting party was specified.");
			}

			if (Utils.clean(event.getEventType()) == null) {
				throw new EventHandlingException("Could not audit event, no event type was specified.");
			}

			if (Utils.clean(event.getMessage()) == null) {
				throw new EventHandlingException("Could not audit event, no event message was specified.");
			}

			if (event.getOccurredAt() == null) {
				throw new EventHandlingException("Could not audit event, no occurred at date was specified.");
			}

			c = db.getConnection();
			PreparedStatement s = c.prepareStatement("INSERT INTO " + this.table + " SET " + TARGET_ID + "= ?, "
				+ REPORTING_PARTY_ID + "= ?, " + EVENT_TYPE + "= ?, " + OCCURRED_AT + "= ?, " + MESSAGE + "= ?");

			s.setString(1, event.getTargetId());
			s.setString(2, event.getReportingPartyId());
			s.setString(3, event.getEventType());
			s.setLong(4, event.getOccurredAt().getTime());
			s.setString(5, event.getMessage());
			s.execute();
			event.setEventId(db.getLastAutoId(c));
			s.close();
		} catch (Exception e) {
			getLog().error(e.getMessage(), e);
			throw new DatabaseException("An unexpected database error occurred.", e);
		} finally {
			if (c != null) {
				db.releaseConnection(c);
			}
		}
		return event;
	}


	public void clearDatabase() throws DatabaseException {
		buildDatabase();
		db.update("DROP TABLE IF EXISTS " + this.table);
		dbBuilt = false;
	}


	private void buildDatabase() throws DatabaseException {
		if (!dbBuilt) {
			if (!this.db.tableExists(getName())) {
				String trust = "CREATE TABLE " + this.table + " (" + EVENT_ID
					+ " INT NOT NULL AUTO_INCREMENT PRIMARY KEY," + TARGET_ID + " VARCHAR(255) NOT NULL,"
					+ REPORTING_PARTY_ID + " VARCHAR(255) NOT NULL," + EVENT_TYPE + " VARCHAR(50) NOT NULL,"
					+ OCCURRED_AT + " BIGINT NOT NULL," + MESSAGE + " TEXT NOT NULL,"
					+ "INDEX document_index (EVENT_ID));";
				db.update(trust);
			}
			dbBuilt = true;
		}
	}
}
