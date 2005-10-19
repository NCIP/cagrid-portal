package gov.nih.nci.cagrid.gums.common;

import gov.nih.nci.cagrid.gums.bean.Metadata;
import gov.nih.nci.cagrid.gums.service.gumsca.GUMSCertificateAuthority;

import java.io.File;

import junit.framework.TestCase;

import org.jdom.Document;
import org.projectmobius.common.XMLUtilities;
import org.projectmobius.db.ConnectionManager;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestMetadataManager extends TestCase {
	private static final String DB = "TEST_GUMS";

	private static final String TABLE = "TEST_METADATA";

	public static String DB_CONFIG = "resources" + File.separator
			+ "general-test" + File.separator + "db-config.xml";

	private Database db;

	public void testDelete() {
		try {
			MetadataManager mm = new MetadataManager(db, TABLE);
			mm.destroy();
			int count = 20;

			// Test Insert;
			for (int i = 0; i < count; i++) {
				Metadata data = new Metadata("name" + i, "value" + i);
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
		} finally {
			try {
				db.destroyDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void testUpdate() {
		try {
			MetadataManager mm = new MetadataManager(db, TABLE);
			mm.destroy();
			int count = 20;

			// Test Insert;
			for (int i = 0; i < count; i++) {
				Metadata data = new Metadata("name" + i, "value" + i);
				mm.insert(data);
				Metadata out = mm.get(data.getName());
				assertNotNull(out);
				assertEquals(data, out);
			}
			// Test update
			for (int i = 0; i < count; i++) {
				Metadata data = new Metadata("name" + i, "changedvalue"
						+ i);
				mm.update(data);
				Metadata out = mm.get(data.getName());
				assertNotNull(out);
				assertEquals(data, out);
			}

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				db.destroyDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void testInsert() {
		try {
			MetadataManager mm = new MetadataManager(db, TABLE);
			mm.destroy();
			int count = 20;

			// Test Insert;
			for (int i = 0; i < count; i++) {
				Metadata data = new Metadata("name" + i, "value" + i);
				mm.insert(data);
				Metadata out = mm.get(data.getName());
				assertNotNull(out);
				assertEquals(data, out);
			}

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				db.destroyDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void setUp() throws Exception {
		super.setUp();
		try {
			Document doc = XMLUtilities.fileNameToDocument(DB_CONFIG);
			ConnectionManager cm = new ConnectionManager(doc.getRootElement());
			db = new Database(cm, DB);
			db.destroyDatabase();
			db.createDatabaseIfNeeded();
			GUMSCertificateAuthority.CA_TABLE = TABLE;
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

}
