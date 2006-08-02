package gov.nih.nci.cagrid.portal;

import gov.nih.nci.cagrid.portal.utils.DatabaseInitUtility;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

import java.util.HashSet;


/**
 * Mock tests base class DAO
 * testing.
 *
 *
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jul 31, 2006
 * Time: 10:31:16 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseSpringDataAccessAbstractTestCase
    extends AbstractTransactionalDataSourceSpringContextTests {
    private DatabaseInitUtility initBean;
    protected HashSet rootIndexSet = new HashSet();

    protected void onSetUpBeforeTransaction() throws Exception {
        super.onSetUpBeforeTransaction(); //To change body of overridden methods use File | Settings | File Templates.
        rootIndexSet = initBean.getIndexSet();
    }

    protected String[] getConfigLocations() {
        return new String[] {
            "classpath*:/**/applicationContext-data-access-mock.xml",
            "classpath*:/**/applicationContext-data-access.xml",
        };
    }

    public void setInitBean(DatabaseInitUtility initBean) {
        this.initBean = initBean;
    }
}
