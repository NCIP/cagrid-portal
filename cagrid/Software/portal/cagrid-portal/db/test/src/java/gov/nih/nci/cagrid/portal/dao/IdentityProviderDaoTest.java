package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.DBTestBase;
import gov.nih.nci.cagrid.portal.DaoTestBase;
import gov.nih.nci.cagrid.portal.domain.IdentityProvider;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class IdentityProviderDaoTest extends DaoTestBase<IdentityProviderDao> {

    @Test
    public void create() {
        IdentityProvider idp = new IdentityProvider();
        idp.setLabel("");
        idp.setUrl("");
        getDao().save(idp);

        assertNotNull(getDao().getAll());
    }


}
