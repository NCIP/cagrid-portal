package gov.nih.nci.cagrid.portal.dao.hibernate;

import gov.nih.nci.cagrid.portal.dao.ResearchCenterDAO;
import org.springframework.dao.DataAccessException;

import java.util.List;

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
        return getHibernateTemplate().find("from ResearchCenter rc group by rc.latitude");
    }
}
