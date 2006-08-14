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
package org.globus.resolution;

import java.util.Vector;

import org.apache.axis.types.URI;

public class SimpleDataService implements DataServiceInterface {
    
    private Vector eprList = new Vector();
    
    public SimpleDataService(URI[] epis) {
        
        for (int i=0; i<epis.length; i++) {
            eprList.add(epis[i]);
        }
    }
    
    public boolean exists(URI epi, String dn) {
        return this.eprList.contains(epi);
    }
    
    public void addEPI(URI epi) {
        eprList.add(epi);
    }
}

