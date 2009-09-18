package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.DBTestBase;
import gov.nih.nci.cagrid.portal.domain.IndexService;
import static org.junit.Assert.*;
import org.junit.Test;


/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class IndexServiceDaoTest extends DBTestBase<IndexServiceDao> {


    @Test
    public void get() {
        IndexService idxById = getDao().getById(-1);

        IndexService idxByUrl = getDao().getIndexServiceByUrl("http://index1");

        assertEquals(idxById, idxByUrl);

        IndexService idxByUrl2 = getDao().getIndexServiceByUrl("http://index2");
        assertNotNull(idxByUrl2.getServices());


    }


}
