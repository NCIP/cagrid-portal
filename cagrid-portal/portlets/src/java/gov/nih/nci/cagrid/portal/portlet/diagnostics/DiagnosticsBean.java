package gov.nih.nci.cagrid.portal.portlet.diagnostics;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class DiagnosticsBean {

    private String url;
    private boolean ipcRequest;
    private String wikiURL;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isIpcRequest() {
        return ipcRequest;
    }

    public void setIpcRequest(boolean ipcRequest) {
        this.ipcRequest = ipcRequest;
    }

    public String getWikiURL() {
        return wikiURL;
    }

    public void setWikiURL(String wikiURL) {
        this.wikiURL = wikiURL;
    }
}
