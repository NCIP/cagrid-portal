package gov.nih.nci.cagrid.gums.service;

import gov.nih.nci.cagrid.gums.bean.ApplicationNotFoundFault;
import gov.nih.nci.cagrid.gums.bean.Attribute;
import gov.nih.nci.cagrid.gums.bean.AttributeDescriptor;
import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.bean.InvalidApplicationFault;
import gov.nih.nci.cagrid.gums.bean.UserApplication;
import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.FaultUtil;
import gov.nih.nci.cagrid.gums.common.GUMSObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.globus.wsrf.utils.FaultHelper;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: UserManager.java,v 1.1 2005-10-10 19:13:17 langella Exp $
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