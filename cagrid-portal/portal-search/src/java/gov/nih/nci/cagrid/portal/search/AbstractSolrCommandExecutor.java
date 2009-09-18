package gov.nih.nci.cagrid.portal.search;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public abstract class AbstractSolrCommandExecutor {
    private String baseSolrURL;
    private String localhostURL;
    private String command;

    HttpClient httpClient;

    protected Log logger = LogFactory.getLog(getClass());

    protected AbstractSolrCommandExecutor(String command) throws URISyntaxException {
        this.command = command;
    }


    public abstract void runCommand() throws Exception;

    public String getBaseSolrURL() {
        try {
            URI uri = new URI(this.baseSolrURL);
            if (!uri.isAbsolute())
                throw new URISyntaxException("Relative URL will not work", "");
        } catch (URISyntaxException e) {
            logger.warn("SOLR URL looks relative. Assuming localhost");
            this.baseSolrURL = this.localhostURL.concat(this.baseSolrURL);

        }

        logger.info("SOLR URL " + this.baseSolrURL + " looks correct");

        return this.baseSolrURL;
    }

    /**
     * Synchronize calls to SOLR
     * @param httpMethod
     * @throws IOException
     */
    public synchronized void execute(HttpMethod httpMethod) throws IOException {
        getHttpClient().executeMethod(httpMethod);

    }
    public void setBaseSolrURL(String baseSolrURL) {
        this.baseSolrURL = baseSolrURL;
    }

    public String getLocalhostURL() {
        return localhostURL;
    }

    public void setLocalhostURL(String localhostURL) {
        this.localhostURL = localhostURL;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }
}
