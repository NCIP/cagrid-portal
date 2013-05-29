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
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import java.io.IOException;
import java.util.*;

/**
 * This class will queue execute requests and have a quiet period
 * and only execute the requests if no additional requests are recieved
 * during the quiet/wait period.
 * If another request is received, it will wait for the quiet period again
 * ...so on and so forth.
 * <p/>
 * It will finally execute the last call. This class helps in not
 * overwhelming the SOLR dataimport servlet
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CachingHttpCommandExecutor implements HttpCommandExecutor {

    private HttpClient httpClient;
    private List<HttpMethod> queue = Collections.synchronizedList(new ArrayList<HttpMethod>());
    private long quietTime = 1000;
    private Log log = LogFactory.getLog(getClass());
    private WaitingExecutor executor;

    public CachingHttpCommandExecutor() {
        executor = new WaitingExecutor();
    }

    public void execute(HttpMethod httpMethod) throws IOException {
        queue.add(httpMethod);

        //** if the executor is not running, schedule a run. Otherwise just add to queue and move on
        if (!executor.isAlive()) {
            executor = new WaitingExecutor();
            executor.start();
        }
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public long getQuietTime() {
        return quietTime;
    }

    public void setQuietTime(long quietTime) {
        this.quietTime = quietTime;
    }

    private class WaitingExecutor extends Thread {


        public void run() {

            do {
                //adjust the queue so only 1 request remains
                if (queue.size() > 1) {
                    HttpMethod lastMethod = queue.get(queue.size() - 1);
                    queue.clear();
                    queue.add(lastMethod);
                }
                try {
                    sleep(getQuietTime());
                } catch (InterruptedException e) {
                    log.error("Error " + e);
                }
            } while (queue.size() > 1);

            try {
                getHttpClient().executeMethod(queue.get(queue.size() - 1));
            } catch (IOException e) {
                log.warn("Error running method on HTTP Client ", e);
            }

        }


    }
}
