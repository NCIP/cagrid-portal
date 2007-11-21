/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.status;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.ParticipantDirectory;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.ServiceDirectory;
import gov.nih.nci.cagrid.portal.portlet.discovery.map.ServiceInfo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

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
	
	public List<ServiceInfo> getLatestServices(){
		List<ServiceInfo> serviceInfos = new ArrayList<ServiceInfo>();
		List<GridService> services = getGridServiceDao().getLatestServices(getLatestServicesLimit());
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
