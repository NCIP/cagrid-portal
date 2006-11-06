package gov.nih.nci.cagrid.portal.dao.hibernate;

import gov.nih.nci.cagrid.portal.dao.CaBIGParticipantDAO;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 26, 2006
 * Time: 12:11:41 PM
 * To change this template use File | Settings | File Templates.
 */
public final class CaBIGParticipantDAOImpl extends BaseDAOImpl
        implements CaBIGParticipantDAO {


    public List keywordSearch(String keyword) throws DataAccessException {
        StringBuffer sb = new StringBuffer("from CaBIGParticipant p where ");

        sb.append("p.name like '%").append(keyword.trim()).append("%'");
        sb.append(" or p.institute like '%").append(keyword.trim()).append("%'");
        sb.append(" or p.homepageURL like '%").append(keyword.trim()).append("%'");
        sb.append(" or p.city like '%").append(keyword.trim()).append("%'");
        sb.append(" or p.postalCode like '%").append(keyword.trim()).append("%'");
        sb.append(" or p.phoneNumber like '%").append(keyword.trim()).append("%'");
        sb.append(" or p.email like '%").append(keyword.trim()).append("%'");
        sb.append(" group by p.name");


        _logger.debug("Finding caBIG Participants for keyword" + keyword);
        try {
            return getHibernateTemplate().find(sb.toString());
        } catch (DataAccessException e) {
            _logger.error(e);
            throw e;
        }

    }


    public List getUniqueParticipants() throws DataAccessException {
        return getHibernateTemplate().find("from CaBIGParticipant participant order by participant.name");
    }
}
