package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.DaoTestBase;
import gov.nih.nci.cagrid.portal.domain.catalog.Terminology;
import gov.nih.nci.cagrid.portal.domain.catalog.Term;
import org.junit.Test;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class TermDaoTest  extends DaoTestBase<TermDao> {

    @Test
    public void save(){
        Term term = new Term();

        getDao().save(term);

    }
}