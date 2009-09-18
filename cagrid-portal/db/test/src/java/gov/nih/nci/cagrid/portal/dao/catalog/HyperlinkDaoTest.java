package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.DaoTestBase;
import gov.nih.nci.cagrid.portal.domain.catalog.Hyperlink;
import org.junit.Test;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class HyperlinkDaoTest extends DaoTestBase<HyperlinkDao> {

    @Test
    public void save(){
        Hyperlink href = new Hyperlink();

        getDao().save(href);

    }
}