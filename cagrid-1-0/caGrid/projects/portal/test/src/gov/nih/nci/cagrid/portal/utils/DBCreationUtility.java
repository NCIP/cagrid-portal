package gov.nih.nci.cagrid.portal.utils;

import gov.nih.nci.cagrid.portal.BaseSpringAbstractTestCase;
import gov.nih.nci.cagrid.portal.dao.JdbcDAO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Written as a Spring test. THis will create the
 * portal database
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
    private Resource zipcodesResource;

    private String ddlScriptFilename = "Portal_Data_Model.SQL";
    private String zipcodesFilename = "ZIP_CODES.txt";


    protected void onSetUp() throws Exception {
        super.onSetUp();    //To change body of overridden methods use File | Settings | File Templates.

        ddlScriptResource = new ClassPathResource(ddlScriptFilename);
        zipcodesResource = new ClassPathResource(zipcodesFilename);
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

    /**
     * Populates Geocodes for ZIP codes
     */
    public void testPopulateZipCodes() {
        try {
            String createZipTable = "Create table ZIPCODES_GEOCODES(ZIP TEXT, LATITUDE FLOAT, LONGITUDE FLOAT, CITY TEXT, STATE TEXT, COUNTY TEXT, ZIP_CLASS TEXT)";
            jdbcDAO.executeUpdate(createZipTable);
        } catch (DataAccessException e) {
            //don't fail
            e.printStackTrace();
        }

        try {
            String absolutePath = zipcodesResource.getFile().getAbsolutePath();

            // replace backslashes since MySQL interprets them as escape sequences
            if (absolutePath.indexOf('\\') > -1)
                //absolutePath = absolutePath.replace('\\', '/');
                absolutePath = replaceBS(absolutePath);

            System.out.println(absolutePath);

            String query = "LOAD DATA LOCAL INFILE  '" + absolutePath
                    + "' REPLACE INTO TABLE ZIPCODES_GEOCODES "
                    + "FIELDS"
                    + " TERMINATED BY ','"
                    + " ENCLOSED BY '\"'"
                    + " LINES TERMINATED BY '\\r\\n' STARTING BY ''"
                    + " (ZIP, LATITUDE, LONGITUDE, CITY, STATE, COUNTY, ZIP_CLASS);";

            jdbcDAO.executeUpdate(query);
        } catch (IOException e) {
            System.out.println("Could not create zip codes");
            e.printStackTrace();

        }
        System.out.println("Create Zipcodes in DB.");
    }

    private String replaceBS(String fname) {
        StringBuffer n = new StringBuffer();
        char c;
        for (int i = 0; i < fname.length(); i++) {
            c = fname.charAt(i);
            switch (c) {
                case '\\':
                    n.append("\\\\");
                    break;
                default:
                    n.append(c);
                    break;
            }
        }
        return n.toString();
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
