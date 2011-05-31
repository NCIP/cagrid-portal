/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.browse.ajax;

import gov.nih.nci.cagrid.portal.dao.catalog.CatalogEntryRelationshipTypeDao;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRelationshipType;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRoleType;
import gov.nih.nci.cagrid.portal.portlet.UserModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 */
@Transactional
public class CatalogEntryRelationshipTypeManagerFacade {

    private static final Log logger = LogFactory
            .getLog(CatalogEntryRelationshipTypeManagerFacade.class);

    private UserModel userModel;
    private CatalogEntryRelationshipTypeDao catalogEntryRelationshipTypeDao;

    private String name;
    private String description;
    private String roleAType;
    private String roleBType;
    private String roleAName;
    private String roleBName;
    private String roleADescription;
    private String roleBDescription;

    /**
     *
     */
    public CatalogEntryRelationshipTypeManagerFacade() {

    }

    public String setName(String name) {
        String message = null;
        try {

            List l = getCatalogEntryRelationshipTypeDao()
                    .getHibernateTemplate().find(
                            "from CatalogEntryRelationshipType where name = ?",
                            name);
            if (l.size() > 0) {
                message = "A relationship type with that name already exists.";
            } else {
                this.name = name;

            }
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Error setting name: " + ex.getMessage(), ex);
        }
        return message;
    }

    public String setDescription(String description) {
        String message = null;
        try {
            this.description = description;
        } catch (Exception ex) {
            throw new RuntimeException("Error setting description: "
                    + ex.getMessage(), ex);
        }
        return message;
    }

    public String setRoleAType(String roleAType) {
        String message = null;
        try {
            this.roleAType = roleAType;

        } catch (Exception ex) {
            throw new RuntimeException("Error setting role A type: "
                    + ex.getMessage(), ex);
        }
        return message;
    }

    public String setRoleBType(String roleBType) {
        String message = null;
        try {
            this.roleBType = roleBType;

        } catch (Exception ex) {
            throw new RuntimeException("Error setting role B type: "
                    + ex.getMessage(), ex);
        }
        return message;
    }

    public String setRoleAName(String roleAName) {
        String message = null;
        try {

            if (roleBName != null && roleBName.equals(roleAName)) {
                return "The name of role A must be different than role B.";
            }
            this.roleAName = roleAName;

        } catch (Exception ex) {
            throw new RuntimeException("Error setting role A name: "
                    + ex.getMessage(), ex);
        }
        return message;
    }

    public String setRoleBName(String roleBName) {
        String message = null;
        try {

            if (roleAName != null && roleAName.equals(roleBName)) {
                return "The name of role B must be different than role A.";
            }
            this.roleBName = roleBName;

        } catch (Exception ex) {
            throw new RuntimeException("Error setting role B name: "
                    + ex.getMessage(), ex);
        }
        return message;
    }

    public String setRoleADescription(String roleADescription) {
        String message = null;
        try {
            this.roleADescription = roleADescription;

        } catch (Exception ex) {
            throw new RuntimeException("Error setting role A description: "
                    + ex.getMessage(), ex);
        }
        return message;
    }

    public String setRoleBDescription(String roleBDescription) {
        String message = null;
        try {
            this.roleBDescription = roleBDescription;

        } catch (Exception ex) {
            throw new RuntimeException("Error setting role B description: "
                    + ex.getMessage(), ex);
        }
        return message;
    }

    public String validate() {
        String message = null;
        // TODO: do some real validation
        return message;
    }

    public Integer save() {
        Integer id = null;
        try {
            HibernateTemplate templ = getCatalogEntryRelationshipTypeDao()
                    .getHibernateTemplate();
            CatalogEntryRelationshipType relType = getUserModel()
                    .getCurrentRelationshipType();
            if (relType.getId() == null) {
                relType.setCreatedAt(new Date());
                templ.save(relType);
                CatalogEntryRoleType roleTypeA = new CatalogEntryRoleType();
                roleTypeA.setRelationshipType(relType);
                roleTypeA.setCreatedAt(new Date());
                templ.save(roleTypeA);
                relType.setRoleTypeA(roleTypeA);
                CatalogEntryRoleType roleTypeB = new CatalogEntryRoleType();
                roleTypeB.setRelationshipType(relType);
                roleTypeB.setCreatedAt(new Date());
                templ.save(roleTypeB);
                relType.setRoleTypeB(roleTypeB);
            }
            relType.setName(name);
            relType.setDescription(description);
            relType.getRoleTypeA().setName(roleAName);
            relType.getRoleTypeA().setType(roleAType);
            relType.getRoleTypeA().setDescription(roleADescription);
            relType.getRoleTypeB().setName(roleBName);
            relType.getRoleTypeB().setType(roleBType);
            relType.getRoleTypeB().setDescription(roleBDescription);
            templ.saveOrUpdate(relType.getRoleTypeA());
            templ.saveOrUpdate(relType.getRoleTypeB());
            templ.saveOrUpdate(relType);

            id = relType.getId();
        } catch (Exception ex) {
            String msg = "Error saving: " + ex.getMessage();
            logger.error(msg);
            throw new RuntimeException(msg, ex);
        }
        return id;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public CatalogEntryRelationshipTypeDao getCatalogEntryRelationshipTypeDao() {
        return catalogEntryRelationshipTypeDao;
    }

    public void setCatalogEntryRelationshipTypeDao(
            CatalogEntryRelationshipTypeDao catalogEntryRelationshipTypeDao) {
        this.catalogEntryRelationshipTypeDao = catalogEntryRelationshipTypeDao;
    }

}
