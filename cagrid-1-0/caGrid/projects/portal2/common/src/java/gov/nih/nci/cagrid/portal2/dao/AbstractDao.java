/**
 * 
 */
package gov.nih.nci.cagrid.portal2.dao;

import gov.nih.nci.cagrid.portal2.dao.exception.NonUniqueResultException;
import gov.nih.nci.cagrid.portal2.domain.DomainObject;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public abstract class AbstractDao<T extends DomainObject> extends HibernateDaoSupport {
	
	public abstract Class domainClass();
	
	public T getByExample(final T sample){
		T result = null;
		List<T> results = searchByExample(sample, false);
		if(results.size() > 1){
			throw new NonUniqueResultException("Found " + results.size() + " "+ sample.getClass().getName() + " objects.");
		}else if(results.size() == 1){
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

                return session.createCriteria(domainClass()).add(example).list();
            }
        });
    }

    public List<T> searchByExample(T example) {
        return searchByExample(example, true);
    }
    
    public void save(T domainObject){
    	getHibernateTemplate().saveOrUpdate(domainObject);
    }
    
    public void delete(T domainObject){
    	getHibernateTemplate().delete(domainObject);
    }
    
    public List<T> getAll(){
    	return getHibernateTemplate().find("from " + domainClass().getSimpleName());
    }
    
}
