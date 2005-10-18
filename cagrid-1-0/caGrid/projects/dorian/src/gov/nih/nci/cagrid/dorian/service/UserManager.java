package gov.nih.nci.cagrid.gums.service;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.GUMSObject;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: UserManager.java,v 1.3 2005-10-18 23:23:48 langella Exp $
 */
public class UserManager extends GUMSObject {

	private static final String USERS_TABLE = "USERS";

	private static final String USER_NOTES_TABLE = "USER_NOTES";

	private Database db;

	private boolean dbBuilt = false;

	public UserManager(Database db) {
		this.db = db;
	}

	private void buildDatabase() throws GUMSInternalFault {
		if (!dbBuilt) {
			if (!this.db.tableExists(USERS_TABLE)) {
				String users = "CREATE TABLE " + USERS_TABLE + " ("
						+ "UID VARCHAR(255) NOT NULL PRIMARY KEY,"
						+ "GID VARCHAR(255) NOT NULL,"
						+ "STATUS VARCHAR(10) NOT NULL,"
						+ "ADMIN VARCHAR(5) NOT NULL, "
						+ "EMAIL VARCHAR(255) NOT NULL, "
						+ "START_DATE VARCHAR(20) NOT NULL,"
						+ "INDEX document_index (UID));";
				db.update(users);
			}
			if (!this.db.tableExists(USER_NOTES_TABLE)) {
				String userNotes = "CREATE TABLE "
						+ USER_NOTES_TABLE
						+ " ("
						+ "UID VARCHAR(255) NOT NULL,"
						+ "NOTERY_UID VARCHAR(255) NOT NULL,"
						+ "NOTERY_GID VARCHAR(255) NOT NULL,"
						+ "DATE_CREATED VARCHAR(20) NOT NULL,NOTE_SUBJECT VARCHAR(255) NOT NULL,"
						+ "NOTE TEXT NOT NULL,"
						+ "INDEX document_index (UID));";
				db.update(userNotes);
			}
			this.dbBuilt = true;
		}
	}
   
}