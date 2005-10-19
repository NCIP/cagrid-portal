package gov.nih.nci.cagrid.gums.service;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.FaultUtil;
import gov.nih.nci.cagrid.gums.common.GUMSObject;
import gov.nih.nci.cagrid.gums.ifs.bean.ApplicationNotFoundFault;
import gov.nih.nci.cagrid.gums.ifs.bean.Attribute;
import gov.nih.nci.cagrid.gums.ifs.bean.AttributeDescriptor;
import gov.nih.nci.cagrid.gums.ifs.bean.InvalidApplicationFault;
import gov.nih.nci.cagrid.gums.ifs.bean.UserApplication;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.globus.wsrf.utils.FaultHelper;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class ApplicationManager extends GUMSObject {

	public static final String ALL_APPLICATIONS = "*";

	public static final String APPROVED = "A";

	public static final String REJECTED = "R";

	public static final String PENDING_REVIEW = "P";

	private static final String APPLICATIONS_TABLE = "USER_APPLICATIONS";

	private static final String USER_APPLICATION_ATTRIBUTES = "USER_APPLICATION_ATTRIBUTES";

	private static final String REGISTRATION_MESSAGE = "Thank you for your applicaiton, after your application is reviewed you will be contacted via email with the result.";
	
	private Database db;

	private boolean dbBuilt = false;

	public ApplicationManager(Database db){
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
					.executeQuery("select count(*) from " + APPLICATIONS_TABLE
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

	public UserApplication getApplication(String username)
			throws GUMSInternalFault,ApplicationNotFoundFault {
		this.buildDatabase();
		UserApplication app = new UserApplication();
		app.setUsername(username);
		Connection c = null;
		try {
			c = db.getConnectionManager().getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select * from " + APPLICATIONS_TABLE
					+ " where username='" + username + "'");
			if (rs.next()) {
				app.setPassword(rs.getString("PASSWORD"));
				app.setEmail(rs.getString("EMAIL"));
				app.setStatus(rs.getString("STATUS"));
			} else {
				app = null;
			}

			rs.close();
			s.close();

		} catch (Exception e) {
			logError(e.getMessage(), e);
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Unexpected Database Error, could not obtain the application for the user "
							+ username + ".");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault)helper.getFault();
			throw fault;
		} finally {
			db.getConnectionManager().releaseConnection(c);
		}
		if (app == null) {
			ApplicationNotFoundFault fault = new ApplicationNotFoundFault();
			fault.setFaultString("The application for the user " + username
					+ " does not exist.");
			throw fault;
		}
		List info = getUserInformation(username);
		Attribute[] atts = new Attribute[info.size()];
		for (int i = 0; i < info.size(); i++) {
			atts[i] = (Attribute) info.get(i);
		}
		app.setUserAttributes(atts);
		return app;
	}

	private List getUserInformation(String username) throws GUMSInternalFault {
		this.buildDatabase();
		List l = new ArrayList();
		Connection c = null;
		try {
			c = db.getConnectionManager().getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select * from "
					+ USER_APPLICATION_ATTRIBUTES + " where username='" + username
					+ "'");
			while (rs.next()) {
				Attribute att = new Attribute();
				AttributeDescriptor des = new AttributeDescriptor(rs.getString("NAMESPACE"),rs.getString("NAME"));
				att.setAttributeDesc(des);
				att.setAttributeXML(rs.getString("XML"));
				l.add(att);
			}

			rs.close();
			s.close();

		} catch (Exception e) {
			logError(e.getMessage(), e);
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Unexpected Database Error, could not obtain the attributes for the user "
							+ username + ".");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault)helper.getFault();
			throw fault;
		} finally {
			db.getConnectionManager().releaseConnection(c);
		}

		return l;
	}

	public UserApplication[] getApplications(String status)
			throws GUMSInternalFault {
		this.buildDatabase();
		StringBuffer sql = new StringBuffer();
		sql.append("select USERNAME from " + APPLICATIONS_TABLE);
		if (!status.equals(ALL_APPLICATIONS)) {
			sql.append(" where STATUS='" + status + "'");
		}

		Connection c = null;
		List l = new ArrayList();
		try {
			c = db.getConnectionManager().getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery(sql.toString());
			while (rs.next()) {
				l.add(rs.getString(1));
			}
			rs.close();
			s.close();

		} catch (Exception e) {
			logError(e.getMessage(), e);
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Unexpected Database Error, could not obtain the applications.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault)helper.getFault();
			throw fault;
		} finally {
			db.getConnectionManager().releaseConnection(c);
		}

		UserApplication[] apps = new UserApplication[l.size()];
		for (int i = 0; i < l.size(); i++) {
			try{
			apps[i] = getApplication((String) l.get(i));
			}catch (ApplicationNotFoundFault e) {
				FaultUtil.printFault(e);
			}
		}
		return apps;
	}

	private void insertApplication(UserApplication app)
	throws GUMSInternalFault{
		this.buildDatabase();
		db.update("INSERT INTO " + APPLICATIONS_TABLE + " VALUES('"
				+ app.getUsername() + "','" + app.getPassword() + "','"
				+ PENDING_REVIEW + "','" + app.getEmail() + "')");
		Attribute[] atts = app.getUserAttributes();
		for (int i = 0; i < atts.length; i++) {
			this.insertUserAttribute(app.getUsername(), atts[i]);
		}
	}

	private void insertUserAttribute(String username, Attribute att)
			throws GUMSInternalFault{
		this.buildDatabase();
		db.update("INSERT INTO " + USER_APPLICATION_ATTRIBUTES + " VALUES('"
				+ username + "','" + att.getAttributeDesc().getNamespace() + "','"
				+ att.getAttributeDesc().getName() + "','" + att.getAttributeXML()
				+ "')");

	}

	private void buildDatabase() throws GUMSInternalFault {
		if (!dbBuilt) {
			if (!this.db.tableExists(APPLICATIONS_TABLE)) {
				String applications = "CREATE TABLE " + APPLICATIONS_TABLE
						+ " (" + "USERNAME VARCHAR(255) NOT NULL PRIMARY KEY,"
						+ "PASSWORD VARCHAR(255) NOT NULL,"
						+ "STATUS VARCHAR(1) NOT NULL,"
						+ "EMAIL VARCHAR(255) NOT NULL, "
						+ "INDEX document_index (USERNAME));";
				db.update(applications);
			}
			if (!this.db.tableExists(USER_APPLICATION_ATTRIBUTES)) {
				String info = "CREATE TABLE " + USER_APPLICATION_ATTRIBUTES + " ("
						+ "USERNAME VARCHAR(255) NOT NULL,"
						+ "NAMESPACE VARCHAR(255) NOT NULL,"
						+ "NAME VARCHAR(255) NOT NULL, "
						+ "XML TEXT NOT NULL, "
						+ "INDEX document_index (USERNAME));";
				db.update(info);
			}
			this.dbBuilt = true;
		}
	}

	private void changeApplicationStatus(String username, String status)
			throws GUMSInternalFault {
		this.buildDatabase();
		db.update("update " + APPLICATIONS_TABLE + " SET STATUS='" + status
				+ "' where USERNAME='" + username + "'");
	}


	public String registerUser(UserApplication app) throws GUMSInternalFault,InvalidApplicationFault {
		this.buildDatabase();
		/*
		if (userManager.userExists(app.getUsername())) {
			InvalidApplicationFault fault = new InvalidApplicationFault();
			fault.setFaultString("Cannot register the user "
					+ app.getUsername() + " the username already exists.");
			throw fault;
		}
		*/
		if (applicationExists(app.getUsername())) {
			InvalidApplicationFault fault = new InvalidApplicationFault();
			fault.setFaultString("Cannot register the user "
					+ app.getUsername()
					+ ", an application for that username has already been received.");
			throw fault;
		} 
			int ulen = app.getUsername().length();
			if ((ulen < GUMSManager.getInstance().getGUMSConfiguration().getMinimumUsernameLength())
					|| (ulen > GUMSManager.getInstance().getGUMSConfiguration().getMaximumUsernameLength())) {
				InvalidApplicationFault fault = new InvalidApplicationFault();
				fault.setFaultString("Cannot register the user "
						+ app.getUsername() + " the username must be between "
						+ GUMSManager.getInstance().getGUMSConfiguration().getMinimumUsernameLength()
						+ " and "
						+ GUMSManager.getInstance().getGUMSConfiguration().getMaximumUsernameLength()
						+ " characters.");
				throw fault;
			}

			int plen = app.getPassword().length();
			if ((plen < GUMSManager.getInstance().getGUMSConfiguration().getMinimumPasswordLength())
					|| (plen > GUMSManager.getInstance().getGUMSConfiguration().getMaximumPasswordLength())) {
				InvalidApplicationFault fault = new InvalidApplicationFault();
				fault.setFaultString("Cannot register the user "
						+ app.getUsername() + " the password must be between "
						+ GUMSManager.getInstance().getGUMSConfiguration().getMinimumPasswordLength()
						+ " and "
						+ GUMSManager.getInstance().getGUMSConfiguration().getMaximumPasswordLength()
						+ " characters.");
				throw fault;
			}
			insertApplication(app);

		return REGISTRATION_MESSAGE;
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