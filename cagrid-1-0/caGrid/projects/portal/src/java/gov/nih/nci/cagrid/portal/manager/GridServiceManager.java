package gov.nih.nci.cagrid.portal.manager;

import gov.nih.nci.cagrid.portal.domain.DomainObject;
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


    public void save(ResearchCenter rc) throws DataAccessException;

    /**
     * Will create domain object if new
     * object needs to be created
     * or will re-assign existing
     * id
     *
     * @param poc
     */
    public void save(DomainObject poc) throws DataAccessException;

    public void save(RegisteredService rService);
}
