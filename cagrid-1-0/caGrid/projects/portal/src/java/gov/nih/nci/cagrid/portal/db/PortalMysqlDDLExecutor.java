package gov.nih.nci.cagrid.portal.db;

import gov.nih.nci.cagrid.portal.dao.JdbcDAO;
import gov.nih.nci.cagrid.portal.exception.PortalInitializationException;
import gov.nih.nci.cagrid.portal.exception.PortalRuntimeException;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Has all mysql specific loading
 * If you'd like to use a different DB,
 * an implementation of these methods (from PortalDDLExecutor interface)
 * need to be provided.
 * <p/>
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 16, 2006
 * Time: 9:31:04 AM
 * To change this template use File | Settings | File Templates.
 */
public final class PortalMysqlDDLExecutor extends AbstractPortalDDLExecutor {
    protected JdbcDAO jdbcDAO;

    public void executePortalDDL() throws PortalInitializationException {
        try {
            //Tokenize ddl script
            StringTokenizer ddlTokens = new StringTokenizer(readFileAsString(ddlScriptResource.getFile()), ";");
            String[] ddl = new String[ddlTokens.countTokens()];
            int count = 0;

            while (ddlTokens.hasMoreElements()) {
                jdbcDAO.executeUpdate(ddlTokens.nextToken() + ";");
            }
        } catch (Exception e) {
            _logger.error(e);
            throw new PortalInitializationException(e);
        }
        _logger.info("Portal DDL excuted successfully");
    }

    /**
     * Populates Geocodes for ZIP codes
     *
     * @throws gov.nih.nci.cagrid.portal.exception.PortalRuntimeException
     *
     */
    public void executePopulateDBWithZipCodes() throws PortalRuntimeException {

        try {
            String absolutePath = zipCodeSeedData.getFile().getAbsolutePath();

            // replace backslashes since MySQL interprets them as escape sequences
            if (absolutePath.indexOf('\\') > -1)
                //absolutePath = absolutePath.replace('\\', '/');
                absolutePath = replaceBS(absolutePath);

            String query = "LOAD DATA LOCAL INFILE  '" + absolutePath
                    + "' REPLACE INTO TABLE ZIPCODES_GEOCODES "
                    + "FIELDS"
                    + " TERMINATED BY ','"
                    + " ENCLOSED BY '\"'"
                    + " (ZIP, LATITUDE, LONGITUDE, CITY, STATE, COUNTY, ZIP_CLASS);";

            jdbcDAO.executeUpdate(query);
        } catch (IOException e) {
            _logger.error(e);
            throw new PortalRuntimeException(e);
        }
        _logger.info("Populated Portal DB with Zipcode Seed data");

    }

    private String replaceBS(String fname) {
        StringBuffer n = new StringBuffer();
        char c;
        for (int i = 0; i < fname.length(); i++) {
            c = fname.charAt(i);
            switch (c) {
                case'\\':
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
