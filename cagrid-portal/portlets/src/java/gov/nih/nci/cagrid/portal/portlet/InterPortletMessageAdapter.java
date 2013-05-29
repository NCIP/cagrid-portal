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
package gov.nih.nci.cagrid.portal.portlet;

import javax.portlet.PortletRequest;

import org.springframework.beans.factory.annotation.Required;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class InterPortletMessageAdapter implements InterPortletMessageSender,
		InterPortletMessageReceiver {

	private InterPortletMessageManager interPortletMessageManager;
	private String modePreferenceName;
	private String inputQueuePreferenceName;
	private String outputQueuePreferenceName;

	/**
	 * 
	 */
	public InterPortletMessageAdapter() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.portlet.InterPortletMessageSender#handles(javax.portlet.PortletRequest)
	 */
	public boolean handles(PortletRequest request) {
		return InterPortletMode.INTERPORTLET.toString().equals(
				request.getPreferences().getValue(getModePreferenceName(),
						InterPortletMode.STANDALONE.toString()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.portlet.InterPortletMessageSender#send(javax.portlet.PortletRequest,
	 *      java.lang.Object)
	 */
	public void send(PortletRequest request, Object object) {
		if (!handles(request)) {
			return;
		}
		String outputQueueName = request.getPreferences().getValue(
				getOutputQueuePreferenceName(), null);
		getInterPortletMessageManager().send(request, outputQueueName, object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.portlet.InterPortletReceiver#receive(javax.portlet.PortletRequest)
	 */
	public Object receive(PortletRequest request) {
		if (!handles(request)) {
			return null;
		}
		String inputQueueName = request.getPreferences().getValue(
				getInputQueuePreferenceName(), null);
		return getInterPortletMessageManager().receive(request, inputQueueName);

	}

	@Required
	public InterPortletMessageManager getInterPortletMessageManager() {
		return interPortletMessageManager;
	}

	public void setInterPortletMessageManager(
			InterPortletMessageManager interPortletMessageManager) {
		this.interPortletMessageManager = interPortletMessageManager;
	}

	@Required
	public String getModePreferenceName() {
		return modePreferenceName;
	}

	public void setModePreferenceName(String modePreferenceName) {
		this.modePreferenceName = modePreferenceName;
	}

	@Required
	public String getInputQueuePreferenceName() {
		return inputQueuePreferenceName;
	}

	public void setInputQueuePreferenceName(String inputQueuePreferenceName) {
		this.inputQueuePreferenceName = inputQueuePreferenceName;
	}

	@Required
	public String getOutputQueuePreferenceName() {
		return outputQueuePreferenceName;
	}

	public void setOutputQueuePreferenceName(String outputQueuePreferenceName) {
		this.outputQueuePreferenceName = outputQueuePreferenceName;
	}

}
