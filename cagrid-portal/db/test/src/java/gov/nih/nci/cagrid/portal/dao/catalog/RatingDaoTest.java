package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.DaoTestBase;
import gov.nih.nci.cagrid.portal.domain.catalog.Rating;
import org.junit.Test;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class RatingDaoTest extends DaoTestBase<RatingDao> {

    @Test
    public void save(){
        Rating rat = new Rating();

        getDao().save(rat);

    }
}
