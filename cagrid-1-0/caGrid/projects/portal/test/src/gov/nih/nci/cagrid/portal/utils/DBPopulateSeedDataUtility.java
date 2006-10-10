package gov.nih.nci.cagrid.portal.utils;

import gov.nih.nci.cagrid.portal.BaseSpringAbstractTestCase;
import gov.nih.nci.cagrid.portal.dao.JdbcDAO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;

import java.io.IOException;

/**
 * Will put all seed data in the portal DB
 * <p/>
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 10, 2006
 * Time: 10:26:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class DBPopulateSeedDataUtility extends BaseSpringAbstractTestCase {

    private JdbcDAO jdbcDAO;
    private Resource zipcodesResource;

    private String zipcodesFilename = "ZIP_CODES.txt";


    protected void onSetUp() throws Exception {
        super.onSetUp();    //To change body of overridden methods use File | Settings | File Templates.

        zipcodesResource = new ClassPathResource(zipcodesFilename);

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

    public void setJdbcDAO(JdbcDAO jdbcDAO) {
        this.jdbcDAO = jdbcDAO;
    }
}
