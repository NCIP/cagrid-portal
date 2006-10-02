package gov.nih.nci.cagrid.portal.dao.hibernate;

import gov.nih.nci.cagrid.portal.dao.PointOfContactDAO;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.StringTokenizer;

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
        return getHibernateTemplate().find("from PointOfContact poc group by poc.email");
    }

    public List keywordSearch(String keyword) {

        StringBuffer sb = new StringBuffer("from PointOfContact poc where ");
        StringTokenizer st = new StringTokenizer(keyword);
        while (st.hasMoreTokens()) {
            String hqlQueryStr = st.nextToken();
            sb.append("poc.affiliation like '%").append(keyword.trim()).append("%'");
            sb.append(" or poc.email like '%").append(keyword.trim()).append("%'");
            sb.append(" or poc.firstName like '%").append(keyword.trim()).append("%'");
            sb.append(" or poc.lastName like '%").append(keyword.trim()).append("%'");
            sb.append(" or poc.phoneNumber like '%").append(keyword.trim()).append("%'");
            sb.append(" or poc.role like '%").append(keyword.trim()).append("%'");
        }

        _logger.debug("Finding Point of Contacts for keyword" + keyword);
        try {
            return getHibernateTemplate().find(sb.toString());
        } catch (DataAccessException e) {
            _logger.error(e);
            throw e;
        }

    }

}
