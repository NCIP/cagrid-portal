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
package gov.nih.nci.cagrid.portal.portlet.discovery;

import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.Participant;
import gov.nih.nci.cagrid.portal.domain.Person;
import gov.nih.nci.cagrid.portal.portlet.tab.AbstractInterPortletMessageSelectedPathHandler;

import javax.portlet.PortletRequest;

import org.springframework.beans.factory.annotation.Required;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class SelectDiscoveryItemPathHandler extends
		AbstractInterPortletMessageSelectedPathHandler {

	private DiscoveryModel discoveryModel;
	private String serviceSelectedTabPath;
	private String participantSelectedTabPath;
	private String pocSelectedTabPath;
	
	/**
	 * 
	 */
	public SelectDiscoveryItemPathHandler() {

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.portlet.tab.AbstractInterPortletMessageSelectedPathHandler#getSelectedPathInternal(javax.portlet.PortletRequest, java.lang.Object)
	 */
	@Override
	protected String getSelectedPathInternal(PortletRequest request,
			Object obj) {

		String selectedPath = null;
		
		if(obj instanceof GridService){
			getDiscoveryModel().setSelectedService((GridService)obj);
			selectedPath = getServiceSelectedTabPath();
		}else if(obj instanceof Participant){
			getDiscoveryModel().setSelectedParticipant((Participant)obj);
			selectedPath = getParticipantSelectedTabPath();
		}else if(obj instanceof Person){
			getDiscoveryModel().setSelectedPointOfContact((Person)obj);
			selectedPath = getPocSelectedTabPath();
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
