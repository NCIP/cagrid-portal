package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.DaoTestBase;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRoleInstance;
import org.junit.Test;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CatalogEntryRoleInstanceDaoTest  extends DaoTestBase<CatalogEntryRoleInstanceDao> {

    @Test
    public void save(){
        CatalogEntryRoleInstance cRole = new CatalogEntryRoleInstance();

        getDao().save(cRole);

    }
}