/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.tab;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.portlet.handler.AbstractHandlerMapping;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class TabHandlerMapping extends AbstractHandlerMapping {

	private static final Log logger = LogFactory.getLog(TabHandlerMapping.class);
	
	private TabControlConfig tabControlConfig;
	private Map tabPathMappings = new HashMap();
	
	/**
	 * 
	 */
	public TabHandlerMapping() {

	}

	/* (non-Javadoc)
	 * @see org.springframework.web.portlet.handler.AbstractHandlerMapping#getHandlerInternal(javax.portlet.PortletRequest)
	 */
	@Override
	protected Object getHandlerInternal(PortletRequest request) throws Exception {
		
		logger.debug("operation=" + request.getParameter("operation"));
		
		Object handler = null;
		if(request instanceof ActionRequest){
			logger.debug("Ignoring action request");
			return null;
		}
		
		String selectedPath = request.getParameter(getTabControlConfig().getSelectedPathParameterName());
		if(selectedPath == null){
			logger.debug("No selectedPath found under " + getTabControlConfig().getSelectedPathParameterName() + ". Ignoring request.");
			return null;
		}
		
		logger.debug("selecting path '" + selectedPath + "'");
		getTabControlConfig().getTabModel().select(selectedPath);
		request.setAttribute(getTabControlConfig().getTabModelRequestAttributeName(), getTabControlConfig().getTabModel());
		String path = getTabControlConfig().getTabModel().getCurrentPath();

		logger.debug("looking for handler for path '" + path + "'");
		handler = getTabPathMappings().get(path);
		if(handler == null){
			logger.debug("no handler found");
		}else{
			logger.debug("found handler " + handler);
		}
		return handler;
	}

	@Required
	public Map getTabPathMappings() {
		return tabPathMappings;
	}

	public void setTabPathMappings(Map tabPathMappings) {
		this.tabPathMappings = tabPathMappings;
	}
	public TabControlConfig getTabControlConfig() {
		return tabControlConfig;
	}

	public void setTabControlConfig(TabControlConfig tabControlConfig) {
		this.tabControlConfig = tabControlConfig;
	}

}
