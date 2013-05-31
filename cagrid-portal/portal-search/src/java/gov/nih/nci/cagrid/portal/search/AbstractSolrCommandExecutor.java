/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.search;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public abstract class AbstractSolrCommandExecutor {
    private String baseSolrURL;
    private String localhostURL;
    private String command;
    protected final String URL_ENCODING = "UTF-8";

      HttpCommandExecutor executor;

    protected Log logger = LogFactory.getLog(getClass());

    protected AbstractSolrCommandExecutor() {
    }

    protected AbstractSolrCommandExecutor(String command) throws URISyntaxException {
        this.command = command;
    }


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
     *
     * @param httpMethod
     * @throws IOException
     */
    public synchronized void execute(HttpMethod httpMethod) throws IOException {
      getExecutor().execute(httpMethod);  

    }

    public String encode(String url) throws Exception {
        return URLEncoder.encode(url, URL_ENCODING);
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

    public HttpCommandExecutor getExecutor() {
        return executor;
    }

    @Required
    public void setExecutor(HttpCommandExecutor executor) {
        this.executor = executor;
    }
}
