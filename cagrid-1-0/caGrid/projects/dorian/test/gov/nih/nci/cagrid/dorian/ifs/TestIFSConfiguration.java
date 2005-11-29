package gov.nih.nci.cagrid.gums.ifs;

import gov.nih.nci.cagrid.gums.common.FaultUtil;

import gov.nih.nci.cagrid.gums.test.TestResourceManager;

import java.io.File;

import junit.framework.TestCase;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestIFSConfiguration extends TestCase {
	
	public static final int DEFAULT_MIN_LENGTH = 4;

	public static final int DEFAULT_MAX_LENGTH = 10;

	public static final int DEFAULT_CREDENTIALS_VALID_YEARS = 12;

	public static final int DEFAULT_CREDENTIALS_VALID_MONTHS = 0;

	public static final int DEFAULT_CREDENTIALS_VALID_DAYS = 0;

	public static final int DEFAULT_CREDENTIALS_VALID_HOURS = 0;

	public static final int DEFAULT_CREDENTIALS_VALID_MINUTES = 0;

	public static final int DEFAULT_CREDENTIALS_VALID_SECONDS = 0;
	
	public static final int DEFAULT_MAX_PROXY_VALID_HOURS = 0;

	public static final int DEFAULT_MAX_PROXY_VALID_MINUTES = 0;

	public static final int DEFAULT_MAX_PROXY_VALID_SECONDS = 0;

	
	public static String IFS_CONF = "resources" + File.separator
	+ "general-test" + File.separator + "ifs-conf.xml";
	

	public void testConfiguration() {
		try {
			 TestResourceManager trm = new TestResourceManager(IFS_CONF);
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
			assertEquals(DEFAULT_MAX_PROXY_VALID_HOURS, conf
					.getMaxProxyValidHours());
			assertEquals(DEFAULT_MAX_PROXY_VALID_MINUTES, conf
					.getMaxProxyValidMinutes());
			assertEquals(DEFAULT_MAX_PROXY_VALID_SECONDS, conf
					.getMaxProxyValidSeconds());	
		
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} 
	}
	

}
