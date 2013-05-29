/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.domain.metadata.common;

import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;
import gov.nih.nci.cagrid.portal.domain.Address;
import gov.nih.nci.cagrid.portal.domain.metadata.ServiceMetadata;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
	private List<ResearchCenterPointOfContact> pointOfContactCollection = new ArrayList<ResearchCenterPointOfContact>();
	private String description;
	private String rssNewsUrl;
	private String imageUrl;
	private String homepageUrl;
	private Address address;

	@ManyToOne
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

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "researchCenter")
	public List<ResearchCenterPointOfContact> getPointOfContactCollection() {
		return pointOfContactCollection;
	}

	public void setPointOfContactCollection(
			List<ResearchCenterPointOfContact> pointOfContactCollection) {
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
