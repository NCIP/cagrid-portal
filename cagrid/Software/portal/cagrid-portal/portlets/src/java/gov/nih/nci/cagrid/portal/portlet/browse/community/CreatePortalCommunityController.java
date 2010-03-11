package gov.nih.nci.cagrid.portal.portlet.browse.community;

import com.liferay.portal.PortalException;
import com.liferay.portal.model.Group;
import com.liferay.portal.service.GroupServiceUtil;
import com.liferay.portal.service.ServiceContext;
import gov.nih.nci.cagrid.portal.dao.catalog.CommunityCatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.catalog.CommunityCatalogEntry;
import gov.nih.nci.cagrid.portal.portlet.browse.BaseCatalogEntryAbstractController;
import org.springframework.transaction.annotation.Transactional;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CreatePortalCommunityController extends BaseCatalogEntryAbstractController {

    private CommunityCatalogEntryDao communityCatalogEntryDao;

    @Override
    protected void handleActionRequestInternal(ActionRequest req, ActionResponse res) throws Exception {
        CommunityCatalogEntry ce = (CommunityCatalogEntry) getCatalogEntry(req);

        logger.debug("Will try and create new Portal Community");
        String name = ce.getName();
        String description = ce.getDescription();
        int type = 1;
        String friendlyURL = "";
        boolean active = true;
        // Add group
        try {
            Group group = GroupServiceUtil.addGroup(
                    name, description, type, friendlyURL, active, new ServiceContext());
            ce.setCommunityUrl(group.getFriendlyURL());
            getCommunityCatalogEntryDao().save(ce);
            getUserModel().setCurrentCatalogEntry(ce);
        } catch (Exception e) {
            logger.warn("Could not create Portal Community. Will rollback", e);
            getCommunityCatalogEntryDao().delete(ce);
            getUserModel().setCurrentCatalogEntry(null);
            throw new PortalException("Could not create Portal Community", e);
        }
        logger.info("Created new Portal community with name " + ce.getName());

        // forward it to view details controller in the action phase
        res.setRenderParameter("operation", "viewDetails");
    }

    public CommunityCatalogEntryDao getCommunityCatalogEntryDao() {
        return communityCatalogEntryDao;
    }

    public void setCommunityCatalogEntryDao(CommunityCatalogEntryDao communityCatalogEntryDao) {
        this.communityCatalogEntryDao = communityCatalogEntryDao;
    }
}
