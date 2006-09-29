package gov.nih.nci.cagrid.portal.dao.hibernate;

import gov.nih.nci.cagrid.portal.dao.PointOfContactDAO;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Sep 29, 2006
 * Time: 12:42:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class PointOfContactDAOImpl extends BaseDAOImpl
        implements PointOfContactDAO {

    /**
     * Will return a unique list
     * of Points Of Contact.
     * <p/>
     * Unique by a combination of {Email,Role}
     *
     * @return List PointOfContacts
     */
    public List getUniquePeople() throws DataAccessException {
        return getHibernateTemplate().find("from PointOfContact poc group by poc.email, role");
    }


}
