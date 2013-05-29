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
package gov.nih.nci.cagrid.portal.portlet.tab;

import gov.nih.nci.cagrid.portal.portlet.InterPortletMessageReceiver;

import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public abstract class AbstractInterPortletMessageSelectedPathHandler implements
		SelectedPathHandler {

	private static final Log logger = LogFactory
			.getLog(AbstractInterPortletMessageSelectedPathHandler.class);

	private InterPortletMessageReceiver interPortletMessageReceiver;

	/**
	 * 
	 */
	public AbstractInterPortletMessageSelectedPathHandler() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.portlet.tab.SelectedPathHandler#getSelectedPath(javax.portlet.PortletRequest)
	 */
	public String getSelectedPath(PortletRequest request) {
		String selectedPath = null;
		if (getInterPortletMessageReceiver().handles(request)) {
			Object obj = getInterPortletMessageReceiver().receive(request);
			selectedPath = getSelectedPathInternal(request, obj);
		}
		return selectedPath;
	}

	protected abstract String getSelectedPathInternal(PortletRequest request,
			Object object);

	@Required
	public InterPortletMessageReceiver getInterPortletMessageReceiver() {
		return interPortletMessageReceiver;
	}

	public void setInterPortletMessageReceiver(
			InterPortletMessageReceiver interPortletMessageReceiver) {
		this.interPortletMessageReceiver = interPortletMessageReceiver;
	}

}
