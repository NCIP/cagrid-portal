package gov.nih.nci.cagrid.fqp.engine;

import gov.nih.nci.cagrid.cqlresultset.CQLQueryResultsType;
import gov.nih.nci.cagrid.fqp.exception.FQPException;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: May 4, 2006
 * Time: 4:32:38 PM
 * To change this template use File | Settings | File Templates.
 */
public interface QueryProcessor {

    public CQLQueryResultsType execute(Object queryObject) throws FQPException;

}
