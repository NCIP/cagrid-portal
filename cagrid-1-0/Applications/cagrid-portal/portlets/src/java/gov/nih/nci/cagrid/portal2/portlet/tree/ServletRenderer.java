/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.tree;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ServletRenderer implements TreeNodeRenderer {

	private static final Log logger = LogFactory.getLog(ServletRenderer.class);
	
	private String servletUrl;

	private String requestAttribute;

	/**
	 * 
	 */
	public ServletRenderer() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see test.TreeNodeRenderer#render(test.TreeNode)
	 */
	public String render(TreeNode node) {
		
		logger.debug("Rendering " + node.getPath());

		String out = null;
		
		WebContext webContext = WebContextFactory.get();
		HttpServletRequest request = webContext.getHttpServletRequest();
		request.setAttribute(getRequestAttribute(), node);
		try {
			out =  webContext.forwardToString(getServletUrl());
		} catch (Exception ex) {
			throw new RuntimeException("Error rendering: " + ex.getMessage(), ex);
		}
		logger.debug("Returning HTML:\n" + out);
		return out;
	}

	public String getServletUrl() {
		return servletUrl;
	}

	public void setServletUrl(String servletUrl) {
		this.servletUrl = servletUrl;
	}

	public String getRequestAttribute() {
		return requestAttribute;
	}

	public void setRequestAttribute(String requestAttribute) {
		this.requestAttribute = requestAttribute;
	}

}
