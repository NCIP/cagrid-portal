/**
 * 
 */
package gov.nih.nci.cagrid.portal2.util;

import gov.nih.nci.cagrid.portal2.dao.AddressDao;
import gov.nih.nci.cagrid.portal2.dao.ParticipantDao;
import gov.nih.nci.cagrid.portal2.dao.WorkspaceDao;
import gov.nih.nci.cagrid.portal2.domain.Address;
import gov.nih.nci.cagrid.portal2.domain.Participant;
import gov.nih.nci.cagrid.portal2.domain.ParticipantStatus;
import gov.nih.nci.cagrid.portal2.domain.Workspace;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.persistence.NonUniqueResultException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
@Transactional
public class WorkspacesLoader {

	private static final Log logger = LogFactory.getLog(WorkspacesLoader.class);

	private WorkspaceDao workspaceDao;

	private ParticipantDao participantDao;

	private AddressDao addressDao;

	private List<Workspace> defaultWorkspaces;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("Usage: <path_to_participants_spreadsheet>");
			System.exit(1);
		} else {
			try {

				ApplicationContext ctx = new ClassPathXmlApplicationContext(
						new String[] { "classpath:applicationContext-db.xml",
								"classpath:applicationContext-workspaces.xml" });

				WorkspacesLoader loader = (WorkspacesLoader) ctx
						.getBean("workspacesLoader");
				loader.load(new FileInputStream(new File(args[0])));

			} catch (Exception ex) {
				ex.printStackTrace();
				System.exit(1);
			}
		}
		System.exit(0);
	}

	public void load(InputStream inputStream) throws IOException {

		// Delete existing workspaces and participants
		for (Workspace workspace : getWorkspaceDao().getAll()) {
			for (Participant participant : workspace.getParticipants()) {
				getParticipantDao().delete(participant);
			}
			workspace.setParticipants(null);
			getWorkspaceDao().delete(workspace);
		}

		HSSFWorkbook excelBook = new HSSFWorkbook(inputStream);
		for (Workspace workspace : getDefaultWorkspaces()) {

			HSSFSheet sheet = excelBook.getSheet(workspace.getAbbreviation());

			if (sheet == null) {
				throw new RuntimeException(
						"Couldn't find worksheet with name '"
								+ workspace.getAbbreviation() + "'");
			} else {
				logger.debug("Populating workspace '"
						+ workspace.getAbbreviation() + "'");

				// start from 2 as Row 1 is header
				for (int i = 1; i <= sheet.getLastRowNum(); i++) {
					HSSFRow dataRow = sheet.getRow(i);
					Participant participant = buildParticipant(dataRow);
					participant.getWorkspaces().add(workspace);
					workspace.getParticipants().add(participant);
				}
				getWorkspaceDao().save(workspace);
			}
		}
	}

	private Participant buildParticipant(HSSFRow dataRow) {

		Participant participant = new Participant();
		participant.setName(getDataCellValue(dataRow.getCell((short) 0)));
		participant.setInstitution(getDataCellValue(dataRow.getCell((short) 1)));
		participant.setHomepageUrl(getDataCellValue(dataRow.getCell((short) 2)));
		
		String status = getDataCellValue(dataRow.getCell((short) 3));
		if("Voluntary".equals(status)){
			participant.setStatus(ParticipantStatus.VOLUNTARY);
		}else if("Funded".equals(status)){
			participant.setStatus(ParticipantStatus.FUNDED);
		}else{
			participant.setStatus(ParticipantStatus.UNKNOWN);
		}
		
		participant.setPhone(getDataCellValue(dataRow.getCell((short) 10)));
		participant.setEmailAddress(getDataCellValue(dataRow.getCell((short) 11)));
		
		Address address = new Address();
		address.setStreet1(getDataCellValue(dataRow.getCell((short) 4)));
		address.setStreet2(getDataCellValue(dataRow.getCell((short) 5)));
		address.setLocality(getDataCellValue(dataRow.getCell((short) 6)));
		address.setStateProvince(getDataCellValue(dataRow.getCell((short) 7)));
		address.setCountry(getDataCellValue(dataRow.getCell((short) 8)));
		address.setPostalCode(getDataCellValue(dataRow.getCell((short) 9)));
		try{
			Address existing = getAddressDao().getByExample(address);
			if(existing != null){
				address = existing;
			}else{
				getAddressDao().save(address);
			}
			participant.setAddress(address);
		}catch(NonUniqueResultException ex){
			logger.debug("Bad address for participant '" + participant.getName() + "'");
		}
		getParticipantDao().save(participant);
		
		return participant;
	}

	private String getDataCellValue(HSSFCell cell) {
		if (cell != null) {
			if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
				return new String(""
						+ new Double(cell.getNumericCellValue()).longValue());
			else
				return cell.getStringCellValue();
		} else
			return null;
	}

	public AddressDao getAddressDao() {
		return addressDao;
	}

	public void setAddressDao(AddressDao addressDao) {
		this.addressDao = addressDao;
	}

	public ParticipantDao getParticipantDao() {
		return participantDao;
	}

	public void setParticipantDao(ParticipantDao participantDao) {
		this.participantDao = participantDao;
	}

	public WorkspaceDao getWorkspaceDao() {
		return workspaceDao;
	}

	public void setWorkspaceDao(WorkspaceDao workspaceDao) {
		this.workspaceDao = workspaceDao;
	}

	public List<Workspace> getDefaultWorkspaces() {
		return defaultWorkspaces;
	}

	public void setDefaultWorkspaces(List<Workspace> defaultWorkspaces) {
		this.defaultWorkspaces = defaultWorkspaces;
	}

}
