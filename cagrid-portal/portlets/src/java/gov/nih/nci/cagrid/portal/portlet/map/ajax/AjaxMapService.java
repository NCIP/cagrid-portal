package gov.nih.nci.cagrid.portal.portlet.map.ajax;

import gov.nih.nci.cagrid.portal.portlet.AjaxViewGenerator;
import gov.nih.nci.cagrid.portal.portlet.CaGridPortletApplicationException;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryModel;
import gov.nih.nci.cagrid.portal.portlet.discovery.DiscoveryType;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.DiscoveryDirectory;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.ParticipantDirectory;
import gov.nih.nci.cagrid.portal.portlet.discovery.dir.ServiceDirectory;
import gov.nih.nci.cagrid.portal.portlet.discovery.map.MapBean;
import gov.nih.nci.cagrid.portal.portlet.map.ajax.CachedMap;
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
    private CachedMap cachedMap;

    @RemoteMethod
    public String getMap(String directoryId) throws Exception {
        if (directoryId != null)
            getDiscoveryModel().selectDirectory(directoryId);

        final MapBean mapBean;
        DiscoveryDirectory selectedDirectory = getDiscoveryModel().getSelectedDirectory();

        if (selectedDirectory != null) {
            if (selectedDirectory.getType().equals(DiscoveryType.SERVICE)) {
                ServiceDirectory serviceDir = (ServiceDirectory) selectedDirectory;
                mapBean = getCachedMap().get(serviceDir.getServiceDirectoryType());
            } else if (selectedDirectory.getType().equals(DiscoveryType.PARTICIPANT)) {
                mapBean = (MapBean) getApplicationContext().getBean("mapBeanPrototype");
                mapBean.addParticipants((ParticipantDirectory) selectedDirectory);
            } else {
                throw new CaGridPortletApplicationException("Unsupported directory type: " + selectedDirectory.getType());
            }
            mapBean.setSelectedDirectory(selectedDirectory.getId());
        } else {
            return super.getView(getView(), new HashMap<String, Object>() {{
                put("mapBean", getCachedMap().get(null));
            }});
        }

        return super.getView(getView(), new HashMap<String, Object>() {{
            put("mapBean", mapBean);
        }});
    }


    @RemoteMethod
    public SummaryBean getSummary(){
        return cachedMap.getSummary();
    }

    public CachedMap getCachedMap() {
        return cachedMap;
    }

    public void setCachedMap(CachedMap cachedMap) {
        this.cachedMap = cachedMap;
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
