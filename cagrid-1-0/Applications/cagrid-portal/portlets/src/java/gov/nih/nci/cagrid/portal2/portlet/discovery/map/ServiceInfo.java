/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.discovery.map;

import gov.nih.nci.cagrid.portal2.domain.GridService;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ServiceInfo {

	private String name;
	private String center;
	private String status;	
	private String url;
	private String id;
	
	/**
	 * @param service 
	 * 
	 */
	public ServiceInfo(GridService service) {
		setName(service.getServiceMetadata().getServiceDescription().getName());
		setCenter(service.getServiceMetadata().getHostingResearchCenter().getDisplayName());
		setStatus(service.getCurrentStatus().toString());
		setUrl(service.getUrl());
		setId(String.valueOf(service.getId()));
	}
	
	public ServiceInfo(){
		
	}

	public String getCenter() {
		return center;
	}

	public void setCenter(String center) {
		this.center = center;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}	
	
}