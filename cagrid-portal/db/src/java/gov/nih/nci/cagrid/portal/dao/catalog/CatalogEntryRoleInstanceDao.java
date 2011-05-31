package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.dao.AbstractDao;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRoleInstance;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CatalogEntryRoleInstanceDao extends AbstractDao<CatalogEntryRoleInstance> {

    public CatalogEntryRoleInstanceDao() {
    }

    /* (non-Javadoc)
    * @see gov.nih.nci.cagrid.portal.dao.AbstractDao#domainClass()
    */
    @Override
    public Class domainClass() {
        return CatalogEntryRoleInstance.class;
    }
}