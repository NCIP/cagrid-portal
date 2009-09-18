package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.PortalTestUtils;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal.domain.dataservice.QueryInstance;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class QueryInstanceDaoTest extends QueryInstanceDaoTestBase {

    @Test
    public void error(){
        QueryInstance instance = new CQLQueryInstance();

        instance.setError(PortalTestUtils.createReallyLongString(1650000));
        getDao().save(instance);
        assertNotNull(instance);

    }
}
