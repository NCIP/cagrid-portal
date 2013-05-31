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
package gov.nih.nci.cagrid.portal.portlet.browse;

import java.util.Map;

import javax.portlet.PortletMode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.liferay.portal.kernel.portlet.BaseFriendlyURLMapper;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;


/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 *
 */
public class BrowseFriendlyURLMapper extends BaseFriendlyURLMapper {
	
	private static final Log logger = LogFactory.getLog(BrowseFriendlyURLMapper.class);
	
	private static final String PORTLETID = "BrowsePortlet";
	private static final String MAPPING = "browse_catalog";

	/* (non-Javadoc)
	 * @see com.liferay.portal.kernel.portlet.BaseFriendlyURLMapper#getPortletId()
	 */
	@Override
	public String getPortletId() {
		return PORTLETID;
	}

	/* (non-Javadoc)
	 * @see com.liferay.portal.kernel.portlet.FriendlyURLMapper#buildPath(com.liferay.portal.kernel.portlet.LiferayPortletURL)
	 */
	public String buildPath(LiferayPortletURL portletURL) {
		String path = null;
//		String path = "/" + MAPPING + "/" + portletURL.getParameter("entryId"); 
//		logger.debug("returning friendly path: " + path);
		return path;
	}

	/* (non-Javadoc)
	 * @see com.liferay.portal.kernel.portlet.FriendlyURLMapper#getMapping()
	 */
	public String getMapping() {
		return MAPPING;
	}

	/* (non-Javadoc)
	 * @see com.liferay.portal.kernel.portlet.FriendlyURLMapper#populateParams(java.lang.String, java.util.Map)
	 */
	public void populateParams(String friendlyURLPath, Map<String, String[]> params) {
		
		logger.debug("friendlyURLPath: " + friendlyURLPath);
		
		addParam(params, "p_p_id", PORTLETID);
		addParam(params, "p_p_lifecycle", "0");
		addParam(params, "p_p_mode", PortletMode.VIEW);
		
		int x = friendlyURLPath.indexOf("/", 1);
		if(x == -1){
			logger.debug("no id provided 1");
			addParam(params, "operation", "view");
			return;
		}
		
		if((x + 1) == friendlyURLPath.length()){
			logger.debug("no id provided 2");
			addParam(params, "operation", "view");
			return;
		}
		
		String entryId = null;
		int y = friendlyURLPath.indexOf("/", x + 1);
		if(y != -1){
			entryId = friendlyURLPath.substring(x + 1, y); 
		}else{
			entryId = friendlyURLPath.substring(x + 1);
		}
		
		addParam(params, "operation", "viewDetails");
		addParam(params, "entryId", entryId);
		
		logger.debug("Params: " + params);
		
	}
}
