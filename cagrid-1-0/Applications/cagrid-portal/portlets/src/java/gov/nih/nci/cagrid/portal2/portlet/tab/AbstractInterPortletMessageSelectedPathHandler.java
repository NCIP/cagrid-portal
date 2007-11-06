/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.tab;

import gov.nih.nci.cagrid.portal2.portlet.InterPortletMessageManager;

import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public abstract class AbstractInterPortletMessageSelectedPathHandler implements
		SelectedPathHandler {

	private static final Log logger = LogFactory.getLog(AbstractInterPortletMessageSelectedPathHandler.class);
	
	private String modePreferenceName;
	private String inputQueuePreferenceName;
	private String inputQueueNameDefaultValue;
	private InterPortletMessageManager interPortletMessageManager;

	/**
	 * 
	 */
	public AbstractInterPortletMessageSelectedPathHandler() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal2.portlet.tab.SelectedPathHandler#getSelectedPath(javax.portlet.PortletRequest)
	 */
	public String getSelectedPath(PortletRequest request) {
		String selectedPath = null;
		if (handles(request)) {
			logger.debug("Handling request");
			String inputQueueName = request.getPreferences().getValue(
					getInputQueuePreferenceName(),
					getInputQueueNameDefaultValue());
			Object obj = getInterPortletMessageManager().receive(request,
					inputQueueName);
			selectedPath = getSelectedPathInternal(request, obj);
		}else{
			logger.debug("No handling request");
		}
		return selectedPath;
	}

	protected boolean handles(PortletRequest request) {
		return InterPortletMode.INTERPORTLET.toString().equals(
				request.getPreferences().getValue(getModePreferenceName(),
						InterPortletMode.STANDALONE.toString()));
	}

	protected abstract String getSelectedPathInternal(PortletRequest request,
			Object object);

	public static enum InterPortletMode {
		INTERPORTLET, STANDALONE;
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

	public InterPortletMessageManager getInterPortletMessageManager() {
		return interPortletMessageManager;
	}

	public void setInterPortletMessageManager(
			InterPortletMessageManager interPortletMessageManager) {
		this.interPortletMessageManager = interPortletMessageManager;
	}

}
