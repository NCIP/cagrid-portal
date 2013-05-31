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
package gov.nih.nci.cagrid.portal.portlet.browse.aspects;

import gov.nih.nci.cagrid.portal.search.SolrCommandExecutor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;

import java.net.URISyntaxException;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
@Aspect
public class CatalogEntryManagerAspect extends SolrCommandExecutor implements Ordered {
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
    public void setRating() throws Exception {
        runCommand();
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
