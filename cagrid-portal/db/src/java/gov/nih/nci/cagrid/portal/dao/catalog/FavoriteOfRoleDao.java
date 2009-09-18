package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.dao.AbstractDao;
import gov.nih.nci.cagrid.portal.domain.catalog.FavoriteOfRole;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class FavoriteOfRoleDao extends AbstractDao<FavoriteOfRole> {

    public FavoriteOfRoleDao() {
    }

    /* (non-Javadoc)
    * @see gov.nih.nci.cagrid.portal.dao.AbstractDao#domainClass()
    */
    @Override
    public Class domainClass() {
        return FavoriteOfRole.class;
    }
}
