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
package gov.nih.nci.cagrid.portal.portlet.discovery;

import gov.nih.nci.cagrid.portal.portlet.tab.AbstractInterPortletMessageSelectedPathHandler;

import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class SelectDiscoveryResultsPathHandler extends
		AbstractInterPortletMessageSelectedPathHandler {

	private static final Log logger = LogFactory.getLog(SelectDiscoveryResultsPathHandler.class);
	
	private DiscoveryModel discoveryModel;
	private String selectedTabPath;

	/**
	 * 
	 */
	public SelectDiscoveryResultsPathHandler() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.portlet.tab.AbstractInterPortletMessageSelectedPathHandler#getSelectedPathInternal(javax.portlet.PortletRequest,
	 *      java.lang.Object)
	 */
	@Override
	protected String getSelectedPathInternal(PortletRequest request,
			Object object) {

		if(object == null){
			logger.debug("object is null");
		}else{
			logger.debug("object is " + object.getClass().getName());
		}
		
		String selectedPath = null;
		if (object != null && object instanceof DiscoveryResults) {
			DiscoveryResults results = (DiscoveryResults) object;
			logger.debug("results.id = " + results.getId());
			getDiscoveryModel().getResults().add(results);
			getDiscoveryModel().selectResults(results.getId());
			selectedPath = getSelectedTabPath();
		}
		return selectedPath;
	}

	public DiscoveryModel getDiscoveryModel() {
		return discoveryModel;
	}

	public void setDiscoveryModel(DiscoveryModel discoveryModel) {
		this.discoveryModel = discoveryModel;
	}

	public String getSelectedTabPath() {
		return selectedTabPath;
	}

	public void setSelectedTabPath(String selectedTabPath) {
		this.selectedTabPath = selectedTabPath;
	}

}
