package gov.nih.nci.cagrid.portal.portlet.gss;

import java.util.Map;

public class SummaryQueryResults {

    private SummaryQueryWithLocations query;
    private Map<String, Long> details;

    public SummaryQueryResults(SummaryQueryWithLocations _query, Map<String, Long> _details) {
        this.query = _query;
        this.details = _details;
    }

    public Long getTotal() {
        long res = 0;
        if (this.details != null) {
            for (String k : this.details.keySet()) {
                Long l = this.details.get(k);
                if (l != null) {
                    res = res + l.longValue();
                }
            }
        }
        return new Long(res);
    }

    public SummaryQueryWithLocations getQuery() {
        return query;
    }

    public Map<String, Long> getDetails() {
        return details;
    }

}
