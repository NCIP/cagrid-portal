package gov.nih.nci.cagrid.data.ui.browser;

import java.util.HashSet;
import java.util.Set;

/** 
 *  SetUtil
 *  Utilities for set manipulation
 * 
 * @author David Ervin
 * 
 * @created Jul 16, 2007 11:02:33 AM
 * @version $Id: SetUtil.java,v 1.1 2007-07-17 13:40:36 dervin Exp $ 
 */
public class SetUtil {

    public static Set difference(Set a, Set b) {
        Set diff = new HashSet();
        for (Object o : a) {
            if (!b.contains(o)) {
                diff.add(o);
            }
        }
        for (Object o : b) {
            if (!a.contains(o)) {
                diff.add(o);
            }
        }
        return diff;
    }
    
    public static Set intersect(Set a, Set b) {
        Set intersect = new HashSet();
        Set union = new HashSet();
        union.addAll(a);
        union.addAll(b);
        for (Object o : union) {
            if (a.contains(o) && b.contains(o)) {
                intersect.add(o);
            }
        }
        return intersect;
    }
}
