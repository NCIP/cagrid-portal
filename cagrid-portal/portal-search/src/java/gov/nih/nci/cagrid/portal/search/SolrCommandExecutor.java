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
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class SolrCommandExecutor extends AbstractSolrCommandExecutor {


    public SolrCommandExecutor(String command) throws URISyntaxException {
        super(command);

    }

    public void runCommand() throws Exception {
        logger.info("Calling Import on SOLR.");
        HttpMethod method = new GetMethod(getBaseSolrURL() + getCommand());

        try {
            execute(method);
            logger.info("Import successfully executed on SOLR.");
        } catch (IOException e) {
            String msg = "Could not execut Import command on SOLR HTTP service. Make sure SOLR is running.";
            logger.warn(msg);
            throw new PortalSearchRuntimeException(msg);
        }


    }


}
