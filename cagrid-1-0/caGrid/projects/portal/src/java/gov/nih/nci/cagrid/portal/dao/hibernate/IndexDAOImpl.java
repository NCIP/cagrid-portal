package gov.nih.nci.cagrid.portal.dao.hibernate;

import gov.nih.nci.cagrid.portal.dao.IndexDAO;
import gov.nih.nci.cagrid.portal.domain.IndexService;
import gov.nih.nci.cagrid.portal.domain.RegisteredService;
import gov.nih.nci.cagrid.portal.exception.RecordNotFoundException;


/**
 * @version 1.0
 * @created 22-Jun-2006 6:56:33 PM
 */
public class IndexDAOImpl extends GridServiceBaseDAOImpl implements IndexDAO {
    /**
     * Add or update a registered service
     * belonging to this index
     *
     * @param idx
     * @param rService
     */
    public IndexService addRegisteredService(IndexService idx,
        RegisteredService rService) {
        // Set index on service typically its not set
        rService.setIndex(idx);

        try {
            Integer objectID = getID4EPR(rService.getEpr());
            logger.debug("Existing identifier found for service " + objectID);
            rService.setPk(objectID);

            //if id is found then we need to attach it to session
            logger.debug("Reattaching object to session");
            getSession().merge(rService);
        } catch (RecordNotFoundException e) {
            //expected behavior. Swallow exception condtion
            logger.debug("No record found for service " + rService.getEpr() +
                ". Hibernate will assign ID");
        } catch (Exception e) {
            logger.error("Unable to retreive ID for service " +
                rService.getEpr() + ". Nested Exception is" + e.getMessage());
        }

        idx.getRegisteredServicesCollection().add(rService);
        getHibernateTemplate().saveOrUpdate(idx);

        return idx;
    }
}
