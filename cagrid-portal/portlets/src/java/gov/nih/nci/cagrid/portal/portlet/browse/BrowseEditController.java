/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.browse;

import gov.nih.nci.cagrid.portal.dao.catalog.CatalogEntryRelationshipTypeDao;
import gov.nih.nci.cagrid.portal.portlet.AbstractViewObjectController;

import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.portlet.ModelAndView;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 *
 */
public class BrowseEditController extends AbstractViewObjectController {
	
	private static final Log logger = LogFactory.getLog(BrowseEditController.class);
	
	private CatalogEntryRelationshipTypeDao catalogEntryRelationshipTypeDao;

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.portlet.AbstractViewObjectController#getObject(javax.portlet.RenderRequest)
	 */
	@Override
	protected Object getObject(RenderRequest request) {
		BrowseCommand command = new BrowseCommand();
		PortletPreferences prefs = request.getPreferences();
		command.setBrowseType(prefs.getValue("browseType", "DATASET"));
		return command;
	}
	
	protected void addData(RenderRequest request, ModelAndView mav){
		mav.addObject("relationshipTypes", getCatalogEntryRelationshipTypeDao().getAll());
	}
	

	public CatalogEntryRelationshipTypeDao getCatalogEntryRelationshipTypeDao() {
		return catalogEntryRelationshipTypeDao;
	}

	public void setCatalogEntryRelationshipTypeDao(
			CatalogEntryRelationshipTypeDao catalogEntryRelationshipTypeDao) {
		this.catalogEntryRelationshipTypeDao = catalogEntryRelationshipTypeDao;
	}

}
