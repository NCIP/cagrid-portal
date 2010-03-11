package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.DaoTestBase;
import gov.nih.nci.cagrid.portal.domain.catalog.File;
import org.junit.Test;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class FileDaoTest  extends DaoTestBase<FileDao> {

    @Test
    public void save(){
        File file = new File();

        getDao().save(file);

    }
}