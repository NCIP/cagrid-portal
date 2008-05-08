package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.domain.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.portal.domain.Participant;

import java.util.List;
import java.sql.SQLException;

import org.springframework.orm.hibernate3.HibernateCallback;
import org.hibernate.Session;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class UMLAttributeDao extends AbstractDao<UMLAttribute> {

    public Class domainClass() {
        return UMLAttribute.class;
    }

    public List<UMLAttribute> getSemanticallyEquivalentAttributes(final UMLAttribute attr) {

        final String code = attr.getSemanticMetadata().get(0).getConceptCode();

        List<UMLAttribute> attributes = (List<UMLAttribute>) getHibernateTemplate()
                .execute(new HibernateCallback() {
                    public Object doInHibernate(Session session)
                            throws HibernateException, SQLException {

                        return session.createCriteria(UMLAttribute.class)
                                .add(Restrictions.ne("id", attr.getId()))
                                .createCriteria("semanticMetadata")
                                .add(Restrictions.eq("conceptCode",
                                        code))
                                .list();
                    }
                });
        logger.debug("Retreived " + attributes.size()
                + " attributes   for concept code '" + code + "'.");
        return attributes;
    }
}
