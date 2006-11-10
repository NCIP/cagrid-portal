package gov.nih.nci.cagrid.browser.beans;

import org.apache.axis.message.addressing.EndpointReferenceType;

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

    private EndpointReferenceType epr;
    private String url;

    public CaGridService(EndpointReferenceType epr) {
        this.epr = epr;

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
        return epr.toString().trim();
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
