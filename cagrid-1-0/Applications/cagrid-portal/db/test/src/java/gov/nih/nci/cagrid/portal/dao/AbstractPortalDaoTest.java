package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.AbstractPortalTest;
import gov.nih.nci.cagrid.portal.TestDB;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public abstract class AbstractPortalDaoTest<T extends AbstractDao> extends AbstractPortalTest { 


    public T getDao(){
        return (T)getApplicationContext().getBean(getDaoName());
    }

    protected String getDataSet() throws Exception
    {
        return "db/test/data/" + getDaoName().substring(0,1).toUpperCase() + getDaoName().substring(1) + ".xml";
    }

    public String getDaoName(){
        return getDaoName().substring(0,1).toLowerCase() + getDaoName().substring(1, getDaoName().indexOf("Test"));
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        TestDB.create();
        TestDB.loadData(getDataSet());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();    //To change body of overridden methods use File | Settings | File Templates.
        TestDB.drop();
    }




}
