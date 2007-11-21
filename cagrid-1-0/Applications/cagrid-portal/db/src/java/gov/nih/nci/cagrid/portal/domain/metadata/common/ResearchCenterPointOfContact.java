/**
 * 
 */
package gov.nih.nci.cagrid.portal.domain.metadata.common;


import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@DiscriminatorValue("ResearchCenterPointOfContact")
public class ResearchCenterPointOfContact extends PointOfContact {

	private ResearchCenter researchCenter;
	
	/**
	 * 
	 */
	public ResearchCenterPointOfContact() {

	}

	@ManyToOne
	@JoinColumn(name = "center_id")
	public ResearchCenter getResearchCenter() {
		return researchCenter;
	}

	public void setResearchCenter(ResearchCenter hostingResearchCenter) {
		this.researchCenter = hostingResearchCenter;
	}

	
}
