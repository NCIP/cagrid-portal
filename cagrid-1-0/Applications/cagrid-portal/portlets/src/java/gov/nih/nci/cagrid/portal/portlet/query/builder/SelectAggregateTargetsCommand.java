package gov.nih.nci.cagrid.portal.portlet.query.builder;

import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class SelectAggregateTargetsCommand {
    List<String> urls;

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
