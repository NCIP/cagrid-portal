package gov.nih.nci.cagrid.portal;

import gov.nih.nci.cagrid.portal.dao.AbstractDao;
import org.springframework.context.ApplicationContext;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class DBTestBase<T extends AbstractDao> extends AbstractDBTestBase {

    public T getDao() {
        return (T) getApplicationContext().getBean(getNamingStrategy().substring(0, 1).toLowerCase() + getNamingStrategy().substring(1, getNamingStrategy().indexOf("Test")));
    }

    public ApplicationContext getApplicationContext() {
        return TestDB.getApplicationContext();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();    //To change body of overridden methods use File | Settings | File Templates.
        TestDB.loadData(getDataSet());
    }

    protected String getDataSet() throws Exception {
        return "db/test/data/" + getNamingStrategy() + ".xml";
    }

    public String getNamingStrategy() {
        return getClass().getSimpleName();
    }


}
