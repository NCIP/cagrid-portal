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
package gov.nih.nci.cagrid.portal.portlet.tree;

import java.util.Map;
import java.util.Map.Entry;

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

	public String render(TreeNode node, Map<String, Object> params) {
		
//		logger.debug("Rendering " + node.getPath());

		String out = null;
		
		WebContext webContext = WebContextFactory.get();
		HttpServletRequest request = webContext.getHttpServletRequest();
		request.setAttribute(getRequestAttribute(), node);
		for(Entry<String,Object> entry : params.entrySet()){
			request.setAttribute(entry.getKey(), entry.getValue());
		}
		try {
			out =  webContext.forwardToString(getServletUrl());
		} catch (Exception ex) {
			throw new RuntimeException("Error rendering: " + ex.getMessage(), ex);
		}
//		logger.debug("Returning HTML:\n" + out);
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
