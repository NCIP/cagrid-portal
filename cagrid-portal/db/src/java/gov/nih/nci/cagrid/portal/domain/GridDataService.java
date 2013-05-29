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
package gov.nih.nci.cagrid.portal.domain;

import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.DomainModel;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@DiscriminatorValue("GridDataService")
public class GridDataService extends GridService {

	private DomainModel domainModel;
	

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "service")
	public DomainModel getDomainModel() {
		return domainModel;
	}
	public void setDomainModel(DomainModel domainModel) {
		this.domainModel = domainModel;
	}
	
}
