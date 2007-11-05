/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.discovery;

import gov.nih.nci.cagrid.portal2.domain.GridService;
import gov.nih.nci.cagrid.portal2.domain.Participant;
import gov.nih.nci.cagrid.portal2.domain.metadata.common.PointOfContact;
import gov.nih.nci.cagrid.portal2.portlet.InterPortletMessageManager;
import gov.nih.nci.cagrid.portal2.portlet.tab.TabHandlerMapping;

import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class DiscoveryTabHandlerMapping extends TabHandlerMapping {
	
	private static final Log logger = LogFactory.getLog(DiscoveryTabHandlerMapping.class);
	
	private InterPortletMessageManager interPortletMessageManager;
	private DiscoveryModel discoveryModel;
	private String serviceSelectedTabPath;
	private String participantSelectedTabPath;
	private String pocSelectedTabPath;

	/**
	 * 
	 */
	public DiscoveryTabHandlerMapping() {

	}
	
	protected String getSelectedPath(PortletRequest request) {
		String selectedPath = super.getSelectedPath(request);
		
		String mode = request.getPreferences().getValue("discoveryItemSelectMode", "STANDALONE");
		logger.debug("mode: " + mode);
		if("INTERPORTLET".equals(mode)){
		
			String inputQueueName = request.getPreferences().getValue("selectedDiscoveryItemInputQueueName", "selectedDiscoveryItem");
			Object obj = getInterPortletMessageManager().receive(request, inputQueueName);
			if(obj != null){
				if(obj instanceof GridService){
					getDiscoveryModel().setSelectedService((GridService)obj);
					selectedPath = getServiceSelectedTabPath();
				}else if(obj instanceof Participant){
					getDiscoveryModel().setSelectedParticipant((Participant)obj);
					selectedPath = getParticipantSelectedTabPath();
				}else if(obj instanceof PointOfContact){
					getDiscoveryModel().setSelectedPointOfContact((PointOfContact)obj);
					selectedPath = getPocSelectedTabPath();
				}else{
					throw new IllegalArgumentException("Unknown object type: "+ obj.getClass().getName());
				}
			}
		}
		
		return selectedPath;
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
	public DiscoveryModel getDiscoveryModel() {
		return discoveryModel;
	}

	public void setDiscoveryModel(DiscoveryModel discoveryModel) {
		this.discoveryModel = discoveryModel;
	}

	@Required
	public String getServiceSelectedTabPath() {
		return serviceSelectedTabPath;
	}

	public void setServiceSelectedTabPath(String serviceSelectedTabPath) {
		this.serviceSelectedTabPath = serviceSelectedTabPath;
	}

	@Required
	public String getParticipantSelectedTabPath() {
		return participantSelectedTabPath;
	}

	public void setParticipantSelectedTabPath(String participantSelectedTabPath) {
		this.participantSelectedTabPath = participantSelectedTabPath;
	}

	@Required
	public String getPocSelectedTabPath() {
		return pocSelectedTabPath;
	}

	public void setPocSelectedTabPath(String pocSelectedTabPath) {
		this.pocSelectedTabPath = pocSelectedTabPath;
	}

}
