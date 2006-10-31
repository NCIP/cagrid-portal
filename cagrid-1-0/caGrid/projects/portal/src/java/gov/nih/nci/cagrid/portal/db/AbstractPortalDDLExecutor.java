package gov.nih.nci.cagrid.portal.db;

import gov.nih.nci.cagrid.portal.domain.CaBIGParticipant;
import gov.nih.nci.cagrid.portal.domain.CaBIGWorkspace;
import gov.nih.nci.cagrid.portal.exception.PortalInitializationException;
import gov.nih.nci.cagrid.portal.exception.PortalRuntimeException;
import gov.nih.nci.cagrid.portal.manager.CaBIGWorkspaceManager;
import org.apache.log4j.Category;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Abstract class provides support for
 * running portal DDL and making ready
 * the portal DB.
 * <p/>
 * Any DDL execution done in this class
 * is DB agnostic (done through ORM).
 * <p/>
 * Subclasses provide implementation
 * for DB flavor specific execution.
 * <p/>
 * <p/>
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Oct 16, 2006
 * Time: 11:58:20 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractPortalDDLExecutor implements PortalDDLExecutor {

    protected CaBIGWorkspaceManager caBIGWorkspaceManager;

    protected Resource ddlScriptResource;
    protected Resource zipCodeSeedData;
    protected Resource cabigParticipantsSeedData;
    /**
     * populate with caBIG workspace names (see spring config) *
     */
    private Map caBIGWorkspaces = new HashMap();


    protected Category _logger = Category.getInstance(getClass().getName());

    /**
     * Will load caBIG woskpaces and participants from a Map data structure
     * and configured in spring applicationConfig-db.xml
     *
     * @throws PortalInitializationException
     */
    public void executePopulateDBWithWorkspaces() throws PortalInitializationException {
        Set keys = caBIGWorkspaces.keySet();

        for (Iterator iter = keys.iterator(); iter.hasNext();) {
            String key = (String) iter.next();

            CaBIGWorkspace workspace = new CaBIGWorkspace();
            workspace.setShortName(key);
            workspace.setLongName((String) caBIGWorkspaces.get(key));

            caBIGWorkspaceManager.save(workspace);
        }
    }

    /**
     * Loads caBIG Participant data from a excel sheet.
     * Excel sheet is specified by cabigParticipantsSeedData
     * <p/>
     * Remember excel workbook should have sheets for all caBIG workspaces
     *
     * @throws PortalInitializationException
     */
    public void executePopulateDBWithParticipants() throws PortalInitializationException {
        try {
            HSSFWorkbook excelBook = new HSSFWorkbook(cabigParticipantsSeedData.getInputStream());

            List worskpaces = caBIGWorkspaceManager.loadAll(CaBIGWorkspace.class);

            for (Iterator iter = worskpaces.iterator(); iter.hasNext();) {
                CaBIGWorkspace workspace = (CaBIGWorkspace) iter.next();
                //clear existing participants
                workspace.removeAllParticipants();

                //extract from excel
                HSSFSheet sheet = excelBook.getSheet(workspace.getShortName());

                if (sheet != null) {
                    _logger.debug("Sheet found for workspace " + workspace.getShortName() + " with " + sheet.getLastRowNum() + " rows of data");

                    //start from 2 as Row 1 is header
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        HSSFRow dataRow = sheet.getRow(i);
                        CaBIGParticipant participant = new CaBIGParticipant();

                        //row ordering is important
                        //Todo check integrity of sheet
                        String name = getDataCellValue(dataRow.getCell((short) 0));
                        String institute = getDataCellValue(dataRow.getCell((short) 1));
                        String homepage = getDataCellValue(dataRow.getCell((short) 2));
                        String status = getDataCellValue(dataRow.getCell((short) 3));
                        String street1 = getDataCellValue(dataRow.getCell((short) 4));
                        String street2 = getDataCellValue(dataRow.getCell((short) 5));
                        String city = getDataCellValue(dataRow.getCell((short) 6));
                        String state = getDataCellValue(dataRow.getCell((short) 7));
                        String country = getDataCellValue(dataRow.getCell((short) 8));
                        String postalCode = getDataCellValue(dataRow.getCell((short) 9));
                        String phone = getDataCellValue(dataRow.getCell((short) 10));
                        String email = getDataCellValue(dataRow.getCell((short) 11));

                        /** only insert valid participant **/
                        if (name != null && postalCode != null && postalCode.trim().length() > 4) {
                            participant.setName(name);
                            participant.setInstitute(institute);
                            participant.setHomepageURL(homepage);
                            participant.setStatus(status);
                            participant.setStreet1(street1);
                            participant.setStreet2(street2);
                            participant.setCity(city);
                            participant.setState(state);
                            participant.setCountry(country);
                            participant.setPostalCode(postalCode);
                            participant.setPhoneNumber(phone);
                            participant.setEmail(email);

                            workspace.addParticipant(participant);
                        } else if (name != null)
                            _logger.warn("In sheet for " + workspace.getShortName() + " Pariticpant " + getDataCellValue(dataRow.getCell((short) 0)) + " has invalid zip code and will not be saved");
                    }

                } else
                    _logger.warn("Sheet for " + workspace.getShortName() + " workspace not found. Please check the excel file.");

                try {
                    _logger.info("Saving workspace " + workspace.getShortName() + " with " + workspace.getParticipants().size() + " participants.");
                    caBIGWorkspaceManager.save(workspace);
                } catch (PortalRuntimeException e) {
                    _logger.error(e.getMessage());
                    throw new PortalInitializationException(e);
                    //log and continue
                }
                _logger.debug("Workspace saved " + workspace.getShortName());
            }

        } catch (IOException e) {
            _logger.error(e);
            throw new PortalInitializationException(e);
        }
    }

    private String getDataCellValue(HSSFCell cell) {
        if (cell != null) {
            if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
                return new String("" + new Double(cell.getNumericCellValue()).longValue());
            else
                return cell.getStringCellValue();
        } else
            return null;
    }

    protected String readFileAsString(File file)
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

//setters for spring

    public void setCaBIGWorkspaceManager(CaBIGWorkspaceManager caBIGWorkspaceManager) {
        this.caBIGWorkspaceManager = caBIGWorkspaceManager;
    }

    public void setDdlScriptResource(Resource ddlScriptResource) {
        this.ddlScriptResource = ddlScriptResource;
    }

    public void setZipCodeSeedData(Resource zipCodeSeedData) {
        this.zipCodeSeedData = zipCodeSeedData;
    }

    public void setCabigParticipantsSeedData(Resource cabigParticipantsSeedData) {
        this.cabigParticipantsSeedData = cabigParticipantsSeedData;
    }


    public void setCaBIGWorkspaces(Map caBIGWorkspaces) {
        this.caBIGWorkspaces = caBIGWorkspaces;
    }
}
