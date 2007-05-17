package gov.nih.nci.cagrid.data.service.auditing;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.data.MalformedQueryException;

import java.util.Date;

/** 
 *  ValidationAuditingEvent
 *  Auditing event fired when validation is performed on a CQL query
 * 
 * @author David Ervin
 * 
 * @created May 17, 2007 11:22:35 AM
 * @version $Id: ValidationAuditingEvent.java,v 1.1 2007-05-17 18:39:15 dervin Exp $ 
 */
public class ValidationAuditingEvent extends BaseAuditingEvent {

    private MalformedQueryException cqlStructureException;
    private MalformedQueryException domainValidityException;
    
    public ValidationAuditingEvent(CQLQuery query, String callerId, Date queryStartDate,
            MalformedQueryException cqlStructureException, MalformedQueryException domainValidityException) {
        super(query, callerId, queryStartDate);
        this.cqlStructureException = cqlStructureException;
        this.domainValidityException = domainValidityException;
    }

    
    /**
     * @return Returns the cqlStructureException.  This may be
     * <code>null</code> if no exception was thrown
     */
    public MalformedQueryException getCqlStructureException() {
        return cqlStructureException;
    }

    
    /**
     * @return Returns the domainValidityException.  This may be
     * <code>null</code> if no exception was thrown
     */
    public MalformedQueryException getDomainValidityException() {
        return domainValidityException;
    }
}
