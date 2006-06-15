package gov.nih.nci.cagrid.data.cql;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;

import java.util.Iterator;


/** 
 *  LazyCQLQueryProcessor
 *  Extends CQLQueryProcessor to include a method for retrieving results lazily (on demand)
 *  from the backend data source
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Jun 12, 2006 
 * @version $Id$ 
 */
public abstract class LazyCQLQueryProcessor extends CQLQueryProcessor {

	public abstract Iterator processQueryLazy(CQLQuery cqlQuery) 
		throws MalformedQueryException, QueryProcessingException;
}
