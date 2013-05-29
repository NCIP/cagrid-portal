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
/**
 *
 */
package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.domain.DomainObject;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import javax.persistence.NonUniqueResultException;
import java.sql.SQLException;
import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
 */
public abstract class AbstractDao<T extends DomainObject> extends HibernateDaoSupport {

    public abstract Class domainClass();

    public T getByExample(final T sample) {
        T result = null;
        List<T> results = searchByExample(sample, false);
        if (results.size() > 1) {
            throw new NonUniqueResultException("Found " + results.size() + " " + sample.getClass().getName() + " objects.");
        } else if (results.size() == 1) {
            result = results.get(0);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public T getById(int id) {
        return (T) getHibernateTemplate().get(domainClass(), id);
    }

    @SuppressWarnings("unchecked")
    public List<T> searchByExample(final T sample, final boolean inexactMatches) {
        return (List<T>) getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Example example = Example.create(sample).excludeZeroes();
                if (inexactMatches) example.ignoreCase().enableLike(MatchMode.ANYWHERE);

                return session.createCriteria(domainClass()).add(example).setResultTransformer(
                        Criteria.DISTINCT_ROOT_ENTITY).list();
            }
        });
    }

    public List<T> searchByExample(T example) {
        return searchByExample(example, true);
    }

    public void save(T domainObject) {
        getHibernateTemplate().saveOrUpdate(domainObject);
    }

//    public void update(T domainObject){
//    	getHibernateTemplate().update(domainObject);
//    }

    public void delete(T domainObject) {
        getHibernateTemplate().delete(domainObject);
    }

    public List<T> getAll() {
        return getHibernateTemplate().find("from " + domainClass().getSimpleName());
    }

}
