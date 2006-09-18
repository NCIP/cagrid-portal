package gov.nih.nci.cagrid.fqp.processor;

import gov.nih.nci.cagrid.common.Utils;
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
	 * Process root element DCQLQuery Element
	 * 
	 * @param dcqlQryPlan
	 * @return
	 * @throws FederatedQueryProcessingException
	 */
	public CQLQuery processDCQLQuery(Object targetObject) throws FederatedQueryProcessingException {
		CQLQuery cqlQuery = new CQLQuery();

		// initialize CQLObject .all the nested Queries would get resolved and
		// attached to this CQL object .
		gov.nih.nci.cagrid.cqlquery.Object cqlObject = new gov.nih.nci.cagrid.cqlquery.Object();
		cqlObject.setName(targetObject.getName());

		// process the DCQL object, building up the CQL object
		populateObjectFromDCQLObject(targetObject, cqlObject);
		// this CQL Object is our target
		cqlQuery.setTarget(cqlObject);

		return cqlQuery;
	}


	/**
	 * Recursively processes the given DCQLObject, building up the given
	 * cqlObject
	 * 
	 * @param dcqlObject
	 * @param cqlObject
	 * @throws FederatedQueryProcessingException
	 */

	private void populateObjectFromDCQLObject(Object dcqlObject, gov.nih.nci.cagrid.cqlquery.Object cqlObject)
		throws FederatedQueryProcessingException {
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
			cqlObject.setGroup(resultedGroup);

		}
	}


	/**
	 * Process Group, which builds CQL Group. DCQL Groups are processed exactly
	 * as DCQL Object, except Groups can contain multiple children predicates,
	 * so an array of each must be processed.
	 * 
	 * @param dcqlGroup
	 * @return
	 * @throws FederatedQueryProcessingException
	 */
	private gov.nih.nci.cagrid.cqlquery.Group processGroup(Group dcqlGroup) throws FederatedQueryProcessingException {
		// convert basic group information and attach group to CQL object
		gov.nih.nci.cagrid.cqlquery.Group cqlGroup = new gov.nih.nci.cagrid.cqlquery.Group();
		// attach logical relationship
		cqlGroup.setLogicRelation(gov.nih.nci.cagrid.cqlquery.LogicalOperator.fromValue(dcqlGroup.getLogicRelation()
			.toString()));

		// attributes (PASS THRU)
		if (dcqlGroup.getAttribute() != null) {
			cqlGroup.setAttribute(dcqlGroup.getAttribute());
		}

		// associations
		if (dcqlGroup.getAssociation() != null && dcqlGroup.getAssociation().length > 0) {
			Association dcqlAssociationArray[] = dcqlGroup.getAssociation();
			gov.nih.nci.cagrid.cqlquery.Association[] cqlAssociationArray = new gov.nih.nci.cagrid.cqlquery.Association[dcqlAssociationArray.length];
			for (int i = 0; i < dcqlAssociationArray.length; i++) {
				cqlAssociationArray[i] = processAssociation(dcqlAssociationArray[i]);
			}
			cqlGroup.setAssociation(cqlAssociationArray);
		}

		// groups
		if (dcqlGroup.getGroup() != null && dcqlGroup.getGroup().length > 0) {
			Group dcqlGroupArray[] = dcqlGroup.getGroup();
			gov.nih.nci.cagrid.cqlquery.Group[] cqlGroupArray = new gov.nih.nci.cagrid.cqlquery.Group[dcqlGroupArray.length];
			for (int i = 0; i < dcqlGroupArray.length; i++) {
				gov.nih.nci.cagrid.cqlquery.Group cqlNestedGroup = processGroup(dcqlGroupArray[i]);
				cqlGroupArray[i] = cqlNestedGroup;
			}
			cqlGroup.setGroup(cqlGroupArray);
		}

		// foreign associations
		if (dcqlGroup.getForeignAssociation() != null && dcqlGroup.getForeignAssociation().length > 0) {
			ForeignAssociation[] foreignAssociationArray = dcqlGroup.getForeignAssociation();
			gov.nih.nci.cagrid.cqlquery.Group[] cqlGroupArray = new gov.nih.nci.cagrid.cqlquery.Group[foreignAssociationArray.length];
			for (int i = 0; i < foreignAssociationArray.length; i++) {
				// need to attach the results as crieteria ...
				gov.nih.nci.cagrid.cqlquery.Group resultedGroup = processForeignAssociation(foreignAssociationArray[i]);
				cqlGroupArray[i] = resultedGroup;
			}
			// merge in these groups with any that already exist (from group
			// processing above)
			cqlGroup.setGroup((gov.nih.nci.cagrid.cqlquery.Group[]) Utils.concatenateArrays(
				gov.nih.nci.cagrid.cqlquery.Group.class, cqlGroup.getGroup(), cqlGroupArray));
		}

		return cqlGroup;

	}


	/**
	 * Process Association convert DCQL Association into CQL Association.
	 * 
	 * @param dcqlAssociation
	 * @return
	 * @throws QueryExecutionException
	 */
	private gov.nih.nci.cagrid.cqlquery.Association processAssociation(Association dcqlAssociation)
		throws FederatedQueryProcessingException {

		// create a new CQL Association from the DCQL Association
		gov.nih.nci.cagrid.cqlquery.Association cqlAssociation = new gov.nih.nci.cagrid.cqlquery.Association();
		cqlAssociation.setRoleName(dcqlAssociation.getRoleName());
		cqlAssociation.setName(dcqlAssociation.getName());

		// process the association's Object
		populateObjectFromDCQLObject(dcqlAssociation, cqlAssociation);

		return cqlAssociation;
	}


	/**
	 * process ForeignAssociation, which is basically a Query that can be
	 * executed by a grid service mentioned in serviceURL attribute As
	 * ForeignAssocitaion itself is a DCQL Object , the Object is processed and
	 * CQL Query would be passed to DataServiceQueryExecutor obtained results
	 * from services are then aggreated.
	 * 
	 * @param foreignAssociation
	 * @return
	 * @throws FederatedQueryProcessingException
	 */
	private gov.nih.nci.cagrid.cqlquery.Group processForeignAssociation(ForeignAssociation foreignAssociation)
		throws FederatedQueryProcessingException {
		// get Foreign Object
		Object dcqlObject = foreignAssociation.getForeignObject();

		// make a new query with the CQL Object created by processing the
		// foreign association
		CQLQuery cqlQuery = new CQLQuery();
		gov.nih.nci.cagrid.cqlquery.Object cqlObject = new gov.nih.nci.cagrid.cqlquery.Object();
		cqlObject.setName(dcqlObject.getName());
		populateObjectFromDCQLObject(dcqlObject, cqlObject);
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

}