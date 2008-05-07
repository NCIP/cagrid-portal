package org.cagrid.gaards.core;

import gov.nih.nci.cagrid.common.FaultUtil;
import junit.framework.TestCase;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestBasicAuthentication extends TestCase {

	public void testEncoding() {
		try {
			BasicAuthenticationCredential credA = new BasicAuthenticationCredential("jdoe","password");
			BasicAuthenticationCredential credB = new BasicAuthenticationCredential("jdoe2","password");
			assertFalse(credA.equals(credB));
			String str = org.cagrid.gaards.core.Utils.serialize(credA);
			BasicAuthenticationCredential credA2 = (BasicAuthenticationCredential)org.cagrid.gaards.core.Utils.deserialize(str, BasicAuthenticationCredential.class);
			assertTrue(credA.equals(credA2));
			assertFalse(credA2.equals(credB));
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		}
	}
}
