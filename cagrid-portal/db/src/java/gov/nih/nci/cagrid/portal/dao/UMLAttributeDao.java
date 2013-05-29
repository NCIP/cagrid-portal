/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.domain.metadata.common.UMLAttribute;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

/**
 * User: kherm
 * 
 * @author kherm manav.kher@semanticbits.com
 */
public class UMLAttributeDao extends AbstractDao<UMLAttribute> {

	public Class domainClass() {
		return UMLAttribute.class;
	}

	public List<UMLAttribute> getSemanticallyEquivalentAttributes(
			final UMLAttribute attr) {

		final String code = attr.getSemanticMetadata().get(0).getConceptCode();

		List<UMLAttribute> attributes = (List<UMLAttribute>) getHibernateTemplate()
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {

						return session.createCriteria(UMLAttribute.class).add(
								Restrictions.ne("id", attr.getId()))
								.createCriteria("semanticMetadata").add(
										Restrictions.eq("conceptCode", code))
								.list();
					}
				});
		logger.debug("Retreived " + attributes.size()
				+ " attributes   for concept code '" + code + "'.");
		return attributes;
	}

	public List<UMLAttribute> getAttributesForClass(Integer umlClassId) {
		return getHibernateTemplate().find(
				"from UMLAttribute u where u.umlClass.id = ?", umlClassId);
	}
}
