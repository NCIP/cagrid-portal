package gov.nih.nci.cagrid.browser.beans;

/**
 * Created by the caGrid Team
 * User: kherm
 * Date: Jun 20, 2005
 * Time: 9:09:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class BrowserConfig {
    private String caCOREServiceURL;
    private IndexService indexService;
    private String locale;

    //~--- get methods --------------------------------------------------------

    public String getCaCOREServiceURL() {
        return caCOREServiceURL;
    }

    public IndexService getIndexService() {
        return indexService;
    }

    public String getLocale() {
        if (locale == null) {
            this.locale = "en";
        }

        return locale;
    }

    //~--- set methods --------------------------------------------------------

    public void setCaCOREServiceURL(String caCOREServiceURL) {
        this.caCOREServiceURL = caCOREServiceURL;
    }

    public void setIndexService(IndexService indexService) {
        this.indexService = indexService;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}

//~ Formatted by Jindent --- http://www.jindent.com
