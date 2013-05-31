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
package gov.nih.nci.cagrid.portal.domain.dataservice;

import gov.nih.nci.cagrid.portal.domain.GridService;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
@Entity
@DiscriminatorValue("DCQLQuery")
public class DCQLQueryInstance extends QueryInstance {
	
	private GridService fqpService;

	/**
	 * 
	 */
	public DCQLQueryInstance() {

	}

	@ManyToOne
	@JoinColumn(name = "fqp_service_id")
	public GridService getFqpService() {
		return fqpService;
	}

	public void setFqpService(GridService fqpService) {
		this.fqpService = fqpService;
	}

	@Transient
	public String getType() {
		return "DCQL";
	}
	public void setType(String type) {
		//Do nothing. Immutable.
	}

}
