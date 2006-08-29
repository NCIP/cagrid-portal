package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.portal.domain.IndexService;
import gov.nih.nci.cagrid.portal.domain.RegisteredService;
import gov.nih.nci.cagrid.portal.domain.ResearchCenter;
import org.springframework.dao.DataAccessException;


/**
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jun 29, 2006
 * Time: 5:47:44 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GridServiceManager extends BaseManager {

    public void save(IndexService idx) throws DataAccessException;

    public void save(RegisteredService rService) throws DataAccessException;

    public void save(ResearchCenter rc) throws DataAccessException;
}
