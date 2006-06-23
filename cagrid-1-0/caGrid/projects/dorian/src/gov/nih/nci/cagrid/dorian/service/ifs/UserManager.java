package gov.nih.nci.cagrid.dorian.service.ifs;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.dorian.ca.CertificateAuthority;
import gov.nih.nci.cagrid.dorian.common.AddressValidator;
import gov.nih.nci.cagrid.dorian.common.Database;
import gov.nih.nci.cagrid.dorian.common.LoggingObject;
import gov.nih.nci.cagrid.dorian.ifs.bean.CredentialsFault;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserFilter;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserRole;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserStatus;
import gov.nih.nci.cagrid.dorian.ifs.bean.InvalidPasswordFault;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP;
import gov.nih.nci.cagrid.dorian.stubs.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.stubs.InvalidUserFault;
import gov.nih.nci.cagrid.gridca.common.CRLEntry;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;
import gov.nih.nci.cagrid.gts.client.GTSAdminClient;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.projectmobius.common.MobiusPoolManager;
import org.projectmobius.common.MobiusRunnable;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class UserManager extends LoggingObject {

	private static final String USERS_TABLE = "IFS_USERS";

	private Database db;

	private boolean dbBuilt = false;

	private CredentialsManager credentialsManager;

	private IFSConfiguration conf;

	private CertificateAuthority ca;

	private TrustedIdPManager tm;

	private MobiusPoolManager poolManager;

	private Object mutex = new Object();


	public UserManager(Database db, IFSConfiguration conf, CertificateAuthority ca, TrustedIdPManager tm) {
		this.db = db;
		this.tm = tm;
		this.credentialsManager = new CredentialsManager(db);
		this.conf = conf;
		this.ca = ca;
		poolManager = new MobiusPoolManager();
	}


	public synchronized boolean determineIfUserExists(long idpId, String uid) throws DorianInternalFault {
		buildDatabase();
		Connection c = null;
		boolean exists = false;
		try {
			c = db.getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select count(*) from " + USERS_TABLE + " WHERE IDP_ID=" + idpId
				+ " AND UID='" + uid + "'");
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count > 0) {
					exists = true;
				}
			}
			rs.close();
			s.close();

		} catch (Exception e) {
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Unexpected Database Error");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
		return exists;
	}


	private String getCredentialsManagerUID(long idpId, String uid) {
		return "[IdPId=" + idpId + ", UID=" + uid + "]";
	}


	public PrivateKey getUsersPrivateKey(IFSUser user) throws DorianInternalFault {
		try {
			return this.credentialsManager
				.getPrivateKey(getCredentialsManagerUID(user.getIdPId(), user.getUID()), null);
		} catch (InvalidPasswordFault e) {
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Error loading the user " + user.getGridId() + "'s private key.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}
	}


	public synchronized IFSUser renewUserCredentials(IFSUser user) throws DorianInternalFault, InvalidUserFault {
		X509Certificate cert = createUserCredentials(user.getIdPId(), user.getUID());
		user.setGridId(subjectToIdentity(cert.getSubjectDN().getName()));
		try {
			this.updateUser(user);

			if (!user.getUserStatus().equals(IFSUserStatus.Active)) {
				publishCRL();
			}
		} catch (InvalidUserFault iuf) {
			this.credentialsManager.deleteCredentials(getCredentialsManagerUID(user.getIdPId(), user.getUID()));
			throw iuf;
		} catch (DorianInternalFault gif) {
			this.credentialsManager.deleteCredentials(getCredentialsManagerUID(user.getIdPId(), user.getUID()));
			throw gif;
		}
		try {
			user.setCertificate(new gov.nih.nci.cagrid.dorian.bean.X509Certificate(CertUtil.writeCertificate(cert)));
		} catch (IOException ioe) {
			this.credentialsManager.deleteCredentials(getCredentialsManagerUID(user.getIdPId(), user.getUID()));
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Error renewing credentials.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(ioe);
			fault = (DorianInternalFault) helper.getFault();
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


	private synchronized X509Certificate createUserCredentials(long idpId, String uid) throws DorianInternalFault {
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

			PKCS10CertificationRequest req = CertUtil.generateCertficateRequest(sub, pair);
			X509Certificate cert = ca.requestCertificate(req, start, end);
			this.credentialsManager.deleteCredentials(getCredentialsManagerUID(idpId, uid));
			this.credentialsManager.addCredentials(getCredentialsManagerUID(idpId, uid), null, cert, pair.getPrivate());
			return cert;
		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Error creating credentials.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}
	}


	private StringBuffer appendWhereOrAnd(boolean firstAppended, StringBuffer sql) {
		if (firstAppended) {
			sql.append(" AND ");
		} else {
			sql.append(" WHERE");
		}
		return sql;
	}


	public synchronized IFSUser getUser(long idpId, String uid) throws DorianInternalFault, InvalidUserFault {
		this.buildDatabase();
		IFSUser user = new IFSUser();
		Connection c = null;
		try {
			c = db.getConnection();
			Statement s = c.createStatement();

			StringBuffer sql = new StringBuffer();
			sql.append("select * from " + USERS_TABLE + " WHERE IDP_ID=" + idpId + " AND UID='" + uid + "'");
			ResultSet rs = s.executeQuery(sql.toString());
			if (rs.next()) {
				user.setIdPId(rs.getLong("IDP_ID"));
				user.setUID(rs.getString("UID"));
				user.setGridId(rs.getString("GID"));
				String firstName = rs.getString("FIRST_NAME");
				if ((firstName != null) && (!firstName.equalsIgnoreCase("null"))) {
					user.setFirstName(firstName);
				}

				String lastName = rs.getString("LAST_NAME");
				if ((lastName != null) && (!lastName.equalsIgnoreCase("null"))) {
					user.setLastName(lastName);
				}
				String email = rs.getString("EMAIL");
				if ((email != null) && (!email.equals("null"))) {
					user.setEmail(email);
				}
				user.setUserStatus(IFSUserStatus.fromValue(rs.getString("STATUS")));
				String role = rs.getString("ROLE");
				user.setUserRole(IFSUserRole.fromValue(role));
				X509Certificate cert = credentialsManager.getCertificate(getCredentialsManagerUID(user.getIdPId(), user
					.getUID()));
				user
					.setCertificate(new gov.nih.nci.cagrid.dorian.bean.X509Certificate(CertUtil.writeCertificate(cert)));
			} else {
				InvalidUserFault fault = new InvalidUserFault();
				fault.setFaultString("No such user " + getCredentialsManagerUID(user.getIdPId(), user.getUID()));
				throw fault;

			}
			rs.close();
			s.close();
		} catch (InvalidUserFault iuf) {
			throw iuf;
		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Unexpected Error, could not obtain the user "
				+ getCredentialsManagerUID(user.getIdPId(), user.getUID()));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
		return user;
	}


	public synchronized IFSUser getUser(String gridId) throws DorianInternalFault, InvalidUserFault {
		this.buildDatabase();
		IFSUser user = new IFSUser();
		Connection c = null;
		try {
			c = db.getConnection();
			Statement s = c.createStatement();

			StringBuffer sql = new StringBuffer();
			sql.append("select * from " + USERS_TABLE + " WHERE GID='" + gridId + "'");
			ResultSet rs = s.executeQuery(sql.toString());
			if (rs.next()) {
				user.setIdPId(rs.getLong("IDP_ID"));
				user.setUID(rs.getString("UID"));
				user.setGridId(rs.getString("GID"));
				String firstName = rs.getString("FIRST_NAME");
				if ((firstName != null) && (!firstName.equalsIgnoreCase("null"))) {
					user.setFirstName(firstName);
				}

				String lastName = rs.getString("LAST_NAME");
				if ((lastName != null) && (!lastName.equalsIgnoreCase("null"))) {
					user.setLastName(lastName);
				}
				String email = rs.getString("EMAIL");
				if ((email != null) && (!email.equalsIgnoreCase("null"))) {
					user.setEmail(email);
				}
				user.setUserStatus(IFSUserStatus.fromValue(rs.getString("STATUS")));
				String role = rs.getString("ROLE");
				user.setUserRole(IFSUserRole.fromValue(role));
				X509Certificate cert = credentialsManager.getCertificate(getCredentialsManagerUID(user.getIdPId(), user
					.getUID()));
				user
					.setCertificate(new gov.nih.nci.cagrid.dorian.bean.X509Certificate(CertUtil.writeCertificate(cert)));
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
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Unexpected Error, could not obtain the user " + gridId);
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
		return user;
	}


	public synchronized IFSUser[] getUsers(IFSUserFilter filter) throws DorianInternalFault {

		this.buildDatabase();
		Connection c = null;
		List users = new ArrayList();
		try {
			c = db.getConnection();
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

				if (filter.getFirstName() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" FIRST_NAME LIKE '%" + filter.getFirstName() + "%'");
				}

				if (filter.getLastName() != null) {
					sql = appendWhereOrAnd(firstAppended, sql);
					firstAppended = true;
					sql.append(" LAST_NAME LIKE '%" + filter.getLastName() + "%'");
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
				String firstName = rs.getString("FIRST_NAME");
				if ((firstName != null) && (!firstName.equalsIgnoreCase("null"))) {
					user.setFirstName(firstName);
				}

				String lastName = rs.getString("LAST_NAME");
				if ((lastName != null) && (!lastName.equalsIgnoreCase("null"))) {
					user.setLastName(lastName);
				}
				String email = rs.getString("EMAIL");
				if ((email != null) && (!email.equals("null"))) {
					user.setEmail(email);
				}
				user.setUserStatus(IFSUserStatus.fromValue(rs.getString("STATUS")));
				String role = rs.getString("ROLE");
				user.setUserRole(IFSUserRole.fromValue(role));
				X509Certificate cert = credentialsManager.getCertificate(getCredentialsManagerUID(user.getIdPId(), user
					.getUID()));
				user
					.setCertificate(new gov.nih.nci.cagrid.dorian.bean.X509Certificate(CertUtil.writeCertificate(cert)));
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
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Unexpected Error, could not obtain a list of users");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
	}


	public synchronized IFSUser addUser(IFSUser user) throws DorianInternalFault, CredentialsFault, InvalidUserFault {
		this.buildDatabase();
		if (!determineIfUserExists(user.getIdPId(), user.getUID())) {
			X509Certificate cert = createUserCredentials(user.getIdPId(), user.getUID());
			try {
				// Write method for creating and setting a users credentials
				user
					.setCertificate(new gov.nih.nci.cagrid.dorian.bean.X509Certificate(CertUtil.writeCertificate(cert)));
				user.setGridId(subjectToIdentity(cert.getSubjectDN().toString()));
				user.setUserRole(IFSUserRole.Non_Administrator);
				user.setUserStatus(IFSUserStatus.Pending);
				try {
					AddressValidator.validateEmail(user.getEmail());
				} catch (IllegalArgumentException e) {
					InvalidUserFault fault = new InvalidUserFault();
					fault.setFaultString(e.getMessage());
					throw fault;
				}
				validateSpecifiedField("UID", user.getUID());
				validateSpecifiedField("Grid Id", user.getGridId());
				validateSpecifiedField("First Name", user.getFirstName());
				validateSpecifiedField("Last Name", user.getLastName());

				db.update("INSERT INTO " + USERS_TABLE + " SET IDP_ID='" + user.getIdPId() + "',UID='" + user.getUID()
					+ "', GID='" + user.getGridId() + "',STATUS='" + user.getUserStatus().toString() + "',ROLE='"
					+ user.getUserRole().toString() + "', FIRST_NAME='" + user.getFirstName() + "', LAST_NAME='"
					+ user.getLastName() + "',EMAIL='" + user.getEmail() + "'");

				if (!user.getUserStatus().equals(IFSUserStatus.Active)) {
					publishCRL();
				}
			} catch (InvalidUserFault iuf) {
				throw iuf;
			} catch (Exception e) {
				try {
					this.removeUser(user.getIdPId(), user.getUID());
				} catch (Exception ex) {

				}

				try {
					this.credentialsManager.deleteCredentials(getCredentialsManagerUID(user.getIdPId(), user.getUID()));
				} catch (Exception ex) {

				}
				logError(e.getMessage(), e);
				DorianInternalFault fault = new DorianInternalFault();
				fault.setFaultString("Error adding the user "
					+ getCredentialsManagerUID(user.getIdPId(), user.getUID())
					+ " to the IFS, an unexpected database error occurred.");
				FaultHelper helper = new FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (DorianInternalFault) helper.getFault();
				throw fault;
			}

		} else {
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Error adding the user, " + getCredentialsManagerUID(user.getIdPId(), user.getUID())
				+ ", the user already exists!!!");
			throw fault;

		}

		return user;
	}


	public synchronized void updateUser(IFSUser u) throws DorianInternalFault, InvalidUserFault {
		this.buildDatabase();
		String credId = getCredentialsManagerUID(u.getIdPId(), u.getUID());
		boolean publishCRL = false;
		if (determineIfUserExists(u.getIdPId(), u.getUID())) {
			StringBuffer sb = new StringBuffer();
			sb.append("update " + USERS_TABLE + " SET ");
			int changes = 0;
			IFSUser curr = this.getUser(u.getIdPId(), u.getUID());

			if ((u.getFirstName() != null) && (!u.getFirstName().equals(curr.getFirstName()))) {
				validateSpecifiedField("First Name", u.getFirstName());
				
				if (changes > 0) {
					sb.append(",");
				}
				sb.append("FIRST_NAME='" + u.getFirstName() + "'");
				changes = changes + 1;
			}

			if ((u.getLastName() != null) && (!u.getLastName().equals(curr.getLastName()))) {
				validateSpecifiedField("Last Name", u.getLastName());
				if (changes > 0) {
					sb.append(",");
				}
				sb.append("LAST_NAME='" + u.getLastName() + "'");
				changes = changes + 1;
			}

			if ((u.getEmail() != null) && (!u.getEmail().equals(curr.getEmail()))) {
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

			if ((u.getGridId() != null) && (!u.getGridId().equals(curr.getGridId()))) {
				if (changes > 0) {
					sb.append(",");
				}
				validateSpecifiedField("Grid Id", u.getGridId());
				sb.append("GID='" + u.getGridId() + "'");
				changes = changes + 1;
			}

			if ((u.getUserStatus() != null) && (!u.getUserStatus().equals(curr.getUserStatus()))) {
				if (changes > 0) {
					sb.append(",");
				}

				if (accountCreated(curr.getUserStatus()) && !accountCreated(u.getUserStatus())) {
					InvalidUserFault fault = new InvalidUserFault();
					fault.setFaultString("Error, cannot change " + credId
						+ "'s status from a post-created account status (" + curr.getUserStatus()
						+ ") to a pre-created account status (" + u.getUserStatus() + ").");
					throw fault;
				}
				if (curr.getUserStatus().equals(IFSUserStatus.Active)) {
					publishCRL = true;
				} else if (u.getUserStatus().equals(IFSUserStatus.Active)) {
					publishCRL = true;
				}

				sb.append("STATUS='" + u.getUserStatus().getValue() + "'");
				changes = changes + 1;
			}

			if ((u.getUserRole() != null) && (!u.getUserRole().equals(curr.getUserRole()))) {
				if (changes > 0) {
					sb.append(",");
				}
				sb.append("ROLE='" + u.getUserRole().getValue() + "'");
				changes = changes + 1;
			}
			sb.append(" where IDP_ID=" + u.getIdPId() + " AND UID='" + u.getUID() + "'");
			if (changes > 0) {
				db.update(sb.toString());
			}
			if (publishCRL) {
				publishCRL();
			}

		} else {
			InvalidUserFault fault = new InvalidUserFault();
			fault.setFaultString("Could not update user, the user " + credId + " does not exist.");
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


	public synchronized void removeUser(IFSUser user) throws DorianInternalFault, InvalidUserFault {
		this.buildDatabase();
		if (determineIfUserExists(user.getIdPId(), user.getUID())) {
			this.removeUser(user.getIdPId(), user.getUID());
		} else {
			InvalidUserFault fault = new InvalidUserFault();
			fault.setFaultString("Could not remove user, the specified user does not exist.");
			throw fault;
		}
	}


	public synchronized void removeUser(long idpId, String uid) throws DorianInternalFault {
		this.buildDatabase();
		db.update("delete from " + USERS_TABLE + " WHERE IDP_ID=" + idpId + " AND UID='" + uid + "'");
	}


	private void validateSpecifiedField(String type, String name) throws InvalidUserFault {
		name = Utils.clean(name);
		if (name == null) {
			throw new IllegalArgumentException("No " + type + " specified.");
		}
		if (name.length() > 255) {
			throw new IllegalArgumentException("The " + type
				+ " specified is too long, it must be less than 255 characters.");
		}
	}


	public List getDisabledUsersSerialIds() throws DorianInternalFault {

		this.buildDatabase();
		Connection c = null;
		List sn = new ArrayList();
		try {
			// First get all the users who's accounts are disabled.
			c = db.getConnection();
			Statement s = c.createStatement();

			StringBuffer sql = new StringBuffer();
			sql.append("select IDP_ID,UID from " + USERS_TABLE + " WHERE STATUS='" + IFSUserStatus.Suspended
				+ "' OR STATUS='" + IFSUserStatus.Pending + "' OR STATUS='" + IFSUserStatus.Rejected + "' OR STATUS='"
				+ IFSUserStatus.Expired + "'");
			ResultSet rs = s.executeQuery(sql.toString());
			while (rs.next()) {
				String id = getCredentialsManagerUID(rs.getLong("IDP_ID"), rs.getString("UID"));
				sn.add(new Long(credentialsManager.getCertificateSerialNumber(id)));
			}
			rs.close();
			s.close();

			// Now get all the IdPs who are suspended.
			TrustedIdP[] idp = this.tm.getSuspendedTrustedIdPs();
			if (idp != null) {
				for (int i = 0; i < idp.length; i++) {
					Statement stmt = c.createStatement();
					StringBuffer sb = new StringBuffer();
					sb.append("select IDP_ID,UID from " + USERS_TABLE + " WHERE IDP_ID=" + idp[i].getId());
					ResultSet result = stmt.executeQuery(sb.toString());
					while (result.next()) {
						String id = getCredentialsManagerUID(result.getLong("IDP_ID"), result.getString("UID"));
						sn.add(new Long(credentialsManager.getCertificateSerialNumber(id)));
					}
					stmt.close();
					result.close();
				}
			}
			return sn;

		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Unexpected Error, could not obtain a list of users");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
	}


	public void buildDatabase() throws DorianInternalFault {
		if (!dbBuilt) {
			if (!this.db.tableExists(USERS_TABLE)) {
				String users = "CREATE TABLE " + USERS_TABLE + " (" + "IDP_ID INT NOT NULL,"
					+ "UID VARCHAR(255) NOT NULL," + "FIRST_NAME VARCHAR(255) NOT NULL,"
					+ "LAST_NAME VARCHAR(255) NOT NULL," + "GID VARCHAR(255) NOT NULL,"
					+ "STATUS VARCHAR(50) NOT NULL," + "ROLE VARCHAR(50) NOT NULL, " + "EMAIL VARCHAR(255) NOT NULL, "
					+ "INDEX document_index (UID));";
				db.update(users);

				try {

					if (conf.getInitalTrustedIdP() != null) {
						TrustedIdP idp = tm.addTrustedIdP(conf.getInitalTrustedIdP());
						IFSUser usr = conf.getInitialUser();
						usr.setIdPId(idp.getId());
						if (usr != null) {
							this.addUser(usr);
							usr.setUserRole(IFSUserRole.Administrator);
							usr.setUserStatus(IFSUserStatus.Active);
							this.updateUser(usr);
						} else {
							DorianInternalFault fault = new DorianInternalFault();
							fault
								.setFaultString("Unexpected error initializing the User Manager, No initial IFS user specified.");
							throw fault;
						}
					} else {
						DorianInternalFault fault = new DorianInternalFault();
						fault
							.setFaultString("Unexpected error initializing the User Manager, No initial trusted IdP specified.");
						throw fault;
					}

				} catch (DorianInternalFault e) {
					throw e;

				} catch (Exception e) {
					DorianInternalFault fault = new DorianInternalFault();
					fault.setFaultString("Unexpected error initializing the User Manager.");
					FaultHelper helper = new FaultHelper(fault);
					helper.addDescription(Utils.getExceptionMessage(e));
					helper.addFaultCause(e);
					fault = (DorianInternalFault) helper.getFault();
					throw fault;

				}

			}
			this.dbBuilt = true;
		}
	}


	public X509CRL getCRL() throws DorianInternalFault {
		List sn = this.getDisabledUsersSerialIds();
		CRLEntry[] entries = new CRLEntry[sn.size()];
		for (int i = 0; i < sn.size(); i++) {
			Long l = (Long) sn.get(i);
			entries[i] = new CRLEntry(BigInteger.valueOf(l.longValue()), CRLReason.PRIVILEGE_WITHDRAWN);
		}
		try {
			X509CRL crl = ca.getCRL(entries);
			return crl;
		} catch (Exception e) {
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Unexpected error obtaining the CRL.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addDescription(Utils.getExceptionMessage(e));
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;

		}
	}


	protected void publishCRL() {
		List services = conf.getGTSServices();
		if ((services != null) && (services.size() > 0)) {
			MobiusRunnable runner = new MobiusRunnable() {
				public void execute() {
					synchronized (mutex) {
						List services = conf.getGTSServices();
						if ((services != null) && (services.size() > 0)) {
							try {
								X509CRL crl = getCRL();
								gov.nih.nci.cagrid.gts.bean.X509CRL x509 = new gov.nih.nci.cagrid.gts.bean.X509CRL();
								x509.setCrlEncodedString(CertUtil.writeCRL(crl));
								String authName = ca.getCACertificate().getSubjectDN().getName();
								for (int i = 0; i < services.size(); i++) {
									String uri = (String) services.get(i);
									try {
										debug("Publishing CRL to the GTS " + uri);
										GTSAdminClient client = new GTSAdminClient(uri, null);
										client.updateCRL(authName, x509);
										debug("Published CRL to the GTS " + uri);
									} catch (Exception ex) {
										getLog().error("Error publishing the CRL to the GTS " + uri + "!!!", ex);
									}

								}

							} catch (Exception e) {
								getLog().error("Unexpected Error publishing the CRL!!!", e);
							}
						}
					}
				}
			};
			try {
				poolManager.executeInBackground(runner);
			} catch (Exception t) {
				t.getMessage();
			}
		}

	}


	public static String identityToSubject(String identity) {
		String s = identity.substring(1);
		return s.replace('/', ',');
	}


	public static String subjectToIdentity(String subject) {
		return "/" + subject.replace(',', '/');
	}

}