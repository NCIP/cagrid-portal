/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query;

import javax.portlet.PortletRequest;

import gov.nih.nci.cagrid.portal.portlet.tab.AbstractInterPortletMessageSelectedPathHandler;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SelectUmlClassPathHandler extends
		AbstractInterPortletMessageSelectedPathHandler {

	private String umlClassSelectedTabPath;
	private QueryModel queryModel;
	
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
			getQueryModel().selectUmlClassForQuery((Integer)object);
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

	public QueryModel getQueryModel() {
		return queryModel;
	}

	public void setQueryModel(QueryModel queryModel) {
		this.queryModel = queryModel;
	}

}
