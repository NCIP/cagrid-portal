package gov.nih.nci.cagrid.portal;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import java.util.HashSet;


/**
 * Mock tests base class DAO
 * testing.
 * <p/>
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jul 31, 2006
 * Time: 10:31:16 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseSpringDataAccessAbstractTestCase
        extends AbstractTransactionalDataSourceSpringContextTests {
    protected HashSet rootIndexSet = new HashSet();

    protected void onSetUpBeforeTransaction() throws Exception {
        super.onSetUpBeforeTransaction(); //To change body of overridden methods use File | Settings | File Templates.

        //rootIndexSet.add("http://cagrid01.bmi.ohio-state.edu:8080/wsrf/services/DefaultIndexService");
        rootIndexSet.add(
                "http://cagrid04.bmi.ohio-state.edu:7080/wsrf/services/DefaultIndexService");
    }

    protected String[] getConfigLocations() {
        return new String[]{
                "classpath*:/**/applicationContext-data-access-mock.xml",
                "classpath*:/**/applicationContext-data-access.xml",
        };
    }
}
