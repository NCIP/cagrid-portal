package gov.nih.nci.cagrid.portal.portlet.browse.ajax;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.core.Ordered;

import java.net.URISyntaxException;
import java.io.IOException;

import gov.nih.nci.cagrid.portal.search.AbstractSolrCommandExecutor;
import gov.nih.nci.cagrid.portal.search.PortalSearchRuntimeException;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
@Aspect
public class CatalogEntryManagerAspect extends AbstractSolrCommandExecutor implements Ordered {
    private int order = 1;


    public CatalogEntryManagerAspect(String command) throws URISyntaxException {
        super(command);
    }

    @AfterReturning("execution(* gov.nih.nci.cagrid.portal.portlet.browse.ajax.*.save())")
    public void runCommand() throws Exception {
        try {
            logger.info("Calling update on SOLR. Changes detected to the Portal DB through Ajax facade");
            HttpMethod method = new GetMethod(getBaseSolrURL() + getCommand());
            execute(method);
            logger.info("Update sucessfully executed on SOLR.");
        } catch (IOException e) {
            String msg = "Could not execute Update command on SOLR HTTP service. Make sure SOLR is running.";
            logger.warn(msg);
            throw new PortalSearchRuntimeException(msg);
        }


    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
