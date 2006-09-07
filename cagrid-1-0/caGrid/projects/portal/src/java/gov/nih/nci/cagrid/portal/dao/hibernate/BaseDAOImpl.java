package gov.nih.nci.cagrid.portal.dao.hibernate;

import gov.nih.nci.cagrid.portal.dao.BaseDAO;
import org.apache.log4j.Category;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.Collection;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jun 29, 2006
 * Time: 5:52:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class BaseDAOImpl extends HibernateDaoSupport implements BaseDAO {
    Category _logger;

    protected void initDao() throws Exception {
        super.initDao(); //To change body of overridden methods use File | Settings | File Templates.
        _logger = Category.getInstance(getClass());
    }

    public Object getObjectByPrimaryKey(Class cls, Integer id) throws DataAccessException {
        return getHibernateTemplate().load(cls, id);
    }

    public void saveOrUpdate(Collection objects) throws DataAccessException {
        getHibernateTemplate().saveOrUpdateAll(objects);
    }

    public void saveOrUpdate(Object obj) throws DataAccessException {
        getHibernateTemplate().saveOrUpdate(obj);
    }

    public List loadAll(Class cls) throws DataAccessException {
        return getHibernateTemplate().loadAll(cls);
    }

}
