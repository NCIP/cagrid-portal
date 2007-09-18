/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.map;

import gov.nih.nci.cagrid.portal2.domain.ServiceStatus;

import java.util.ArrayList;
import java.util.List;


/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ServiceMapNode {


	private String latitude;
	private String longitude;
	private List<ServiceInfo> serviceInfos = new ArrayList<ServiceInfo>();
	
	/**
	 * 
	 */
	public ServiceMapNode() {

	}
	

	public boolean isActive(){
		boolean isActive = false;
		for(ServiceInfo info : getServiceInfos()){
			if(ServiceStatus.ACTIVE.toString().equals(info.getStatus())){
				return true;
			}
		}
		return isActive;
	}


	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}




	public List<ServiceInfo> getServiceInfos() {
		return serviceInfos;
	}




	public void setServiceInfos(List<ServiceInfo> serviceInfos) {
		this.serviceInfos = serviceInfos;
	}
	

}
