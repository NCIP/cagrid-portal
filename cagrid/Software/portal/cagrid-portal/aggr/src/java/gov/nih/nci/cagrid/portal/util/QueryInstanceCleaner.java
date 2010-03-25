package gov.nih.nci.cagrid.portal.util;

import gov.nih.nci.cagrid.portal.dao.QueryInstanceDao;
import gov.nih.nci.cagrid.portal.domain.dataservice.QueryInstance;
import gov.nih.nci.cagrid.portal.service.QueryInstanceService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.Date;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class QueryInstanceCleaner {

    private QueryInstanceDao queryInstanceDao;
    // default to 1 day
    private long queryCacheLifetime = 86400000;
    private Log log = LogFactory.getLog(getClass());
    private QueryInstanceService queryInstanceService;

    public void clean() {
        Date now = new Date();

        for (QueryInstance instance : queryInstanceDao.getAll()) {
            Date _timeToCompare = null;

            // if any of these times are too old, the query will be deleted
            if (instance.getFinishTime() != null)
                _timeToCompare = instance.getFinishTime();
            else if (instance.getStartTime() != null)
                _timeToCompare = instance.getStartTime();
            else if (instance.getCreateTime() != null)
                _timeToCompare = instance.getCreateTime();

            if (_timeToCompare != null) {
                if ((now.getTime() - _timeToCompare.getTime()) > queryCacheLifetime) {
                    log.info("Will clean out old query instance with id " + instance.getId());

                    getQueryInstanceService().delete(instance.getId());
                } else
                    log.warn("Query instance with ID " + instance.getId() + " has no start or finish time");
            }
        }
    }

    public long getQueryCacheLifetime() {
        return queryCacheLifetime;
    }

    public void setQueryCacheLifetime(long queryCacheLifetime) {
        this.queryCacheLifetime = queryCacheLifetime;
    }

    @Required
    public QueryInstanceService getQueryInstanceService() {
        return queryInstanceService;
    }

    public void setQueryInstanceService(QueryInstanceService queryInstanceService) {
        this.queryInstanceService = queryInstanceService;
    }

    @Required
    public QueryInstanceDao getQueryInstanceDao() {
        return queryInstanceDao;
    }

    public void setQueryInstanceDao(QueryInstanceDao queryInstanceDao) {
        this.queryInstanceDao = queryInstanceDao;
    }
}
