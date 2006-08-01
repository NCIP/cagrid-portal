package gov.nih.nci.cagrid.dorian.common;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.dorian.bean.Metadata;
import gov.nih.nci.cagrid.dorian.ca.DorianCertificateAuthority;
import gov.nih.nci.cagrid.dorian.test.Utils;
import junit.framework.TestCase;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestMetadataManager extends TestCase {

	private static final String TABLE = "TEST_METADATA";

	private Database db;

	public void testDelete() {
		try {
			MetadataManager mm = new MetadataManager(db, TABLE);
			//mm.destroy();
			int count = 20;

			// Test Insert;
			for (int i = 0; i < count; i++) {
				Metadata data = new Metadata();
				data.setName("name" + i);
				data.setValue("value" + i);
				data.setDescription("description"+i);
				mm.insert(data);
				Metadata out = mm.get(data.getName());
				assertNotNull(out);
				assertEquals(data, out);
			}

			// Test delete
			for (int i = 0; i < count; i++) {
				String n = "name" + count;
				assertNull(mm.get(n));

			}

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} 
	}

	public void testUpdate() {
		try {
			MetadataManager mm = new MetadataManager(db, TABLE);
			//mm.destroy();
			int count = 20;

			// Test Insert;
			for (int i = 0; i < count; i++) {
				Metadata data = new Metadata();
				data.setName("name" + i);
				data.setValue("value" + i);
				data.setDescription("description"+i);
				mm.insert(data);
				Metadata out = mm.get(data.getName());
				assertNotNull(out);
				assertEquals(data, out);
			}
			// Test update
			for (int i = 0; i < count; i++) {
				Metadata data = new Metadata();
				data.setName("name" + i);
				data.setValue("changedvalue" + i);
				data.setDescription("description"+i);
				mm.update(data);
				Metadata out = mm.get(data.getName());
				assertNotNull(out);
				assertEquals(data, out);
			}

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

	public void testInsert() {
		try {
			MetadataManager mm = new MetadataManager(db, TABLE);
			//mm.destroy();
			int count = 20;

			// Test Insert;
			for (int i = 0; i < count; i++) {
				Metadata data = new Metadata();
				data.setName("name" + i);
				data.setValue("value" + i);
				data.setDescription("description"+i);
				mm.insert(data);
				Metadata out = mm.get(data.getName());
				assertNotNull(out);
				assertEquals(data, out);
			}

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} 
	}

	protected void setUp() throws Exception {
		super.setUp();
		try {
			db=Utils.getDB();
			assertEquals(0,db.getUsedConnectionCount());
			DorianCertificateAuthority.CA_TABLE = TABLE;
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}
	
	protected void tearDown() throws Exception {
		super.setUp();
		try {
			assertEquals(0,db.getUsedConnectionCount());
			db.destroyDatabase();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

}
