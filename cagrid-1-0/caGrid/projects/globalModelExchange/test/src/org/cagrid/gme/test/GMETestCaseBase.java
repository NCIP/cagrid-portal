package org.cagrid.gme.test;

import org.cagrid.gme.service.GME;


public abstract class GMETestCaseBase extends GMEIntegrationTestCaseBase {
    protected GME gme;


    @Override
    protected void onSetUp() throws Exception {
        super.onSetUp();
        assertNotNull(gme);
    }
}
