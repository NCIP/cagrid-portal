package gov.nih.nci.cagrid.gums.idm;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.GUMSObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.globus.wsrf.utils.FaultHelper;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Langella </A>
 * @version $Id: IdentityManager.java,v 1.1 2005-10-18 23:23:48 langella Exp $
 */
public class IdentityManager extends GUMSObject {

	public static final String ALL_APPLICATIONS = "*";

	public static final String APPROVED = "A";

	public static final String REJECTED = "R";

	public static final String PENDING_REVIEW = "P";

	private static final String IDM_USERS_TABLE = "IDM_USERS";


	private static final String REGISTRATION_MESSAGE = "Thank you for your applicaiton, after your application is reviewed you will be contacted via email with the result.";
	
	private Database db;

	private boolean dbBuilt = false;

	public IdentityManager(Database db){
		this.db = db;
	}

	public boolean applicationExists(String username) throws GUMSInternalFault {
		this.buildDatabase();
		Connection c = null;
		boolean exists = false;
		try {
			c = db.getConnectionManager().getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s
					.executeQuery("select count(*) from " + IDM_USERS_TABLE
							+ " where username='" + username + "'");
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count > 0) {
					exists = true;
				}
			}
			rs.close();
			s.close();

		} catch (Exception e) {
			logError(e.getMessage(), e);
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Unexpected Database Error, Error determining if the user " + username
					+ " exists.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault)helper.getFault();
			throw fault;
		} finally {
			db.getConnectionManager().releaseConnection(c);
		}
		return exists;
	}

	

	private void insertUser(String email, String password,String firstName, String lastName, String organization, String address, String address2, String city, String state, String zipcode,boolean admin, String status)
	throws GUMSInternalFault{
		this.buildDatabase();
		/*
		
		db.update("INSERT INTO " + IDM_USERS_TABLE + " VALUES('"
				+ app.getUsername() + "','" + app.getPassword() + "','"
				+ PENDING_REVIEW + "','" + app.getEmail() + "')");
		Attribute[] atts = app.getUserAttributes();
		for (int i = 0; i < atts.length; i++) {
			this.insertUserAttribute(app.getUsername(), atts[i]);
		}
		*/
	}


	private void buildDatabase() throws GUMSInternalFault {
		if (!dbBuilt) {
			if (!this.db.tableExists(IDM_USERS_TABLE)) {
				String applications = "CREATE TABLE " + IDM_USERS_TABLE
						+ " (" + "EMAIL VARCHAR(255) NOT NULL PRIMARY KEY,"
						+ "PASSWORD VARCHAR(255) NOT NULL,"
						+ "FIRST_NAME VARCHAR(255) NOT NULL,"
						+ "LAST_NAME VARCHAR(255) NOT NULL,"
						+ "ORGANIZATION VARCHAR(255) NOT NULL,"
						+ "ADDRESS VARCHAR(255) NOT NULL,"
						+ "ADDRESS2 VARCHAR(255) NOT NULL,"
						+ "CITY VARCHAR(255) NOT NULL,"
						+ "STATE VARCHAR(255) NOT NULL,"
						+ "ZIP_CODE VARCHAR(255) NOT NULL,"
						+ "STATUS VARCHAR(1) NOT NULL,"
						+ "ADMIN VARCHAR(5) NOT NULL,"
						+ "INDEX document_index (EMAIL));";
				db.update(applications);
			}
			this.dbBuilt = true;
		}
	}

	private void changeApplicationStatus(String username, String status)
			throws GUMSInternalFault {
		this.buildDatabase();
		db.update("update " + IDM_USERS_TABLE + " SET STATUS='" + status
				+ "' where USERNAME='" + username + "'");
	}


	
/*
	public void approveApplication(String approver, ApplicationReview review)
			throws GumsException {
		this.buildDatabase();
		RegistrationApplication app = null;

		try {
			app = this.getApplication(review.getUsername());
		} catch (Exception e) {
			this.logError(e.getMessage(), e);
			throw new GumsException(
					"Could not approve the application for the user "
							+ review.getUsername()
							+ " the application could not be found.");
		}

		if (!app.getStatus().equals(APPROVED)) {
			UserNote[] notes = new UserNote[1];
			notes[0] = new UserNote();
			notes[0].setNoteryGridId(approver);
			notes[0].setSubject("Account Creation");
			notes[0].setNote(review.getReviewerNotes());
			notes[0].setDate(new GregorianCalendar());
			this.userManager.addUser(app.getUsername(), app.getPassword(), app
					.getEmail(), false, notes);
			User user = this.userManager.getUserByUsername(app.getUsername());
			try {
				UserInformation[] info = app.getUserInfo();
				if (info != null) {

					BasicAuthCredential bac = new BasicAuthCredential();
					bac.setUsername(UserManager.GUMS_USER);
					bac.setPassword(userManager.getUserByUsername(
							UserManager.GUMS_USER).getPassword());
					GridUserManagementServer server = new GridUserManagementServer(
							bac);
					GlobusCredential cred = null;
					if (server.hasProxy()) {
						cred = server.getProxy();
					} else {
						cred = server.createProxy(12);
					}
					List services = conf.getGumsOptions()
							.getAttributeServices();
					for (int i = 0; i < services.size(); i++) {
						String serviceId = (String) services.get(i);
						AttributeManagementClient client = new AttributeManagementClient(
								new URL(serviceId), cred);
						for (int j = 0; j < info.length; j++) {
							client.addAttribute(user.getGridId(), info[j]
									.getInformationXML());
						}
					}
				}
			} catch (Exception e) {
				logError(e.getMessage(), e);
				userManager.removeUser(review.getUsername());
				throw new GumsException(
						"Could not approve the application for the user "
								+ review.getUsername()
								+ " an unexpected error occurred in submitting the user attributes.");
			}
		} else {
			throw new GumsException(
					"Could not approve the application for the user "
							+ review.getUsername()
							+ " the application has already been approved.");
		}
		try {
			this.changeApplicationStatus(review.getUsername(), APPROVED);
		} catch (Exception e) {
			logError(e.getMessage(), e);
			userManager.removeUser(review.getUsername());
			throw new GumsException(
					"Could not approve the application for the user "
							+ review.getUsername()
							+ " an unexpected error occurred.");
		}
		this.sendApprovalNotification(userManager.getUserByGridId(approver),
				userManager.getUserByUsername(review.getUsername()), review
						.getReviewerUserNotes());
	}
*/
	/*
	public void rejectApplication(String rejector, ApplicationReview review)
			throws GumsException {
		this.buildDatabase();
		try {
			this.changeApplicationStatus(review.getUsername(), REJECTED);
		} catch (Exception e) {
			logError(e.getMessage(), e);
			userManager.removeUser(review.getUsername());
			throw new GumsException(
					"Could not reject the application for the user "
							+ review.getUsername()
							+ " an unexpected error occurred.");
		}
		this.sendRejectionNotification(userManager.getUserByGridId(rejector),
				getApplication(review.getUsername()), review
						.getReviewerUserNotes());
	}

	private void sendRejectionNotification(User reviewer,
			RegistrationApplication app, String note) {
		try {
			Mailer mailer = GumsResourceManager.getInstance().getMailer();
			String userEmail = app.getEmail();
			String reviewerEmail = reviewer.getEmail();

			// TODO: THIS SHOULD COME FROM A TEMPLATE
			StringBuffer sb = new StringBuffer();
			sb.append("We regret to inform you that your application for ");
			sb.append("a grid account has been rejected.");
			if ((note != null) && (note.trim().length() > 0)) {
				sb
						.append("  The reviewer of you application included the following note:\n\n");
				sb.append("    " + note + "\n\n");
			}
			sb
					.append("  If you have any questions regarding your application, you can email your reviewer at "
							+ reviewerEmail + ".\n\n");
			sb.append("--Administration");
			mailer.send(userEmail, reviewerEmail,
					"Grid Account Application Rejected", sb.toString());
		} catch (Exception e) {
			logError(e.getMessage(), e);
		}
	}
*/
	/*
	private void sendApprovalNotification(User reviewer, User user, String note) {
		try {
			Mailer mailer = GumsResourceManager.getInstance().getMailer();
			String userEmail = user.getEmail();
			String reviewerEmail = reviewer.getEmail();

			// TODO: THIS SHOULD COME FROM A TEMPLATE
			StringBuffer sb = new StringBuffer();
			sb
					.append("We are pleased to inform you that your application for ");
			sb
					.append("a cagrid account has been approved.  An account under the ");
			sb.append("username ").append(user.getUsername()).append(
					" has been created.");
			sb
					.append("  Your account will allow you access to your grid credentials.");
			sb.append("  Your grid id is:\n\n");
			sb.append("   " + user.getGridId() + "\n\n");
			if ((note != null) && (note.trim().length() > 0)) {
				sb
						.append("  The reviewer of you application included the following note:\n\n");
				sb.append("    " + note + "\n\n");
			}
			sb
					.append("  If you have any questions regarding your application, you can email your reviewer at "
							+ reviewerEmail + ".\n\n");
			sb.append("--Administration");
			mailer.send(userEmail, reviewerEmail,
					"Grid Account Application Approved", sb.toString());
		} catch (Exception e) {
			logError(e.getMessage(), e);
		}
	}
*/
}