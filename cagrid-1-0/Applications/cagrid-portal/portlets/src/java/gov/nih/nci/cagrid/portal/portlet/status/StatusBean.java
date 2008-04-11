/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.status;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.ParticipantDirectory;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.ServiceDirectory;
import gov.nih.nci.cagrid.portal.portlet.discovery.map.ServiceInfo;
import gov.nih.nci.cagrid.portal.portlet.util.PortletUtils;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class StatusBean {

    private ParticipantDirectory participantsDirectory;
    private ServiceDirectory servicesDirectory;
    private ServiceDirectory analyticalServicesDirectory;
    private ServiceDirectory dataServicesDirectory;
    private GridServiceDao gridServiceDao;
    private int latestServicesLimit = 5;

    /**
     *
     */
    public StatusBean() {

    }

    /**
     * Will find latest x number of valid services where
     * x is defined by the latestServicesLimit
      * @return
     */
    public List<ServiceInfo> getLatestServices(){
        List<ServiceInfo> serviceInfos = new ArrayList<ServiceInfo>();

        List<GridService> services;
        int serviceLookupIncrement = 0;
        int totalServicesAvailable = getGridServiceDao().getAll().size();
        do{
            List<GridService> latest = getGridServiceDao().getLatestServices(getLatestServicesLimit()+ serviceLookupIncrement++);
            services = PortletUtils.filterServicesByInvalidMetadata(PortletUtils.filterDormantServices(PortletUtils.filterBannedServices(latest)));
        }
        //run this loop till we find <latestServicesLimit> number of valid  services
        //But at the same time don't get more than available services
        while(services.size()<getLatestServicesLimit() && (getLatestServicesLimit() + serviceLookupIncrement) <= totalServicesAvailable);

        for(GridService service : services){
            serviceInfos.add(new ServiceInfo(service));
        }
        return serviceInfos;
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

    @Required
    public ServiceDirectory getAnalyticalServicesDirectory() {
        return analyticalServicesDirectory;
    }

    public void setAnalyticalServicesDirectory(
            ServiceDirectory analyticalServicesDirectory) {
        this.analyticalServicesDirectory = analyticalServicesDirectory;
    }

    @Required
    public ServiceDirectory getDataServicesDirectory() {
        return dataServicesDirectory;
    }

    public void setDataServicesDirectory(ServiceDirectory dataServicesDirectory) {
        this.dataServicesDirectory = dataServicesDirectory;
    }

    @Required
    public GridServiceDao getGridServiceDao() {
        return gridServiceDao;
    }

    public void setGridServiceDao(GridServiceDao gridServiceDao) {
        this.gridServiceDao = gridServiceDao;
    }

    public int getLatestServicesLimit() {
        return latestServicesLimit;
    }

    public void setLatestServicesLimit(int latestServicesLimit) {
        this.latestServicesLimit = latestServicesLimit;
    }

}