/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.map;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ParticipantMapNode {

	private String latitude;
	private String longitude;
	private List<ParticipantInfo> participantInfos = new ArrayList<ParticipantInfo>();
	
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

	public List<ParticipantInfo> getParticipantInfos() {
		return participantInfos;
	}

	public void setParticipantInfos(List<ParticipantInfo> participantInfos) {
		this.participantInfos = participantInfos;
	}

}
