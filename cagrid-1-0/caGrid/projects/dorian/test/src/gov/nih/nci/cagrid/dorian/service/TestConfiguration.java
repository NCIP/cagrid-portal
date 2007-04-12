package gov.nih.nci.cagrid.dorian.service;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.common.Utils;
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
public class TestConfiguration extends TestCase {

	public void testConfiguration() {
		try {
			InputStream resource = TestCase.class.getResourceAsStream(Constants.DORIAN_CONF);
			gov.nih.nci.cagrid.dorian.conf.DorianConfiguration conf = (gov.nih.nci.cagrid.dorian.conf.DorianConfiguration) Utils
				.deserializeObject(new InputStreamReader(resource),
					gov.nih.nci.cagrid.dorian.conf.DorianConfiguration.class);
			assertNotNull(conf);
		} catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}
	}

}
