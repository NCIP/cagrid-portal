package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.DaoTestBase;
import gov.nih.nci.cagrid.portal.domain.catalog.Citation;
import org.junit.Test;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CitationDaoTest extends DaoTestBase<CitationDao> {

    @Test
    public void save(){
        Citation catalog = new Citation();

        getDao().save(catalog);

    }
}
