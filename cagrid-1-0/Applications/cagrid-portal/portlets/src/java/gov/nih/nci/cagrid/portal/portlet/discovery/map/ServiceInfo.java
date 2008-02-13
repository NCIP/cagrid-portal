/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.discovery.map;

import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.metadata.common.ResearchCenter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ServiceInfo {
	
	private static final Log logger = LogFactory.getLog(ServiceInfo.class);

	private String name;
	private String center;
	private String status;	
	private String url;
	private String urlAbbrv;
	private String id;
	private ServiceType type;
	
	/**
	 * @param service 
	 * 
	 */
	public ServiceInfo(GridService service) {
		setName(service.getServiceMetadata().getServiceDescription().getName());
		ResearchCenter rc = service.getServiceMetadata().getHostingResearchCenter();
		if(rc != null){
			setCenter(rc.getShortName());
		}
		setStatus(service.getCurrentStatus().toString());
		setUrl(service.getUrl());
		try{
			setUrlAbbrv(getUrl().substring(0, Math.min(35, getUrl().length() - 1)));
		}catch(Exception ex){
			setUrlAbbrv("");
			logger.error("Error generating urlAbbrv: " + ex.getMessage(), ex);
		}
		setId(String.valueOf(service.getId()));
		if(service instanceof GridDataService){
			setType(ServiceType.DATA);
		}else{
			setType(ServiceType.ANALYTICAL);
		}
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
	
	public static enum ServiceType{
		DATA, ANALYTICAL;
	}

	public ServiceType getType() {
		return type;
	}

	public void setType(ServiceType type) {
		this.type = type;
	}

	public String getUrlAbbrv() {
		return urlAbbrv;
	}

	public void setUrlAbbrv(String urlAbbrv) {
		this.urlAbbrv = urlAbbrv;
	}
	
}
