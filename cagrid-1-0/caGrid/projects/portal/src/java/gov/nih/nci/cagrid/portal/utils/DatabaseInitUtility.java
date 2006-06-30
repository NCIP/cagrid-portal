package gov.nih.nci.cagrid.portal.utils;

import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.Set;
import java.util.Iterator;

import gov.nih.nci.cagrid.portal.domain.IndexService;
import gov.nih.nci.cagrid.portal.manager.IndexServiceManager;

/**
 * This will initialize the database or make sure
 * that database is properly initialized with appropriate
 * seed data
 *
 *  Created by IntelliJ IDEA.
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

    public void afterPropertiesSet() throws Exception {

        for(Iterator idxIter=indexList.iterator();idxIter.hasNext();){
            IndexService idx = new IndexService(idxIter.next().toString());

            manager. save(idx);

        }




    }

    public Set getIndexList() {
        return indexList;
    }

    public void setIndexList(Set indexList) {
        this.indexList = indexList;
    }
}
