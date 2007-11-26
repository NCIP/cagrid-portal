/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.map;

import gov.nih.nci.cagrid.portal.portlet.AbstractViewObjectController;
import gov.nih.nci.cagrid.portal.portlet.CaGridPortletApplicationException;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryModel;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryType;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.DiscoveryDirectory;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.ParticipantDirectory;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.PointOfContactDirectory;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.ServiceDirectory;
import gov.nih.nci.cagrid.portal.portlet.discovery.map.MapBean;

import javax.portlet.RenderRequest;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ViewMapController extends AbstractViewObjectController {

	private ServiceDirectory allServicesDirectory;
	private ParticipantDirectory allParticipantsDirectory;
	private PointOfContactDirectory allPocsDirectory;
	private DiscoveryModel discoveryModel;
	
	/**
	 * 
	 */
	public ViewMapController() {

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.portlet.AbstractViewObjectController#getObject(javax.portlet.RenderRequest)
	 */
	@Override
	protected Object getObject(RenderRequest request) {
		
		MapBean mapBean = (MapBean) getApplicationContext().getBean("mapBeanPrototype");
		DiscoveryDirectory selectedDirectory = getDiscoveryModel().getSelectedDirectory();
		if(selectedDirectory != null){
			if(selectedDirectory.getType().equals(DiscoveryType.SERVICE)){
				mapBean.addServices((ServiceDirectory)selectedDirectory);
			}else if(selectedDirectory.getType().equals(DiscoveryType.PARTICIPANT)){
				mapBean.addParticipants((ParticipantDirectory)selectedDirectory);
			}else if(selectedDirectory.getType().equals(DiscoveryType.POC)){
				mapBean.addPointOfContacts((PointOfContactDirectory)selectedDirectory);
			}else{
				throw new CaGridPortletApplicationException("Unsupported directory type: " + selectedDirectory.getType());
			}
		}else{
			mapBean.addServices(getAllServicesDirectory());
			mapBean.addParticipants(getAllParticipantsDirectory());
			//mapBean.addPointOfContacts(getAllPocsDirectory());	
		}
		
		return mapBean;
	}

	public ServiceDirectory getAllServicesDirectory() {
		return allServicesDirectory;
	}

	public void setAllServicesDirectory(ServiceDirectory allServicesDirectory) {
		this.allServicesDirectory = allServicesDirectory;
	}

	public ParticipantDirectory getAllParticipantsDirectory() {
		return allParticipantsDirectory;
	}

	public void setAllParticipantsDirectory(
			ParticipantDirectory allParticipantsDirectory) {
		this.allParticipantsDirectory = allParticipantsDirectory;
	}

	public PointOfContactDirectory getAllPocsDirectory() {
		return allPocsDirectory;
	}

	public void setAllPocsDirectory(PointOfContactDirectory allPocsDirectory) {
		this.allPocsDirectory = allPocsDirectory;
	}

	public DiscoveryModel getDiscoveryModel() {
		return discoveryModel;
	}

	public void setDiscoveryModel(DiscoveryModel discoveryModel) {
		this.discoveryModel = discoveryModel;
	}

}
