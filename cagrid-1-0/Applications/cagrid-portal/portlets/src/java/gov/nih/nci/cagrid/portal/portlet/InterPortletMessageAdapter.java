/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet;

import javax.portlet.PortletRequest;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class InterPortletMessageAdapter implements InterPortletMessageSender,
		InterPortletMessageReceiver {

	private InterPortletMessageManager interPortletMessageManager;
	private String modePreferenceName;
	private String inputQueuePreferenceName;
	private String inputQueueNameDefaultValue;
	private String outputQueuePreferenceName;
	private String outputQueueNameDefaultValue;

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
				getOutputQueuePreferenceName(),
				getOutputQueueNameDefaultValue());
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
				getInputQueuePreferenceName(), getInputQueueNameDefaultValue());
		return getInterPortletMessageManager().receive(request, inputQueueName);

	}

	public InterPortletMessageManager getInterPortletMessageManager() {
		return interPortletMessageManager;
	}

	public void setInterPortletMessageManager(
			InterPortletMessageManager interPortletMessageManager) {
		this.interPortletMessageManager = interPortletMessageManager;
	}

	public String getModePreferenceName() {
		return modePreferenceName;
	}

	public void setModePreferenceName(String modePreferenceName) {
		this.modePreferenceName = modePreferenceName;
	}

	public String getInputQueuePreferenceName() {
		return inputQueuePreferenceName;
	}

	public void setInputQueuePreferenceName(String inputQueuePreferenceName) {
		this.inputQueuePreferenceName = inputQueuePreferenceName;
	}

	public String getInputQueueNameDefaultValue() {
		return inputQueueNameDefaultValue;
	}

	public void setInputQueueNameDefaultValue(String inputQueueNameDefaultValue) {
		this.inputQueueNameDefaultValue = inputQueueNameDefaultValue;
	}

	public String getOutputQueuePreferenceName() {
		return outputQueuePreferenceName;
	}

	public void setOutputQueuePreferenceName(String outputQueuePreferenceName) {
		this.outputQueuePreferenceName = outputQueuePreferenceName;
	}

	public String getOutputQueueNameDefaultValue() {
		return outputQueueNameDefaultValue;
	}

	public void setOutputQueueNameDefaultValue(
			String outputQueueNameDefaultValue) {
		this.outputQueueNameDefaultValue = outputQueueNameDefaultValue;
	}

}
