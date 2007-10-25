package org.cagrid.gaards.cds.service;

import gov.nih.nci.cagrid.common.FaultUtil;
import junit.framework.TestCase;

import org.cagrid.gaards.cds.testutils.Utils;

public class CDSTest extends TestCase {


	public void testDelegatedCredentialCreateDestroy() {
		try {
			CDS cds = Utils.getCDS();
			cds.clear();
		} catch (Exception e) {
			FaultUtil.printFault(e);
			fail(e.getMessage());
		}
	}
	
	
}
