package gov.nih.nci.cagrid.caarray.cql;

import gov.nih.nci.cagrid.cqlquery.Association;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Group;
import gov.nih.nci.cagrid.cqlquery.LogicalOperator;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlquery.QueryModifier;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.data.cql.cacore.ClassAccessUtilities;
import gov.nih.nci.common.search.AssociationCriterion;
import gov.nih.nci.common.search.AttributeCriterion;
import gov.nih.nci.common.search.CriteriaGroup;
import gov.nih.nci.common.search.Criterion;
import gov.nih.nci.common.search.InvalidCriterionException;
import gov.nih.nci.common.search.SearchCriteria;
import gov.nih.nci.mageom.search.SearchCriteriaFactory;

import java.util.ArrayList;
import java.util.Collection;

public class CQL2SC {

	public static SearchCriteria translate(CQLQuery query,
			boolean caseInsensitive) throws QueryProcessingException {
		SearchCriteria sc = processObject(query.getTarget(), caseInsensitive);
		QueryModifier queryMod = query.getQueryModifier();
		if (queryMod != null && queryMod.isCountOnly()) {
			sc.setReturnCount(true);
			sc.setReturnObjects(false);
		}
		return sc;
	}

	private static SearchCriteria processObject(
			gov.nih.nci.cagrid.cqlquery.Object object, boolean caseInsensitive)
			throws QueryProcessingException {
		SearchCriteria sc = (SearchCriteria) SearchCriteriaFactory
				.newSearchCriteria(object.getName());
		
		if(sc == null){
			throw new QueryProcessingException("Unknown type: " + object.getName());
		}
		
		if (object.getAttribute() != null) {
			AttributeCriterion attCrit = processAttribute(
					object.getAttribute(), caseInsensitive);

			try {
				sc.putCriterion(attCrit);
			} catch (InvalidCriterionException ex) {
				throw new QueryProcessingException("InvalidCriterion: "
						+ ex.getMessage(), ex);
			}

		}
		if (object.getAssociation() != null) {
			String roleName = ClassAccessUtilities.getRoleName(sc
					.getBeanClassName(), object.getAssociation());
			if (roleName == null) {
				throw new QueryProcessingException("Association from type "
						+ sc.getBeanClassName() + " to type "
						+ object.getAssociation().getName()
						+ " does not exist. Use only direct associations.");
			}
			AssociationCriterion assocCrit = processAssociation(roleName,
					object.getAssociation(), caseInsensitive);
			try {
				sc.putCriterion(assocCrit);
			} catch (InvalidCriterionException ex) {
				throw new QueryProcessingException("InvalidCriterion: "
						+ ex.getMessage(), ex);
			}
		}
		if (object.getGroup() != null) {
			CriteriaGroup group = processGroup(sc.getBeanClassName(), object
					.getGroup(), caseInsensitive);
			sc.addCriteriaGroup(group);
		}
		return sc;
	}

	private static CriteriaGroup processGroup(String parentName, Group group,
			boolean caseInsensitive) throws QueryProcessingException {
		int booleanOp = convertLogicalOperator(group.getLogicRelation());
		Collection criteria = new ArrayList();
		Attribute[] atts = group.getAttribute();
		if (atts != null) {
			for (int i = 0; i < atts.length; i++) {
				AttributeCriterion attCrit = processAttribute(atts[i],
						caseInsensitive);
				criteria.add(attCrit);
			}
		}
		Association[] assocs = group.getAssociation();
		if (assocs != null) {
			for (int i = 0; i < assocs.length; i++) {
				String roleName = ClassAccessUtilities.getRoleName(parentName,
						assocs[i]);
				if (roleName == null) {
					throw new QueryProcessingException("Association from type "
							+ parentName + " to type " + assocs[i].getName()
							+ " does not exist. Use only direct associations.");
				}
				AssociationCriterion assocCrit = processAssociation(roleName,
						assocs[i], caseInsensitive);
				criteria.add(assocCrit);
			}
		}
		Group[] groups = group.getGroup();
		if (groups != null) {
			for (int i = 0; i < groups.length; i++) {
				CriteriaGroup subGroup = processGroup(parentName, groups[i],
						caseInsensitive);
				criteria.add(subGroup);
			}
		}
		return new CriteriaGroup(booleanOp, criteria);
	}

	private static int convertLogicalOperator(LogicalOperator op)
			throws QueryProcessingException {
		int booleanOp = -1;
		if (LogicalOperator._AND.equals(op.getValue())) {
			booleanOp = Criterion.AND;
		} else if (LogicalOperator._OR.equals(op.getValue())) {
			booleanOp = Criterion.OR;
		} else {
			throw new QueryProcessingException("Unsupported logical operator: "
					+ op);
		}
		return booleanOp;
	}

	private static AssociationCriterion processAssociation(String roleName,
			Association association, boolean caseInsensitive)
			throws QueryProcessingException {

		SearchCriteria subSc = processObject(association, caseInsensitive);
		AssociationCriterion assocCrit = new AssociationCriterion(roleName,
				subSc);
		return assocCrit;
	}

	private static AttributeCriterion processAttribute(Attribute attribute,
			boolean caseInsensitive) {
		Collection values = new ArrayList();
		values.add(attribute.getValue());
		Predicate predicate = attribute.getPredicate();
		int conditional = convertPredicate(predicate);
		AttributeCriterion crit = new AttributeCriterion(attribute.getName(),
				conditional, values, caseInsensitive);
		return crit;
	}

	private static int convertPredicate(Predicate predicate) {
		int conditional = -1;
		if (Predicate.EQUAL_TO.equals(predicate)) {
			conditional = Criterion.EQUAL_TO;
		} else if (Predicate.GREATER_THAN.equals(predicate)) {
			conditional = Criterion.GREATER_THAN;
		} else if (Predicate.GREATER_THAN_EQUAL_TO.equals(predicate)) {
			conditional = Criterion.EQUAL_TO;
		} else if (Predicate.LESS_THAN.equals(predicate)) {
			conditional = Criterion.LESS_THAN;
		} else if (Predicate.LESS_THAN_EQUAL_TO.equals(predicate)) {
			conditional = Criterion.LESS_THAN_OR_EQUAL_TO;
		} else if (Predicate.LIKE.equals(predicate)) {
			conditional = Criterion.LIKE;
		} else if (Predicate.NOT_EQUAL_TO.equals(predicate)) {
			conditional = Criterion.NOT_EQUAL_TO;
		} else {
			throw new RuntimeException("Unsupported predicate: "
					+ predicate.getValue());
		}
		return conditional;
	}

}
