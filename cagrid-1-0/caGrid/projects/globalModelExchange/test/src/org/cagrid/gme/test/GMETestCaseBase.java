package org.cagrid.gme.test;

import org.cagrid.gme.service.GME;


public abstract class GMETestCaseBase extends GMEIntegrationTestCaseBase {
    protected GME gme;


    public void setGme(GME gme) {
        this.gme = gme;
    }


    public void testGME() {
        assertNotNull(gme);
    }

}
