/**
 * 
 */
package gov.nih.nci.cagrid.portal2.domain.metadata.common;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import gov.nih.nci.cagrid.portal2.domain.AbstractDomainObject;
import gov.nih.nci.cagrid.portal2.domain.Address;
import gov.nih.nci.cagrid.portal2.domain.metadata.ServiceMetadata;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@Table(name = "res_ctr")
@GenericGenerator(name="id-generator", strategy = "native",
    parameters = {
        @Parameter(name="sequence", value="seq_res_ctr")
    }
)
public class ResearchCenter extends AbstractDomainObject {

	private ServiceMetadata serviceMetadata;
	private String displayName;
	private String shortName;
	private List<PointOfContact> pointOfContactCollection = new ArrayList<PointOfContact>();
	private String description;
	private String rssNewsUrl;
	private String imageUrl;
	private String homepageUrl;
	private Address address;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "addr_id")
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@Column(length = 4000)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getHomepageUrl() {
		return homepageUrl;
	}

	public void setHomepageUrl(String homepageUrl) {
		this.homepageUrl = homepageUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@ManyToMany
	@JoinTable(
			name = "res_ctr_pocs", 
			joinColumns = @JoinColumn(name = "res_ctr_id"), 
			inverseJoinColumns = @JoinColumn(name = "poc_id"), 
			uniqueConstraints =	@UniqueConstraint(columnNames = 
				{"res_ctr_id", "poc_id" })
	)
	public List<PointOfContact> getPointOfContactCollection() {
		return pointOfContactCollection;
	}

	public void setPointOfContactCollection(
			List<PointOfContact> pointOfContactCollection) {
		this.pointOfContactCollection = pointOfContactCollection;
	}

	public String getRssNewsUrl() {
		return rssNewsUrl;
	}

	public void setRssNewsUrl(String rssNewsUrl) {
		this.rssNewsUrl = rssNewsUrl;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
	@OneToOne
	public ServiceMetadata getServiceMetadata() {
		return serviceMetadata;
	}

	public void setServiceMetadata(ServiceMetadata serviceMetadata) {
		this.serviceMetadata = serviceMetadata;
	}
	
	
}
