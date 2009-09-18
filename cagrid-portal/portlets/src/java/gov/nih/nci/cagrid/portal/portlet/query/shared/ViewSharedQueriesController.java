/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.shared;

import javax.portlet.RenderRequest;

import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.portlet.query.AbstractQueryRenderController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ViewSharedQueriesController extends AbstractQueryRenderController {

	private PortalUserDao portalUserDao;
	
	/**
	 * 
	 */
	public ViewSharedQueriesController() {

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.portlet.AbstractViewObjectController#getObject(javax.portlet.RenderRequest)
	 */
	@Override
	protected Object getObject(RenderRequest request) {
		PortalUser portalUser = getPortalUserDao().getById(getQueryModel().getPortalUser().getId());
		return portalUser.getSharedQueries();
	}

	public PortalUserDao getPortalUserDao() {
		return portalUserDao;
	}

	public void setPortalUserDao(PortalUserDao portalUserDao) {
		this.portalUserDao = portalUserDao;
	}

}
