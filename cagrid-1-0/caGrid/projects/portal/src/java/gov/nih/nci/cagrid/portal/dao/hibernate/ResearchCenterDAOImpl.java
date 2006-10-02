package gov.nih.nci.cagrid.portal.dao.hibernate;

import gov.nih.nci.cagrid.portal.dao.ResearchCenterDAO;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Sep 7, 2006
 * Time: 11:27:05 AM
 * To change this template use File | Settings | File Templates.
 */
public final class ResearchCenterDAOImpl extends BaseDAOImpl
        implements ResearchCenterDAO {


    public List getUniqueCenters() throws DataAccessException {
        try {
            return getHibernateTemplate().find("from ResearchCenter rc group by rc.latitude");
        } catch (DataAccessException e) {
            _logger.error(e);
            throw e;
        }
    }

    /**
     * Will do a free text search
     *
     * @param keyword
     * @return List of ResearchCenter objects
     */
    public List keywordSearch(String keyword) {

        StringBuffer sb = new StringBuffer("from ResearchCenter rc where");
        StringTokenizer st = new StringTokenizer(keyword);
        while (st.hasMoreTokens()) {
            String hqlQueryStr = st.nextToken();
            sb.append(" rc.country like '%").append(keyword.trim()).append("%'");
            sb.append(" or rc.description like '%").append(keyword.trim()).append("%'");
            sb.append(" or rc.displayName like '%").append(keyword.trim()).append("%'");
            sb.append(" or rc.homepageURL like '%").append(keyword.trim()).append("%'");
            sb.append(" or rc.imageURL like '%").append(keyword.trim()).append("%'");
            sb.append(" or rc.locality like '%").append(keyword.trim()).append("%'");
            sb.append(" or rc.postalCode like '%").append(keyword.trim()).append("%'");
            sb.append(" or rc.street1 like '%").append(keyword.trim()).append("%'");
            sb.append(" or rc.street2 like '%").append(keyword.trim()).append("%'");
            sb.append(" or rc.state like '%").append(keyword.trim()).append("%'");
            sb.append(" or rc.shortName like '%").append(keyword.trim()).append("%'");
            sb.append(" or rc.rssNewsURL like '%").append(keyword.trim()).append("%'");
        }

        _logger.debug("Find Research Centers for keyword" + keyword);
        try {
            return getHibernateTemplate().find(sb.toString());
        } catch (DataAccessException e) {
            _logger.error(e);
            throw e;
        }

    }
}
