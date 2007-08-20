package gov.nih.nci.cagrid.dorian.service.idp;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.dorian.conf.IdentityProviderConfiguration;
import gov.nih.nci.cagrid.dorian.test.Constants;

import java.io.InputStream;
import java.io.InputStreamReader;

import junit.framework.TestCase;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestIdPConfiguration extends TestCase {

	public final static int DEFAULT_MIN_PASSWORD_LENGTH = 10;

	public final static int DEFAULT_MAX_PASSWORD_LENGTH = 30;

	public final static int DEFAULT_MIN_UID_LENGTH = 4;

	public final static int DEFAULT_MAX_UID_LENGTH = 20;

	public final static String DEFAULT_KEY_PASSWORD = "idpkey";

	public final static String DEFAULT_IDP_NAME = "Dorian IdP";

	private final static String DEFAULT_REGISTRATION_POLICY = ManualRegistrationPolicy.class.getName();


	public void testConfiguration() {
		try {
			InputStream resource = TestCase.class.getResourceAsStream(Constants.IDP_CONFIG);
			IdentityProviderConfiguration conf = (IdentityProviderConfiguration) gov.nih.nci.cagrid.common.Utils
				.deserializeObject(new InputStreamReader(resource), IdentityProviderConfiguration.class);

			assertEquals(DEFAULT_MIN_PASSWORD_LENGTH, conf.getPasswordLength().getMin());
			assertEquals(DEFAULT_MAX_PASSWORD_LENGTH, conf.getPasswordLength().getMax());
			assertEquals(DEFAULT_MIN_UID_LENGTH, conf.getUIDLength().getMin());
			assertEquals(DEFAULT_MAX_UID_LENGTH, conf.getUIDLength().getMax());
			assertEquals(DEFAULT_REGISTRATION_POLICY, conf.getRegistrationPolicy());
			assertEquals(DEFAULT_KEY_PASSWORD, conf.getAssertingCredentials().getKeyPassword());
			assertEquals(DEFAULT_IDP_NAME, conf.getIdentityProviderName());

			assertEquals(true, conf.getAssertingCredentials().isAutoRenew());
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

}
