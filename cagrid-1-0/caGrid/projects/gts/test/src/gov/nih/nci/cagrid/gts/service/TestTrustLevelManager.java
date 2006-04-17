package gov.nih.nci.cagrid.gts.service;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.gts.bean.TrustLevel;
import gov.nih.nci.cagrid.gts.common.Database;
import gov.nih.nci.cagrid.gts.stubs.GTSInternalFault;
import gov.nih.nci.cagrid.gts.stubs.IllegalTrustLevelFault;
import gov.nih.nci.cagrid.gts.stubs.InvalidTrustLevelFault;
import gov.nih.nci.cagrid.gts.test.Utils;
import junit.framework.TestCase;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestTrustLevelManager extends TestCase implements TrustLevelStatus {

	private Database db;

	private TrustLevel ref;


	public TestTrustLevelManager() {
		ref = new TrustLevel();
		ref.setName("REF");
	}


	public void testCreateAndDestroy() {
		TrustLevelManager trust = new TrustLevelManager(this, db);
		try {
			trust.buildDatabase();
			trust.destroy();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				trust.destroy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testAddRemoveTrustLevel() {
		TrustLevelManager trust = new TrustLevelManager(this, db);
		try {
			TrustLevel level = new TrustLevel();
			level.setName("One");
			level.setDescription("Trust Level One");
			trust.addTrustLevel(level);
			assertEquals(1, trust.getTrustLevels().length);
			assertEquals(true, trust.doesTrustLevelExist(level.getName()));
			assertEquals(level, trust.getTrustLevel(level.getName()));
			trust.removeTrustLevel(level.getName());
			assertEquals(0, trust.getTrustLevels().length);
			assertEquals(false, trust.doesTrustLevelExist(level.getName()));
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		} finally {
			try {
				trust.destroy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testAddIllegalTrustLevel() {
		TrustLevelManager trust = new TrustLevelManager(this, db);
		try {
			TrustLevel level = new TrustLevel();
			level.setName("One");
			level.setDescription("Trust Level One");
			trust.addTrustLevel(level);
			assertEquals(1, trust.getTrustLevels().length);
			assertEquals(true, trust.doesTrustLevelExist(level.getName()));
			assertEquals(level, trust.getTrustLevel(level.getName()));
			try {
				trust.addTrustLevel(level);
				fail("Trust Level should not be able to be added when it already exists!!!");
			} catch (IllegalTrustLevelFault f) {

			}
			assertEquals(1, trust.getTrustLevels().length);
			assertEquals(true, trust.doesTrustLevelExist(level.getName()));
			assertEquals(level, trust.getTrustLevel(level.getName()));

			try {
				trust.addTrustLevel(new TrustLevel());
				fail("Trust Level should not be able to be added without an name!!!");
			} catch (IllegalTrustLevelFault f) {

			}
			assertEquals(1, trust.getTrustLevels().length);
			assertEquals(true, trust.doesTrustLevelExist(level.getName()));
			assertEquals(level, trust.getTrustLevel(level.getName()));

			trust.removeTrustLevel(level.getName());
			assertEquals(0, trust.getTrustLevels().length);
			assertEquals(false, trust.doesTrustLevelExist(level.getName()));

		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		} finally {
			try {
				trust.destroy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public void testRemoveIllegalTrustLevel() {
		TrustLevelManager trust = new TrustLevelManager(this, db);
		try {
			TrustLevel level = new TrustLevel();
			level.setName("One");
			level.setDescription("Trust Level One");

			try {
				trust.removeTrustLevel(level.getName());
				fail("Trust Level should not be able to be remove when it does not exist!!!");
			} catch (InvalidTrustLevelFault f) {

			}

			assertEquals(0, trust.getTrustLevels().length);
			assertEquals(false, trust.doesTrustLevelExist(level.getName()));

			trust.addTrustLevel(level);
			assertEquals(1, trust.getTrustLevels().length);
			assertEquals(true, trust.doesTrustLevelExist(level.getName()));
			assertEquals(level, trust.getTrustLevel(level.getName()));

			trust.addTrustLevel(ref);

			assertEquals(2, trust.getTrustLevels().length);
			assertEquals(true, trust.doesTrustLevelExist(ref.getName()));
			assertEquals(ref, trust.getTrustLevel(ref.getName()));

			try {
				trust.removeTrustLevel(ref.getName());
				fail("Trust Level should not be able to be removed if it is referenced!!!");
			} catch (IllegalTrustLevelFault f) {

			}

			trust.removeTrustLevel(level.getName());
			assertEquals(1, trust.getTrustLevels().length);
			assertEquals(false, trust.doesTrustLevelExist(level.getName()));
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		} finally {
			try {
				trust.destroy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}


	public void testAddGetUpdateRemoveTrustLevels() {
		TrustLevelManager trust = new TrustLevelManager(this, db);
		try {
			int size = 5;
			TrustLevel[] level = new TrustLevel[size];
			for (int i = 0; i < size; i++) {
				level[i] = new TrustLevel();
				level[i].setName("Level " + i);
				level[i].setDescription("Trust Level " + i);
				trust.addTrustLevel(level[i]);
				assertEquals((i + 1), trust.getTrustLevels().length);
				assertEquals(true, trust.doesTrustLevelExist(level[i].getName()));
				assertEquals(level[i], trust.getTrustLevel(level[i].getName()));
				level[i].setDescription("Updated Trust Level " + i);
				trust.updateTrustLevel(level[i]);
				assertEquals((i + 1), trust.getTrustLevels().length);
				assertEquals(true, trust.doesTrustLevelExist(level[i].getName()));
				assertEquals(level[i], trust.getTrustLevel(level[i].getName()));
			}
			int count = size;
			for (int i = 0; i < size; i++) {
				trust.removeTrustLevel(level[i].getName());
				count = count - 1;
				assertEquals(count, trust.getTrustLevels().length);
				assertEquals(false, trust.doesTrustLevelExist(level[i].getName()));
			}
			assertEquals(0, trust.getTrustLevels().length);

		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		} finally {
			try {
				trust.destroy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	public boolean isTrustLevelUsed(String name) throws GTSInternalFault {
		if (ref.getName().equals(name)) {
			return true;
		} else {
			return false;
		}
	}


	protected void setUp() throws Exception {
		super.setUp();
		try {
			db = Utils.getDB();
			assertEquals(0, db.getUsedConnectionCount());
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	protected void tearDown() throws Exception {
		super.setUp();
		try {
			assertEquals(0, db.getUsedConnectionCount());
			db.destroyDatabase();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

}
