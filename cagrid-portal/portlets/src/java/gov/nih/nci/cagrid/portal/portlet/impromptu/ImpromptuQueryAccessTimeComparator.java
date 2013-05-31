/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.portlet.impromptu;

import java.util.Comparator;

public class ImpromptuQueryAccessTimeComparator implements Comparator<ImpromptuQuery> {

    public int compare(ImpromptuQuery o1, ImpromptuQuery o2) {
        if ((o1 == null) || (o2 == null)) {
            return 0;
        }
        return Long.signum(o1.getAccessedOn() - o2.getAccessedOn());
    }

}
