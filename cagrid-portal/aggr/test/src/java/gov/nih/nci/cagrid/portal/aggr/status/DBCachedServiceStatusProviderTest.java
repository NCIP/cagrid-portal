package gov.nih.nci.cagrid.portal.aggr.status;

import gov.nih.nci.cagrid.portal.DBTestBase;
import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class DBCachedServiceStatusProviderTest extends DBTestBase<GridServiceDao> {

    @Test
    // make sure case insenitive queries will work
    public void caseInsensitive() {
        DBCachedServiceStatusProvider provider = new DBCachedServiceStatusProvider();
        provider.setGridServiceDao(getDao());

        for (GridService service : getDao().getAll()) {
            ServiceStatus status = provider.getStatus(service.getUrl());
            assertEquals(ServiceStatus.UNKNOWN, status);
        }

    }

    public String getNamingStrategy() {
        return "GridServiceDaoTest";
    }
}
