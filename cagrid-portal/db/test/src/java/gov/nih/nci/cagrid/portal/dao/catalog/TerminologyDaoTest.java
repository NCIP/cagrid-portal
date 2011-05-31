package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.DaoTestBase;
import gov.nih.nci.cagrid.portal.domain.catalog.Terminology;
import org.junit.Test;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class TerminologyDaoTest  extends DaoTestBase<TerminologyDao> {

    @Test
    public void save(){
        Terminology terminology = new Terminology();

        getDao().save(terminology);

    }
}
