package gov.nih.nci.cagrid.data.cql.validation;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

/** 
 *  CQLValidator
 *  Interface for validation of CQL, both for conformance to the CQL schema,
 *  and for correct queries with respect to the data service's
 *  exposed Domain Model
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created May 18, 2006 
 * @version $Id$ 
 */
public interface CQLValidator {

	public void validateCql(CQLQuery query, DomainModel model) throws MalformedQueryException;
}
