/**
 *
 */
package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import gov.nih.nci.cagrid.portal.domain.catalog.GridServiceEndPointCatalogEntry;
import gov.nih.nci.cagrid.portal.domain.metadata.common.SemanticMetadata;
import gov.nih.nci.cagrid.portal.domain.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.*;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.sql.SQLException;
import java.util.*;

import javax.persistence.NonUniqueResultException;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class UMLClassDao extends AbstractDao<UMLClass> {
	public int maxResultSize = 10;

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.dao.AbstractDao#domainClass()
	 */
	@Override
	public Class domainClass() {
		return UMLClass.class;
	}

	/**
	 * Will get a list of same UMLClasses from other models
	 * 
	 * @param example
	 * @return ToDo refactor to SBE?
	 */
	public List<UMLClass> getSameClassesInDifferentModel(final UMLClass example) {
		List<UMLClass> resultSet = (List<UMLClass>) getHibernateTemplate()
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						return session.createCriteria(UMLClass.class).add(
								Restrictions.eq("className", example
										.getClassName())).add(
								Restrictions.eq("packageName", example
										.getPackageName())).add(
								Restrictions.eq("projectName", example
										.getProjectName())).add(
								Restrictions.ne("model.id", example.getModel()
										.getId())).setResultTransformer(
								Criteria.DISTINCT_ROOT_ENTITY).list();
					}
				});
		List<UMLClass> classes = new ArrayList<UMLClass>();

		try {
			for (UMLClass kclass : resultSet) {
				if (kclass.getModel().getService().getCurrentStatus().equals(
						ServiceStatus.ACTIVE))
					classes.add(kclass);
			}
		} catch (Exception e) {
			logger.error("Error filtering resultset" + e.getMessage());
		}

		return classes;

	}
	
	public UMLClass getClassInGivenService(final String umlClassName, final int serviceId) {

		UMLClass umlClass = null;

		int idx = umlClassName.lastIndexOf(".");
		String packageName = umlClassName.substring(0, idx);
		String className = umlClassName.substring(idx + 1);
		List l = getHibernateTemplate()
				.find(
						"from UMLClass c where c.model.service.id = ? and c.className = ? and c.packageName = ?",
						new Object[] { serviceId, className,packageName });
		if (l.size() > 1) {
			throw new NonUniqueResultException("More than one UMLClass for '"
					+ umlClassName + "' found in Service:"
					+ serviceId);
		}
		if(l.size() == 1){
			umlClass = (UMLClass) l.iterator().next();
		}
		return umlClass;
	}

	public List<UMLClass> getClassesWithSameConceptCode(final UMLClass example) {
		List<UMLClass> resultSet = (List<UMLClass>) getHibernateTemplate()
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						Set<String> codes = new HashSet<String>();
						for (SemanticMetadata sm : example
								.getSemanticMetadata()) {
							codes.add(sm.getConceptCode());
						}
						List<UMLClass> l = new ArrayList<UMLClass>();
						if (codes.size() > 0) {
							l = session
									.createCriteria(UMLClass.class)
									.add(Restrictions.ne("id", example.getId()))
									// not from the same model
									.add(
											Restrictions.ne("model.id", example
													.getModel().getId()))
									.addOrder(Order.asc("className"))
									.createCriteria("semanticMetadata").add(
											Restrictions.in("conceptCode",
													codes))
									.setResultTransformer(
											Criteria.DISTINCT_ROOT_ENTITY)
									.list();
						}
						return l;
					}
				});

		List<UMLClass> classes = new ArrayList<UMLClass>();

		try {
			for (UMLClass kclass : resultSet) {
				if (kclass.getModel().getService().getCurrentStatus().equals(
						ServiceStatus.ACTIVE))
					classes.add(kclass);
				if (classes.size() >= maxResultSize)
					break;
			}
		} catch (Exception e) {
			logger.error("Error filtering resultset" + e.getMessage());
		}

		return classes;
	}

	public List<UMLClass> getSemanticallyEquivalentClassesBasedOnAssociations(
			final UMLClass umlClass) {
		Map<Integer, UMLClass> classes = new HashMap<Integer, UMLClass>();

		for (UMLAssociationEdge edge : umlClass.getAssociations()) {
			if (edge instanceof SourceUMLAssociationEdge) {
				UMLAssociation assoc = ((SourceUMLAssociationEdge) edge)
						.getAssociation();
				TargetUMLAssociationEdge target = assoc.getTarget();
				final String code;
				try {
					code = target.getType().getSemanticMetadata().get(0)
							.getConceptCode();

					List<UMLClass> resultSet = (List<UMLClass>) getHibernateTemplate()
							.execute(new HibernateCallback() {
								public Object doInHibernate(Session session)
										throws HibernateException, SQLException {

									return session
											.createCriteria(UMLClass.class)
											.createAlias("model", "m")
											.add(
													Restrictions.ne("id",
															umlClass.getId()))
											.add(
													Restrictions.ne("m.id",
															umlClass.getModel()
																	.getId()))
											.add(
													Restrictions
															.eq(
																	"allowableAsTarget",
																	Boolean.TRUE))
											.createCriteria("associations")
											.createCriteria("type")
											.createCriteria("semanticMetadata")
											.add(
													Restrictions
															.eq("conceptCode",
																	code))
											.setResultTransformer(
													Criteria.DISTINCT_ROOT_ENTITY)
											.setMaxResults(maxResultSize)
											.list();
								}
							});

					logger.debug("Retreived " + resultSet.size()
							+ " attributes   for concept code '" + code + "'.");

					for (UMLClass kclass : resultSet) {
						if (!classes.containsKey(kclass.getId()))
							classes.put(kclass.getId(), kclass);
					}
				} catch (Exception e) {
					logger.warn("Exception getting concept code for UMLClass:"
							+ umlClass.getId());
				}
			}
		}
		List<UMLClass> returnList = new ArrayList<UMLClass>();
		returnList.addAll(classes.values());
		return returnList;
	}

	public List<UMLClass> getSemanticalyEquivalentClassesBasedOnAtrributes(
			final UMLClass umlClass) {

		List<UMLClass> classes = new ArrayList<UMLClass>();

		for (UMLAttribute attr : umlClass.getUmlAttributeCollection()) {
			final String code = attr.getSemanticMetadata().get(0)
					.getConceptCode();
			List<UMLClass> resultSet = (List<UMLClass>) getHibernateTemplate()
					.execute(new HibernateCallback() {
						public Object doInHibernate(Session session)
								throws HibernateException, SQLException {

							return session.createCriteria(UMLClass.class)
									.createAlias("model", "m").add(
											Restrictions.ne("id", umlClass
													.getId())).add(
											Restrictions.ne("m.id", umlClass
													.getModel().getId())).add(
											Restrictions.eq(
													"allowableAsTarget",
													Boolean.TRUE))
									.createCriteria("umlAttributeCollection")
									.createCriteria("semanticMetadata").add(
											Restrictions
													.eq("conceptCode", code))
									.setResultTransformer(
											Criteria.DISTINCT_ROOT_ENTITY)
									.setMaxResults(maxResultSize).list();
						}
					});
			logger.debug("Retreived " + resultSet.size()
					+ " attributes   for concept code '" + code + "'.");
			classes.addAll(resultSet);
		}
		return classes;
	}

	public UMLClass getUmlClassFromModel(final DomainModel domainModel,
			final String umlClassName) {

		UMLClass umlClass = null;

		int idx = umlClassName.lastIndexOf(".");
		String packageName = umlClassName.substring(0, idx);
		String className = umlClassName.substring(idx + 1);
		List l = getHibernateTemplate()
				.find(
						"from UMLClass c where c.model.id = ? and c.className = ? and c.packageName = ?",
						new Object[] { domainModel.getId(), className,
								packageName });
		if (l.size() > 1) {
			throw new NonUniqueResultException("More than one UMLClass for '"
					+ umlClassName + "' found in DomainModel:"
					+ domainModel.getId());
		}
		if(l.size() == 1){
			umlClass = (UMLClass) l.iterator().next();
		}
		return umlClass;

//		return (UMLClass) getHibernateTemplate().execute(
//				new HibernateCallback() {
//					public Object doInHibernate(Session session)
//							throws HibernateException, SQLException {
//						UMLClass umlClass = null;
//						for (UMLClass klass : domainModel.getClasses()) {
//							String className = klass.getPackageName() + "."
//									+ klass.getClassName();
//							if (className.equals(umlClassName)) {
//								umlClass = klass;
//								break;
//							}
//						}
//						return umlClass;
//					}
//				});
	}

	public int getMaxResultSize() {
		return maxResultSize;
	}

	public void setMaxResultSize(int maxResultSize) {
		this.maxResultSize = maxResultSize;
	}
}
