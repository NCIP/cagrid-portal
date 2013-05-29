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
package gov.nih.nci.cagrid.portal.portlet.browse;

import gov.nih.nci.cagrid.portal.dao.catalog.CatalogEntryRelationshipTypeDao;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRelationshipType;
import gov.nih.nci.cagrid.portal.portlet.UserModel;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 * 
 */
public class ViewRelationshipTypeController extends AbstractController {

	private static final Log logger = LogFactory
			.getLog(ViewRelationshipTypeController.class);
	private CatalogEntryRelationshipTypeDao catalogEntryRelationshipTypeDao;
	private String objectName;
	private String viewName;
	private UserModel userModel;

	protected ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {

		ModelAndView mav = null;
		CatalogEntryRelationshipType relType = null;

		Integer relTypeId = null;
		try {
			relTypeId = Integer.valueOf(request.getParameter("relTypeId"));
		} catch (Exception ex) {

		}
		if (relTypeId != null) {
			relType = getCatalogEntryRelationshipTypeDao().getById(relTypeId);
		} else {
			relType = getUserModel().getCurrentRelationshipType();
		}

		if (relType == null) {
			throw new RuntimeException("No relationship type found.");
		}
		if (relType.getId() != null) {
			relType = getCatalogEntryRelationshipTypeDao().getById(
					relType.getId());
		}

		mav = new ModelAndView(getViewName());
		mav.addObject(getObjectName(), relType);
		if (request.getParameter("viewMode") != null) {
			mav.addObject("viewMode", request.getParameter("viewMode"));
		}

		return mav;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public UserModel getUserModel() {
		return userModel;
	}

	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}

	public CatalogEntryRelationshipTypeDao getCatalogEntryRelationshipTypeDao() {
		return catalogEntryRelationshipTypeDao;
	}

	public void setCatalogEntryRelationshipTypeDao(
			CatalogEntryRelationshipTypeDao catalogEntryRelationshipTypeDao) {
		this.catalogEntryRelationshipTypeDao = catalogEntryRelationshipTypeDao;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

}
