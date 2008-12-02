/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.status;

import gov.nih.nci.cagrid.portal.aggr.TrackableMonitor;
import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.ServiceInfo;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.ParticipantDirectory;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.ServiceDirectory;
import gov.nih.nci.cagrid.portal.portlet.discovery.filter.ServiceFilter;
import org.springframework.beans.factory.annotation.Required;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class StatusBean {

    private ParticipantDirectory participantsDirectory;
    private ServiceDirectory servicesDirectory;
    private ServiceDirectory analyticalServicesDirectory;
    private ServiceDirectory dataServicesDirectory;
    private GridServiceDao gridServiceDao;
    private int latestServicesLimit = 5;

    private TrackableMonitor monitor;
    private ServiceFilter servicefilter;


    /**
     *
     */
    public StatusBean() {

    }



    @Required
    public ParticipantDirectory getParticipantsDirectory() {
        return participantsDirectory;
    }

    public void setParticipantsDirectory(ParticipantDirectory participantsDirectory) {
        this.participantsDirectory = participantsDirectory;
    }

    @Required
    public ServiceDirectory getServicesDirectory() {
        return servicesDirectory;
    }

    public void setServicesDirectory(ServiceDirectory servicesDirectory) {
        this.servicesDirectory = servicesDirectory;
    }

    public ServiceDirectory getAnalyticalServicesDirectory() {
        return analyticalServicesDirectory;
    }

    @Required
    public void setAnalyticalServicesDirectory(
            ServiceDirectory analyticalServicesDirectory) {
        this.analyticalServicesDirectory = analyticalServicesDirectory;
    }

    public ServiceDirectory getDataServicesDirectory() {
        return dataServicesDirectory;
    }

    @Required
    public void setDataServicesDirectory(ServiceDirectory dataServicesDirectory) {
        this.dataServicesDirectory = dataServicesDirectory;
    }

    public GridServiceDao getGridServiceDao() {
        return gridServiceDao;
    }

    @Required
    public void setGridServiceDao(GridServiceDao gridServiceDao) {
        this.gridServiceDao = gridServiceDao;
    }

    public int getLatestServicesLimit() {
        return latestServicesLimit;
    }

    public void setLatestServicesLimit(int latestServicesLimit) {
        this.latestServicesLimit = latestServicesLimit;
    }

    public TrackableMonitor getMonitor() {
        return monitor;
    }

    public void setMonitor(TrackableMonitor monitor) {
        this.monitor = monitor;
    }


    public String getLastUpdated() {
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
            return null;
        }

    }

    @Required
    public ServiceFilter getServicefilter() {
        return servicefilter;
    }

    public void setServicefilter(ServiceFilter servicefilter) {
        this.servicefilter = servicefilter;
    }
}