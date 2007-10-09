/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.map;

import gov.nih.nci.cagrid.portal2.domain.Participant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ParticipantMapNode {

	private String latitude;
	private String longitude;
	private List<Participant> participants = new ArrayList<Participant>();
	
	/**
	 * 
	 */
	public ParticipantMapNode() {

	}
	
	

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public List<Participant> getParticipants() {
		return participants;
	}

	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}

}
