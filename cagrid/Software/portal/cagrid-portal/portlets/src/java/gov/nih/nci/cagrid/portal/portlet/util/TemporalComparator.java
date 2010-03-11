/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.util;

import gov.nih.nci.cagrid.portal.domain.catalog.Temporal;

import java.util.Comparator;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 *
 */
public class TemporalComparator implements Comparator<Temporal> {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Temporal t1, Temporal t2) {
		return t1.getCreatedAt().compareTo(t2.getCreatedAt());
	}

}
