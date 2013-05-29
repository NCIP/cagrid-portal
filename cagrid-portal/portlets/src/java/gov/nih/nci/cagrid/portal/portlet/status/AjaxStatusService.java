/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.portlet.status;

import gov.nih.nci.cagrid.portal.aggr.TrackableMonitor;
import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.dao.ParticipantDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.ServiceInfo;
import gov.nih.nci.cagrid.portal.portlet.AjaxViewGenerator;
import org.directwebremoting.annotations.Param;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.SpringCreator;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */

@RemoteProxy(name = "StatusService",
        creator = SpringCreator.class,
        creatorParams = @Param(name = "beanName",
                value = "ajaxStatusService"))
public class AjaxStatusService extends AjaxViewGenerator {

    private GridServiceDao gridServiceDao;
    private ParticipantDao participantDao;

    private int latestServicesLimit;
    private TrackableMonitor monitor;
    private static int servicesCount, analServicesCount, dataServicesCount, participantCount;
    final static List<ServiceInfo> serviceInfos = Collections.synchronizedList(new ArrayList<ServiceInfo>());


    @RemoteMethod
    public String latestServices() throws Exception {
        return super.getView(getView(), new HashMap<String, Object>() {{
            put("latestServices", serviceInfos);
        }});
    }

    @RemoteMethod
    public int getLatestServicesLimit() {
        return latestServicesLimit;
    }

    public void setLatestServicesLimit(int latestServicesLimit) {
        this.latestServicesLimit = latestServicesLimit;
    }

    @RemoteMethod
    public int servicesCount() {
        return servicesCount;
    }


    @RemoteMethod
    public int dataServicesCount() {
        return dataServicesCount;
    }

    @RemoteMethod
    public int analServicesCount() {
        return analServicesCount;
    }

    @RemoteMethod
    public int participantCount() {
        return participantCount;
    }

    @RemoteMethod
    public String lastUpdated() {
        try {
            long _elapsedTime = new Date().getTime() - monitor.getLastExecutedOn().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("mm");

            if (_elapsedTime < 60000) {
                //in seconds
                formatter.applyPattern("ss");
                return formatter.format(new java.util.Date(_elapsedTime)) + " seconds ago";
            } else if (_elapsedTime < 120000) {
                //less than 2 minutue(s)
                formatter.applyPattern("m");
                return formatter.format(new java.util.Date(_elapsedTime)) + " minute ago";
            } else if (_elapsedTime < 600000) {
                //less than 10(mm) minutues
                formatter.applyPattern("m");
                return formatter.format(new java.util.Date(_elapsedTime)) + " minutes ago";
            }
            return formatter.format(new java.util.Date(_elapsedTime)) + " minutes ago";
        } catch (RuntimeException e) {
            logger.warn("Error getting last updated" + e.getMessage());
            return "n/a";
        }
    }

    @Transactional
    public void refreshCache() {
        logger.debug("Will refresh Cache");

        servicesCount = getFilter().filter(getGridServiceDao().getAll()).size();
        dataServicesCount = getFilter().filter(getGridServiceDao().getAllDataServices()).size();
        analServicesCount = getFilter().filter(getGridServiceDao().getAllAnalyticalServices()).size();
        participantCount = getParticipantDao().getAll().size();

        List<GridService> services;
        // start with 1 extra
        int serviceLookupIncrement = 1;
        int totalServicesAvailable = getGridServiceDao().getAll().size();
        do {
            List<GridService> latest = getGridServiceDao().getLatestServices(latestServicesLimit + serviceLookupIncrement++);
            services = getFilter().filter(latest);
        }
        //run this loop till we find <latestServicesLimit> number of valid  services
        //But at the same time don't get more than available services
        while (services.size() < latestServicesLimit && (latestServicesLimit + serviceLookupIncrement) <= totalServicesAvailable);

        // wrap the pair in sync block because its a transaction
        synchronized (serviceInfos) {
            serviceInfos.clear();

            for (GridService service : services) {
                serviceInfos.add(service.getServiceInfo());
            }
        }
        logger.debug("Finished refreshing Cache");
    }

    public GridServiceDao getGridServiceDao() {
        return gridServiceDao;
    }

    public void setGridServiceDao(GridServiceDao gridServiceDao) {
        this.gridServiceDao = gridServiceDao;
    }

    public ParticipantDao getParticipantDao() {
        return participantDao;
    }

    public void setParticipantDao(ParticipantDao participantDao) {
        this.participantDao = participantDao;
    }

    public TrackableMonitor getMonitor() {
        return monitor;
    }

    public void setMonitor(TrackableMonitor monitor) {
        this.monitor = monitor;
    }


}
