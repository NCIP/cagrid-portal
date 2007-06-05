/**
 * 
 */
package gov.nih.nci.cagrid.portal2.domain;

import gov.nih.nci.cagrid.portal2.domain.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.portal2.domain.metadata.dataservice.DomainModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForceDiscriminator;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@Table(name = "grid_services")
@GenericGenerator(name="id-generator", strategy = "native",
    parameters = {
        @Parameter(name="sequence", value="seq_grid_services")
    }
)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "service_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("GridService")
@ForceDiscriminator
public class GridService extends AbstractDomainObject {
	
	private String url;
	private ServiceMetadata serviceMetadata;
	private Date firstSeen;
	private Date lastSeen;
	private List<ServiceAnnotation> annotations = new ArrayList<ServiceAnnotation>();
	
	private ServiceStatusType status = ServiceStatusType.UNKNOWN;
	
	@OneToMany(mappedBy="service", cascade = CascadeType.ALL)
	public List<ServiceAnnotation> getAnnotations() {
		return annotations;
	}
	public void setAnnotations(List<ServiceAnnotation> annotations) {
		this.annotations = annotations;
	}
	public Date getFirstSeen() {
		return firstSeen;
	}
	public void setFirstSeen(Date firstSeen) {
		this.firstSeen = firstSeen;
	}
	public Date getLastSeen() {
		return lastSeen;
	}
	public void setLastSeen(Date lastSeen) {
		this.lastSeen = lastSeen;
	}
	
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "service")
	public ServiceMetadata getServiceMetadata() {
		return serviceMetadata;
	}
	public void setServiceMetadata(ServiceMetadata serviceMetadata) {
		this.serviceMetadata = serviceMetadata;
	}
	
	
	public ServiceStatusType getStatus() {
		return status;
	}
	public void setStatus(ServiceStatusType status) {
		this.status = status;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
