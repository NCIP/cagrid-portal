package gov.nih.nci.cagrid.dorian.service.ifs;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.dorian.common.Database;
import gov.nih.nci.cagrid.dorian.ifs.bean.UserAttributeDescriptor;
import gov.nih.nci.cagrid.dorian.test.Utils;
import junit.framework.TestCase;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestUserAttributeManager extends TestCase {

	private static final String TABLE_PREFIX = "TEST_ATTRIBUTE_";

	private Database db;


	public void testCreateAndDestroy() {
		try {
			UserAttributeDescriptor des = new UserAttributeDescriptor();
			des.setName("Test");
			UserAttributeManager manager = this.createAttributeManager(1, des);
			manager.hasAttribute("");
			assertTrue(db.tableExists(manager.getDBTableName()));
			manager.delete();
			assertFalse(db.tableExists(manager.getDBTableName()));
			manager.hasAttribute("");
			assertTrue(db.tableExists(manager.getDBTableName()));
			manager.delete();
			assertFalse(db.tableExists(manager.getDBTableName()));
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	public void testAddUpdateAndDeleteAttributes() {
		try {
			String userId = "jdoe";
			UserAttributeDescriptor des = new UserAttributeDescriptor();
			des.setName("Email");
			des.setRegularExpressionValueRestriction("[A-Za-z0-9\\.]+@[A-Za-z0-9\\.]+");
			UserAttributeManager manager = this.createAttributeManager(1, des);
			assertFalse(manager.hasAttribute(userId));
			String value = "jdoe@somewhere.com";
			manager.addUpdateAttribute(userId, value);
			assertTrue(manager.hasAttribute(userId));
			assertEquals(value, manager.getAttribute(userId));
			value = "jdoe2@somewhere.com";
			manager.addUpdateAttribute(userId, value);
			assertTrue(manager.hasAttribute(userId));
			assertEquals(value, manager.getAttribute(userId));
			manager.removeAttribute(userId);
			assertFalse(manager.hasAttribute(userId));
			assertNull(manager.getAttribute(userId));
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	public void testAddUpdateAndDeleteMultipleAttributes() {
		try {
			int size = 3;
			UserAttributeDescriptor[] des = new UserAttributeDescriptor[size];
			UserAttributeManager[] manager = new UserAttributeManager[size];
			for (int i = 0; i < size; i++) {
				des[i] = new UserAttributeDescriptor();
				des[i].setName("Email" + i);
				des[i].setRegularExpressionValueRestriction("[A-Za-z0-9\\.]+@[A-Za-z0-9\\.]+");
				manager[i] = this.createAttributeManager(i, des[i]);
				assertFalse(db.tableExists(manager[i].getDBTableName()));
				manager[i].hasAttribute("");
				assertTrue(db.tableExists(manager[i].getDBTableName()));
				for (int j = 0; j < size; j++) {
					String userId = "jdoe" + j;
					String value = "jdoe" + j + "@somewhere.com";
					assertFalse(manager[i].hasAttribute(userId));
					manager[i].addUpdateAttribute(userId, value);
					assertTrue(manager[i].hasAttribute(userId));
					assertEquals(value, manager[i].getAttribute(userId));
					value = "jdoe" + j + "@somewhere2.com";
					manager[i].addUpdateAttribute(userId, value);
					assertTrue(manager[i].hasAttribute(userId));
					assertEquals(value, manager[i].getAttribute(userId));
				}
				for (int j = 0; j < size; j++) {
					String userId = "jdoe" + j;
					manager[i].removeAttribute(userId);
					assertFalse(manager[i].hasAttribute(userId));
					assertNull(manager[i].getAttribute(userId));
				}

			}
			for (int i = 0; i < size; i++) {
				manager[i].delete();
				assertFalse(db.tableExists(manager[i].getDBTableName()));
			}

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	private UserAttributeManager createAttributeManager(long id, UserAttributeDescriptor des) {
		UserAttributeManager manager = new UserAttributeManager(db, id, des);
		try {
			manager.delete();
			assertFalse(db.tableExists(manager.getDBTableName()));
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
		return manager;
	}


	protected void setUp() throws Exception {
		super.setUp();
		try {
			db = Utils.getDB();
			assertEquals(0, db.getUsedConnectionCount());
			UserAttributeManager.ATTRIBUTE_TABLE_PREFIX = TABLE_PREFIX;
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	protected void tearDown() throws Exception {
		super.setUp();
		try {
			assertEquals(0, db.getUsedConnectionCount());
			assertEquals(0, db.getRootUsedConnectionCount());
			db.destroyDatabase();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail("Exception occured:" + e.getMessage());
		}
	}

}
