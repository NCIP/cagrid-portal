/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.browse.ajax;

import gov.nih.nci.cagrid.portal.dao.catalog.CommunityCatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;
import gov.nih.nci.cagrid.portal.domain.catalog.CommunityCatalogEntry;


/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 * @author kherm manav.kher@semanticbits.com
 */
public class CommunityCatalogEntryManagerFacade extends
        CatalogEntryManagerFacade {

    private CommunityCatalogEntryDao communityCatalogEntryDao;

    /**
     *
     */
    public CommunityCatalogEntryManagerFacade() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public String setName(String name) {
        if (getCommunityCatalogEntryDao().isUnique(name))
            return super.setName(name);    //To change body of overridden methods use File | Settings | File Templates.
        return "Community with the same name exists!";
    }

    @Override
    protected Integer saveInternal(CatalogEntry catalogEntry) {
        CommunityCatalogEntry communityCe = (CommunityCatalogEntry) catalogEntry;
        getCatalogEntryDao().save(communityCe);
        return catalogEntry.getId();
    }

    public CommunityCatalogEntryDao getCommunityCatalogEntryDao() {
        return communityCatalogEntryDao;
    }

    public void setCommunityCatalogEntryDao(CommunityCatalogEntryDao communityCatalogEntryDao) {
        this.communityCatalogEntryDao = communityCatalogEntryDao;
    }
}
