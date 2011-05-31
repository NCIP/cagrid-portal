/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.discovery.map;

import gov.nih.nci.cagrid.portal.domain.Address;
import gov.nih.nci.cagrid.portal.domain.DomainObject;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.Participant;
import gov.nih.nci.cagrid.portal.domain.Person;
import gov.nih.nci.cagrid.portal.domain.metadata.common.ResearchCenter;
import gov.nih.nci.cagrid.portal.portlet.CaGridPortletApplicationException;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryResults;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.AbstractDirectoryBean;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.ParticipantDirectory;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.PointOfContactDirectory;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.ServiceDirectory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
 *
 */
@Transactional
public class MapBean extends AbstractDirectoryBean {


	private static final Log logger = LogFactory.getLog(MapBean.class);

	private String baseUrl;

	private String apiKey;

	private double centerLatitude;

	private double centerLongitude;

	private int zoomLevel;

	private Map<String, ServiceMapNode> svcNodes = new HashMap<String, ServiceMapNode>();

	private Map<String, ParticipantMapNode> pNodes = new HashMap<String, ParticipantMapNode>();
	
	private Map<String, PointOfContactMapNode> pocNodes = new HashMap<String, PointOfContactMapNode>();
	
	private HibernateTemplate hibernateTemplate;

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

    public List<PointOfContactMapNode> getPointOfContactNodes() {
            List<PointOfContactMapNode> pointOfContactNodes = new ArrayList<PointOfContactMapNode>();
            pointOfContactNodes.addAll(pocNodes.values());
            return pointOfContactNodes;
        }


	public int getZoomLevel() {
		return zoomLevel;
	}

	public void setZoomLevel(int zoomLevel) {
		this.zoomLevel = zoomLevel;
	}
	
	public void addServices(ServiceDirectory dir){
		List<GridService> services = dir.getObjects();
		for(GridService service: services){
			addService(service);
		}
	}

	public void addService(GridService service) {
		String key = getGeoKey(service);
		if (key == null) {
			logger.info("Got null key for service " + service.getUrl() + ". Not adding to map.");
		} else {
			ServiceMapNode node = this.svcNodes.get(key);
			if (node == null) {
				node = new ServiceMapNode();
				putNode(svcNodes, node, key);
			}
			if(service.getId() == null){
				throw new CaGridPortletApplicationException("service '" + service.getUrl() + " has no id");
			}
			node.getServiceInfos().add(service.getServiceInfo());
		}
	}
	
	public void addParticipants(ParticipantDirectory dir){
		List<Participant> participants = dir.getObjects();
		for(Participant participant : participants){
			addParticipant(participant);
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
				putNode(pNodes, node, key);
			}
			node.getParticipants().add(participant);
		}
	}
	
	public void addPointOfContacts(PointOfContactDirectory dir){
		List<Person> pocs = dir.getObjects();
		for(Person poc : pocs){
			addPointOfContact(poc);
		}
	}

	public void addPointOfContact(Person poc) {
		String key = getGeoKey(poc);
		if (key == null) {
			logger
					.warn("Got null key for POC "
							+ poc.getId() + ". Not adding to map.");
		} else {
			PointOfContactMapNode node = this.pocNodes.get(key);
			if (node == null) {
				node = new PointOfContactMapNode();
				putNode(pocNodes, node, key);
			}
			node.getPointOfContacts().add(poc);
		}
	}
	
	private String getGeoKey(Person poc) {
		String key = null;
		List<Address> addresses = poc.getAddresses();
		if(addresses.size() > 0){
			key = getGeoKey(addresses.get(0));
		}
		return key; 
	}

	private void putNode(Map map, MapNode node, String key){
		String[] latLon = key.split(":");
		node.setLatitude(latLon[0]);
		node.setLongitude(latLon[1]);
		map.put(key, node);
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

	public void addResults(DiscoveryResults selectedResults) {
		for(DomainObject obj : selectedResults.getObjects()){
			if(obj instanceof Participant){
				addParticipant((Participant)getHibernateTemplate().get(Participant.class, obj.getId()));
			}else if(obj instanceof GridService){
				addService((GridService)getHibernateTemplate().get(GridService.class, obj.getId()));
			}else if(obj instanceof Person){
				addPointOfContact((Person)getHibernateTemplate().get(Person.class, obj.getId()));
			}
		}
	}

	@Required
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}



}
