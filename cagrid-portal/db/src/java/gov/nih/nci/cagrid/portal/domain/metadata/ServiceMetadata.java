/**
 * 
 */
package gov.nih.nci.cagrid.portal.domain.metadata;

import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.metadata.common.ResearchCenter;
import gov.nih.nci.cagrid.portal.domain.metadata.service.Service;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@Table(name = "svc_meta")
@GenericGenerator(name="id-generator", strategy = "native",
    parameters = {
        @Parameter(name="sequence", value="seq_svc_meta")
    }
)
public class ServiceMetadata extends AbstractDomainObject {

	private ResearchCenter hostingResearchCenter;
	private Service serviceDescription;
	private GridService service;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="host_res_ctr_id")
	public ResearchCenter getHostingResearchCenter() {
		return hostingResearchCenter;
	}
	public void setHostingResearchCenter(ResearchCenter hostingResearchCenter) {
		this.hostingResearchCenter = hostingResearchCenter;
	}
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "service_desc_id")
	public Service getServiceDescription() {
		return serviceDescription;
	}
	public void setServiceDescription(Service serviceDescription) {
		this.serviceDescription = serviceDescription;
	}
	
	@OneToOne
	@JoinColumn(name = "service_id")
	public GridService getService() {
		return service;
	}
	public void setService(GridService service) {
		this.service = service;
	}
	
}
