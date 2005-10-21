package gov.nih.nci.cagrid.gums.idp;

import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.FaultUtil;

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
public class TestIdPProperties extends TestCase {
	private static final String DB = "TEST_GUMS";

	public static String DB_CONFIG = "resources" + File.separator
			+ "general-test" + File.separator + "db-config.xml";

	private Database db;

	public void testDefaultProperties() {
		try {
			IdPProperties props = new IdPProperties(db);
			assertEquals(IdPProperties.DEFAULT_MIN_PASSWORD_LENGTH, props
					.getMinimumPasswordLength());
			assertEquals(IdPProperties.DEFAULT_MAX_PASSWORD_LENGTH, props
					.getMaximumPasswordLength());
			assertEquals(IdPProperties.DEFAULT_MIN_UID_LENGTH, props
					.getMinimumUIDLength());
			assertEquals(IdPProperties.DEFAULT_MAX_UID_LENGTH, props
					.getMaximumUIDLength());
			assertEquals(ManualRegistrationPolicy.class.getName(), props.getRegistrationPolicy().getClass().getName());
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
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

}
