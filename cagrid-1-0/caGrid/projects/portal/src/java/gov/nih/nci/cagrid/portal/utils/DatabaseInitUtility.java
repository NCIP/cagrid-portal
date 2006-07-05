package gov.nih.nci.cagrid.portal.utils;

import gov.nih.nci.cagrid.portal.domain.IndexService;
import gov.nih.nci.cagrid.portal.manager.IndexServiceManager;
import gov.nih.nci.cagrid.discovery.MetadataUtils;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import org.springframework.beans.factory.InitializingBean;
import org.apache.axis.message.addressing.EndpointReferenceType;

import java.util.Iterator;
import java.util.Set;

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
    private Set indexList;
    private IndexServiceManager manager;

    public DatabaseInitUtility() {
    }

    public void afterPropertiesSet() throws Exception {

        for (Iterator idxIter = indexList.iterator(); idxIter.hasNext();) {
            EndpointReferenceType serviceEPR = GridUtils.getEPR(idxIter.next().toString());
            IndexService idxService = new IndexService(serviceEPR);
            
            manager.save(idxService);
        }
    }

    public Set getIndexList() {
        return indexList;
    }

    public void setIndexList(Set indexList) {
        this.indexList = indexList;
    }

    public IndexServiceManager getManager() {
        return manager;
    }

    public void setManager(IndexServiceManager manager) {
        this.manager = manager;
    }
}
