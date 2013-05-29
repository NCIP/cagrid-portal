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
