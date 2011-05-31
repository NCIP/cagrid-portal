package gov.nih.nci.cagrid.hibernate;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.mapping.Mappings;

import java.util.List;

/**
 * 
 */

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public interface CQLQueryResultsBuilder {
	CQLQueryResults build(List rawResults, CQLQuery cqlQuery, Mappings classToQName);
}
