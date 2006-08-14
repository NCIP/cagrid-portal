/*
 * Portions of this file Copyright 1999-2005 University of Chicago
 * Portions of this file Copyright 1999-2005 The University of Southern California.
 *
 * This file or a portion of this file is licensed under the
 * terms of the Globus Toolkit Public License, found at
 * http://www.globus.org/toolkit/download/license.html.
 * If you redistribute this file, with or without
 * modifications, you must include this notice in the file.
 */
package org.globus.transfer;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestResult;

import org.globus.wsrf.test.GridTestSuite;

public class PackageTests extends GridTestSuite {

    public PackageTests(String name) {
        super(name);
    }

    public void run(TestResult result) {

	super.run(result);
    }
    
    public static Test suite() throws Exception {

        TestSuite suite = new PackageTests("Transfer Service Tests");
        suite.addTestSuite(TransferServiceTest.class);
        return suite;
    }
}
