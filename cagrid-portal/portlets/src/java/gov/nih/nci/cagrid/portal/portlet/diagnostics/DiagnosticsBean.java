/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
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
