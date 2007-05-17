package gov.nih.nci.cagrid.data.service.auditing;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;

import java.util.Date;

/** 
 *  QueryResultsAuditingEvent
 *  Auditing event fired when query results are produced
 * 
 * @author David Ervin
 * 
 * @created May 17, 2007 3:41:22 PM
 * @version $Id: QueryResultsAuditingEvent.java,v 1.1 2007-05-17 19:42:29 dervin Exp $ 
 */
public abstract class QueryResultsAuditingEvent extends BaseAuditingEvent {

    private CQLQueryResults results;
    
    public QueryResultsAuditingEvent(CQLQuery query, String callerId, Date queryStartDate, CQLQueryResults results) {
        super(query, callerId, queryStartDate);
        this.results = results;
    }

    
    public CQLQueryResults getResults() {
        return results;
    }
}
