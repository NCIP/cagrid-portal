package gov.nih.nci.cagrid.portal.portlet.browse;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class GridServiceEndpointDescriptorBean {

    private String catalogId;
    private String gridServiceId;
    private String gridServiceUrl;

    public GridServiceEndpointDescriptorBean(String catalogId, String gridServiceId, String gridServiceUrl) {
        this.catalogId = catalogId;
        this.gridServiceId = gridServiceId;
        this.gridServiceUrl = gridServiceUrl;
    }

    public String getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(String catalogId) {
        this.catalogId = catalogId;
    }

    public String getGridServiceUrl() {
        return gridServiceUrl;
    }

    public void setGridServiceUrl(String gridServiceUrl) {
        this.gridServiceUrl = gridServiceUrl;
    }

    public String getGridServiceId() {
        return gridServiceId;
    }

    public void setGridServiceId(String gridServiceId) {
        this.gridServiceId = gridServiceId;
    }
}
