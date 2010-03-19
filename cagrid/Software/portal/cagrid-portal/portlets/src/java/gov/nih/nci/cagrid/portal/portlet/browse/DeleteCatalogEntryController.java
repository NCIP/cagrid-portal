/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.browse;

import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.dao.catalog.CatalogEntryDao;
import gov.nih.nci.cagrid.portal.dao.catalog.CatalogEntryRelationshipInstanceDao;
import gov.nih.nci.cagrid.portal.domain.catalog.*;
import gov.nih.nci.cagrid.portal.portlet.UserModel;
import gov.nih.nci.cagrid.portal.service.CatalogEntryService;
import gov.nih.nci.cagrid.portal.service.CommunityService;
import gov.nih.nci.cagrid.portal.service.PersonService;
import org.springframework.web.portlet.mvc.AbstractController;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 */
public class DeleteCatalogEntryController extends AbstractController {

    private UserModel userModel;
    private CatalogEntryDao catalogEntryDao;
    private PortalUserDao portalUserDao;
    private CatalogEntryRelationshipInstanceDao catalogEntryRelationshipInstanceDao;

    private CommunityService communityService;
    private PersonService personService;
    private CatalogEntryService catalogEntryService;

    public PortalUserDao getPortalUserDao() {
        return portalUserDao;
    }

    public void setPortalUserDao(PortalUserDao portalUserDao) {
        this.portalUserDao = portalUserDao;
    }

    /**
     *
     */
    public DeleteCatalogEntryController() {

    }

    protected void handleActionRequestInternal(ActionRequest request,
                                               ActionResponse response) throws Exception {

        Integer entryId = null;
        try {
            entryId = Integer.valueOf(request.getParameter("entryId"));
        } catch (Exception ex) {
            throw new RuntimeException("Error getting entryId from request: "
                    + ex.getMessage(), ex);
        }
        CatalogEntry catalogEntry = getCatalogEntryDao().getById(entryId);
        if (catalogEntry == null) {
            throw new RuntimeException("Couldn't find catalog entry for id: "
                    + entryId);
        }

        //only if Community CE delete community
        if (catalogEntry.getClass().equals(CommunityCatalogEntry.class)) {
            getCommunityService().deleteCommunity(getUserModel().getPortalUser(), (CommunityCatalogEntry) catalogEntry);
        } else if (catalogEntry instanceof PersonCatalogEntry) {
            getPersonService().deletePerson(getUserModel().getPortalUser(), (PersonCatalogEntry) catalogEntry);
        } else {
            getCatalogEntryService().deleteCatalogEntry(catalogEntry);
        }

//		deleteRelationships(catalogEntry);
//		if (catalogEntry instanceof PersonCatalogEntry) {
//			PersonCatalogEntry personCatalogEntry = (PersonCatalogEntry) catalogEntry;
//			PortalUser portalUser = personCatalogEntry.getAbout();
//			String portalId = portalUser.getPortalId();
//			int idx = portalId.indexOf(":");
//			long liferayUserId = Long.parseLong(portalId.substring(idx + 1));
//			getPortalUserDao().delete(portalUser);
//			UserServiceUtil.deleteUser(liferayUserId);
//		}
//		getCatalogEntryDao().delete(catalogEntry);

        getUserModel().setCurrentCatalogEntry(null);

        response.setRenderParameter("operation", "view");
    }

    private void deleteRelationships(CatalogEntry catalogEntry) {
        for (CatalogEntryRoleInstance roleInst : catalogEntry.getRoles()) {
            CatalogEntryRelationshipInstance relInst = roleInst
                    .getRelationship();
            getCatalogEntryRelationshipInstanceDao().delete(relInst);
        }
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public CatalogEntryDao getCatalogEntryDao() {
        return catalogEntryDao;
    }

    public void setCatalogEntryDao(CatalogEntryDao catalogEntryDao) {
        this.catalogEntryDao = catalogEntryDao;
    }

    public CatalogEntryRelationshipInstanceDao getCatalogEntryRelationshipInstanceDao() {
        return catalogEntryRelationshipInstanceDao;
    }

    public void setCatalogEntryRelationshipInstanceDao(
            CatalogEntryRelationshipInstanceDao catalogEntryRelationshipInstanceDao) {
        this.catalogEntryRelationshipInstanceDao = catalogEntryRelationshipInstanceDao;
    }

    public CommunityService getCommunityService() {
        return communityService;
    }

    public void setCommunityService(CommunityService communityService) {
        this.communityService = communityService;
    }

    public PersonService getPersonService() {
        return personService;
    }

    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    public CatalogEntryService getCatalogEntryService() {
        return catalogEntryService;
    }

    public void setCatalogEntryService(CatalogEntryService catalogEntryService) {
        this.catalogEntryService = catalogEntryService;
    }

}
