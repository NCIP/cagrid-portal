/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.map;

import gov.nih.nci.cagrid.portal2.domain.Address;
import gov.nih.nci.cagrid.portal2.domain.GridService;
import gov.nih.nci.cagrid.portal2.domain.Participant;
import gov.nih.nci.cagrid.portal2.domain.metadata.common.ResearchCenter;

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

	private Map<String, ServiceMapNode> svcNodes = new HashMap<String, ServiceMapNode>();

	private Map<String, ParticipantMapNode> pNodes = new HashMap<String, ParticipantMapNode>();

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

	public List<ParticipantMapNode> getParticipantNodes() {
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
		if (key == null) {
			logger.warn("Got null key for service " + service.getUrl() + ". Not adding to map.");
		} else {
			ServiceMapNode node = this.svcNodes.get(key);
			if (node == null) {
				node = new ServiceMapNode();
				String[] latLon = key.split(":");
				node.setLatitude(latLon[0]);
				node.setLongitude(latLon[1]);
				svcNodes.put(key, node);
			}
			ServiceInfo info = new ServiceInfo(service);
			node.getServiceInfos().add(info);
		}
	}

	public void addParticipant(Participant participant) {
		String key = getGeoKey(participant);
		if (key == null) {
			logger
					.warn("Got null key for participant "
							+ participant.getName() + ". Not adding to map.");
		} else {
			ParticipantMapNode node = this.pNodes.get(key);
			if (node == null) {
				node = new ParticipantMapNode();
				String[] latLon = key.split(":");
				node.setLatitude(latLon[0]);
				node.setLongitude(latLon[1]);
				pNodes.put(key, node);
			}
			node.getParticipants().add(participant);
		}
	}

	private String getGeoKey(GridService service) {
		String key = null;
		ResearchCenter rc = service.getServiceMetadata()
				.getHostingResearchCenter();
		if (rc == null) {
			logger.warn("Service " + service.getUrl()
					+ " has no hostingResearchCenter");
		} else {
			key = getGeoKey(rc.getAddress());
		}
		return key;
	}

	private String getGeoKey(Participant participant) {
		return getGeoKey(participant.getAddress());
	}

	private String getGeoKey(Address addr) {
		String key = null;
		if (addr != null) {
			
			Float la = addr.getLatitude();
			Float lo = addr.getLongitude();
			
			if(la == null){
				logger.warn("latitude is null");
			}
			if(lo == null){
				logger.warn("longitude is null");
			}

			if (la != null && lo != null) {
				key = la.toString() + ":" + lo.toString();
			}
		}
		return key;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
