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

import org.w3c.dom.Element;
import org.w3c.dom.Document;

import java.util.Vector;
import java.util.HashMap;

import org.globus.wsrf.utils.XmlUtils;

import org.apache.axis.types.URI;

public class TestDataService implements DataServiceInterface {

    String base = "bigid://some.stuff/eduPerson?id=";

    HashMap hashMap = new HashMap();

    public URI create(int i) throws Exception {

        TestData data = new TestData(i);
        int id = data.hashCode();
        hashMap.put(new Integer(id), data);
        return new URI(base + id);
    }

    public Element getData(URI epi, String dn) {

        String id = epi.getQueryString();
        if (id == null) {
            return null;
        }
        id = id.substring(3, id.length());
        Integer idInt = Integer.valueOf(id);
        TestData data = (TestData)hashMap.get(idInt);
        if (data != null) {
            return data.getData();
        } 
        return null;
    }

    public class TestData {
        
        Element element;

        public TestData(int i) throws Exception {

            Document doc = XmlUtils.newDocument();

            Element intElem = doc.createElementNS("http://temp", "integerVal");
            intElem.setAttributeNS("http://temp", "value", 
                                   Integer.toString(i));
            doc.appendChild(intElem);

            this.element = doc.getDocumentElement();

        }
        
        public Element getData() {
            
            return this.element;

        }
    }
}
