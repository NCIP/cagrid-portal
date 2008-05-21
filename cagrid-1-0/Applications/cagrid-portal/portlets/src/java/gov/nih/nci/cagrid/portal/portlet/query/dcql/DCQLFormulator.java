package gov.nih.nci.cagrid.portal.portlet.query.dcql;

import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.LogicalOperator;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.dcql.*;
import gov.nih.nci.cagrid.fqp.common.DCQLConstants;
import gov.nih.nci.cagrid.portal.portlet.query.QueryConstants;
import gov.nih.nci.cagrid.portal.portlet.query.QueryFormulator;
import gov.nih.nci.cagrid.portal.portlet.query.builder.AggregateTargetsCommand;
import gov.nih.nci.cagrid.portal.portlet.query.cql.AssociationBean;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CQLQueryBean;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CriteriaBean;
import gov.nih.nci.cagrid.portal.portlet.query.cql.CriterionBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class DCQLFormulator implements QueryFormulator<DCQLQuery> {

    public static final QName qname = DCQLConstants.DCQL_QUERY_QNAME;
    private Log logger = LogFactory.getLog(CriteriaBean.class);


    public QName getQName() {
        return qname;
    }

    public DCQLQuery toQuery(CQLQueryBean bean) {
        logger.debug("Forming DCQL query");
        DCQLQuery query = new DCQLQuery();

        AggregateTargetsCommand cmd = bean.getAggregateTargets();
        // add root target       
        cmd.getSelected().add(bean.getUmlClass().getModel().getService().getUrl());

        logger.debug("Adding target URLS to dcql query");
        query.setTargetServiceURL(cmd.getSelected().toArray(new String[]{}));

        gov.nih.nci.cagrid.dcql.Object targetObject = new gov.nih.nci.cagrid.dcql.Object();
        query.setTargetObject(toTarget(targetObject, bean));
        return query;
    }

    private gov.nih.nci.cagrid.dcql.Object toTarget(
            gov.nih.nci.cagrid.dcql.Object targetObject, CriteriaBean bean) {
        targetObject.setName(bean.getUmlClass().getPackageName() + "."
                + bean.getUmlClass().getClassName());

        Group targetGroup = new Group();
        targetGroup.setLogicRelation(LogicalOperator.AND);

        List<Attribute> attEls = new ArrayList<Attribute>();
        List<Group> groupEls = new ArrayList<Group>();
        List<Association> assocEls = new ArrayList<Association>();
        List<ForeignAssociation> fassocEls = new ArrayList<ForeignAssociation>();

        // Add attributes
        for (CriterionBean criterion : bean.getCriteria()) {

            Predicate predicate = Predicate.fromString(criterion.getPredicate());
            if (predicate.equals(Predicate.IS_NULL)
                    || predicate.equals(Predicate.IS_NOT_NULL)) {
                attEls.add(new Attribute(criterion.getUmlAttribute().getName(),
                        predicate, null));
            } else {
                String[] values = criterion.getValue().split(",");
                if (values.length == 1) {
                    attEls.add(new Attribute(criterion.getUmlAttribute().getName(),
                            predicate, values[0].trim()));
                } else if (values.length > 1) {
                    Group group = new Group();
                    group.setLogicRelation(LogicalOperator.OR);
                    Attribute[] atts = new Attribute[values.length];
                    for (int i = 0; i < atts.length; i++) {
                        atts[i] = new Attribute(criterion.getUmlAttribute()
                                .getName(), predicate, values[i].trim());
                    }
                    group.setAttribute(atts);
                    groupEls.add(group);
                }
            }
        }

        // Add associations
        for (AssociationBean assocBean : bean.getAssociations()) {
            if (assocBean.getRoleName().startsWith(QueryConstants.FOREIGN_UML_CLASS_PREFIX)) {
                ForeignAssociation assoc = new ForeignAssociation();
                assoc.setTargetServiceURL(assocBean.getCriteriaBean().getUmlClass().getModel().getService().getUrl());

                gov.nih.nci.cagrid.dcql.JoinCondition joinCondition = new gov.nih.nci.cagrid.dcql.JoinCondition(assocBean.getCriteriaBean().getJoin().getLocalAttributeName(), assocBean.getCriteriaBean().getJoin().getForeignAttributeName(),
                        ForeignPredicate.fromString(assocBean.getCriteriaBean().getJoin().getPredicate()));

                assoc.setJoinCondition(joinCondition);
                gov.nih.nci.cagrid.dcql.Object obj = new gov.nih.nci.cagrid.dcql.Object();
                assoc.setForeignObject(obj);
                toTarget(obj, assocBean.getCriteriaBean());
                fassocEls.add(assoc);
            } else {
                Association assoc = new Association();
                assoc.setRoleName(assocBean.getRoleName());
                toTarget(assoc, assocBean.getCriteriaBean());
                assocEls.add(assoc);
            }
        }

        boolean addedSomething = false;
        if (attEls.size() > 0) {
            addedSomething = true;
            targetGroup.setAttribute((Attribute[]) attEls
                    .toArray(new Attribute[attEls.size()]));
        }
        if (groupEls.size() > 0) {
            addedSomething = true;
            targetGroup.setGroup((Group[]) groupEls.toArray(new Group[groupEls
                    .size()]));
        }
        if (assocEls.size() > 0) {
            addedSomething = true;
            targetGroup.setAssociation((Association[]) assocEls
                    .toArray(new Association[assocEls.size()]));
        }
        if (fassocEls.size() > 0) {
            addedSomething = true;
            targetGroup.setForeignAssociation((ForeignAssociation[]) fassocEls
                    .toArray(new ForeignAssociation[fassocEls.size()]));
        }

        if (addedSomething) {
            targetObject.setGroup(targetGroup);
        }
        return targetObject;

    }
}
