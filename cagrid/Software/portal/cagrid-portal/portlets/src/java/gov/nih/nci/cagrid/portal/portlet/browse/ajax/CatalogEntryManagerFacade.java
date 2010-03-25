/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.browse.ajax;

import gov.nih.nci.cagrid.portal.PortalSystemException;
import gov.nih.nci.cagrid.portal.dao.catalog.CatalogEntryDao;
import gov.nih.nci.cagrid.portal.dao.catalog.TermDao;
import gov.nih.nci.cagrid.portal.dao.catalog.TerminologyDao;
import gov.nih.nci.cagrid.portal.domain.PortalUser;
import gov.nih.nci.cagrid.portal.domain.catalog.*;
import gov.nih.nci.cagrid.portal.portlet.AjaxViewGenerator;
import gov.nih.nci.cagrid.portal.portlet.UserModel;
import gov.nih.nci.cagrid.portal.portlet.browse.CatalogEntryViewBean;
import gov.nih.nci.cagrid.portal.portlet.browse.CatalogEntryViewBeanFactory;
import gov.nih.nci.cagrid.portal.portlet.browse.LabelDescriptionBean;
import gov.nih.nci.cagrid.portal.portlet.terms.TermBean;
import gov.nih.nci.cagrid.portal.portlet.terms.TerminologyProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 * @author kherm manav.kher@semanticbits.com
 */
@Transactional
public class CatalogEntryManagerFacade extends AjaxViewGenerator {

    protected final Log logger = LogFactory
            .getLog(getClass());
    private CatalogEntryDao catalogEntryDao;
    private CatalogEntryViewBeanFactory catalogEntryViewBeanFactory;
    private UserModel userModel;
    private HibernateTemplate hibernateTemplate;
    private TerminologyProvider terminologyProvider;
    private TerminologyDao terminologyDao;
    private TermDao termDao;

    private String roleTypeRenderServletUrl;
    private String newRelatedItemFormRenderServletUrl;
    private String relatedItemsRenderServletUrl;

    private String sourceRoleDescription;
    private String targetRoleDescription;
    private Integer relatedEntryId;
    private Integer roleTypeId;
    private String areaOfFocusValues;

    /**
     *
     */
    public CatalogEntryManagerFacade() {

    }

    /**
     * WIll load the current CE and attach it an open Session
     *
     * @return
     * @throws PortalSystemException
     */
    public CatalogEntry loadCurrentCE() throws PortalSystemException {
        CatalogEntry entry = getCatalogEntryDao().getById(
                getUserModel().getCurrentCatalogEntry().getId());
        if (entry == null) {
            throw new PortalSystemException("No current catalog entry");
        }
        return entry;
    }

    public Integer save() {
        Integer id = null;
        try {
            PortalUser portalUser = getUserModel().getPortalUser();
            CatalogEntry ce = getUserModel().getCurrentCatalogEntry();
            ce.setAuthor(portalUser);
            if (ce.getId() == null) {
//                PortletUtils.addResource(getUserModel().getPortalUser(),
//                        CatalogEntry.class, ce.getId());
            }
            logger.debug("Saving CE named:" + ce.getName() + " Will now mark the CE as published.");
            ce.setPublished(true);
            getCatalogEntryDao().save(ce);
            saveAreasOfFocus();

            id = saveInternal(ce);
        } catch (Exception ex) {
            String msg = "Error saving catalog entry: " + ex.getMessage();
            logger.debug(msg, ex);
            throw new RuntimeException(msg, ex);
        }
        return id;
    }

    /**
     * Make this item a featured item
     *
     * @param featured
     * @return null if no error
     */
    public String setFeatured(String featured) {
        getUserModel().getCurrentCatalogEntry().setFeatured(Boolean.parseBoolean(featured));
        return null;
    }

    protected void saveAreasOfFocus() {
        try {
            List<Term> areasOfFocus = new ArrayList<Term>();
            if (areaOfFocusValues != null) {

                for (String uriStr : areaOfFocusValues.split(",")) {

                    Term term = getTermDao().getByIdentifier(uriStr);
                    if (term == null) {
                        term = new Term();
                        TermBean termBean = getTerminologyProvider()
                                .getTermForUri(uriStr);

                        Terminology terminology = getTerminologyDao()
                                .getByIdentifier(
                                        termBean.getTerminology().getUri());
                        if (terminology == null) {
                            terminology = new Terminology();
                            terminology.setIdentifier(termBean.getTerminology()
                                    .getUri());
                            terminology.setLabel(termBean.getTerminology()
                                    .getLabel());
                            getTerminologyDao().save(terminology);
                        }
                        term.setTerminology(terminology);
                        term.setDescription(termBean.getComment());
                        term.setIdentifier(termBean.getUri());
                        term.setLabel(termBean.getLabel());
                        getTermDao().save(term);
                    }
                    areasOfFocus.add(term);
                }

            }
            CatalogEntry ce = getUserModel().getCurrentCatalogEntry();
            ce.setAreasOfFocus(areasOfFocus);
            getCatalogEntryDao().save(ce);
        } catch (Exception ex) {
            String msg = "Error setting area of focus values: "
                    + ex.getMessage();
            logger.error(msg, ex);
            throw new RuntimeException(msg, ex);
        }
    }

    public String validate() {
        return null;
    }

    /**
     * Is target type possible for the current selected CE
     * @param targetType
     * @return
     */
    public boolean isTarget(final String targetType){
        return getRoleTypes(targetType).size() > 0;
    }

    public List<CatalogEntryRoleType> getRoleTypes(final String targetType) {

        final String sourceType = getUserModel().getCurrentCatalogEntry()
                .getClass().getName();
        Set<String> types = new HashSet<String>();
            Set<String> sourceTypes = new HashSet<String>() {{
                add(sourceType);
            }};
            Set<String> targetTypes = new HashSet<String>() {{
                add(targetType);
            }};

            types.addAll(sourceTypes);
            types.addAll(targetTypes);

            logger.debug("sourceTypes: " + sourceTypes);
            logger.debug("targetTypes: " + targetTypes);

            StringBuilder sb = new StringBuilder();
            for (Iterator<String> i = types.iterator(); i.hasNext();) {
                String type = i.next();
                sb.append("'").append(type).append("'");
                if (i.hasNext()) {
                    sb.append(",");
                }
            }
            String query = "from CatalogEntryRoleType where type in (" + sb
                    + ")";
            logger.debug("Query: " + query);

            List l = getHibernateTemplate().find(query);

            List<CatalogEntryRoleType> roleTypes = new ArrayList<CatalogEntryRoleType>();

            logger.debug("Fetched " + l.size() + " role types.");

            for (Iterator<CatalogEntryRoleType> i = l.iterator(); i.hasNext();) {
                CatalogEntryRoleType targetTypeObj = i.next();
                if (targetTypes.contains(targetTypeObj.getType())) {
                    CatalogEntryRelationshipType relType = targetTypeObj
                            .getRelationshipType();
                    CatalogEntryRoleType sourceTypeObj = relType.getRoleTypeA();
                    if (sourceTypeObj.getId().equals(targetTypeObj.getId())) {
                        sourceTypeObj = relType.getRoleTypeB();
                    }
                    if (sourceTypes.contains(sourceTypeObj.getType())) {
                        roleTypes.add(targetTypeObj);
                    }
                }
            }
        return roleTypes;
    }


    public String renderRoleTypesForTargetType(final String targetType, final String namespace) {
        String html = null;

        List<CatalogEntryRoleType> roleTypes = getRoleTypes(targetType);

        logger.debug("roleTypes.size: " + roleTypes.size());

        Map<String, Object> attrMap = new HashMap<String, Object>();
        attrMap.put("roleTypes", roleTypes);
        attrMap.put("namespace", namespace);
        try {
            html = getView(getRoleTypeRenderServletUrl(), attrMap);
        } catch (Exception ex) {
            String msg = "Error rendering role types: " + ex.getMessage();
            logger.error(msg, ex);
            throw new RuntimeException(msg, ex);
        }

        return html;
    }

    public String renderNewRelatedItemForm(Integer roleTypeId, String namespace) {
        this.roleTypeId = roleTypeId;

        String html = null;
        try {
            getUserModel().setCurrentRelationshipInstance(null);
            CatalogEntryRoleType targetRoleType = (CatalogEntryRoleType) getHibernateTemplate()
                    .find("from CatalogEntryRoleType where id = ?", roleTypeId)
                    .iterator().next();

            CatalogEntryRelationshipType relType = targetRoleType
                    .getRelationshipType();
            CatalogEntryRoleType sourceRoleType = relType.getRoleTypeA();
            if (sourceRoleType.getId().equals(targetRoleType.getId())) {
                sourceRoleType = relType.getRoleTypeB();
            }


            Map<String, Object> attrMap = new HashMap<String, Object>();
            attrMap.put("targetRoleType", targetRoleType);
            attrMap.put("sourceRoleType", sourceRoleType);
            attrMap.put("namespace", namespace);

            html = getView(getNewRelatedItemFormRenderServletUrl(), attrMap);
        } catch (Exception ex) {
            String msg = "Error rendering role types: " + ex.getMessage();
            logger.error(msg, ex);
            throw new RuntimeException(msg, ex);
        }

        return html;
    }

    public List<LabelDescriptionBean> getCatalogEntriesForPartialName(
            String name, String type) {
        List<LabelDescriptionBean> ldbs = new ArrayList<LabelDescriptionBean>();
        try {
            List<CatalogEntry> entries = getCatalogEntryDao().getByPartialNameAndType(name, type);
            for (CatalogEntry entry : entries) {
                ldbs.add(new LabelDescriptionBean(
                        String.valueOf(entry.getId()), entry.getName(), entry
                                .getDescription()));
            }
        } catch (Exception ex) {
            String msg = "Error getting entries by partial name: "
                    + ex.getMessage();
            logger.error(msg, ex);
            throw new RuntimeException(msg, ex);
        }
        return ldbs;
    }

    public String renderRelatedItems(String namespace) {
        String html = null;
        try {

            CatalogEntry entry = getCatalogEntryDao().getById(
                    getUserModel().getCurrentCatalogEntry().getId());
            if (entry == null) {
                throw new RuntimeException("No current catalog entry");
            }
            CatalogEntryViewBean catalogEntryViewBean = getCatalogEntryViewBeanFactory()
                    .newCatalogEntryViewBean(entry);


            Map<String, Object> attrMap = new HashMap<String, Object>();
            attrMap.put("catalogEntryViewBean", catalogEntryViewBean);
            attrMap.put("namespace", namespace);
            html = getView(getRelatedItemsRenderServletUrl(), attrMap);
        } catch (Exception ex) {
            String msg = "Error rendering role types: " + ex.getMessage();
            logger.error(msg, ex);
            throw new RuntimeException(msg, ex);
        }

        return html;
    }

    public String setSourceRoleDescription(String sourceRoleDescription) {
        String message = null;
        this.sourceRoleDescription = sourceRoleDescription;
        return message;
    }

    public String setTargetRoleDescription(String targetRoleDescription) {
        String message = null;
        this.targetRoleDescription = targetRoleDescription;
        this.setSourceRoleDescription(targetRoleDescription);
        return message;
    }

    public String setRelatedEntryId(Integer relatedEntryId) {
        String message = null;
        this.relatedEntryId = relatedEntryId;
        return message;
    }

    public String setRoleTypeId(Integer roleTypeId) {
        String message = null;
        this.roleTypeId = roleTypeId;
        return message;
    }

    public String saveRelationship() {
        String message = null;

        try {

            CatalogEntry entry = getUserModel().getCurrentCatalogEntry();
            CatalogEntryRelationshipInstance relInst = getUserModel()
                    .getCurrentRelationshipInstance();

            if (relInst == null) {

                relInst = new CatalogEntryRelationshipInstance();

                CatalogEntry relatedEntry = (CatalogEntry) getHibernateTemplate()
                        .find("from CatalogEntry where id = ?",
                                this.relatedEntryId).iterator().next();
                CatalogEntryRoleType targetRoleType = (CatalogEntryRoleType) getHibernateTemplate()
                        .find("from CatalogEntryRoleType where id = ?",
                                this.roleTypeId).iterator().next();
                CatalogEntryRelationshipType relType = targetRoleType
                        .getRelationshipType();
                CatalogEntryRoleType sourceRoleType = relType.getRoleTypeA();
                if (sourceRoleType.getId().equals(targetRoleType.getId())) {
                    sourceRoleType = relType.getRoleTypeB();
                }

                relInst.setCreatedAt(new Date());
                relInst.setAuthor(getUserModel().getPortalUser());
                relInst.setType(relType);
                getHibernateTemplate().save(relInst);

                CatalogEntryRoleInstance roleAInst = new CatalogEntryRoleInstance();
                roleAInst.setCreatedAt(new Date());
                roleAInst.setCatalogEntry(entry);
                roleAInst.setRelationship(relInst);
                roleAInst.setType(sourceRoleType);
                getHibernateTemplate().save(roleAInst);
                relInst.setRoleA(roleAInst);

                CatalogEntryRoleInstance roleBInst = new CatalogEntryRoleInstance();
                roleBInst.setCreatedAt(new Date());
                roleBInst.setCatalogEntry(relatedEntry);
                roleBInst.setRelationship(relInst);
                roleBInst.setType(targetRoleType);
                getHibernateTemplate().save(roleBInst);
                relInst.setRoleB(roleBInst);

                getUserModel().setCurrentRelationshipInstance(relInst);
            }

            CatalogEntryRoleInstance sourceRoleInst = relInst.getRoleA();
            CatalogEntryRoleInstance targetRoleInst = relInst.getRoleB();
            if (!sourceRoleInst.getCatalogEntry().getId().equals(entry.getId())) {
                sourceRoleInst = relInst.getRoleB();
                targetRoleInst = relInst.getRoleA();
            }

            sourceRoleInst.setDescription(sourceRoleDescription);
            sourceRoleInst.setUpdatedAt(new Date());
            targetRoleInst.setDescription(targetRoleDescription);
            targetRoleInst.setUpdatedAt(new Date());
            getHibernateTemplate().update(sourceRoleInst);
            getHibernateTemplate().update(targetRoleInst);
            getHibernateTemplate().update(relInst);

            getHibernateTemplate().flush();

//            PortletUtils.addResource(getUserModel().getPortalUser(),
//                    CatalogEntryRelationshipInstance.class, relInst.getId());

        } catch (Exception ex) {
            String msg = "Error saving relationship: " + ex.getMessage();
            logger.error(msg, ex);
            throw new RuntimeException(msg, ex);
        }
        return message;
    }

    public String deleteRelationship(Integer relationshipId) {
        String message = null;
        try {
            HibernateTemplate templ = getHibernateTemplate();
            CatalogEntryRelationshipInstance relInst = (CatalogEntryRelationshipInstance) templ
                    .find("from CatalogEntryRelationshipInstance where id = ?",
                            relationshipId).iterator().next();

//            PortletUtils.deleteResource(getUserModel().getPortalUser(),
//                    CatalogEntryRelationshipInstance.class, relInst.getId());

            CatalogEntryRoleInstance roleAInst = relInst.getRoleA();
            CatalogEntryRoleInstance roleBInst = relInst.getRoleB();
            roleAInst.setRelationship(null);
            roleBInst.setRelationship(null);
            relInst.setRoleA(null);
            relInst.setRoleB(null);
            templ.delete(roleAInst);
            templ.delete(roleBInst);
            templ.delete(relInst);
            templ.flush();

        } catch (Exception ex) {
            String msg = "Error deleting relationship: " + ex.getMessage();
            logger.error(msg, ex);
            throw new RuntimeException(msg, ex);
        }
        return message;
    }

    protected Integer saveInternal(CatalogEntry catalogEntry) throws Exception {
        return catalogEntry.getId();
    }

    public String setName(String name) {
        String message = null;
        if (getUserModel() == null) {
            throw new RuntimeException("userModel is null");
        }
        if (getUserModel().getCurrentCatalogEntry() == null) {
            throw new RuntimeException("currentCatalogEntry is null");
        }
        try {
            getUserModel().getCurrentCatalogEntry().setName(name);
        } catch (Exception ex) {
            String msg = "Error setting name: " + ex.getMessage();
            logger.error(msg, ex);
            throw new RuntimeException(msg, ex);
        }
        return message;
    }

    public Integer getAverageRating() {
        try {
            int entryId = getUserModel().getCurrentCatalogEntry().getId();

            int sum = 0;
            List<Rating> ratings = getHibernateTemplate().find(
                    "from Rating r where r.ratingOf.id = ?", entryId);
            int numRatings = 0;
            for (Rating r : ratings) {
                numRatings++;
                sum += r.getRating();
            }
            return Math.round(sum / Math.max(1, numRatings));
        } catch (Exception ex) {
            String msg = "Error calculating average rating: " + ex.getMessage();
            logger.error(msg, ex);
            throw new RuntimeException(msg, ex);
        }
    }

    public String setRating(Integer value) {
        String message = null;
        try {
            PortalUser portalUser = getUserModel().getPortalUser();
            CatalogEntry catalogEntry = getUserModel().getCurrentCatalogEntry();
            Rating rating = null;
            List<Rating> ratings = getHibernateTemplate()
                    .find(
                            "from Rating r where r.ratingContributor.id = ? and r.ratingOf.id = ?",
                            new Object[]{portalUser.getCatalog().getId(),
                                    catalogEntry.getId()});
            if (ratings.size() > 1) {
                throw new RuntimeException(
                        "More than one rating of this catalog entry ["
                                + catalogEntry.getId()
                                + "] for this person entry ["
                                + portalUser.getCatalog().getId() + "]");
            }
            if (ratings.size() == 1) {
                rating = ratings.iterator().next();
            }
            if (rating == null) {
                rating = new Rating();
                rating.setRatingOf(catalogEntry);
                rating.setRatingContributor(portalUser.getCatalog());
                getHibernateTemplate().save(rating);
            }
            rating.setRating(value);
            getHibernateTemplate().update(rating);
        } catch (Exception ex) {
            String msg = "Error setting rating: " + ex.getMessage();
            logger.error(msg, ex);
            throw new RuntimeException(msg, ex);
        }
        return message;
    }

    public String setDescription(String description) {
        String message = null;
        getUserModel().getCurrentCatalogEntry().setDescription(description);
        return message;
    }

    public String setAreaOfFocusValues(String values) {
        String message = null;
        this.areaOfFocusValues = values;

        return message;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public String getRoleTypeRenderServletUrl() {
        return roleTypeRenderServletUrl;
    }

    public void setRoleTypeRenderServletUrl(String roleTypeRenderServletUrl) {
        this.roleTypeRenderServletUrl = roleTypeRenderServletUrl;
    }

    public String getNewRelatedItemFormRenderServletUrl() {
        return newRelatedItemFormRenderServletUrl;
    }

    public void setNewRelatedItemFormRenderServletUrl(
            String newRelatedItemFormRenderServletUrl) {
        this.newRelatedItemFormRenderServletUrl = newRelatedItemFormRenderServletUrl;
    }

    public String getRelatedItemsRenderServletUrl() {
        return relatedItemsRenderServletUrl;
    }

    public void setRelatedItemsRenderServletUrl(
            String relatedItemsRenderServletUrl) {
        this.relatedItemsRenderServletUrl = relatedItemsRenderServletUrl;
    }

    public CatalogEntryDao getCatalogEntryDao() {
        return catalogEntryDao;
    }

    public void setCatalogEntryDao(CatalogEntryDao catalogEntryDao) {
        this.catalogEntryDao = catalogEntryDao;
    }

    public CatalogEntryViewBeanFactory getCatalogEntryViewBeanFactory() {
        return catalogEntryViewBeanFactory;
    }

    public void setCatalogEntryViewBeanFactory(
            CatalogEntryViewBeanFactory catalogEntryViewBeanFactory) {
        this.catalogEntryViewBeanFactory = catalogEntryViewBeanFactory;
    }

    public TerminologyProvider getTerminologyProvider() {
        return terminologyProvider;
    }

    public void setTerminologyProvider(TerminologyProvider terminologyProvider) {
        this.terminologyProvider = terminologyProvider;
    }

    public TerminologyDao getTerminologyDao() {
        return terminologyDao;
    }

    public void setTerminologyDao(TerminologyDao terminologyDao) {
        this.terminologyDao = terminologyDao;
    }

    public TermDao getTermDao() {
        return termDao;
    }

    public void setTermDao(TermDao termDao) {
        this.termDao = termDao;
    }

}
