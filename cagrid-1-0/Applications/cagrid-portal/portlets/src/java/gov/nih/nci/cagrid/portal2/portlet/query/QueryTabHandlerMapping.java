/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query;

import gov.nih.nci.cagrid.portal2.portlet.InterPortletMessageManager;
import gov.nih.nci.cagrid.portal2.portlet.tab.TabHandlerMapping;

import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class QueryTabHandlerMapping extends TabHandlerMapping {

	private static final Log logger = LogFactory.getLog(QueryTabHandlerMapping.class);
	
	private InterPortletMessageManager interPortletMessageManager;
	private String umlClassSelectedTabPath;
	private QueryModel queryModel;
	
	
	/**
	 * 
	 */
	public QueryTabHandlerMapping() {

	}
	
	protected String getSelectedPath(PortletRequest request) {
		String selectedPath = super.getSelectedPath(request);
	
		String mode = request.getPreferences().getValue("umlClassSelectMode", "STANDALONE");
		logger.debug("mode: " + mode);
		if("INTERPORTLET".equals(mode)){
			
			String inputQueueName = request.getPreferences().getValue("selectedUmlClassInputQueueName", "selectedUmlClassId");
			Integer id = (Integer)getInterPortletMessageManager().receive(request, inputQueueName);
			logger.debug("ID: " + id);
			if(id != null){
				getQueryModel().selectUmlClassForQuery(id);
				selectedPath = getUmlClassSelectedTabPath();
			}
		}
		logger.debug("Returning selected path: " + selectedPath);
		return selectedPath;
	}

	public InterPortletMessageManager getInterPortletMessageManager() {
		return interPortletMessageManager;
	}

	public void setInterPortletMessageManager(
			InterPortletMessageManager interPortletMessageManager) {
		this.interPortletMessageManager = interPortletMessageManager;
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
