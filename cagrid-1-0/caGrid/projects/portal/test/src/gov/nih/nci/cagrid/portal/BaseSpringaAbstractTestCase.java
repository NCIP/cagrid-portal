package gov.nih.nci.cagrid.portal;

import gov.nih.nci.cagrid.portal.utils.DatabaseInitUtility;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import java.util.HashSet;


/**
 *
 * Sets up the spring container for subclasses
 * to use
 *
 *
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jul 24, 2006
 * Time: 11:06:03 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseSpringaAbstractTestCase
    extends AbstractDependencyInjectionSpringContextTests {
    private DatabaseInitUtility initBean;

    //root index is a collection of indexes that portal aggregates from
    public HashSet rootIndexSet = new HashSet();

    protected void onSetUp() throws Exception {
        super.onSetUp(); //To change body of overridden methods use File | Settings | File Templates.

        rootIndexSet = initBean.getIndexSet();
    }

    protected String[] getConfigLocations() {
        return new String[] {
            "classpath*:/**/applicationContext-data-access.xml",
            "classpath*:/**/applicationContext-data-access-mock.xml",
        };
    }

    public void setInitBean(DatabaseInitUtility initBean) {
        this.initBean = initBean;
    }
}
