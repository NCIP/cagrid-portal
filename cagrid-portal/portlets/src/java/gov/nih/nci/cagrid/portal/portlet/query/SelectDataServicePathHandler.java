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
package gov.nih.nci.cagrid.portal.portlet.query;

import gov.nih.nci.cagrid.portal.dao.GridDataServiceDao;
import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.portlet.UserModel;
import gov.nih.nci.cagrid.portal.portlet.tab.AbstractInterPortletMessageSelectedPathHandler;

import javax.portlet.PortletRequest;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SelectDataServicePathHandler extends
		AbstractInterPortletMessageSelectedPathHandler {

	private String dataServiceSelectedTabPath;
	private UserModel userModel;
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
			getUserModel().setSelectedService(service);
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

	public GridDataServiceDao getGridDataServiceDao() {
		return gridDataServiceDao;
	}

	public void setGridDataServiceDao(GridDataServiceDao gridDataServiceDao) {
		this.gridDataServiceDao = gridDataServiceDao;
	}

	public UserModel getUserModel() {
		return userModel;
	}

	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}

}
