package org.cagrid.gme.service;

import org.cagrid.gme.test.GMEIntegrationTestCase;


// TODO test something

public class GMETestCase extends GMEIntegrationTestCase {
    private GME gme;


    public void setGme(GME gme) {
        this.gme = gme;
    }


    public void testGME() {
        assertNotNull(gme);
    }

}
