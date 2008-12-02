package gov.nih.nci.cagrid.portal.portlet.status;

import gov.nih.nci.cagrid.portal.domain.ServiceInfo;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.dao.ParticipantDao;
import gov.nih.nci.cagrid.portal.portlet.discovery.filter.ServiceFilter;
import gov.nih.nci.cagrid.portal.portlet.AjaxViewGenerator;
import gov.nih.nci.cagrid.portal.aggr.TrackableMonitor;

import java.util.*;
import java.text.SimpleDateFormat;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.annotations.Param;
import org.directwebremoting.spring.SpringCreator;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

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
    private ServiceFilter servicefilter;
    private int latestServicesLimit;
    private TrackableMonitor monitor;

    private Log logger = LogFactory.getLog(AjaxStatusService.class);


    @RemoteMethod
    public String latestServices() throws Exception {
       final List<ServiceInfo> serviceInfos = new ArrayList<ServiceInfo>();

        List<GridService> services;
        // start with 1 extra
        int serviceLookupIncrement = 1;
        int totalServicesAvailable = gridServiceDao.getAll().size();
        do {
            List<GridService> latest = gridServiceDao.getLatestServices(latestServicesLimit + serviceLookupIncrement++);
            services = servicefilter.filter(latest);
        }
        //run this loop till we find <latestServicesLimit> number of valid  services
        //But at the same time don't get more than available services
        while (services.size() < latestServicesLimit && (latestServicesLimit + serviceLookupIncrement) <= totalServicesAvailable);

        for (GridService service : services) {
            serviceInfos.add(service.getServiceInfo());
            if(serviceInfos.size()>=latestServicesLimit)
                break;
        }
        return super.getView("/WEB-INF/jsp/status/latestServices.jsp",new HashMap<String,Object>(){{put("latestServices", serviceInfos);}});

    }

    @RemoteMethod
    public int getLatestServicesLimit() {
        return latestServicesLimit;
    }

    public void setLatestServicesLimit(int latestServicesLimit) {
        this.latestServicesLimit = latestServicesLimit;
    }

    
   @RemoteMethod
    public int servicesCount(){
        return gridServiceDao.getAll().size();
    }

    @RemoteMethod
    public int dataServicesCount(){
        return gridServiceDao.getAllDataServices().size();
    }

    @RemoteMethod
    public int analServicesCount(){
        return gridServiceDao.getAllAnalyticalServices().size();
    }

    @RemoteMethod
    public int participantCount(){
        return participantDao.getAll().size();
    }

    @RemoteMethod
    public String lastUpdated(){
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


    // spring getters and setters
    public GridServiceDao getGridServiceDao() {
        return gridServiceDao;
    }

    public void setGridServiceDao(GridServiceDao gridServiceDao) {
        this.gridServiceDao = gridServiceDao;
    }

    public ServiceFilter getServicefilter() {
        return servicefilter;
    }

    public void setServicefilter(ServiceFilter servicefilter) {
        this.servicefilter = servicefilter;
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
