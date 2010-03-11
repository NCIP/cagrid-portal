/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.query.cql;

import gov.nih.nci.cagrid.cqlquery.*;
import gov.nih.nci.cagrid.portal.domain.ServiceInfo;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLAssociationEdge;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.portlet.query.QueryConstants;
import gov.nih.nci.cagrid.portal.portlet.query.builder.AggregateTargetsCommand;
import gov.nih.nci.cagrid.portal.portlet.query.dcql.ForeignUMLClassBean;
import gov.nih.nci.cagrid.portal.portlet.query.dcql.JoinCondition;
import gov.nih.nci.cagrid.portal.portlet.tree.TreeFacade;
import gov.nih.nci.cagrid.portal.portlet.tree.TreeNode;
import gov.nih.nci.cagrid.portal.portlet.util.PortletUtils;
import gov.nih.nci.cagrid.portal.util.PortalUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.*;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
 */

@Transactional
public class CriteriaBean implements ApplicationContextAware {

    private static final Log logger = LogFactory.getLog(CriteriaBean.class);

    private UMLClass umlClass;
    private Set<CriterionBean> criteria = new HashSet<CriterionBean>();
    private Set<AssociationBean> associations = new HashSet<AssociationBean>();
    private ApplicationContext applicationContext;

    private AggregateTargetsCommand aggregateTargets;
    private JoinCondition join;
    private ServiceInfo serviceInfo;
    private TreeFacade umlClassTreeFacade;

    private HibernateTemplate hibernateTemplate;

    /**
     *
     */
    public CriteriaBean() {
        aggregateTargets = new AggregateTargetsCommand();
    }

    public UMLClass getUmlClass() {
        return umlClass;
    }

    public void setUmlClass(UMLClass umlClass) {
        this.umlClass = umlClass;
        setServiceInfo(umlClass.getModel().getService().getServiceInfo());
    }

    public Set<CriterionBean> getCriteria() {
        return criteria;
    }

    public void setCriteria(Set<CriterionBean> criteria) {
        this.criteria = criteria;
    }

    public Set<AssociationBean> getAssociations() {
        return associations;
    }

    public void setAssociations(Set<AssociationBean> associations) {
        this.associations = associations;
    }

    public CriterionBean find(String path) {
        CriterionBean criterion = null;
        String[] parts = PortletUtils.parsePath(path);
        if (parts.length == 1) {
            for (CriterionBean crit : getCriteria()) {
                if (crit.getUmlAttribute().getName().equals(path)) {
                    criterion = crit;
                    break;
                }
            }
        } else {
            for (AssociationBean assoc : getAssociations()) {
                CriterionBean crit = assoc.getCriteriaBean().find(parts[1]);
                if (crit != null) {
                    criterion = crit;
                    break;
                }
            }
        }
        return criterion;
    }

    public void delete(String path) {
        update(path, null, true);
    }

    public void insert(String path, CriterionBean criterion) {
        update(path, criterion, false);
    }

    public void update(String path, CriterionBean criterion, boolean delete) {
        logger.debug("path: " + path);

        final String[] parts = PortletUtils.parsePath(path);

        if (parts.length == 1) {

            if (!delete) {
                getCriteria().add(criterion);
            } else {
                if (!getCriteria().remove(find(path))) {
                    throw new RuntimeException("Didn't remove " + path);
                }
            }
        } else {

            AssociationBean assoc = null;
            for (AssociationBean a : getAssociations()) {
                if (a.getRoleName().equals(parts[0])) {
                    assoc = a;
                    break;
                }
            }
            if (assoc == null) {
                assoc = new AssociationBean();
                assoc.setRoleName(parts[0]);
                CriteriaBean subCriteria = (CriteriaBean) getApplicationContext()
                        .getBean("criteriaBeanPrototype");


                if (parts[0].indexOf(QueryConstants.FOREIGN_UML_CLASS_PREFIX) > -1) {
                    //parse and get foreign class name
                    final StringTokenizer fPath = new StringTokenizer(parts[0], ":");
                    //should always return true
                    if (fPath.countTokens() == 3 && QueryConstants.FOREIGN_UML_CLASS_PREFIX.indexOf(fPath.nextToken()) > -1) {
                        // load foreign uml class
                        final String identifier = fPath.nextToken();
                        UMLClass assocType = (UMLClass) getHibernateTemplate().execute(
                                new HibernateCallback() {
                                    public java.lang.Object doInHibernate(Session session)
                                            throws HibernateException, SQLException {
                                        return session.get(UMLClass.class
                                                , Integer.valueOf(identifier));
                                    }
                                });
                        subCriteria.setUmlClass(assocType);
                        // ToDo Simplify. Only way to lookup path for current foreign node                       
                        TreeNode fNode = getUmlClassTreeFacade().getRootNode().find(criterion.getPath().substring(0, criterion.getPath().indexOf(parts[0])) + parts[0]);

                        // if foreign association then save the join
                        if (fNode.getContent() instanceof ForeignUMLClassBean) {
                            ForeignUMLClassBean fumlClassBean = (ForeignUMLClassBean) fNode.getContent();
                            subCriteria.setJoin(fumlClassBean.getJoin());
                        }

                        assoc.setRoleName(parts[0]);
                        assoc.setCriteriaBean(subCriteria);
                    } else {
                        logger.warn("Could not add foreign class. Path is invalid :" + parts[0]);
                    }

                } else {
                    final UMLClass klass = getUmlClass();

                    UMLClass assocType = (UMLClass) getHibernateTemplate().execute(
                            new HibernateCallback() {
                                public java.lang.Object doInHibernate(Session session)
                                        throws HibernateException, SQLException {
                                    UMLClass assocType2 = null;
                                    UMLClass klass2 = (UMLClass) session.get(klass
                                            .getClass(), klass.getId());
                                    UMLClass superClass = klass2;
                                    while (superClass != null) {
                                        for (UMLAssociationEdge edge : PortalUtils
                                                .getOtherEdges(
                                                        superClass
                                                                .getClassName(),
                                                        superClass
                                                                .getAssociations())) {
                                            if (parts[0].equals(edge.getRole())) {
                                                assocType2 = edge.getType();
                                                break;
                                            }
                                        }
                                        superClass = superClass.getSuperClass();
                                    }
                                    return assocType2;
                                }
                            });

                    if (assocType == null) {
                        throw new IllegalArgumentException(
                                "No association for role '" + parts[0] + "' on '"
                                        + getUmlClass().getClassName() + "'");
                    }
                    subCriteria.setUmlClass(assocType);
                    assoc.setCriteriaBean(subCriteria);
                    logger.debug("Adding association "
                            + getUmlClass().getClassName() + "." + parts[0]);
                }
                getAssociations().add(assoc);
            }
            assoc.getCriteriaBean().update(parts[1], criterion, delete);

            // Remove empty associations
            if (assoc.getCriteriaBean().isEmpty()) {
                logger.debug("Removing empty association: " + assoc.getRoleName());
                getAssociations().remove(assoc);
            }
        }
    }

    public boolean isEmpty() {

        boolean oneAssocNotEmpty = false;
        if (getCriteria().size() == 0) {
            for (AssociationBean assoc : getAssociations()) {
                if (!assoc.getCriteriaBean().isEmpty()) {
                    oneAssocNotEmpty = true;
                    break;
                }
            }
        }

        boolean isEmpty = !(getCriteria().size() > 0 || oneAssocNotEmpty);

        return isEmpty;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public gov.nih.nci.cagrid.cqlquery.Object toTarget() {
        gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
        return toTarget(target);
    }

    public gov.nih.nci.cagrid.cqlquery.Object toTarget(
            gov.nih.nci.cagrid.cqlquery.Object target) {
        target.setName(getUmlClass().getPackageName() + "."
                + getUmlClass().getClassName());

        Group targetGroup = new Group();
        targetGroup.setLogicRelation(LogicalOperator.AND);

        List<Attribute> attEls = new ArrayList<Attribute>();
        List<Group> groupEls = new ArrayList<Group>();
        List<Association> assocEls = new ArrayList<Association>();

        // Add attributes
        for (CriterionBean crit : getCriteria()) {

            Predicate predicate = Predicate.fromString(crit.getPredicate());
            if (predicate.equals(Predicate.IS_NULL)
                    || predicate.equals(Predicate.IS_NOT_NULL)) {
                attEls.add(new Attribute(crit.getUmlAttribute().getName(),
                        predicate, ""));
            } else {
                String[] values = crit.getValue().split(",");
                if (values.length == 1) {
                    attEls.add(new Attribute(crit.getUmlAttribute().getName(),
                            predicate, values[0].trim()));
                } else if (values.length > 1) {
                    Group group = new Group();
                    group.setLogicRelation(LogicalOperator.OR);
                    Attribute[] atts = new Attribute[values.length];
                    for (int i = 0; i < atts.length; i++) {
                        atts[i] = new Attribute(crit.getUmlAttribute()
                                .getName(), predicate, values[i].trim());
                    }
                    group.setAttribute(atts);
                    groupEls.add(group);
                }
            }
        }

        // Add associations
        for (AssociationBean assocBean : getAssociations()) {
            Association assoc = new Association();
            assoc.setRoleName(assocBean.getRoleName());
            assocBean.getCriteriaBean().toTarget(assoc);
            assocEls.add(assoc);
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

        if (addedSomething) {
            target.setGroup(targetGroup);
        }

        return target;
    }


    public boolean isDCQLQuery() {
        for (AssociationBean assocBean : getAssociations()) {
            if (assocBean.getRoleName().startsWith(QueryConstants.FOREIGN_UML_CLASS_PREFIX))
                return true;
                //check recursively
            else if (assocBean.getCriteriaBean() != null && assocBean.getCriteriaBean().isDCQLQuery())
                return true;
        }
        return getAggregateTargets() != null && getAggregateTargets().getSelected().size() > 0;
    }


    public AggregateTargetsCommand getAggregateTargets() {
        return aggregateTargets;
    }

    public void setAggregateTargets(AggregateTargetsCommand aggregateTargets) {
        this.aggregateTargets = aggregateTargets;
    }

    public JoinCondition getJoin() {
        return join;
    }

    public void setJoin(JoinCondition join) {
        this.join = join;
    }

    public TreeFacade getUmlClassTreeFacade() {
        if (umlClassTreeFacade == null)
            return (TreeFacade) getApplicationContext().getBean("umlClassTreeFacade");
        return umlClassTreeFacade;
    }

    public void setUmlClassTreeFacade(TreeFacade umlClassTreeFacade) {
        this.umlClassTreeFacade = umlClassTreeFacade;
    }

    public ServiceInfo getServiceInfo() {
        return serviceInfo;
    }

    public void setServiceInfo(ServiceInfo serviceInfo) {
        this.serviceInfo = serviceInfo;
    }
}
