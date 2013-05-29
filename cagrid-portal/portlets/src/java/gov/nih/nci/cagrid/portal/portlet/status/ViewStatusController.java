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
package gov.nih.nci.cagrid.portal.portlet.status;

import javax.portlet.RenderRequest;

import gov.nih.nci.cagrid.portal.portlet.AbstractViewObjectController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ViewStatusController extends AbstractViewObjectController {

	private StatusBean statusBean;
	
	/**
	 * 
	 */
	public ViewStatusController() {
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.portlet.AbstractViewObjectController#getObject(javax.portlet.RenderRequest)
	 */
	@Override
	protected Object getObject(RenderRequest request) {
		return getStatusBean();
	}

	public StatusBean getStatusBean() {
		return statusBean;
	}

	public void setStatusBean(StatusBean statusBean) {
		this.statusBean = statusBean;
	}

}
