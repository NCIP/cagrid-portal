/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.domain.metadata.service;

import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;
import gov.nih.nci.cagrid.portal.domain.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.portal.domain.metadata.common.PointOfContact;
import gov.nih.nci.cagrid.portal.domain.metadata.common.SemanticMetadata;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@Table(name = "svc")
@GenericGenerator(name="id-generator", strategy = "native",
    parameters = {
        @Parameter(name="sequence", value="seq_svc")
    }
)
public class Service extends AbstractDomainObject {

	private ServiceMetadata serviceMetadata;
	private List<ServiceContext> serviceContextCollection = new ArrayList<ServiceContext>();
	private List<ServicePointOfContact> pointOfContactCollection = new ArrayList<ServicePointOfContact>();
	private String description;
	private List<SemanticMetadata> semanticMetadata = new ArrayList<SemanticMetadata>();
	private String version;
	private String name;
	private CaDSRRegistration caDSRRegistration;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cadsr_reg_id")
	public CaDSRRegistration getCaDSRRegistration() {
		return caDSRRegistration;
	}

	public void setCaDSRRegistration(CaDSRRegistration caDSRRegistration) {
		this.caDSRRegistration = caDSRRegistration;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToOne(mappedBy = "serviceDescription")
	public ServiceMetadata getServiceMetadata() {
		return serviceMetadata;
	}

	public void setServiceMetadata(ServiceMetadata serviceMetadata) {
		this.serviceMetadata = serviceMetadata;
	}

	@Column(length = 4000)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "serviceDescription")
	public List<ServicePointOfContact> getPointOfContactCollection() {
		return pointOfContactCollection;
	}

	public void setPointOfContactCollection(
			List<ServicePointOfContact> pointOfContactCollection) {
		this.pointOfContactCollection = pointOfContactCollection;
	}

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
			name = "svc_sem_meta", 
			joinColumns = @JoinColumn(name = "svc_id"), 
			inverseJoinColumns = @JoinColumn(name = "sem_meta_id"), 
			uniqueConstraints =	@UniqueConstraint(columnNames = 
				{"svc_id", "sem_meta_id" })
	)
	public List<SemanticMetadata> getSemanticMetadata() {
		return semanticMetadata;
	}

	public void setSemanticMetadata(List<SemanticMetadata> semanticMetadata) {
		this.semanticMetadata = semanticMetadata;
	}

	@OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
	public List<ServiceContext> getServiceContextCollection() {
		return serviceContextCollection;
	}

	public void setServiceContextCollection(
			List<ServiceContext> serviceContextCollection) {
		this.serviceContextCollection = serviceContextCollection;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
}
