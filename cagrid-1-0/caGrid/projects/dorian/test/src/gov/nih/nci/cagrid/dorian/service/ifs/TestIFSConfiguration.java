package gov.nih.nci.cagrid.dorian.service.ifs;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.common.SimpleResourceManager;
import gov.nih.nci.cagrid.dorian.service.ifs.IFSConfiguration;
import gov.nih.nci.cagrid.dorian.test.Constants;

import java.io.InputStream;

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
			 SimpleResourceManager trm = new SimpleResourceManager(resource);
			 IFSConfiguration conf = (IFSConfiguration)trm.getResource(IFSConfiguration.RESOURCE);
		
			assertEquals(DEFAULT_MIN_LENGTH, conf
					.getMinimumIdPNameLength());
			assertEquals(DEFAULT_MAX_LENGTH, conf
					.getMaximumIdPNameLength());
			assertEquals(DEFAULT_CREDENTIALS_VALID_YEARS, conf
					.getCredentialsValidYears());
			assertEquals(DEFAULT_CREDENTIALS_VALID_MONTHS, conf
					.getCredentialsValidMonths());
			assertEquals(DEFAULT_CREDENTIALS_VALID_DAYS, conf
					.getCredentialsValidDays());
			assertEquals(DEFAULT_CREDENTIALS_VALID_MINUTES, conf
					.getCredentialsValidMinutes());
			assertEquals(DEFAULT_CREDENTIALS_VALID_SECONDS, conf
					.getCredentialsValidSeconds());
			assertEquals(DEFAULT_MAX_PROXY_LIFETIME_HOURS, conf
					.getMaxProxyLifetimeHours());
			assertEquals(DEFAULT_MAX_PROXY_LIFETIME_MINUTES, conf
					.getMaxProxyLifetimeMinutes());
			assertEquals(DEFAULT_MAX_PROXY_LIFETIME_SECONDS, conf
					.getMaxProxyLifetimeSeconds());	
		
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} 
	}
	

}
