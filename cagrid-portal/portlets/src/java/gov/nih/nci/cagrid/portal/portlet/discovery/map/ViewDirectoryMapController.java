/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.discovery.map;

import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryResults;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryType;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.AbstractDirectoryBean;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.BaseDiscoveryViewController;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.DiscoveryDirectory;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.ParticipantDirectory;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.PointOfContactDirectory;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.ServiceDirectory;

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
	protected AbstractDirectoryBean doHandleDirectory(RenderRequest request,
			RenderResponse response, DiscoveryDirectory selectedDirectory) throws Exception {
		MapBean mapBean = (MapBean)newDirectoryBean();
		if(selectedDirectory.getType().equals(DiscoveryType.SERVICE)){
			mapBean.addServices((ServiceDirectory)selectedDirectory);
		}else if(selectedDirectory.getType().equals(DiscoveryType.PARTICIPANT)){
			mapBean.addParticipants((ParticipantDirectory)selectedDirectory);
		}else if(selectedDirectory.getType().equals(DiscoveryType.POC)){
			mapBean.addPointOfContacts((PointOfContactDirectory)selectedDirectory);
		}else{
			throw new Exception("Unsupported directory type: " + selectedDirectory.getType());
		}
		return mapBean;
	}
	

	@Override
	protected AbstractDirectoryBean doHandleResults(RenderRequest request,
			RenderResponse response, DiscoveryResults selectedResults) throws Exception {
		MapBean mapBean = (MapBean) newDirectoryBean();
		mapBean.addResults(selectedResults);
		return mapBean;
	}

}
