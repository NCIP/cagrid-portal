/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.portlet.gss;

import java.util.Iterator;
import java.util.LinkedList;

public class MaxCapacityList<T> {

    private int maxCapacity = 0;
    private LinkedList<T> l = new LinkedList<T>();

    public MaxCapacityList(int capacity) {
        this.maxCapacity = capacity;
    }

    public MaxCapacityList<T> add(T o) {
        while (l.size() >= this.maxCapacity) {
            l.removeFirst();
        }
        l.add(o);
        return this;
    }
    
    public T getFirst() {
        return l.isEmpty() ? null : l.getFirst();
    }
    
    public T getLast() {
        return l.isEmpty() ? null : l.getLast();
    }
    
    public T get(int i) {
        return l.isEmpty() ? null : l.get(i);
    }
    
    public Iterator<T> iterator() {
        return l.listIterator();
    }

}
