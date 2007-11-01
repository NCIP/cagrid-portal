/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query.model;

import gov.nih.nci.cagrid.portal2.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal2.domain.GridDataService;
import gov.nih.nci.cagrid.portal2.portlet.AbstractViewObjectController;
import gov.nih.nci.cagrid.portal2.portlet.query.QueryModel;

import javax.portlet.RenderRequest;

import org.springframework.web.portlet.ModelAndView;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ViewUmlClassListController extends AbstractViewObjectController {

	private GridServiceDao gridServiceDao;
	private QueryModel queryModel;

	/**
	 * 
	 */
	public ViewUmlClassListController() {

	}

	@Override
	protected Object getObject(RenderRequest request) {
		SelectServiceCommand command = new SelectServiceCommand();
		if (getQueryModel().getSelectedService() != null) {
			command
					.setServiceUrl(getQueryModel().getSelectedService()
							.getUrl());
		}
		return command;
	}

	@Override
	protected void addData(RenderRequest request, ModelAndView mav) {
		if (getQueryModel().getSelectedService() != null) {
			//Association with current session.
			GridDataService selectedService = (GridDataService) getGridServiceDao()
					.getById(getQueryModel().getSelectedService().getId());
			mav.addObject("umlClasses", selectedService.getDomainModel()
					.getClasses());
		}
	}

	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}

	public QueryModel getQueryModel() {
		return queryModel;
	}

	public void setQueryModel(QueryModel queryModel) {
		this.queryModel = queryModel;
	}

}
