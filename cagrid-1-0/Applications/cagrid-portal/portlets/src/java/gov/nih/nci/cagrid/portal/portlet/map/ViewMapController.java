/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.map;

import gov.nih.nci.cagrid.portal.portlet.AbstractViewObjectController;
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
		mapBean.addServices(getAllServicesDirectory());
		mapBean.addParticipants(getAllParticipantsDirectory());
		//mapBean.addPointOfContacts(getAllPocsDirectory());
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

}
