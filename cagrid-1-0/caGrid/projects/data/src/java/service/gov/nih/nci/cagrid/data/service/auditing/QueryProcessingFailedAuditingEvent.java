package gov.nih.nci.cagrid.data.service.auditing;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.data.QueryProcessingException;

import java.util.Date;

/** 
 *  QueryProcessingFailedAuditingEvent
 *  Auditing event fired when the query processor throws a query
 *  processing exception while handling a CQL Query
 * 
 * @author David Ervin
 * 
 * @created May 17, 2007 1:38:10 PM
 * @version $Id: QueryProcessingFailedAuditingEvent.java,v 1.1 2007-05-17 18:39:15 dervin Exp $ 
 */
public class QueryProcessingFailedAuditingEvent extends BaseAuditingEvent {
    
    private QueryProcessingException qpException;

    /**
     * @param query
     * @param callerId
     * @param queryStartDate
     */
    public QueryProcessingFailedAuditingEvent(CQLQuery query, String callerId, 
        Date queryStartDate, QueryProcessingException qpException) {
        super(query, callerId, queryStartDate);
        this.qpException = qpException;
    }
    
    
    public QueryProcessingException getQueryProcessingException() {
        return qpException;
    }
}
