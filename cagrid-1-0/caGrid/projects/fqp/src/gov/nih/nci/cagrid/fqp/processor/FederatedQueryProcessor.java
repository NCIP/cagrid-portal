package gov.nih.nci.cagrid.fqp.processor;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.QueryModifier;
import gov.nih.nci.cagrid.cqlresultset.CQLAttributeResult;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.cqlresultset.TargetAttribute;
import gov.nih.nci.cagrid.dcql.Association;
import gov.nih.nci.cagrid.dcql.ForeignAssociation;
import gov.nih.nci.cagrid.dcql.Group;
import gov.nih.nci.cagrid.dcql.Object;
import gov.nih.nci.cagrid.fqp.processor.exceptions.FederatedQueryProcessingException;
import gov.nih.nci.cagrid.fqp.processor.exceptions.RemoteDataServiceException;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * FederatedQueryProcessor decomposes the DCQL into individual CQLs Each
 * individual CQL is executed by specified grid service in serviceURL Results
 * obtained from DCQLQueryDocument are joined by Result Aggregator
 * 
 * @author Srini Akkala
 * @author Scott Oster
 */
class FederatedQueryProcessor {
	protected static Log LOG = LogFactory.getLog(FederatedQueryProcessor.class.getName());


	public FederatedQueryProcessor() {
	}


	/**
	 * process root element DCQLQuery Element
	 * 
	 * @param dcqlQryPlan
	 * @return
	 * @throws FederatedQueryProcessingException
	 */
	CQLQuery processDCQLQueryPlan(Object targetObject) throws FederatedQueryProcessingException {
		CQLQuery cqlQuery = null;
		gov.nih.nci.cagrid.cqlquery.Object cqlObject = processTargetObject(targetObject);
		cqlQuery = new CQLQuery();
		cqlQuery.setTarget(cqlObject);
		return cqlQuery;
	}


	/**
	 * process TargetObject A DCQL target object can contain Attribute (OR)
	 * GROUP (OR) Association (OR) ForeignAssociation Check for the existance of
	 * above elements and process them .
	 * 
	 * @param dcqlObject
	 * @return
	 * @throws FederatedQueryProcessingException
	 */
	private gov.nih.nci.cagrid.cqlquery.Object processTargetObject(Object dcqlObject)
		throws FederatedQueryProcessingException {

		// initialize CQLObject .all the nested Queries would get resolved and
		// attached to this CQL object .
		gov.nih.nci.cagrid.cqlquery.Object cqlObject = new gov.nih.nci.cagrid.cqlquery.Object();

		cqlObject.setName(dcqlObject.getName());

		// check for any attribute (PASS THRU)
		if (dcqlObject.getAttribute() != null) {
			cqlObject.setAttribute(dcqlObject.getAttribute());
		}

		// check for group
		if (dcqlObject.getGroup() != null) {
			// convert group and attach group to CQL object
			gov.nih.nci.cagrid.cqlquery.Group cqlGroup = processGroup(dcqlObject.getGroup());
			cqlObject.setGroup(cqlGroup);
		}

		// check for Association
		if (dcqlObject.getAssociation() != null) {
			// Convert into CQL Associoation
			gov.nih.nci.cagrid.cqlquery.Association cqlAssociation = processAssociation(dcqlObject.getAssociation());
			cqlObject.setAssociation(cqlAssociation);
		}

		// check for ForeignAssociation
		if (dcqlObject.getForeignAssociation() != null) {
			gov.nih.nci.cagrid.cqlquery.Group resultedGroup = processForeignAssociation(dcqlObject
				.getForeignAssociation());
			if (resultedGroup.getAttribute().length > 0) {
				cqlObject.setGroup(resultedGroup);
			}

		}

		return cqlObject;

	}


	/**
	 * process Group builds CQL Group . Group can contain nested Groups or
	 * Association or ForeignAssociation Based on the existance of above
	 * elements , the elements are processed and DCQL Groups or Associations are
	 * converted into CQL Groups or Associations and those would get attached to
	 * CQL Group
	 * 
	 * @param dcqlGroup
	 * @return
	 * @throws FederatedQueryProcessingException
	 * @throws QueryExecutionException
	 */
	private gov.nih.nci.cagrid.cqlquery.Group processGroup(Group dcqlGroup) throws FederatedQueryProcessingException {
		// convert basic group information and attach group to CQL object
		gov.nih.nci.cagrid.cqlquery.Group cqlGroup = new gov.nih.nci.cagrid.cqlquery.Group();
		// attach logical relationship and any aatributes .

		cqlGroup.setLogicRelation(gov.nih.nci.cagrid.cqlquery.LogicalOperator.fromValue(dcqlGroup.getLogicRelation()
			.toString()));

		// attributes (PASS THRU)
		cqlGroup.setAttribute(dcqlGroup.getAttribute());

		// associations
		if (dcqlGroup.getAssociation() != null && dcqlGroup.getAssociation().length > 0) {
			cqlGroup = attachAssociationArrayToGroup(dcqlGroup, cqlGroup);
		}
		// groups
		if (dcqlGroup.getGroup() != null && dcqlGroup.getGroup().length > 0) {
			cqlGroup = attachNestedGroupArray(dcqlGroup, cqlGroup);
		}
		// foreign associations
		if (dcqlGroup.getForeignAssociation() != null && dcqlGroup.getForeignAssociation().length > 0) {
			cqlGroup = attachForeignAssociationArrayToGroup(dcqlGroup, cqlGroup);
		}

		return cqlGroup;

	}


	/**
	 * process Association convert DCQL Association into CQL Association.
	 * 
	 * @param dcqlAssociation
	 * @return
	 * @throws QueryExecutionException
	 */
	private gov.nih.nci.cagrid.cqlquery.Association processAssociation(Association dcqlAssociation)
		throws FederatedQueryProcessingException {

		gov.nih.nci.cagrid.cqlquery.Association cqlAssociation = new gov.nih.nci.cagrid.cqlquery.Association();
		cqlAssociation.setRoleName(dcqlAssociation.getRoleName());
		cqlAssociation.setName(dcqlAssociation.getName());

		// check for attributes (PASS THROUGH)
		if (dcqlAssociation.getAttribute() != null) {
			cqlAssociation.setAttribute(dcqlAssociation.getAttribute());
		}

		// check for nested associations
		if (dcqlAssociation.getAssociation() != null) {
			Association nestedAssociation = dcqlAssociation.getAssociation();
			// call this method recursively...
			cqlAssociation.setAssociation(processAssociation(nestedAssociation));
		}

		// check for foreign associations
		if (dcqlAssociation.getForeignAssociation() != null) {
			gov.nih.nci.cagrid.cqlquery.Group resultedGroup = processForeignAssociation(dcqlAssociation
				.getForeignAssociation());
			if (resultedGroup.getAttribute().length > 0)
				cqlAssociation.setGroup(resultedGroup);
		}
		// check for groups
		if (dcqlAssociation.getGroup() != null)
			cqlAssociation.setGroup(processGroup(dcqlAssociation.getGroup()));

		return cqlAssociation;
	}


	/**
	 * process ForeignAssociation ForeignAssociation is basically a Query that
	 * can be executed by a grid service mentioned in serviceURL attribute As
	 * ForeignAssocitaion itself is a DCQL Object , the Object is processed and
	 * CQL Query would be passed to FederatedQueryExecutor Obtained results from
	 * services are then aggreated using CDEs by ResultAggregator
	 * 
	 * @param foreignAssociation
	 * @return
	 * @throws FederatedQueryProcessingException
	 */
	private gov.nih.nci.cagrid.cqlquery.Group processForeignAssociation(ForeignAssociation foreignAssociation)
		throws FederatedQueryProcessingException {
		// get Foreign Object
		Object dcqlObject = foreignAssociation.getForeignObject();
		gov.nih.nci.cagrid.cqlquery.Object cqlObject = processTargetObject(dcqlObject);

		CQLQuery cqlQuery = new CQLQuery();
		cqlQuery.setTarget(cqlObject);

		// build up a query result modifier to only return distinct values of
		// the attribute we need
		String foreignAttribute = foreignAssociation.getJoinCondition().getForeignAttributeName();
		QueryModifier queryModifier = new QueryModifier();
		queryModifier.setDistinctAttribute(foreignAttribute);
		cqlQuery.setQueryModifier(queryModifier);

		// Execute Foreign Query .....
		String targetServiceURL = foreignAssociation.getTargetServiceURL();
		CQLQueryResults cqlResults = DataServiceQueryExecutor.queryDataService(cqlQuery, targetServiceURL);

		// process the resulting values
		List remoteAttributeValues = new ArrayList();
		if (cqlResults != null && cqlResults.getAttributeResult() != null) {
			CQLAttributeResult[] attributeResult = cqlResults.getAttributeResult();
			for (int i = 0; i < attributeResult.length; i++) {
				CQLAttributeResult attResult = attributeResult[i];
				TargetAttribute[] attribute = attResult.getAttribute();
				// make sure there is a valid result of only the specific
				// attribute we asked for
				if (attribute == null || attribute.length != 1 || !attribute[0].getName().equals(foreignAttribute)) {
					throw new RemoteDataServiceException("Data Service (" + targetServiceURL
						+ ") returned an invalid attribute result.");
				}
				remoteAttributeValues.add(attribute[0].getValue());
			}
			// process the array
		} else {
			// make sure there are NO RESULTS (of other types), and raise an
			// error if there are
			if (hasResults(cqlResults)) {
				throw new RemoteDataServiceException("Data Service (" + targetServiceURL
					+ ") returned invalid results when queried for Attributes.");
			}
		}

		gov.nih.nci.cagrid.cqlquery.Group criteriaGroup = ResultAggregator.buildGroup(foreignAssociation
			.getJoinCondition(), remoteAttributeValues);
		return criteriaGroup;
	}


	/**
	 * Returns true iff the passed result is not null AND contains some type of
	 * result data.
	 * 
	 * @param cqlResults
	 * @return true iff the passed result is not null AND contains some type of
	 *         result data.
	 */
	private static boolean hasResults(CQLQueryResults cqlResults) {
		return cqlResults != null
			&& (cqlResults.getAttributeResult() != null && cqlResults.getCountResult() != null
				|| cqlResults.getIdentifierResult() != null || cqlResults.getObjectResult() != null);
	}


	/**
	 * Converts and attach dcqlGroup Associations to CQL asssociations
	 * 
	 * @param dcqlGroup
	 * @param cqlGroup
	 * @return
	 * @throws FederatedQueryProcessingException
	 * @throws QueryExecutionException
	 */
	private gov.nih.nci.cagrid.cqlquery.Group attachAssociationArrayToGroup(Group dcqlGroup,
		gov.nih.nci.cagrid.cqlquery.Group cqlGroup) throws FederatedQueryProcessingException {
		// associations
		Association[] dcqlAssociationArray = dcqlGroup.getAssociation();
		// build cqlAssociationArray
		gov.nih.nci.cagrid.cqlquery.Association[] cqlAssociationArray = new gov.nih.nci.cagrid.cqlquery.Association[dcqlAssociationArray.length];

		for (int i = 0; i < dcqlAssociationArray.length; i++) {
			gov.nih.nci.cagrid.cqlquery.Association cqlAssociation = processAssociation(dcqlAssociationArray[i]);
			cqlAssociationArray[i] = cqlAssociation;
		}
		cqlGroup.setAssociation(cqlAssociationArray);

		return cqlGroup;
	}


	/**
	 * Converts and attach nested dcqlGroup to CQL asssociations
	 * 
	 * @param dcqlGroup
	 * @param cqlGroup
	 * @return
	 * @throws FederatedQueryProcessingException
	 * @throws QueryExecutionException
	 */
	private gov.nih.nci.cagrid.cqlquery.Group attachNestedGroupArray(Group dcqlGroup,
		gov.nih.nci.cagrid.cqlquery.Group cqlGroup) throws FederatedQueryProcessingException {
		// groups
		Group[] dcqlGroupArray = dcqlGroup.getGroup();
		gov.nih.nci.cagrid.cqlquery.Group[] cqlGroupArray = new gov.nih.nci.cagrid.cqlquery.Group[dcqlGroupArray.length];

		for (int i = 0; i < dcqlGroupArray.length; i++) {
			gov.nih.nci.cagrid.cqlquery.Group cqlNestedGroup = processGroup(dcqlGroupArray[i]);
			cqlGroupArray[i] = cqlNestedGroup;
		}
		cqlGroup.setGroup(cqlGroupArray);

		return cqlGroup;
	}


	/**
	 * process ForeignAssocation and attache the result Group to higher level
	 * Object.
	 * 
	 * @param dcqlGroup
	 * @param cqlGroup
	 * @return
	 * @throws FederatedQueryProcessingException
	 */
	private gov.nih.nci.cagrid.cqlquery.Group attachForeignAssociationArrayToGroup(Group dcqlGroup,
		gov.nih.nci.cagrid.cqlquery.Group cqlGroup) throws FederatedQueryProcessingException {

		// foreign associations
		ForeignAssociation[] foreignAssociationArray = dcqlGroup.getForeignAssociation();

		gov.nih.nci.cagrid.cqlquery.Group[] g = new gov.nih.nci.cagrid.cqlquery.Group[foreignAssociationArray.length];
		for (int i = 0; i < foreignAssociationArray.length; i++) {
			// need to attach the results as crieteria ...
			gov.nih.nci.cagrid.cqlquery.Group resultedGroup = processForeignAssociation(foreignAssociationArray[i]);
			// groupsTomerge[i] = resultedGroup;
			if (resultedGroup.getAttribute().length > 0) {
				g[i] = resultedGroup;
				cqlGroup.setGroup(g);
			}
		}

		return cqlGroup;
	}

}