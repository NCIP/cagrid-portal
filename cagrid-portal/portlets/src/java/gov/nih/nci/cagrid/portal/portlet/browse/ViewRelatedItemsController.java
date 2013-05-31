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
package gov.nih.nci.cagrid.portal.portlet.browse;

import gov.nih.nci.cagrid.portal.dao.catalog.CatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.Controller;

/**
 * @author joshua
 * 
 */
public class ViewRelatedItemsController implements Controller {

	private CatalogEntryDao catalogEntryDao;
	private String viewName;
	private CatalogEntryViewBeanFactory catalogEntryViewBeanFactory;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.portlet.mvc.Controller#handleActionRequest(javax
	 * .portlet.ActionRequest, javax.portlet.ActionResponse)
	 */
	public void handleActionRequest(ActionRequest arg0, ActionResponse arg1)
			throws Exception {
		throw new Exception(
				"This controller should not receive action requests.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.portlet.mvc.Controller#handleRenderRequest(javax
	 * .portlet.RenderRequest, javax.portlet.RenderResponse)
	 */
	public ModelAndView handleRenderRequest(RenderRequest request,
			RenderResponse response) throws Exception {
		ModelAndView mav = new ModelAndView(getViewName());

		Integer entryId = Integer.valueOf(request.getParameter("entryId"));
		CatalogEntry entry = getCatalogEntryDao().getById(entryId);
		mav.addObject("catalogEntryViewBean", getCatalogEntryViewBeanFactory()
				.newCatalogEntryViewBean(entry));
		return mav;
	}

	public CatalogEntryDao getCatalogEntryDao() {
		return catalogEntryDao;
	}

	public void setCatalogEntryDao(CatalogEntryDao catalogEntryDao) {
		this.catalogEntryDao = catalogEntryDao;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public CatalogEntryViewBeanFactory getCatalogEntryViewBeanFactory() {
		return catalogEntryViewBeanFactory;
	}

	public void setCatalogEntryViewBeanFactory(
			CatalogEntryViewBeanFactory catalogEntryViewBeanFactory) {
		this.catalogEntryViewBeanFactory = catalogEntryViewBeanFactory;
	}

}
