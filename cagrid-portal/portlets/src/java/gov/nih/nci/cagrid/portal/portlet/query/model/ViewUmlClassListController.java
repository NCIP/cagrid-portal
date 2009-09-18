/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.model;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.portlet.AbstractViewObjectController;
import gov.nih.nci.cagrid.portal.portlet.query.QueryModel;

import java.util.SortedMap;
import java.util.TreeMap;

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
			String url = getQueryModel().getSelectedService()
			.getUrl();
			command
					.setDataServiceUrl(url);
			logger.debug("selected service " + url);
		}else{
			logger.debug("no service selected");
		}
		return command;
	}

	@Override
	protected void addData(RenderRequest request, ModelAndView mav) {
		if (getQueryModel().getSelectedService() != null) {
			//Association with current session.
			GridDataService selectedService = (GridDataService) getGridServiceDao()
					.getById(getQueryModel().getSelectedService().getId());
			
			//Sort by packageName.className
			SortedMap<String,UMLClass> sorted = new TreeMap<String,UMLClass>();
			for(UMLClass klass : selectedService.getDomainModel().getClasses()){
				sorted.put(klass.getPackageName() + "." + klass.getClassName(), klass);
			}
			mav.addObject("umlClasses", sorted.values());
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
