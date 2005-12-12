package gov.nih.nci.cagrid.gums.ifs;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.ca.CertificateAuthority;
import gov.nih.nci.cagrid.gums.common.AddressValidator;
import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.FaultHelper;
import gov.nih.nci.cagrid.gums.common.GUMSObject;
import gov.nih.nci.cagrid.gums.common.IOUtils;
import gov.nih.nci.cagrid.gums.common.ca.CertUtil;
import gov.nih.nci.cagrid.gums.common.ca.KeyUtil;
import gov.nih.nci.cagrid.gums.ifs.bean.CredentialsFault;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUser;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUserFilter;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUserRole;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUserStatus;
import gov.nih.nci.cagrid.gums.ifs.bean.InvalidPasswordFault;
import gov.nih.nci.cagrid.gums.ifs.bean.InvalidUserFault;
import gov.nih.nci.cagrid.gums.ifs.bean.TrustedIdP;

import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.bouncycastle.jce.PKCS10CertificationRequest;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class UserManager extends GUMSObject {

	private static final String USERS_TABLE = "IFS_USERS";

	private Database db;

	private boolean dbBuilt = false;

	private CredentialsManager credentialsManager;

	private IFSConfiguration conf;

	private CertificateAuthority ca;

	private TrustManager tm;

	public UserManager(Database db, IFSConfiguration conf,
			CertificateAuthority ca, TrustManager tm) {
		this.db = db;
		this.tm = tm;
		this.credentialsManager = new CredentialsManager(db);
		this.conf = conf;
		this.ca = ca;
	}

	public synchronized boolean determineIfUserExists(long idpId, String uid)
			throws GUMSInternalFault {
		buildDatabase();
		Connection c = null;
		boolean exists = false;
		try {
			c = db.getConnectionManager().getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select count(*) from " + USERS_TABLE
					+ " WHERE IDP_ID=" + idpId + " AND UID='" + uid + "'");
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count > 0) {
					exists = true;
				}
			}
			rs.close();
			s.close();

		} catch (Exception e) {
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Unexpected Database Error");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.getConnectionManager().releaseConnection(c);
		}
		return exists;
	}

	private String getCredentialsManagerUID(long idpId, String uid) {
		return "[IdPId=" + idpId + ", UID=" + uid + "]";
	}

	public PrivateKey getUsersPrivateKey(IFSUser user) throws GUMSInternalFault {
		try {
			return this.credentialsManager.getPrivateKey(
					getCredentialsManagerUID(user.getIdPId(), user.getUID()),
					null);
		} catch (InvalidPasswordFault e) {
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Error loading the user " + user.getGridId()
					+ "'s private key.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		}
	}

	public synchronized IFSUser renewUserCredentials(IFSUser user)
			throws GUMSInternalFault, InvalidUserFault {
		X509Certificate cert = createUserCredentials(user.getIdPId(), user
				.getUID());
		user.setGridId(subjectToIdentity(cert.getSubjectDN().getName()));
		try {
			this.updateUser(user);
		} catch (InvalidUserFault iuf) {
			this.credentialsManager.deleteCredentials(getCredentialsManagerUID(
					user.getIdPId(), user.getUID()));
			throw iuf;
		} catch (GUMSInternalFault gif) {
			this.credentialsManager.deleteCredentials(getCredentialsManagerUID(
					user.getIdPId(), user.getUID()));
			throw gif;
		}
		try {
			user.setCertificate(new gov.nih.nci.cagrid.gums.ifs.bean.X509Certificate(CertUtil.writeCertificateToString(cert)));
		} catch (IOException ioe) {
			this.credentialsManager.deleteCredentials(getCredentialsManagerUID(
					user.getIdPId(), user.getUID()));
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Error renewing credentials.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(ioe);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;

		}
		return user;
	}

	public static String getUserSubject(String caSubject, long idpId, String uid) {
		int caindex = caSubject.lastIndexOf(",");
		String caPreSub = caSubject.substring(0, caindex);
		String sub = caPreSub + ",OU=IdP [" + idpId + "],CN=" + uid;
		return sub;
	}

	private synchronized X509Certificate createUserCredentials(long idpId,
			String uid) throws GUMSInternalFault {
		try {

			String caSubject = ca.getCACertificate().getSubjectDN().getName();
			String sub = getUserSubject(caSubject, idpId, uid);
			Calendar c = new GregorianCalendar();
			Date start = c.getTime();
			Date end = conf.getCredentialsValid();
			if (end.after(ca.getCACertificate().getNotAfter())) {
				end = ca.getCACertificate().getNotAfter();
			}

			KeyPair pair = KeyUtil.generateRSAKeyPair1024();

			PKCS10CertificationRequest req = CertUtil
					.generateCertficateRequest(sub, pair);
			X509Certificate cert = ca.requestCertificate(req, start, end);
			this.credentialsManager.deleteCredentials(getCredentialsManagerUID(
					idpId, uid));
			this.credentialsManager.addCredentials(getCredentialsManagerUID(
					idpId, uid), null, cert, pair.getPrivate());
			return cert;
		} catch (Exception e) {
			logError(e.getMessage(), e);
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Error creating credentials.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		}
	}

	private StringBuffer appendWhereOrAnd(boolean firstAppended,
			StringBuffer sql) {
		if (firstAppended) {
			sql.append(" AND ");
		} else {
			sql.append(" WHERE");
		}
		return sql;
	}

	public synchronized IFSUser getUser(long idpId, String uid)
			throws GUMSInternalFault, InvalidUserFault {
		this.buildDatabase();
		IFSUser user = new IFSUser();
		Connection c = null;
		try {
			c = db.getConnectionManager().getConnection();
			Statement s = c.createStatement();

			StringBuffer sql = new StringBuffer();
			sql.append("select * from " + USERS_TABLE + " WHERE IDP_ID="
					+ idpId + " AND UID='" + uid + "'");
			ResultSet rs = s.executeQuery(sql.toString());
			if (rs.next()) {
				user.setIdPId(rs.getLong("IDP_ID"));
				user.setUID(rs.getString("UID"));
				user.setGridId(rs.getString("GID"));
				String email = rs.getString("EMAIL");
				if ((email != null) && (!email.equals("null"))) {
					user.setEmail(email);
				}
				user.setUserStatus(IFSUserStatus.fromValue(rs
						.getString("STATUS")));
				String role = rs.getString("ROLE");
				user.setUserRole(IFSUserRole.fromValue(role));
				X509Certificate cert = credentialsManager
						.getCertificate(getCredentialsManagerUID(user
								.getIdPId(), user.getUID()));
				user.setCertificate(new gov.nih.nci.cagrid.gums.ifs.bean.X509Certificate(CertUtil.writeCertificateToString(cert)));
			} else {
				InvalidUserFault fault = new InvalidUserFault();
				fault.setFaultString("No such user "
						+ getCredentialsManagerUID(user.getIdPId(), user
								.getUID()));
				throw fault;

			}
			rs.close();
			s.close();
		} catch (InvalidUserFault iuf) {
			throw iuf;
		} catch (Exception e) {
			logError(e.getMessage(), e);
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Unexpected Error, could not obtain the user "
					+ getCredentialsManagerUID(user.getIdPId(), user.getUID()));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.getConnectionManager().releaseConnection(c);
		}
		return user;
	}

	public synchronized IFSUser getUser(String gridId)
			throws GUMSInternalFault, InvalidUserFault {
		this.buildDatabase();
		IFSUser user = new IFSUser();
		Connection c = null;
		try {
			c = db.getConnectionManager().getConnection();
			Statement s = c.createStatement();

			StringBuffer sql = new StringBuffer();
			sql.append("select * from " + USERS_TABLE + " WHERE GID='" + gridId
					+ "'");
			ResultSet rs = s.executeQuery(sql.toString());
			if (rs.next()) {
				user.setIdPId(rs.getLong("IDP_ID"));
				user.setUID(rs.getString("UID"));
				user.setGridId(rs.getString("GID"));
				String email = rs.getString("EMAIL");
				if ((email != null) && (!email.equals("null"))) {
					user.setEmail(email);
				}
				user.setUserStatus(IFSUserStatus.fromValue(rs
						.getString("STATUS")));
				String role = rs.getString("ROLE");
				user.setUserRole(IFSUserRole.fromValue(role));
				X509Certificate cert = credentialsManager
						.getCertificate(getCredentialsManagerUID(user
								.getIdPId(), user.getUID()));
				user.setCertificate(new gov.nih.nci.cagrid.gums.ifs.bean.X509Certificate(CertUtil.writeCertificateToString(cert)));
			} else {
				InvalidUserFault fault = new InvalidUserFault();
				fault.setFaultString("No such user " + gridId);
				throw fault;

			}
			rs.close();
			s.close();
		} catch (InvalidUserFault iuf) {
			throw iuf;
		} catch (Exception e) {
			logError(e.getMessage(), e);
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Unexpected Error, could not obtain the user "
					+ gridId);
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.getConnectionManager().releaseConnection(c);
		}
		return user;
	}

	public synchronized IFSUser[] getUsers(IFSUserFilter filter)
			throws GUMSInternalFault {

		this.buildDatabase();
		Connection c = null;
		List users = new ArrayList();
		try {
			c = db.getConnectionManager().getConnection();
			Statement s = c.createStatement();

			StringBuffer sql = new StringBuffer();
			sql.append("select * from " + USERS_TABLE);
			if (filter != null) {
				boolean firstAppended = false;

				if (filter.getIdPId() > 0) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" IDP_ID =" + filter.getIdPId());
				}

				if (filter.getUID() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" UID LIKE '%" + filter.getUID() + "%'");
				}

				if (filter.getGridId() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" GID LIKE '%" + filter.getGridId() + "%'");
				}

				if (filter.getEmail() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" EMAIL LIKE '%" + filter.getEmail() + "%'");
				}

				if (filter.getUserStatus() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" STATUS='" + filter.getUserStatus() + "'");
				}

				if (filter.getUserRole() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" ROLE='" + filter.getUserRole() + "'");
				}
			}

			ResultSet rs = s.executeQuery(sql.toString());
			while (rs.next()) {
				IFSUser user = new IFSUser();
				user.setIdPId(rs.getLong("IDP_ID"));
				user.setUID(rs.getString("UID"));
				user.setGridId(rs.getString("GID"));
				String email = rs.getString("EMAIL");
				if ((email != null) && (!email.equals("null"))) {
					user.setEmail(email);
				}
				user.setUserStatus(IFSUserStatus.fromValue(rs
						.getString("STATUS")));
				String role = rs.getString("ROLE");
				user.setUserRole(IFSUserRole.fromValue(role));
				X509Certificate cert = credentialsManager
						.getCertificate(getCredentialsManagerUID(user
								.getIdPId(), user.getUID()));
				user.setCertificate(new gov.nih.nci.cagrid.gums.ifs.bean.X509Certificate(CertUtil.writeCertificateToString(cert)));
				users.add(user);
			}
			rs.close();
			s.close();

			IFSUser[] list = new IFSUser[users.size()];
			for (int i = 0; i < list.length; i++) {
				list[i] = (IFSUser) users.get(i);
			}
			return list;

		} catch (Exception e) {
			logError(e.getMessage(), e);
			GUMSInternalFault fault = new GUMSInternalFault();
			fault
					.setFaultString("Unexpected Error, could not obtain a list of users");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.getConnectionManager().releaseConnection(c);
		}
	}

	public synchronized IFSUser addUser(IFSUser user) throws GUMSInternalFault,
			CredentialsFault, InvalidUserFault {
		this.buildDatabase();
		if (!determineIfUserExists(user.getIdPId(), user.getUID())) {
			X509Certificate cert = createUserCredentials(user.getIdPId(), user
					.getUID());
			try {
				// Write method for creating and setting a users credentials
				user.setCertificate(new gov.nih.nci.cagrid.gums.ifs.bean.X509Certificate(CertUtil.writeCertificateToString(cert)));
				user
						.setGridId(subjectToIdentity(cert.getSubjectDN()
								.toString()));
				user.setUserRole(IFSUserRole.Non_Administrator);
				user.setUserStatus(IFSUserStatus.Pending);
				if (user.getEmail() != null) {
					try {
						AddressValidator.validateEmail(user.getEmail());
					} catch (IllegalArgumentException e) {
						InvalidUserFault fault = new InvalidUserFault();
						fault.setFaultString(e.getMessage());
						throw fault;
					}
				}
				validateSpecifiedField("UID", user.getUID());
				validateSpecifiedField("Grid Id", user.getGridId());

				db.update("INSERT INTO " + USERS_TABLE + " SET IDP_ID='"
						+ user.getIdPId() + "',UID='" + user.getUID()
						+ "', GID='" + user.getGridId() + "',STATUS='"
						+ user.getUserStatus().toString() + "',ROLE='"
						+ user.getUserRole().toString() + "',EMAIL='"
						+ user.getEmail() + "'");
			} catch (InvalidUserFault iuf) {
				throw iuf;
			} catch (Exception e) {
				try {
					this.removeUser(user.getIdPId(), user.getUID());
				} catch (Exception ex) {

				}

				try {
					this.credentialsManager
							.deleteCredentials(getCredentialsManagerUID(user
									.getIdPId(), user.getUID()));
				} catch (Exception ex) {

				}
				logError(e.getMessage(), e);
				GUMSInternalFault fault = new GUMSInternalFault();
				fault
						.setFaultString("Error adding the user "
								+ getCredentialsManagerUID(user.getIdPId(),
										user.getUID())
								+ " to the IFS, an unexpected database error occurred.");
				FaultHelper helper = new FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (GUMSInternalFault) helper.getFault();
				throw fault;
			}

		} else {
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("Error adding the user, "
					+ getCredentialsManagerUID(user.getIdPId(), user.getUID())
					+ ", the user already exists!!!");
			throw fault;

		}
		return user;
	}

	public synchronized void updateUser(IFSUser u) throws GUMSInternalFault,
			InvalidUserFault {
		this.buildDatabase();
		String credId = getCredentialsManagerUID(u.getIdPId(), u.getUID());
		if (determineIfUserExists(u.getIdPId(), u.getUID())) {
			StringBuffer sb = new StringBuffer();
			sb.append("update " + USERS_TABLE + " SET ");
			int changes = 0;
			IFSUser curr = this.getUser(u.getIdPId(), u.getUID());

			if ((u.getEmail() != null)
					&& (!u.getEmail().equals(curr.getEmail()))) {
				try {
					AddressValidator.validateEmail(u.getEmail());
				} catch (IllegalArgumentException e) {
					InvalidUserFault fault = new InvalidUserFault();
					fault.setFaultString(e.getMessage());
					throw fault;
				}
				if (changes > 0) {
					sb.append(",");
				}
				sb.append("EMAIL='" + u.getEmail() + "'");
				changes = changes + 1;
			}

			if ((u.getGridId() != null)
					&& (!u.getGridId().equals(curr.getGridId()))) {
				if (changes > 0) {
					sb.append(",");
				}
				validateSpecifiedField("Grid Id", u.getGridId());
				sb.append("GID='" + u.getGridId() + "'");
				changes = changes + 1;
			}

			if ((u.getUserStatus() != null)
					&& (!u.getUserStatus().equals(curr.getUserStatus()))) {
				if (changes > 0) {
					sb.append(",");
				}

				if (accountCreated(curr.getUserStatus())
						&& !accountCreated(u.getUserStatus())) {
					InvalidUserFault fault = new InvalidUserFault();
					fault.setFaultString("Error, cannot change " + credId
							+ "'s status from a post-created account status ("
							+ curr.getUserStatus()
							+ ") to a pre-created account status ("
							+ u.getUserStatus() + ").");
					throw fault;
				}

				sb.append("STATUS='" + u.getUserStatus().getValue() + "'");
				changes = changes + 1;
			}

			if ((u.getUserRole() != null)
					&& (!u.getUserRole().equals(curr.getUserRole()))) {
				if (changes > 0) {
					sb.append(",");
				}
				sb.append("ROLE='" + u.getUserRole().getValue() + "'");
				changes = changes + 1;
			}
			sb.append(" where IDP_ID=" + u.getIdPId() + " AND UID='"
					+ u.getUID() + "'");
			if (changes > 0) {
				db.update(sb.toString());
			}

		} else {
			InvalidUserFault fault = new InvalidUserFault();
			fault.setFaultString("Could not update user, the user " + credId
					+ " does not exist.");
			throw fault;
		}
	}

	private boolean accountCreated(IFSUserStatus status) {
		if (status.equals(IFSUserStatus.Pending)) {
			return false;
		} else {
			return true;
		}
	}

	public synchronized void removeUser(IFSUser user) throws GUMSInternalFault,InvalidUserFault {
		this.buildDatabase();
		if (determineIfUserExists(user.getIdPId(), user.getUID())) {
			this.removeUser(user.getIdPId(), user.getUID());
		}else{
			InvalidUserFault fault = new InvalidUserFault();
			fault.setFaultString("Could not remove user, the specified user does not exist.");
			throw fault;
		}
	}

	public synchronized void removeUser(long idpId, String uid)
			throws GUMSInternalFault {
		this.buildDatabase();
		db.update("delete from " + USERS_TABLE + " WHERE IDP_ID=" + idpId
				+ " AND UID='" + uid + "'");
	}

	private void validateSpecifiedField(String fieldName, String name)
			throws InvalidUserFault {
		if ((name == null) || (name.length() == 0)) {
			InvalidUserFault fault = new InvalidUserFault();
			fault.setFaultString("No " + fieldName + " specified.");
			throw fault;
		}
	}

	public void buildDatabase() throws GUMSInternalFault {
		if (!dbBuilt) {
			if (!this.db.tableExists(USERS_TABLE)) {
				String users = "CREATE TABLE " + USERS_TABLE + " ("
						+ "IDP_ID INT NOT NULL," + "UID VARCHAR(255) NOT NULL,"
						+ "GID VARCHAR(255) NOT NULL,"
						+ "STATUS VARCHAR(50) NOT NULL,"
						+ "ROLE VARCHAR(50) NOT NULL, "
						+ "EMAIL VARCHAR(255) NOT NULL, "
						+ "INDEX document_index (UID));";
				db.update(users);

				try {

					if (conf.getInitalTrustedIdP() != null) {
						TrustedIdP idp = tm.addTrustedIdP(conf
								.getInitalTrustedIdP());
						IFSUser usr = conf.getInitialUser();
						usr.setIdPId(idp.getId());
						if (usr != null) {
							this.addUser(usr);
							usr.setUserRole(IFSUserRole.Administrator);
							usr.setUserStatus(IFSUserStatus.Active);
							this.updateUser(usr);
						} else {
							GUMSInternalFault fault = new GUMSInternalFault();
							fault
									.setFaultString("Unexpected error initializing the User Manager, No initial IFS user specified.");
							throw fault;
						}
					} else {
						GUMSInternalFault fault = new GUMSInternalFault();
						fault
								.setFaultString("Unexpected error initializing the User Manager, No initial trusted IdP specified.");
						throw fault;
					}

				} catch (GUMSInternalFault e) {
					throw e;

				} catch (Exception e) {
					GUMSInternalFault fault = new GUMSInternalFault();
					fault
							.setFaultString("Unexpected error initializing the User Manager.");
					FaultHelper helper = new FaultHelper(fault);
					helper.addDescription(IOUtils.getExceptionMessage(e));
					helper.addFaultCause(e);
					fault = (GUMSInternalFault) helper.getFault();
					throw fault;

				}

			}
			this.dbBuilt = true;
		}
	}

	public static String identityToSubject(String identity) {
		String s = identity.substring(1);
		return s.replace('/', ',');
	}

	public static String subjectToIdentity(String subject) {
		String s = subject.substring(0);
		return "/" + s.replace(',', '/');
	}

}