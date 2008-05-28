/**
 *
 */
package model1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
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

    private static Properties props;
    private static ApplicationContext applicationContext;

    static {
        applicationContext = new ClassPathXmlApplicationContext(
                new String[]{"classpath:applicationContext-db.xml"});
        props = new Properties();
        try {
            props.load(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("db.properties"));
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
        config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
                new HsqlDataTypeFactory());

        IDataSet data = new FlatXmlDataSet(new FileInputStream(dataFilePath));
        try {
            DatabaseOperation.CLEAN_INSERT.execute(dbConn, data);
        } finally {
            conn.close();
        }
    }

    private static Connection getJdbcConnection() throws Exception {
        Class.forName(props.getProperty("db.driver"));
        return DriverManager.getConnection(props
                .getProperty("db.url"), props
                .getProperty("db.username"), props
                .getProperty("db.password"));
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
            TestDB.loadData("db/test/data/PersonDaoTest.xml");
//            PersonDao personDao = (PersonDao) TestDB.getApplicationContext()
//                    .getBean("personDao");
//            List<Person> persons = personDao.getAll();
//            System.out.println("size: " + persons.size());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
