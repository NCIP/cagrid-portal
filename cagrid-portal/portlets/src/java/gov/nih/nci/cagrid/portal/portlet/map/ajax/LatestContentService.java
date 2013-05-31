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
package gov.nih.nci.cagrid.portal.portlet.map.ajax;

import gov.nih.nci.cagrid.portal.dao.catalog.CatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;
import gov.nih.nci.cagrid.portal.portlet.AjaxViewGenerator;
import org.directwebremoting.annotations.Param;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.directwebremoting.spring.SpringCreator;

import java.util.HashMap;
import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */

@RemoteProxy(name =
        "LatestContentService",
        creator = SpringCreator.class,
        creatorParams = @Param(name = "beanName",
                value = "latestContentService"))
public class LatestContentService extends AjaxViewGenerator {

    // default
    private int latestContentLimit = 5;
    private CatalogEntryDao catalogEntryDao;

    @RemoteMethod
    public String getContent() throws Exception {
        logger.debug("Returning " + latestContentLimit + " latest catalog entries");
        return super.getView(getView(), new HashMap<String, Object>() {{
            put("latestContent", loadContent());
        }});
    }

    public List<CatalogEntry> loadContent() {
        logger.debug("Will return" + latestContentLimit + " latest catalog entries");
        List<CatalogEntry> list = null;
        try {
            list = catalogEntryDao.getLatestContent(latestContentLimit);
        } catch (Exception e) {
            logger.error(e);
        }
        return list;
    }

    public int getLatestContentLimit() {
        return latestContentLimit;
    }

    public void setLatestContentLimit(int latestContentLimit) {
        this.latestContentLimit = latestContentLimit;
    }

    public CatalogEntryDao getCatalogEntryDao() {
        return catalogEntryDao;
    }

    public void setCatalogEntryDao(CatalogEntryDao catalogEntryDao) {
        this.catalogEntryDao = catalogEntryDao;
    }
}
