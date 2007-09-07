package gov.nih.nci.cagrid.dorian.events;

import gov.nih.nci.cagrid.common.FaultUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestEventManager extends TestCase implements SubjectResolver {

	public void testEventManager() {
		try {
			EventManager em = new EventManager(this);
			String e1 = "Event 1";
			String e2 = "Event 2";
			String e3 = "Event 3";
			String e4 = "Event 4";
			String h1Name = "Handler 1";
			TestEventHandler h1 = new TestEventHandler();
			String h2Name = "Handler 2";
			TestEventHandler h2 = new TestEventHandler();
			em.registerHandler(h1Name, h1);
			em.registerHandler(h2Name, h2);
			em.registerEventWithHandler(e1, h1Name);
			em.registerEventWithHandler(e3, h1Name);
			em.registerEventWithHandler(e2, h2Name);
			em.registerEventWithHandler(e3, h2Name);

			Set s1 = em.getHandlers(e1);
			assertEquals(1, s1.size());
			assertTrue(s1.contains(h1));

			Set s2 = em.getHandlers(e2);
			assertEquals(1, s2.size());
			assertTrue(s2.contains(h2));

			Set s3 = em.getHandlers(e3);
			assertEquals(2, s3.size());
			assertTrue(s3.contains(h1));
			assertTrue(s3.contains(h2));

			em.logEvent("", "", e1, "");
			Set e1h1 = h1.getEventsRecord();
			Set e1h2 = h2.getEventsRecord();
			assertEquals(1, e1h1.size());
			assertTrue(e1h1.contains(e1));
			assertEquals(0, e1h2.size());
			h1.reset();
			h2.reset();

			em.logEvent("", "", e2, "");
			Set e2h1 = h1.getEventsRecord();
			Set e2h2 = h2.getEventsRecord();
			assertEquals(0, e2h1.size());
			assertEquals(1, e2h2.size());
			assertTrue(e2h2.contains(e2));
			h1.reset();
			h2.reset();

			em.logEvent("", "", e3, "");
			Set e3h1 = h1.getEventsRecord();
			Set e3h2 = h2.getEventsRecord();
			assertEquals(1, e3h1.size());
			assertTrue(e3h1.contains(e3));
			assertEquals(1, e3h2.size());
			assertTrue(e3h2.contains(e3));
			h1.reset();
			h2.reset();

			em.logEvent("", "", e4, "");
			Set e4h1 = h1.getEventsRecord();
			Set e4h2 = h2.getEventsRecord();
			assertEquals(0, e4h1.size());
			assertEquals(0, e4h2.size());

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {

		}
	}


	protected void setUp() throws Exception {
		super.setUp();

	}


	protected void tearDown() throws Exception {
		super.setUp();

	}


	public String lookupAttribute(String targetId, String att) {
		// TODO Auto-generated method stub
		return null;
	}


	public List<String> resolveSubjects(String targetGroup) {
		// TODO Auto-generated method stub
		return null;
	}


	public class TestEventHandler extends EventHandler {

		private Set<String> eventsRecord;


		public TestEventHandler() {
			super(null);
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

}
