package gov.nih.nci.cagrid.gums.idp;

import gov.nih.nci.cagrid.gums.ca.CertificateAuthority;
import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.FaultUtil;
import gov.nih.nci.cagrid.gums.test.TestUtils;
import junit.framework.TestCase;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestIdPProperties extends TestCase {

	private Database db;
	
	private CertificateAuthority ca;

	public void testDefaultProperties() {
		try {
		
			IdPProperties props = new IdPProperties(ca,db);
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
			db = TestUtils.getDB();
			ca = TestUtils.getCA();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

}
