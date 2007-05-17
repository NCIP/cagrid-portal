package gov.nih.nci.cagrid.data.service.auditing;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;

import java.util.Date;

/** 
 *  QueryBeginAuditingEvent
 *  Auditing event fired when a call to the query method is first made
 * 
 * @author David Ervin
 * 
 * @created May 17, 2007 11:11:05 AM
 * @version $Id: QueryBeginAuditingEvent.java,v 1.1 2007-05-17 18:39:15 dervin Exp $ 
 */
public class QueryBeginAuditingEvent extends BaseAuditingEvent {

    public QueryBeginAuditingEvent(CQLQuery query, String callerId, Date queryStartDate) {
        super(query, callerId, queryStartDate);
    }
}
