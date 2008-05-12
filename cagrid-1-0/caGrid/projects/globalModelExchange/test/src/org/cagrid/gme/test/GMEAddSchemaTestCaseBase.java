package org.cagrid.gme.test;

import org.cagrid.gme.domain.XMLSchema;


/**
 * @author oster
 */
public abstract class GMEAddSchemaTestCaseBase extends GMETestCaseBase {

    protected XMLSchema schema = null;


    public void testInitialization() {
        assertNotNull(schema);
    }

}