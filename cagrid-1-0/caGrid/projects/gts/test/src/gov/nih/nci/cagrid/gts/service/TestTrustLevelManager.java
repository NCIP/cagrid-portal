package gov.nih.nci.cagrid.gts.service;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.gts.bean.TrustLevel;
import gov.nih.nci.cagrid.gts.common.Database;
import gov.nih.nci.cagrid.gts.stubs.GTSInternalFault;
import gov.nih.nci.cagrid.gts.stubs.IllegalTrustLevelFault;
import gov.nih.nci.cagrid.gts.test.Utils;
import junit.framework.TestCase;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestTrustLevelManager extends TestCase implements TrustedAuthorityLevelRemover {

	private Database db;

	private TrustLevel ref;

	private final static String GTS_URI = "localhost";


	public TestTrustLevelManager() {
		ref = new TrustLevel();
		ref.setName("REF");
	}


	public void testCreateAndDestroy() {
		TrustLevelManager trust = new TrustLevelManager(GTS_URI, this, db);
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
		TrustLevelManager trust = new TrustLevelManager(GTS_URI, this, db);
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


	public void testAddUpdateRemoveExternalTrustLevel() {
		TrustLevelManager trust = new TrustLevelManager(GTS_URI, this, db);
		try {
			TrustLevel level = new TrustLevel();
			level.setName("One");
			level.setDescription("Trust Level One");
			level.setIsAuthority(Boolean.FALSE);
			level.setAuthorityTrustService("somehost");
			level.setSourceTrustService("somehost");
			trust.addTrustLevel(level, false);
			assertEquals(1, trust.getTrustLevels().length);
			assertEquals(true, trust.doesTrustLevelExist(level.getName()));
			assertEquals(level, trust.getTrustLevel(level.getName()));
			level.setAuthorityTrustService("someotherhost");
			level.setSourceTrustService("someotherhost");
			trust.updateTrustLevel(level, false);
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
		TrustLevelManager trust = new TrustLevelManager(GTS_URI, this, db);
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


	public void testAddIllegalExternalTrustLevel() {
		TrustLevelManager trust = new TrustLevelManager(GTS_URI, this, db);
		try {
			TrustLevel level = new TrustLevel();
			level.setName("One");
			level.setDescription("Trust Level One");
			level.setIsAuthority(Boolean.FALSE);
			level.setAuthorityTrustService("somehost");
			level.setSourceTrustService("somehost");
			trust.addTrustLevel(level, false);
			assertEquals(1, trust.getTrustLevels().length);
			assertEquals(true, trust.doesTrustLevelExist(level.getName()));
			assertEquals(level, trust.getTrustLevel(level.getName()));
			try {
				trust.addTrustLevel(level, false);
				fail("Trust Level should not be able to be added when it already exists!!!");
			} catch (IllegalTrustLevelFault f) {

			}
			assertEquals(1, trust.getTrustLevels().length);
			assertEquals(true, trust.doesTrustLevelExist(level.getName()));
			assertEquals(level, trust.getTrustLevel(level.getName()));

			trust.removeTrustLevel(level.getName());
			assertEquals(0, trust.getTrustLevels().length);
			assertEquals(false, trust.doesTrustLevelExist(level.getName()));

			try {
				trust.addTrustLevel(new TrustLevel(), false);
				fail("Trust Level should not be able to be added without an name!!!");
			} catch (IllegalTrustLevelFault f) {

			}
			assertEquals(0, trust.getTrustLevels().length);

			// Test Adding without authority

			try {
				TrustLevel tl = new TrustLevel();
				tl.setName("One");
				tl.setDescription("Trust Level One");
				tl.setAuthorityTrustService("somehost");
				tl.setSourceTrustService("somehost");
				trust.addTrustLevel(tl, false);
				fail("Trust Level should not be able to be added!!!");
			} catch (IllegalTrustLevelFault f) {

			}

			assertEquals(0, trust.getTrustLevels().length);

			// Test Adding without Authority Trust Service

			try {
				TrustLevel tl = new TrustLevel();
				tl.setName("One");
				tl.setDescription("Trust Level One");
				tl.setIsAuthority(Boolean.FALSE);
				tl.setSourceTrustService("somehost");
				trust.addTrustLevel(tl, false);
				fail("Trust Level should not be able to be added!!!");
			} catch (IllegalTrustLevelFault f) {

			}

			assertEquals(0, trust.getTrustLevels().length);

			// Test Adding without source trust service

			try {
				TrustLevel tl = new TrustLevel();
				tl.setName("One");
				tl.setDescription("Trust Level One");
				tl.setIsAuthority(Boolean.FALSE);
				tl.setAuthorityTrustService("somehost");
				trust.addTrustLevel(tl, false);
				fail("Trust Level should not be able to be added!!!");
			} catch (IllegalTrustLevelFault f) {

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


	public void testUpdateIllegalTrustLevel() {
		TrustLevelManager trust = new TrustLevelManager(GTS_URI, this, db);
		try {
			TrustLevel level = new TrustLevel();
			level.setName("One");
			level.setDescription("Trust Level One");
			level.setIsAuthority(Boolean.TRUE);
			level.setAuthorityTrustService("somehost");
			level.setSourceTrustService("somehost");
			trust.addTrustLevel(level, false);
			assertEquals(1, trust.getTrustLevels().length);
			assertEquals(true, trust.doesTrustLevelExist(level.getName()));
			assertEquals(level, trust.getTrustLevel(level.getName()));

			// Test changing the authority

			TrustLevel tl = trust.getTrustLevel(level.getName());
			try {
				tl.setIsAuthority(Boolean.FALSE);
				trust.updateTrustLevel(tl);
				fail("Trust Level should not be able to be updated!!!");
			} catch (IllegalTrustLevelFault f) {

			}

			assertEquals(1, trust.getTrustLevels().length);

			tl = trust.getTrustLevel(level.getName());
			try {
				tl.setAuthorityTrustService("localhost");
				trust.updateTrustLevel(tl);
				fail("Trust Level should not be able to be updated!!!");
			} catch (IllegalTrustLevelFault f) {

			}

			assertEquals(1, trust.getTrustLevels().length);

			tl = trust.getTrustLevel(level.getName());
			try {
				tl.setSourceTrustService("localhost");
				trust.updateTrustLevel(tl);
				fail("Trust Level should not be able to be updated!!!");
			} catch (IllegalTrustLevelFault f) {

			}

			assertEquals(1, trust.getTrustLevels().length);

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


	public void testUpdateIllegalExternalTrustLevel() {
		TrustLevelManager trust = new TrustLevelManager(GTS_URI, this, db);
		try {
			TrustLevel level = new TrustLevel();
			level.setName("One");
			level.setDescription("Trust Level One");
			level.setIsAuthority(Boolean.TRUE);
			level.setAuthorityTrustService("somehost");
			level.setSourceTrustService("somehost");
			trust.addTrustLevel(level, false);
			assertEquals(1, trust.getTrustLevels().length);
			assertEquals(true, trust.doesTrustLevelExist(level.getName()));
			assertEquals(level, trust.getTrustLevel(level.getName()));

			// Test changing the authority

			TrustLevel tl = trust.getTrustLevel(level.getName());
			try {
				tl.setIsAuthority(Boolean.FALSE);
				trust.updateTrustLevel(tl, false);
				fail("Trust Level should not be able to be updated!!!");
			} catch (IllegalTrustLevelFault f) {

			}

			assertEquals(1, trust.getTrustLevels().length);

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
		TrustLevelManager trust = new TrustLevelManager(GTS_URI, this, db);
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


	public void removeAssociatedTrustedAuthorities(String trustLevel) throws GTSInternalFault {
		// TODO Auto-generated method stub

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
