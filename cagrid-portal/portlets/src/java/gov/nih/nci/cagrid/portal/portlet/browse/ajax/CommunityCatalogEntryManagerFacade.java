/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.browse.ajax;

import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
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
        if (name.length() < 2) {
            return "Name is two short";
        }

        if (name.endsWith(StringPool.SLASH)) {
            return "Invalid name. Ends with a slash";
        }

        if (name.indexOf(StringPool.DOUBLE_SLASH) != -1) {
            return "Invalid name. Contains two slashes";
        }

        for (char c : name.toCharArray()) {
            if ((!Validator.isChar(c)) && (!Validator.isDigit(c)) &&
                    (c != CharPool.DASH) && (c != CharPool.PERCENT) &&
                    (c != CharPool.PERIOD) && (c != CharPool.SLASH) &&
                    (c != CharPool.UNDERLINE)) {
                return "Name has invalid characters";
            }
        }
        if (!getCommunityCatalogEntryDao().isUnique(name))
            return "Community with the same name exists";

        return super.setName(name);
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
