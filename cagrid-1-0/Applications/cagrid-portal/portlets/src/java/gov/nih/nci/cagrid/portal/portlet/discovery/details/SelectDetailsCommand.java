/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.discovery.details;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author kherm manav.kher@semanticbits.com
 */
public class SelectDetailsCommand {

    private Integer selectedId;
    private String Url;

    /**
     *
     */
    public SelectDetailsCommand() {

    }


    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public Integer getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(Integer serviceId) {
        this.selectedId = serviceId;
    }

}
