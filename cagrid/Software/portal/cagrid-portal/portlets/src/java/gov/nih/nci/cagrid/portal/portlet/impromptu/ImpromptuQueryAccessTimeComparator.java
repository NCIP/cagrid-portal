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
