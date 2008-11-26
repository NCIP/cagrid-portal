/**
 *
 */
package gov.nih.nci.cagrid.portal.domain;

import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.metadata.common.ResearchCenter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.URL;
import java.net.MalformedURLException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author <a href="mailto:kherm@mail.nih.gov">Manav Kher</a>
 */
public class ServiceInfo {

    private static final Log logger = LogFactory.getLog(ServiceInfo.class);

    private String name;
    private String nameAbbrv;
    private String center;
    private String centerAbbrv;
    private String status;
    private String url;
    private String id;
    private ServiceType type;
    private boolean secure;
    private String version;

    //Todo make it admin configurable
    public static final int URL_MAX_LENGTH_ALLOWED = 30;
    public static final int NAME_MAX_LENGTH_ALLOWED = 14;

    /**
     * @param service
     */
    public ServiceInfo(GridService service) {
        setStatus(service.getCurrentStatus().toString());
        setUrl(service.getUrl());
        setId(String.valueOf(service.getId()));
        try {
            setName(service.getServiceMetadata().getServiceDescription().getName());
            setVersion(service.getServiceMetadata().getServiceDescription().getVersion());
        } catch (Exception e) {
            logger.warn("Error getting Service Description for service: " + getUrl());
            setName(formulateNameFromUrl(getUrl()));
        }

        ResearchCenter rc = service.getServiceMetadata().getHostingResearchCenter();
        if (rc != null) {
            setCenter(rc.getShortName());
        }
        if (service instanceof GridDataService) {
            setType(ServiceType.DATA);
        } else {
            setType(ServiceType.ANALYTICAL);
        }
        secure = url.indexOf("https") > -1 ? true : false;
    }

    public ServiceInfo() {

    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
        setCenterAbbrv(abbreviate(center,NAME_MAX_LENGTH_ALLOWED));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setNameAbbrv(abbreviate(name,NAME_MAX_LENGTH_ALLOWED));
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

    public ServiceType getType() {
        return type;
    }

    public void setType(ServiceType type) {
        this.type = type;
    }

    public String getUrlAbbrv() {
        try {
            URL _url = new URL(url);
            int _HOST_MAX_LENGTH_ALLOWED = URL_MAX_LENGTH_ALLOWED - (_url.getProtocol().length() + "://".length() + String.valueOf(_url.getPort()).length());

            StringBuffer _urlAbbrv = new StringBuffer();
            if (_url.getProtocol().length() > -1)
                _urlAbbrv.append(_url.getProtocol() + "://");
            _urlAbbrv.append(_url.getHost().length() > _HOST_MAX_LENGTH_ALLOWED ? _url.getHost().substring(0, _HOST_MAX_LENGTH_ALLOWED - 3) + ".." : _url.getHost());
            if (_url.getPort() > 0)
                _urlAbbrv.append(":").append(_url.getPort());

            _urlAbbrv.append((_urlAbbrv + _url.getPath()).length() > URL_MAX_LENGTH_ALLOWED ? "" : _url.getPath());
            return _urlAbbrv.toString();

        } catch (MalformedURLException ex) {
            return url.length() > URL_MAX_LENGTH_ALLOWED ? url.substring(0, URL_MAX_LENGTH_ALLOWED) + ".." : url;
        }
    }

    private String formulateNameFromUrl(String Url){
        try {
            if(Url.lastIndexOf("/")>-1){
                return Url.substring(Url.lastIndexOf("/"));
            }
        } catch (Exception e) {
            logger.error("Error formulating name from URL");
        }
        return getUrlAbbrv();
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public String getNameAbbrv() {
        return nameAbbrv;
    }

    public void setNameAbbrv(String nameAbbrv) {
        this.nameAbbrv = nameAbbrv;
    }

    public String getCenterAbbrv() {
        return centerAbbrv;
    }

    public void setCenterAbbrv(String centerAbbrv) {
        this.centerAbbrv = centerAbbrv;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    private String abbreviate(String longStr,int maxLength){
        return longStr.length() > maxLength ? longStr.substring(0, maxLength) + ".." : longStr;

    }
}
