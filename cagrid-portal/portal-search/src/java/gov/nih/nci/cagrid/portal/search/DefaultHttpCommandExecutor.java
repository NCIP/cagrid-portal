package gov.nih.nci.cagrid.portal.search;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpClient;

import java.io.IOException;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class DefaultHttpCommandExecutor implements HttpCommandExecutor{
    HttpClient httpClient;

    public void execute(HttpMethod httpMethod) throws IOException {
        getHttpClient().executeMethod(httpMethod);
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }
}
