/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.discovery.map;

import gov.nih.nci.cagrid.portal2.domain.GridService;
import gov.nih.nci.cagrid.portal2.domain.Participant;
import gov.nih.nci.cagrid.portal2.domain.metadata.common.PointOfContact;
import gov.nih.nci.cagrid.portal2.portlet.discovery.DiscoveryResults;
import gov.nih.nci.cagrid.portal2.portlet.discovery.DiscoveryType;
import gov.nih.nci.cagrid.portal2.portlet.discovery.dir.AbstractDirectoryBean;
import gov.nih.nci.cagrid.portal2.portlet.discovery.dir.BaseDiscoveryViewController;
import gov.nih.nci.cagrid.portal2.portlet.discovery.dir.DiscoveryDirectory;
import gov.nih.nci.cagrid.portal2.portlet.discovery.dir.ParticipantDirectory;
import gov.nih.nci.cagrid.portal2.portlet.discovery.dir.PointOfContactDirectory;
import gov.nih.nci.cagrid.portal2.portlet.discovery.dir.ServiceDirectory;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ViewDirectoryMapController extends BaseDiscoveryViewController {
	
	private static final Log logger = LogFactory.getLog(ViewDirectoryMapController.class);

	/**
	 * 
	 */
	public ViewDirectoryMapController() {

	}
	
	
	protected AbstractDirectoryBean newDirectoryBean() {
		return (AbstractDirectoryBean)getApplicationContext().getBean("mapBeanPrototype");
	}



	@Override
	protected void doHandleDirectory(RenderRequest request,
			RenderResponse response, DiscoveryDirectory selectedDirectory,
			AbstractDirectoryBean dirBean) throws Exception {
		MapBean mapBean = (MapBean)dirBean;
		if(selectedDirectory.getType().equals(DiscoveryType.SERVICE)){
			mapBean.addServices((ServiceDirectory)selectedDirectory);
		}else if(selectedDirectory.getType().equals(DiscoveryType.PARTICIPANT)){
			mapBean.addParticipants((ParticipantDirectory)selectedDirectory);
		}else if(selectedDirectory.getType().equals(DiscoveryType.POC)){
			mapBean.addPointOfContacts((PointOfContactDirectory)selectedDirectory);
		}else{
			throw new Exception("Unsupported directory type: " + selectedDirectory.getType());
		}
	}


	@Override
	protected void doHandleResults(RenderRequest request,
			RenderResponse response, DiscoveryResults selectedResults,
			AbstractDirectoryBean dirBean) throws Exception {
		((MapBean)dirBean).addResults(selectedResults);
	}

}
