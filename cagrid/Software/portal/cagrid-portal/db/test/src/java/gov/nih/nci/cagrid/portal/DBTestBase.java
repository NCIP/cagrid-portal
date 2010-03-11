package gov.nih.nci.cagrid.portal;

import gov.nih.nci.cagrid.portal.dao.AbstractDao;
import org.junit.Before;

/**
 * Will create a test DB and insert sample data
 * <p/>
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class DBTestBase<T extends AbstractDao> extends DaoTestBase<T> {


    @Before
    public void initialize() throws Exception {
        TestDB.loadData(getDataSet());
    }

    protected String getDataSet() throws Exception {
        return "test/data/" + getNamingStrategy() + ".xml";
    }


}
