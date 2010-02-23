package gov.nih.nci.cagrid.portal.portlet.browse;

import gov.nih.nci.cagrid.portal.dao.catalog.CatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;
import gov.nih.nci.cagrid.portal.portlet.UserModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.portlet.mvc.AbstractController;

import javax.portlet.PortletRequest;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class BaseCatalogEntryAbstractController extends AbstractController {
    private CatalogEntryDao catalogEntryDao;
    private String objectName;
    protected UserModel userModel;

    protected Log logger = LogFactory.getLog(getClass());


    protected CatalogEntry getCatalogEntry(PortletRequest request) throws RuntimeException {
        logger.debug("Getting catalog entry from request");

        CatalogEntry entry = null;
        Integer entryId = null;
        try {
            entryId = Integer.valueOf(request.getParameter(BrowseParams.ENTRY_ID));
        } catch (Exception ex) {

        }
        if (entryId != null) {
            entry = getCatalogEntryDao().getById(entryId);
        } else {
            entry = getUserModel().getCurrentCatalogEntry();
            if (entry == null) {
                throw new RuntimeException("No current catalog entry.");
            }
            //do not load if this is a new CE being created
            if (entry.getId() != null) {
                entry = getCatalogEntryDao().getById(entry.getId());
            }
        }
        // no point in returning null.
        if (entry == null) {
            throw new RuntimeException("Could not load catalog entry from DB");
        }
        logger.debug("Returrning catalog entry");
        return entry;
    }

    public CatalogEntryDao getCatalogEntryDao() {
        return catalogEntryDao;
    }

    public void setCatalogEntryDao(CatalogEntryDao catalogEntryDao) {
        this.catalogEntryDao = catalogEntryDao;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

}
