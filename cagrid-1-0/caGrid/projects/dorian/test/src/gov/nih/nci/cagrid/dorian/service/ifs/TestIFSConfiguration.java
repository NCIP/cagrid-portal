package gov.nih.nci.cagrid.dorian.service.ifs;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.dorian.conf.IdentityFederationConfiguration;
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
public class TestIFSConfiguration extends TestCase {

	public static final int DEFAULT_MIN_LENGTH = 1;

	public static final int DEFAULT_MAX_LENGTH = 2;

	public static final int DEFAULT_CREDENTIALS_VALID_YEARS = 3;

	public static final int DEFAULT_CREDENTIALS_VALID_MONTHS = 4;

	public static final int DEFAULT_CREDENTIALS_VALID_DAYS = 5;

	public static final int DEFAULT_CREDENTIALS_VALID_HOURS = 6;

	public static final int DEFAULT_CREDENTIALS_VALID_MINUTES = 7;

	public static final int DEFAULT_CREDENTIALS_VALID_SECONDS = 8;

	public static final int DEFAULT_MAX_PROXY_LIFETIME_HOURS = 9;

	public static final int DEFAULT_MAX_PROXY_LIFETIME_MINUTES = 10;

	public static final int DEFAULT_MAX_PROXY_LIFETIME_SECONDS = 11;


	public void testConfiguration() {
		try {
			InputStream resource = TestCase.class.getResourceAsStream(Constants.IFS_CONF);
			IdentityFederationConfiguration conf = (IdentityFederationConfiguration) gov.nih.nci.cagrid.common.Utils
				.deserializeObject(new InputStreamReader(resource), IdentityFederationConfiguration.class);

			assertEquals(DEFAULT_MIN_LENGTH, conf.getIdentityProviderNameLength().getMin());
			assertEquals(DEFAULT_MAX_LENGTH, conf.getIdentityProviderNameLength().getMax());
			assertEquals(DEFAULT_CREDENTIALS_VALID_YEARS, conf.getCredentialPolicy().getCredentialLifetime().getYears());
			assertEquals(DEFAULT_CREDENTIALS_VALID_MONTHS, conf.getCredentialPolicy().getCredentialLifetime()
				.getMonths());
			assertEquals(DEFAULT_CREDENTIALS_VALID_DAYS, conf.getCredentialPolicy().getCredentialLifetime().getDays());
			assertEquals(DEFAULT_CREDENTIALS_VALID_MINUTES, conf.getCredentialPolicy().getCredentialLifetime()
				.getMinutes());
			assertEquals(DEFAULT_CREDENTIALS_VALID_SECONDS, conf.getCredentialPolicy().getCredentialLifetime()
				.getSeconds());
			assertEquals(DEFAULT_MAX_PROXY_LIFETIME_HOURS, conf.getProxyPolicy().getProxyLifetime().getHours());
			assertEquals(DEFAULT_MAX_PROXY_LIFETIME_MINUTES, conf.getProxyPolicy().getProxyLifetime().getMinutes());
			assertEquals(DEFAULT_MAX_PROXY_LIFETIME_SECONDS, conf.getProxyPolicy().getProxyLifetime().getSeconds());

		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

}
