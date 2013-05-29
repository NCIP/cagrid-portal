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
package gov.nih.nci.cagrid.portal.portlet.query;

import gov.nih.nci.cagrid.portal.portlet.UserModel;
import gov.nih.nci.cagrid.portal.portlet.tab.AbstractInterPortletMessageSelectedPathHandler;

import javax.portlet.PortletRequest;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SelectUmlClassPathHandler extends
		AbstractInterPortletMessageSelectedPathHandler {

	private String umlClassSelectedTabPath;
	private UserModel userModel;
	
	/**
	 * 
	 */
	public SelectUmlClassPathHandler() {

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.portlet.tab.AbstractInterPortletMessageSelectedPathHandler#getSelectedPathInternal(javax.portlet.PortletRequest, java.lang.Object)
	 */
	@Override
	protected String getSelectedPathInternal(PortletRequest request,
			Object object) {
		String selectedPath = null;
		if(object != null && object instanceof Integer){
			getUserModel().selectUmlClassForQuery((Integer)object);
			selectedPath = getUmlClassSelectedTabPath();
		}
		return selectedPath;
	}

	public String getUmlClassSelectedTabPath() {
		return umlClassSelectedTabPath;
	}

	public void setUmlClassSelectedTabPath(String umlClassSelectedTabPath) {
		this.umlClassSelectedTabPath = umlClassSelectedTabPath;
	}


	public UserModel getUserModel() {
		return userModel;
	}

	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}

}
