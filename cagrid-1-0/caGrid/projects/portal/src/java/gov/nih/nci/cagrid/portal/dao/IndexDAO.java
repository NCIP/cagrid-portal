package gov.nih.nci.cagrid.portal.dao;


import gov.nih.nci.cagrid.portal.domain.IndexService;
import gov.nih.nci.cagrid.portal.domain.RegisteredService;

/**
 * @version 1.0
 * @created 22-Jun-2006 6:56:32 PM
 */
public interface IndexDAO extends GridServiceBaseDAO{


    /** Add or update a registered service
     * belonging to this index
     * @param idx
     * @param service
     */
    IndexService addRegisteredService(IndexService idx, RegisteredService service);

}