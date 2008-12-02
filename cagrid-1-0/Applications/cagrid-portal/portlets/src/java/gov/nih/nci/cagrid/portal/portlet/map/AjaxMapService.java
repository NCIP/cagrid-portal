package gov.nih.nci.cagrid.portal.portlet.map;

import gov.nih.nci.cagrid.portal.portlet.AjaxViewGenerator;
import gov.nih.nci.cagrid.portal.portlet.CaGridPortletApplicationException;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryModel;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryType;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.DiscoveryDirectory;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.ParticipantDirectory;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.PointOfContactDirectory;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.ServiceDirectory;
import gov.nih.nci.cagrid.portal.portlet.discovery.map.MapBean;
import org.directwebremoting.annotations.Param;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.SpringCreator;

import java.util.HashMap;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
@RemoteProxy(name = "MapService",
        creator = SpringCreator.class,
        creatorParams = @Param(name = "beanName",
                value = "ajaxMapService"))
public class AjaxMapService extends AjaxViewGenerator {

    private ServiceDirectory allServicesDirectory;
    private ParticipantDirectory allParticipantsDirectory;
    private DiscoveryModel discoveryModel;

    @RemoteMethod
    public String getMap(String directoryId) throws Exception{
        if(directoryId!=null)
            getDiscoveryModel().selectDirectory(directoryId);

    final MapBean mapBean = (MapBean) getApplicationContext().getBean("mapBeanPrototype");
    DiscoveryDirectory selectedDirectory = getDiscoveryModel().getSelectedDirectory();
    if(selectedDirectory != null){
        mapBean.setSelectedDirectory(selectedDirectory.getId());
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
    }

    return super.getView("/WEB-INF/jsp/map/ajaxMap.jsp",new HashMap<String,Object>(){{put("mapBean", mapBean);}});
}

    public DiscoveryModel getDiscoveryModel() {
        return discoveryModel;
    }

    public void setDiscoveryModel(DiscoveryModel discoveryModel) {
        this.discoveryModel = discoveryModel;
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

    public void setAllParticipantsDirectory(ParticipantDirectory allParticipantsDirectory) {
        this.allParticipantsDirectory = allParticipantsDirectory;
    }


}
