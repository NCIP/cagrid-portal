package gov.nih.nci.cagrid.portal.utils;

import gov.nih.nci.cagrid.portal.domain.IndexService;
import gov.nih.nci.cagrid.portal.manager.IndexServiceManager;
import gov.nih.nci.cagrid.portal.exception.PortalInitializationException;
import gov.nih.nci.cagrid.portal.exception.MetadataRetreivalException;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;

import org.springframework.beans.factory.InitializingBean;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;


/**
 * This will initialize the database or make sure
 * that database is properly initialized with appropriate
 * seed data
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jun 30, 2006
 * Time: 2:02:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseInitUtility implements InitializingBean {
    // List of indexes to aggregate from
    // Using set so no duplicates are allowed
    private HashSet indexSet;
    private IndexServiceManager manager;

    public DatabaseInitUtility() {
    }

    public void afterPropertiesSet() throws PortalInitializationException {
        for (Iterator idxIter = indexSet.iterator(); idxIter.hasNext();) {
            try {
                EndpointReferenceType serviceEPR = GridUtils.getEPR(idxIter.next()
                        .toString());
                IndexService idxService = new IndexService(serviceEPR);

                manager.save(idxService);
            } catch (URI.MalformedURIException e) {
                throw new PortalInitializationException(e);
            }
        }
    }

    public HashSet getIndexSet() {
        return indexSet;
    }

    public void setIndexSet(HashSet indexSet) {
        this.indexSet = indexSet;
    }

    public IndexServiceManager getManager() {
        return manager;
    }

    public void setManager(IndexServiceManager manager) {
        this.manager = manager;
    }
}
