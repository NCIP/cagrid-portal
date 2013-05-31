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

import javax.portlet.PortletRequest;

import org.springframework.beans.factory.annotation.Required;

import gov.nih.nci.cagrid.portal.portlet.discovery.dir.AbstractDirectoryBean;
import gov.nih.nci.cagrid.portal.portlet.tab.AbstractInterPortletMessageSelectedPathHandler;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class SelectDiscoveryDirectoryPathHandler extends
		AbstractInterPortletMessageSelectedPathHandler {

	private DiscoveryModel discoveryModel;
	private String selectedTabPath;

	/**
	 * 
	 */
	public SelectDiscoveryDirectoryPathHandler() {

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
		String selectedPath = null;
		if (object != null && object instanceof AbstractDirectoryBean) {
			AbstractDirectoryBean dirBean = (AbstractDirectoryBean) object;
			getDiscoveryModel().selectDirectory(dirBean.getSelectedDirectory());
			selectedPath = getSelectedTabPath();
		}
		return selectedPath;
	}

	@Required
	public DiscoveryModel getDiscoveryModel() {
		return discoveryModel;
	}

	public void setDiscoveryModel(DiscoveryModel discoveryModel) {
		this.discoveryModel = discoveryModel;
	}

	@Required
	public String getSelectedTabPath() {
		return selectedTabPath;
	}

	public void setSelectedTabPath(String selectedTabPath) {
		this.selectedTabPath = selectedTabPath;
	}

}
