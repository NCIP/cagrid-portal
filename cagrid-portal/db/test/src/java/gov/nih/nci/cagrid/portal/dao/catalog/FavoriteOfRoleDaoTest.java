package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.DaoTestBase;
import gov.nih.nci.cagrid.portal.domain.catalog.FavoriteOfRole;
import org.junit.Test;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class FavoriteOfRoleDaoTest  extends DaoTestBase<FavoriteOfRoleDao> {

    @Test
    public void save(){
        FavoriteOfRole fave = new FavoriteOfRole();

        getDao().save(fave);

    }
}