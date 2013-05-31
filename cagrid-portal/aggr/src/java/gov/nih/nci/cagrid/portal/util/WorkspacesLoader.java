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
package gov.nih.nci.cagrid.portal.util;

import gov.nih.nci.cagrid.portal.dao.AddressDao;
import gov.nih.nci.cagrid.portal.dao.ParticipantDao;
import gov.nih.nci.cagrid.portal.dao.WorkspaceDao;
import gov.nih.nci.cagrid.portal.domain.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NonUniqueResultException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
@Transactional
public class WorkspacesLoader {

	private static final boolean DEBUG = false;

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
			for (Participation participation : workspace.getParticipation()) {
				getParticipantDao().delete(participation.getParticipant());
			}
			workspace.setParticipation(null);
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
				if (DEBUG) {
					logger.debug("Populating workspace '"
							+ workspace.getAbbreviation() + "'");
				}

				// start from 2 as Row 1 is header
				for (int i = 1; i <= sheet.getLastRowNum(); i++) {
					HSSFRow dataRow = sheet.getRow(i);

					Participant participant = new Participant();

					try {

						participant.setName(getDataCellValue(dataRow
								.getCell((short) 0)));
						participant.setInstitution(getDataCellValue(dataRow
								.getCell((short) 1)));
						participant.setHomepageUrl(getDataCellValue(dataRow
								.getCell((short) 2)));

						participant.setPhone(getDataCellValue(dataRow
								.getCell((short) 10)));
						participant.setEmailAddress(getDataCellValue(dataRow
								.getCell((short) 11)));

						if (!isValidParticipant(participant)) {
							throw new InvalidParticipantException(
									"Invalid participant on row "
											+ dataRow.getRowNum());
						}

						Participant existing = null;
						try {
							existing = getParticipantDao().getByExample(
									participant);
						} catch (NonUniqueResultException ex) {
							String msg = "Bad participant '"
									+ participant.getName() + "'";
							throw new RuntimeException(msg, ex);
						}

						if (existing != null) {
							participant = existing;
						}

						getParticipantDao().save(participant);

						Address address = new Address();
						address.setStreet1(getDataCellValue(dataRow
								.getCell((short) 4)));
						address.setStreet2(getDataCellValue(dataRow
								.getCell((short) 5)));
						address.setLocality(getDataCellValue(dataRow
								.getCell((short) 6)));
						address.setStateProvince(getDataCellValue(dataRow
								.getCell((short) 7)));
						address.setCountry(getDataCellValue(dataRow
								.getCell((short) 8)));
						address.setPostalCode(getDataCellValue(dataRow
								.getCell((short) 9)));
						try {
							Address existingAddr = getAddressDao()
									.getByExample(address);

							if (existingAddr != null) {
								address = existingAddr;
							} else {
								getAddressDao().save(address);
							}
							participant.setAddress(address);
							getParticipantDao().save(participant);

						} catch (NonUniqueResultException ex) {
							if (DEBUG) {
								String msg = "Bad address for participant '"
										+ participant.getName() + "'";
								logger.warn(msg);
							}
						}

					} catch (InvalidParticipantException ex) {
						if(DEBUG){
							logger.error(ex.getMessage());
						}
						continue;
					}

					getWorkspaceDao().save(workspace);

					String status = getDataCellValue(dataRow.getCell((short) 3));
					Participation participation = new Participation();
					if ("Voluntary".equals(status)) {
						participation.setStatus(ParticipantStatus.VOLUNTARY);
					} else if ("Funded".equals(status)) {
						participation.setStatus(ParticipantStatus.FUNDED);
					} else {
						participation.setStatus(ParticipantStatus.UNKNOWN);
					}
					participation.setWorkspace(workspace);
					participation.setParticipant(participant);
					getParticipantDao().getHibernateTemplate().save(
							participation);

					participant.getParticipation().add(participation);
					getParticipantDao().save(participant);

					workspace.getParticipation().add(participation);

				}
				getWorkspaceDao().save(workspace);
			}
		}
	}

	private boolean isValidParticipant(Participant participant) {
		boolean valid = true;
		if (participant.getName() == null) {
			valid = false;
		}
		return valid;
	}

	private String getDataCellValue(HSSFCell cell) {
		String returnValue = null;

		if (cell != null) {
			if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
				returnValue = String.valueOf(new Double(cell.getNumericCellValue()).intValue());
			} else {
				returnValue = cell.getStringCellValue();
			}
		}

		if (returnValue != null) {
			returnValue = returnValue.trim();
		}
		if (PortalUtils.isEmpty(returnValue)) {
			returnValue = null;
		}

		return returnValue;
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
