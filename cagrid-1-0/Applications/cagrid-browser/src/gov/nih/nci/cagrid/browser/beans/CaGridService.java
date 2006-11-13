package gov.nih.nci.cagrid.browser.beans;

import gov.nih.nci.cagrid.browser.exception.MetadataRetreivalException;
import gov.nih.nci.cagrid.browser.util.CaGridServiceUtils;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.log4j.Logger;

/**
 * Represent a caGrid Service
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Nov 9, 2006
 * Time: 2:06:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class CaGridService {

    private static Logger logger = Logger.getLogger(CaGridService.class);
    private EndpointReferenceType epr;

    private String url;
    private String name;
    private String description;


    public CaGridService(EndpointReferenceType epr) {
        this.epr = epr;
        loadLightMetadata();
    }

    public void loadLightMetadata() {
        try {
            CaGridServiceUtils metadataUtils = new CaGridServiceUtils(this.epr);
            this.description = metadataUtils.getServiceDescription();
            this.name = metadataUtils.getServiceName();
        } catch (MetadataRetreivalException e) {
            logger.warn("Error retrieving research info: " + e.getMessage());
        }

    }

    public String navigateToServiceDetails() {
        return "success";
    }

    /**
     * Will force the bean to self populate
     * with metadata
     */
    public void fillMetadata() {


    }


    public EndpointReferenceType getEpr() {
        return epr;
    }

    public void setEpr(EndpointReferenceType epr) {
        this.epr = epr;
    }


    public String getUrl() {
        return this.epr.toString();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }


}
