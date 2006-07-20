package gov.nih.nci.cagrid.dorian.service.idp;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.common.SimpleResourceManager;
import gov.nih.nci.cagrid.dorian.test.Constants;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;

import java.io.InputStream;

import junit.framework.TestCase;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestIdPConfiguration extends TestCase {

	public final static int DEFAULT_MIN_PASSWORD_LENGTH = 6;

	public final static int DEFAULT_MAX_PASSWORD_LENGTH = 10;

	public final static int DEFAULT_MIN_UID_LENGTH = 4;

	public final static int DEFAULT_MAX_UID_LENGTH = 10;

	public final static String DEFAULT_KEY_PASSWORD = "idpkey";

	private final static String DEFAULT_REGISTRATION_POLICY = ManualRegistrationPolicy.class.getName();


	public void testAutoConfiguration() {
		try {
			InputStream resource = TestCase.class.getResourceAsStream(Constants.IDP_CONFIG);
			SimpleResourceManager trm = new SimpleResourceManager(resource);
			IdPConfiguration conf = (IdPConfiguration) trm.getResource(IdPConfiguration.RESOURCE);

			assertEquals(DEFAULT_MIN_PASSWORD_LENGTH, conf.getMinimumPasswordLength());
			assertEquals(DEFAULT_MAX_PASSWORD_LENGTH, conf.getMaximumPasswordLength());
			assertEquals(DEFAULT_MIN_UID_LENGTH, conf.getMinimumUIDLength());
			assertEquals(DEFAULT_MAX_UID_LENGTH, conf.getMaximumUIDLength());
			assertEquals(DEFAULT_REGISTRATION_POLICY, conf.getRegistrationPolicy().getClass().getName());
			assertEquals(DEFAULT_KEY_PASSWORD, conf.getKeyPassword());

			assertEquals(true, conf.isAutoCreateAssertingCredentials());
			assertEquals(true, conf.isAutoRenewAssertingCredentials());
			assertEquals(null, conf.getAssertingCertificate());
			assertEquals(null, conf.getAssertingKey());

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}


	public void testConfiguration() {
		try {
			InputStream resource = TestCase.class.getResourceAsStream(Constants.IDP_CONFIG2);
			SimpleResourceManager trm = new SimpleResourceManager(resource);
			IdPConfiguration conf = (IdPConfiguration) trm.getResource(IdPConfiguration.RESOURCE);

			assertEquals(DEFAULT_MIN_PASSWORD_LENGTH, conf.getMinimumPasswordLength());
			assertEquals(DEFAULT_MAX_PASSWORD_LENGTH, conf.getMaximumPasswordLength());
			assertEquals(DEFAULT_MIN_UID_LENGTH, conf.getMinimumUIDLength());
			assertEquals(DEFAULT_MAX_UID_LENGTH, conf.getMaximumUIDLength());
			assertEquals(DEFAULT_REGISTRATION_POLICY, conf.getRegistrationPolicy().getClass().getName());
			assertEquals(null, conf.getKeyPassword());

			assertEquals(false, conf.isAutoCreateAssertingCredentials());
			assertEquals(false, conf.isAutoRenewAssertingCredentials());
			resource = TestCase.class.getResourceAsStream(Constants.DORIAN_CERT);
			assertEquals(CertUtil.loadCertificate(resource), conf.getAssertingCertificate());
			resource = TestCase.class.getResourceAsStream(Constants.DORIAN_KEY);
			assertEquals(KeyUtil.loadPrivateKey(resource, conf.getKeyPassword()), conf.getAssertingKey());

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

}
