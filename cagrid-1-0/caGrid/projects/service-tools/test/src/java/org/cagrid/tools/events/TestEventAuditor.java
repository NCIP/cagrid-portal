package org.cagrid.tools.events;

import gov.nih.nci.cagrid.common.FaultUtil;

import java.util.Date;

import org.cagrid.tools.Utils;

import junit.framework.TestCase;


public class TestEventAuditor extends TestCase {
	private final static String TABLE = "EVENT_AUDITOR";


	public void testEventAuditor() {
		EventAuditor auditor = null;
		try {
			auditor = new EventAuditor("Auditor", org.cagrid.tools.Utils.getDB(), TABLE);
			validateEventAuditor(auditor);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				auditor.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	


	public void testEventAuditorFromConfig() {
		EventAuditor auditor = null;
		try {
			auditor = new EventAuditor("Auditor", Utils.getEventAuditorConfiguration());
			validateEventAuditor(auditor);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				auditor.clearDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void testEventAuditorBadConfiguration() {
		try {
			EventHandlerConfiguration conf = Utils.getEventAuditorConfiguration();
			assertTrue(disableProperty(conf, EventAuditor.DB_HOST_PROPERTY));
			new EventAuditor("Auditor", conf);
			fail("Should not be able to construct event auditor with bad configuration,");
		} catch (Exception e) {
		}
		
		try {
			EventHandlerConfiguration conf = Utils.getEventAuditorConfiguration();
			assertTrue(disableProperty(conf, EventAuditor.DB_NAME_PROPERTY));
			new EventAuditor("Auditor", conf);
			fail("Should not be able to construct event auditor with bad configuration,");
		} catch (Exception e) {
		}
		
		try {
			EventHandlerConfiguration conf = Utils.getEventAuditorConfiguration();
			assertTrue(disableProperty(conf, EventAuditor.DB_PASSWORD_PROPERTY));
			new EventAuditor("Auditor", conf);
			fail("Should not be able to construct event auditor with bad configuration,");
		} catch (Exception e) {
		}
		
		try {
			EventHandlerConfiguration conf = Utils.getEventAuditorConfiguration();
			assertTrue(disableProperty(conf, EventAuditor.DB_PORT_PROPERTY));
			new EventAuditor("Auditor", conf);
			fail("Should not be able to construct event auditor with bad configuration,");
		} catch (Exception e) {
		}
		
		try {
			EventHandlerConfiguration conf = Utils.getEventAuditorConfiguration();
			assertTrue(disableProperty(conf, EventAuditor.DB_TABLE_PROPERTY));
			new EventAuditor("Auditor", conf);
			fail("Should not be able to construct event auditor with bad configuration,");
		} catch (Exception e) {
		}
		
		try {
			EventHandlerConfiguration conf = Utils.getEventAuditorConfiguration();
			assertTrue(disableProperty(conf, EventAuditor.DB_USERNAME_PROPERTY));
			new EventAuditor("Auditor", conf);
			fail("Should not be able to construct event auditor with bad configuration,");
		} catch (Exception e) {
		}
	}


	private boolean disableProperty(EventHandlerConfiguration conf, String property) throws Exception {
		boolean disabled = false;
		Property[] prop = conf.getProperty();
		if (prop != null) {
			for (int i = 0; i < prop.length; i++) {
				if (prop[i].getName().equals(property)) {
					prop[i].setValue(null);
					disabled = true;
				}
			}
		}
		return disabled;
	}


	private void validateEventAuditor(EventAuditor auditor) throws Exception {
		int events = 5;
		String target = "Some User";
		String reportingParty = "Some Admin";
		String eventType = "Error";

		for (int i = 0; i < events; i++) {
			Event e = new Event();
			e.setTargetId(target + i);
			e.setReportingPartyId(reportingParty);
			e.setEventType(eventType);
			e.setOccurredAt(new Date().getTime());
			String message = "Testing for " + e.getTargetId();
			e.setMessage(message);
			auditor.handleEvent(e);
			long expectedId = i + 1;
			assertEquals(expectedId, e.getEventId());
			assertTrue(auditor.eventExists(e.getEventId()));
			Event e2 = auditor.getEvent(e.getEventId());
			assertEquals(e, e2);
		}

		for (int i = 0; i < events; i++) {
			long eventId = i + 1;
			assertTrue(auditor.eventExists(eventId));
			auditor.deleteEvent(eventId);
			assertFalse(auditor.eventExists(eventId));
		}
	}
}
