package gov.nih.nci.cagrid.portal.dao;

import org.springframework.dao.DataAccessException;

import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 26, 2006
 * Time: 12:05:37 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CaBIGParticipantDAO extends BaseDAO {

    public List getUniqueParticipants() throws DataAccessException;


}
