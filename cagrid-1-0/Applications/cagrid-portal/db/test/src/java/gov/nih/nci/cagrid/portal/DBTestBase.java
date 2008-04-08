package gov.nih.nci.cagrid.portal;

import junit.framework.TestCase;
import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.operation.DatabaseOperation;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.FileInputStream;
import java.util.Properties;
import java.sql.Connection;
import java.sql.SQLException;

import gov.nih.nci.cagrid.portal.TestDB;
import gov.nih.nci.cagrid.portal.dao.AbstractDao;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class DBTestBase<T extends AbstractDao> extends TestCase{


    public T getDao(){
        return (T)getApplicationContext().getBean(getNamingStrategy().substring(0,1).toLowerCase() + getNamingStrategy().substring(1,getNamingStrategy().indexOf("Test"))); 
    }


    public ApplicationContext getApplicationContext(){
        return TestDB.getApplicationContext();
    }

    protected String getDataSet() throws Exception
    {
        return "db/test/data/" + getNamingStrategy() + ".xml";
    }

    public String getNamingStrategy(){
        return getClass().getSimpleName();
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
