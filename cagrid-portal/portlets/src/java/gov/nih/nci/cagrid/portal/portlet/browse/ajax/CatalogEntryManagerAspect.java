package gov.nih.nci.cagrid.portal.portlet.browse.ajax;

import gov.nih.nci.cagrid.portal.search.AbstractSolrCommandExecutor;
import gov.nih.nci.cagrid.portal.search.PortalSearchRuntimeException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;

import java.io.IOException;
import java.net.URISyntaxException;

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

    /**
     * When CE is rated, this will update the SOLR Lucene index
     *
     * @throws Exception
     */
    @AfterReturning("execution(* gov.nih.nci.cagrid.portal.portlet.browse.ajax.*.setRating(..))")
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

    //ToDo Make these annotations and a single Pointcut
    @AfterReturning("execution(* gov.nih.nci.cagrid.portal.portlet.browse.ajax.*.save(..))")
    public void save() throws Exception {
        runCommand();
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
