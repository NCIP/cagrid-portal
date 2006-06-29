package gov.nih.nci.cagrid.portal.dao.hibernate;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jun 29, 2006
 * Time: 5:52:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class BaseHibernateDAOImpl extends HibernateDaoSupport {

    public List loadAll(Class type) {
        return getHibernateTemplate().loadAll(type);
    }
}
