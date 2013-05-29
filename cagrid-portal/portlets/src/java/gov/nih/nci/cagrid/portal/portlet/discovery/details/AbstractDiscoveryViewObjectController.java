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
package gov.nih.nci.cagrid.portal.portlet.discovery.details;

import gov.nih.nci.cagrid.portal.portlet.AbstractViewObjectController;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryModel;

import org.springframework.beans.factory.annotation.Required;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public abstract class AbstractDiscoveryViewObjectController extends
		AbstractViewObjectController {
	
	private DiscoveryModel discoveryModel;

	/**
	 * 
	 */
	public AbstractDiscoveryViewObjectController() {

	}


	@Required
	public DiscoveryModel getDiscoveryModel() {
		return discoveryModel;
	}

	public void setDiscoveryModel(DiscoveryModel discoveryModel) {
		this.discoveryModel = discoveryModel;
	}
}
