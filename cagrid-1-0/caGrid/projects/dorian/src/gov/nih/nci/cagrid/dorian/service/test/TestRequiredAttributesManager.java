package gov.nih.nci.cagrid.gums.service.test;

import gov.nih.nci.cagrid.gums.bean.AttributeDescriptor;
import gov.nih.nci.cagrid.gums.common.FaultUtil;
import gov.nih.nci.cagrid.gums.service.Database;
import gov.nih.nci.cagrid.gums.service.RequiredAttributesManager;

import java.io.File;

import junit.framework.TestCase;

import org.globus.wsrf.utils.FaultHelper;
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
public class TestRequiredAttributesManager extends TestCase {
	private static final String DB = "TEST_GUMS";

	private static final String TABLE = "TEST_ATTRIBUTES";

	private static final int NUMBER_OF_ATTS = 10;

	public static String RESOURCES_DIR = "resources" + File.separator
			+ "general-test";

	private RequiredAttributesManager ram;

	private Database db;

	public void testInsertAttributes() {
		try {
			AttributeDescriptor[] atts = ram.getRequiredAttributes();
			assertEquals(0, atts.length);
			AttributeDescriptor[] newAtts = getAttributeArray(NUMBER_OF_ATTS);
			for (int i = 0; i < newAtts.length; i++) {
				ram.insertRequiredAttribute(newAtts[i]);
				atts = ram.getRequiredAttributes();
				compareAttributes(newAtts, (i + 1), atts);
			}

			ram.insertRequiredAttribute(newAtts[0]);
			ram.insertRequiredAttribute(newAtts[1]);
			compareAttributes(newAtts, newAtts.length, atts);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				db.destroyDatabase();
			} catch (Exception ex) {
				FaultUtil.printFault(ex);
			}
		}
	}

	public void testDeleteAttributes() {
		try {
			AttributeDescriptor[] atts = ram.getRequiredAttributes();
			assertEquals(0, atts.length);
			AttributeDescriptor[] newAtts = getAttributeArray(NUMBER_OF_ATTS);
			for (int i = 0; i < newAtts.length; i++) {
				ram.insertRequiredAttribute(newAtts[i]);
			}

			atts = ram.getRequiredAttributes();
			compareAttributes(newAtts, newAtts.length, atts);
			int count = newAtts.length;
			for (int i = newAtts.length - 1; i >= 0; i--) {
				ram.removeRequiredAttribute(newAtts[i]);
				count = count - 1;
				atts = ram.getRequiredAttributes();
				compareAttributes(newAtts, count, atts);
			}
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				db.destroyDatabase();
			} catch (Exception ex) {
				FaultUtil.printFault(ex);
			}
		}
	}

	public void testRemoveAllAttributes() {
		try {
			AttributeDescriptor[] atts = ram.getRequiredAttributes();
			assertEquals(0, atts.length);
			AttributeDescriptor[] newAtts = getAttributeArray(NUMBER_OF_ATTS);
			for (int i = 0; i < newAtts.length; i++) {
				ram.insertRequiredAttribute(newAtts[i]);
			}
			atts = ram.getRequiredAttributes();
			compareAttributes(newAtts, newAtts.length, atts);

			ram.removeAllAttributes();

			atts = ram.getRequiredAttributes();
			assertEquals(0, atts.length);

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} finally {
			try {
				db.destroyDatabase();
			} catch (Exception ex) {
				FaultUtil.printFault(ex);
			}
		}
	}

	private void compareAttributes(AttributeDescriptor[] gold, int goldSize,
			AttributeDescriptor[] result) {
		assertEquals(goldSize, result.length);
		for (int i = 0; i < goldSize; i++) {
			boolean found = false;
			for (int j = 0; j < result.length; j++) {
				if (gold[i].equals(result[j])) {
					found = true;
					break;
				}
			}
			assertTrue(found);
		}
	}

	private AttributeDescriptor[] getAttributeArray(int size) {
		AttributeDescriptor[] newAtts = new AttributeDescriptor[size];
		for (int i = 0; i < size; i++) {
			newAtts[i] = new AttributeDescriptor();
			newAtts[i].setNamespace("namespace" + i);
			newAtts[i].setName("name" + i);
		}
		return newAtts;
	}

	protected void setUp() throws Exception {
		super.setUp();
		try {
			Document doc = XMLUtilities.fileNameToDocument(RESOURCES_DIR
					+ File.separator + "db-config.xml");
			ConnectionManager cm = new ConnectionManager(doc.getRootElement());
			db = new Database(cm, DB);
			db.destroyDatabase();
			db.createDatabaseIfNeeded();
			ram = new RequiredAttributesManager(db, TABLE);
			ram.destroyTable();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

}
