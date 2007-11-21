/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query;

import javax.portlet.PortletRequest;

import gov.nih.nci.cagrid.portal.dao.GridDataServiceDao;
import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.portlet.tab.AbstractInterPortletMessageSelectedPathHandler;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SelectDataServicePathHandler extends
		AbstractInterPortletMessageSelectedPathHandler {

	private String dataServiceSelectedTabPath;
	private QueryModel queryModel;
	private GridDataServiceDao gridDataServiceDao;
	
	/**
	 * 
	 */
	public SelectDataServicePathHandler() {

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.portlet.tab.AbstractInterPortletMessageSelectedPathHandler#getSelectedPathInternal(javax.portlet.PortletRequest, java.lang.Object)
	 */
	@Override
	protected String getSelectedPathInternal(PortletRequest request,
			Object object) {
		String selectedPath = null;
		if(object != null && object instanceof Integer){
			GridDataService service = getGridDataServiceDao().getById((Integer)object);
			getQueryModel().setSelectedService(service);
			selectedPath = getDataServiceSelectedTabPath();
		}
		return selectedPath;
	}

	public String getDataServiceSelectedTabPath() {
		return dataServiceSelectedTabPath;
	}

	public void setDataServiceSelectedTabPath(String umlClassSelectedTabPath) {
		this.dataServiceSelectedTabPath = umlClassSelectedTabPath;
	}

	public QueryModel getQueryModel() {
		return queryModel;
	}

	public void setQueryModel(QueryModel queryModel) {
		this.queryModel = queryModel;
	}

	public GridDataServiceDao getGridDataServiceDao() {
		return gridDataServiceDao;
	}

	public void setGridDataServiceDao(GridDataServiceDao gridDataServiceDao) {
		this.gridDataServiceDao = gridDataServiceDao;
	}

}
