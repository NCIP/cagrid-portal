/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.map;

import gov.nih.nci.cagrid.portal2.domain.Address;
import gov.nih.nci.cagrid.portal2.domain.GridService;
import gov.nih.nci.cagrid.portal2.domain.Participant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class MapBean {

	private String category;
	private static final Log logger = LogFactory.getLog(MapBean.class);
	private String baseUrl;
	private String apiKey;
	private double centerLatitude;
	private double centerLongitude;
	private int zoomLevel;
	private Map<String,ServiceMapNode> svcNodes = new HashMap<String,ServiceMapNode>();
	private Map<String,ParticipantMapNode> pNodes = new HashMap<String,ParticipantMapNode>();
	
	/**
	 * 
	 */
	public MapBean() {

	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public double getCenterLatitude() {
		return centerLatitude;
	}

	public void setCenterLatitude(double centerLatitude) {
		this.centerLatitude = centerLatitude;
	}

	public double getCenterLongitude() {
		return centerLongitude;
	}

	public void setCenterLongitude(double centerLongitude) {
		this.centerLongitude = centerLongitude;
	}

	public List<ServiceMapNode> getServiceNodes() {
		List<ServiceMapNode> serviceNodes = new ArrayList<ServiceMapNode>();
		serviceNodes.addAll(svcNodes.values());
		return serviceNodes;
	}
	
	public List<ParticipantMapNode> getParticipantNodes(){
		List<ParticipantMapNode> participantNodes = new ArrayList<ParticipantMapNode>();
		participantNodes.addAll(pNodes.values());
		return participantNodes;
	}

	public int getZoomLevel() {
		return zoomLevel;
	}

	public void setZoomLevel(int zoomLevel) {
		this.zoomLevel = zoomLevel;
	}

	public void addService(GridService service) {
		String key = getGeoKey(service);
		ServiceMapNode node = this.svcNodes.get(key);
		if(node == null){
			node = new ServiceMapNode();
			String[] latLon = key.split(":");
			node.setLatitude(latLon[0]);
			node.setLongitude(latLon[1]);
			svcNodes.put(key, node);
		}
		ServiceInfo info = new ServiceInfo(service);
//		logger.debug("Adding " + info.getName() + " to " + key);
		node.getServiceInfos().add(info);
	}
	
	public void addParticipant(Participant participant){
		String key = getGeoKey(participant);
		ParticipantMapNode node = this.pNodes.get(key);
		if(node == null){
			node = new ParticipantMapNode();
			String[] latLon = key.split(":");
			node.setLatitude(latLon[0]);
			node.setLongitude(latLon[1]);
			pNodes.put(key, node);
		}
		ParticipantInfo info = new ParticipantInfo(participant);
//		logger.debug("Adding participant " + info.getName() + " to " + key);
		node.getParticipantInfos().add(info);
	}

	private String getGeoKey(GridService service){
		return getGeoKey(service.getServiceMetadata().getHostingResearchCenter().getAddress());
	}
	private String getGeoKey(Participant participant){
		return getGeoKey(participant.getAddress());
	}
	private String getGeoKey(Address addr){
		String lat = "0";
		String lon = "0";
		if(addr != null){
			Float la = addr.getLatitude();
			if(la != null){
				lat = la.toString();
			}
			Float lo = addr.getLongitude();
			if(lo != null){
				lon = lo.toString();
			}
		}
		return lat + ":" + lon;		
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
