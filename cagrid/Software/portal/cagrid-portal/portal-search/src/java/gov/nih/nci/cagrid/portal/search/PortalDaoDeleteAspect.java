package gov.nih.nci.cagrid.portal.search;

import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
@Aspect
public class PortalDaoDeleteAspect extends AbstractSolrCommandExecutor {

    public PortalDaoDeleteAspect(String command) throws URISyntaxException {
        super(command);
    }


    @Before(value = "execution(* gov.nih.nci.cagrid.portal.dao.catalog..delete(*))"
            + " && @annotation(gov.nih.nci.cagrid.portal.annotation.UpdatesCatalogs)"
            + " && args(e,..)")
    public void runCommand(CatalogEntry e) throws Exception {
        try {
            HttpMethod method = new GetMethod(getBaseSolrURL() + getCommand());

            //Todo use DOM builder to build the XML
            StringBuilder queryStr = new StringBuilder("stream.body=");
            queryStr.append(encode("<delete><id>"));
            queryStr.append(e.getId());
            queryStr.append(encode("</id></delete>"));
            method.setQueryString(queryStr.toString());

            logger.info("Deleting catalog entry from Index");
            execute(method);

            HttpMethod commitMethod = new GetMethod(getBaseSolrURL() + getCommand());
            StringBuilder commitStr = new StringBuilder("?stream.body=");
            commitStr.append(encode("<commit/>"));
            commitMethod.setQueryString(commitStr.toString());

            //commit
            execute(commitMethod);

        } catch (IOException ex) {
            String msg = "Could not execute delete command on SOLR HTTP service. Make sure SOLR is running.";
            logger.warn(msg);
            throw new PortalSearchRuntimeException(msg);
        }


    }
}
