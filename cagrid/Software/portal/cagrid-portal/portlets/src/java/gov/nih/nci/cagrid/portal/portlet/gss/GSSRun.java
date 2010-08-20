package gov.nih.nci.cagrid.portal.portlet.gss;

import java.util.Date;
import java.util.List;

public class GSSRun {
    
    private List<SummaryQueryResults> results;
    private Date finished = null;

    public GSSRun(List<SummaryQueryResults> results) {
        this.results = results;
        this.finished = new Date();
    }

    public List<SummaryQueryResults> getResults() {
        return results;
    }

    public Date getFinished() {
        return finished;
    }
}
