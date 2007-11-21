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
