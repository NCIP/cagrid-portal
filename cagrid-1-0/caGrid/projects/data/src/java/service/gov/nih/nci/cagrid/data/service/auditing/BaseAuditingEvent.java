package gov.nih.nci.cagrid.data.service.auditing;

import java.util.Date;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;


/** 
 *  BaseAuditingEvent
 *  Base class for data service auditing events
 * 
 * @author David Ervin
 * 
 * @created May 17, 2007 11:02:51 AM
 * @version $Id: BaseAuditingEvent.java,v 1.1 2007-05-17 18:39:15 dervin Exp $ 
 */
public abstract class BaseAuditingEvent {

    private CQLQuery query;
    private String callerId;
    private Date queryStartDate;
    
    public BaseAuditingEvent(CQLQuery query, String callerId, Date queryStartDate) {
        this.query = query;
        this.callerId = callerId;
        this.queryStartDate = queryStartDate;
    }
    
    
    public CQLQuery getQuery() {
        return query;
    }
    
    
    public String getCallerId() {
        return callerId;
    }
    
    
    public Date getQueryStartDate() {
        return queryStartDate;
    }
}
