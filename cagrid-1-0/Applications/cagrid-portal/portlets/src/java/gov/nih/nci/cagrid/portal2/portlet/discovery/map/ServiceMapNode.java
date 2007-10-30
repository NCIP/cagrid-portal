/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.discovery.map;

import gov.nih.nci.cagrid.portal2.domain.ServiceStatus;

import java.util.ArrayList;
import java.util.List;


/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ServiceMapNode extends MapNode {


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

	public List<ServiceInfo> getServiceInfos() {
		return serviceInfos;
	}




	public void setServiceInfos(List<ServiceInfo> serviceInfos) {
		this.serviceInfos = serviceInfos;
	}
	

}
