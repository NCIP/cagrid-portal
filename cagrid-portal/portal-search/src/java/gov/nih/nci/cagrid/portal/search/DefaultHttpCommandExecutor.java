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
