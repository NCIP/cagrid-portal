package gov.nih.nci.cagrid.portal.utils;

import gov.nih.nci.cagrid.portal.BaseSpringAbstractTestCase;
import gov.nih.nci.cagrid.portal.dao.JdbcDAO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.StringTokenizer;

/**
 * Written as a Spring test. THis will create the
 * portal database. Run this program or execute
 * the Portal DDL directly.
 * <p/>
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Aug 29, 2006
 * Time: 11:44:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class DBCreationUtility extends BaseSpringAbstractTestCase {

    private JdbcDAO jdbcDAO;
    private Resource ddlScriptResource;

    private String ddlScriptFilename = "Portal_Data_Model.SQL";


    protected void onSetUp() throws Exception {
        super.onSetUp();    //To change body of overridden methods use File | Settings | File Templates.

        ddlScriptResource = new ClassPathResource(ddlScriptFilename);
    }


    /**
     * Executes Portal DDL Script
     */
    public void testExcecutePortalDDL() {

        try {
            //Tokenize ddl script
            StringTokenizer ddlTokens = new StringTokenizer(readFileAsString(ddlScriptResource.getFile()), ";");
            String[] ddl = new String[ddlTokens.countTokens()];
            int count = 0;

            while (ddlTokens.hasMoreElements()) {
                jdbcDAO.executeUpdate(ddlTokens.nextToken() + ";");
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
        System.out.println("Database created sucessfully");
    }


    private String readFileAsString(File file)
            throws java.io.IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(
                new FileReader(file));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }

    public void setJdbcDAO(JdbcDAO jdbcDAO) {
        this.jdbcDAO = jdbcDAO;
    }
}
