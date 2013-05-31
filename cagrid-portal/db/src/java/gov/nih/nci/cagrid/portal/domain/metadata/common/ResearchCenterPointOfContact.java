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
