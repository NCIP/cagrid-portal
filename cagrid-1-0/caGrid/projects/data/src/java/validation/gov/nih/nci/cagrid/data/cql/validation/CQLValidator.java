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
public abstract class CQLValidator {

	/**
	 * Validates a CQL Query.  If this method successfully returns without throwing an exception,
	 * the given query is valid to the CQL Schema, and all query restrictions specified conform
	 * to the given domain model.
	 * @param query
	 * 		The query to validate
	 * @param model
	 * 		The exposed domain model to validate against
	 * @throws MalformedQueryException
	 */
	public void validateCql(CQLQuery query, DomainModel model) throws MalformedQueryException {
		validateStructure(query);
		validateDomain(query, model);
	}
	
	
	public abstract void validateStructure(CQLQuery query) throws MalformedQueryException;
	
	
	public abstract void validateDomain(CQLQuery query, DomainModel model) throws MalformedQueryException;
}
