package gov.nih.nci.cagrid.gums.common;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.bean.Metadata;
import gov.nih.nci.cagrid.gums.ca.CertUtil;
import gov.nih.nci.cagrid.gums.ca.CertificateAuthorityFault;
import gov.nih.nci.cagrid.gums.ca.KeyUtil;
import gov.nih.nci.cagrid.gums.ca.NoCACredentialsFault;
import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.FaultUtil;
import gov.nih.nci.cagrid.gums.service.gumsca.GUMSCertificateAuthority;
import gov.nih.nci.cagrid.gums.service.gumsca.GUMSCertificateAuthorityConf;

import java.io.File;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.jdom.Document;
import org.projectmobius.common.XMLUtilities;
import org.projectmobius.db.ConnectionManager;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: TestRequiredAttributesManager.java,v 1.1 2005/09/27 18:31:18
 *          langella Exp $
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
