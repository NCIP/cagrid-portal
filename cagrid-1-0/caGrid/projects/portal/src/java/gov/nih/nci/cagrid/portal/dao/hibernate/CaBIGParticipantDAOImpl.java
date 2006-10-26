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
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List getUniqueParticipants() throws DataAccessException {
        return getHibernateTemplate().find("from CaBIGParticipant participant order by participant.name");
    }
}
