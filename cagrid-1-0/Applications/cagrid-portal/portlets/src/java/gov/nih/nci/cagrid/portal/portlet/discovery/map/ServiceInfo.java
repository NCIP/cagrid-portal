/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.discovery.map;

import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.metadata.common.ResearchCenter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author <a href="mailto:kherm@mail.nih.gov">Manav Kher</a>
 *
 */
public class ServiceInfo {

    private static final Log logger = LogFactory.getLog(ServiceInfo.class);

    private String name;
    private String nameAbbrv;
    private String center;
    private String status;
    private String url;
    private String urlAbbrv;
    private String id;
    private ServiceType type;
    private boolean secure;

    //Todo make it admin configurable
    public static final int URL_MAX_LENGTH_ALLOWED = 35;
    public static final int NAME_MAX_LENGTH_ALLOWED=14;

    /**
     * @param service
     *
     */
    public ServiceInfo(GridService service) {
        setName(service.getServiceMetadata().getServiceDescription().getName());
        ResearchCenter rc = service.getServiceMetadata().getHostingResearchCenter();
        if(rc != null){
            setCenter(rc.getShortName());
        }
        setStatus(service.getCurrentStatus().toString());
        setUrl(service.getUrl());
        setId(String.valueOf(service.getId()));
        if(service instanceof GridDataService){
            setType(ServiceType.DATA);
        }else{
            setType(ServiceType.ANALYTICAL);
        }
        secure=url.indexOf("https")>-1?true:false;
    }

    public ServiceInfo(){

    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static enum ServiceType{
        DATA, ANALYTICAL;
    }

    public ServiceType getType() {
        return type;
    }

    public void setType(ServiceType type) {
        this.type = type;
    }

    public String getUrlAbbrv() {
        try{
            urlAbbrv = url.length()>URL_MAX_LENGTH_ALLOWED?url.substring(0,URL_MAX_LENGTH_ALLOWED) + "..":url;
        }catch(Exception ex){
            urlAbbrv = "";
            logger.error("Error generating urlAbbrv: " + ex.getMessage(), ex);
        }
        return urlAbbrv;
    }

    public void setUrlAbbrv(String urlAbbrv) {
        this.urlAbbrv = urlAbbrv;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public String getNameAbbrv() {
       nameAbbrv = name.length()>NAME_MAX_LENGTH_ALLOWED?name.substring(0,NAME_MAX_LENGTH_ALLOWED)+"..":name;
        return nameAbbrv;
    }

    public void setNameAbbrv(String nameAbbrv) {
        this.nameAbbrv = nameAbbrv;
    }
}
