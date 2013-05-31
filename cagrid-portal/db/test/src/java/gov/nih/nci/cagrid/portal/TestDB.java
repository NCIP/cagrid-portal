/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
/**
 *
 */
package gov.nih.nci.cagrid.portal;

import gov.nih.nci.cagrid.portal.dao.PersonDao;
import gov.nih.nci.cagrid.portal.domain.Person;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.Session;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.springframework.orm.hibernate3.SessionFactoryUtils;


/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class TestDB {
	
	private static final Log logger = LogFactory.getLog(TestDB.class);

    private static Properties props;
    private static ApplicationContext applicationContext;

    static {
        applicationContext = new ClassPathXmlApplicationContext(
                new String[]{"classpath:applicationContext-db.xml", "classpath:applicationContext-db-relationships.xml"});
        props = new Properties();
        try {
            props.load(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("cagridportal.properties"));
        } catch (Exception ex) {
            throw new RuntimeException("Error loading properties", ex);
        }
    }

    public static void create() {
        getLocalSessionFactoryBean().createDatabaseSchema();
    }

    public static void drop() {
        getLocalSessionFactoryBean().dropDatabaseSchema();
    }

    private static LocalSessionFactoryBean getLocalSessionFactoryBean() {
        HibernateTransactionManager txMgr = (HibernateTransactionManager) applicationContext
                .getBean("transactionManager");
        Session session = SessionFactoryUtils.getSession(txMgr
                .getSessionFactory(), true);
        if (session != null) {
            session.clear();
        }
        return (LocalSessionFactoryBean) applicationContext
                .getBean("&sessionFactory");
    }

    public static void clean() {
        drop();
        create();
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void loadData(String dataFilePath) throws Exception {

        Connection conn = getJdbcConnection();
        IDatabaseConnection dbConn = new DatabaseConnection(conn);
        DatabaseConfig config = dbConn.getConfig();
        if(isHsql()){
        	config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
        			new HsqlDataTypeFactory());
        }else if(isMySql()){
        	config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
        			new MySqlDataTypeFactory());
        }else{
        	System.out.println("Using default data type factory.");
        }

        IDataSet data = new FlatXmlDataSet(new FileInputStream(dataFilePath));
        try {
            DatabaseOperation.CLEAN_INSERT.execute(dbConn, data);
        } finally {
            conn.close();
        }
    }

    private static boolean isMySql() {
    	String prop = props.getProperty("cagrid.portal.db.driver"); 
		return prop != null && prop.startsWith("com.mysql");
	}

	private static boolean isHsql() {
		String prop = props.getProperty("cagrid.portal.db.driver"); 
		return prop != null && prop.startsWith("org.hsqldb");
	}

	private static Connection getJdbcConnection() throws Exception {
        Class.forName(props.getProperty("cagrid.portal.db.driver"));
        return DriverManager.getConnection(props
                .getProperty("cagrid.portal.db.url"), props
                .getProperty("cagrid.portal.db.username"), props
                .getProperty("cagrid.portal.db.password"));
    }

    public static void exportData(String tableName, String toFile)
            throws Exception {
        IDatabaseConnection conn = new DatabaseConnection(getJdbcConnection());
        QueryDataSet partialDataSet = new QueryDataSet(conn);
        partialDataSet.addTable(tableName);
        try {
            FlatXmlDataSet.write(partialDataSet, new FileOutputStream(toFile));
        } finally {
            conn.close();
        }
    }

    public static void main(String[] args) {
        try {
            TestDB.create();
            TestDB.loadData("test/data/PersonDaoTest.xml");
            PersonDao personDao = (PersonDao) TestDB.getApplicationContext()
                    .getBean("personDao");
            List<Person> persons = personDao.getAll();
            System.out.println("size: " + persons.size());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
