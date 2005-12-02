package gov.nih.nci.cagrid.gums.idp;

import gov.nih.nci.cagrid.gums.common.FaultUtil;
import gov.nih.nci.cagrid.gums.common.SimpleResourceManager;
import gov.nih.nci.cagrid.gums.common.ca.CertUtil;
import gov.nih.nci.cagrid.gums.common.ca.KeyUtil;

import java.io.File;

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


	
	private final static String DEFAULT_REGISTRATION_POLICY = ManualRegistrationPolicy.class
			.getName();

	public static String IDP_CONFIG = "resources" + File.separator
	+ "general-test" + File.separator + "idp-config.xml";
	
	public static String IDP_CONFIG2 = "resources" + File.separator
	+ "general-test" + File.separator + "idp-config2.xml";

	public void testAutoConfiguration() {
		try {
			 SimpleResourceManager trm = new SimpleResourceManager(IDP_CONFIG);
			 IdPConfiguration conf = (IdPConfiguration)trm.getResource(IdPConfiguration.RESOURCE);
		
			assertEquals(DEFAULT_MIN_PASSWORD_LENGTH, conf
					.getMinimumPasswordLength());
			assertEquals(DEFAULT_MAX_PASSWORD_LENGTH, conf
					.getMaximumPasswordLength());
			assertEquals(DEFAULT_MIN_UID_LENGTH, conf
					.getMinimumUIDLength());
			assertEquals(DEFAULT_MAX_UID_LENGTH, conf
					.getMaximumUIDLength());
			assertEquals(DEFAULT_REGISTRATION_POLICY, conf.getRegistrationPolicy().getClass().getName());
			assertEquals(DEFAULT_KEY_PASSWORD,conf.getKeyPassword());
		
			assertEquals(true,conf.isAutoCreateAssertingCredentials());
			assertEquals(true,conf.isAutoRenewAssertingCredentials());
			assertEquals(null,conf.getAssertingCertificate());
			assertEquals(null,conf.getAssertingKey());
			
		
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}
	public void testConfiguration() {
		try {
			 SimpleResourceManager trm = new SimpleResourceManager(IDP_CONFIG2);
			 IdPConfiguration conf = (IdPConfiguration)trm.getResource(IdPConfiguration.RESOURCE);
		
			assertEquals(DEFAULT_MIN_PASSWORD_LENGTH, conf
					.getMinimumPasswordLength());
			assertEquals(DEFAULT_MAX_PASSWORD_LENGTH, conf
					.getMaximumPasswordLength());
			assertEquals(DEFAULT_MIN_UID_LENGTH, conf
					.getMinimumUIDLength());
			assertEquals(DEFAULT_MAX_UID_LENGTH, conf
					.getMaximumUIDLength());
			assertEquals(DEFAULT_REGISTRATION_POLICY, conf.getRegistrationPolicy().getClass().getName());
			assertEquals(null,conf.getKeyPassword());
		
			assertEquals(false,conf.isAutoCreateAssertingCredentials());
			assertEquals(false,conf.isAutoRenewAssertingCredentials());
			assertEquals(CertUtil.loadCertificate("resources/ca-test/gums-cert.pem"),conf.getAssertingCertificate());
			assertEquals(KeyUtil.loadPrivateKey("resources/ca-test/gums-key.pem",conf.getKeyPassword()),conf.getAssertingKey());
			
		
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		} 
	}
	

}
