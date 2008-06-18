package org.cagrid.gaards.dorian.federation;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.Utils;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bouncycastle.asn1.x509.CRLReason;
import org.cagrid.gaards.dorian.ca.CertificateAuthority;
import org.cagrid.gaards.dorian.ca.CertificateAuthorityFault;
import org.cagrid.gaards.dorian.common.AddressValidator;
import org.cagrid.gaards.dorian.common.Lifetime;
import org.cagrid.gaards.dorian.common.LoggingObject;
import org.cagrid.gaards.dorian.service.PropertyManager;
import org.cagrid.gaards.dorian.stubs.types.DorianInternalFault;
import org.cagrid.gaards.dorian.stubs.types.InvalidUserFault;
import org.cagrid.gaards.pki.CertUtil;
import org.cagrid.tools.database.Database;
import org.cagrid.tools.database.DatabaseException;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class UserManager extends LoggingObject {

	public static final String USERS_TABLE = "ifs_users";

	private Database db;

	private boolean dbBuilt = false;

	private IdentityFederationProperties conf;

	private CertificateAuthority ca;

	private TrustedIdPManager tm;

	private FederationDefaults defaults;

	private PropertyManager properties;

	private Publisher publisher;

	private CertificateBlacklistManager blackList;

	public UserManager(Database db, IdentityFederationProperties conf,
			PropertyManager properties, CertificateAuthority ca,
			CertificateBlacklistManager blackList, TrustedIdPManager tm,
			Publisher publisher, FederationDefaults defaults) {
		this.db = db;
		this.tm = tm;
		this.defaults = defaults;
		this.conf = conf;
		this.ca = ca;
		this.properties = properties;
		this.publisher = publisher;
		this.blackList = blackList;
	}

	public synchronized boolean determineIfUserExists(long idpId, String uid)
			throws DorianInternalFault {
		buildDatabase();
		Connection c = null;
		boolean exists = false;
		try {
			c = db.getConnection();
			PreparedStatement s = c.prepareStatement("select count(*) from "
					+ USERS_TABLE + " WHERE IDP_ID= ? AND UID= ?");
			s.setLong(1, idpId);
			s.setString(2, uid);

			ResultSet rs = s.executeQuery();
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

	public String getCredentialsManagerUID(long idpId, String uid) {
		return "IdPId=" + idpId + ":UID=" + uid;
	}

	public PrivateKey getUsersPrivateKey(IFSUser user)
			throws DorianInternalFault {
		try {
			return ca.getPrivateKey(getCredentialsManagerUID(user.getIdPId(),
					user.getUID()), null);
		} catch (CertificateAuthorityFault e) {
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Error loading the user " + user.getGridId()
					+ "'s private key.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}
	}

	public synchronized IFSUser renewUserCredentials(TrustedIdP idp,
			IFSUser user) throws DorianInternalFault, InvalidUserFault {
		this.blackList.addCertificateToBlackList(user.getCertificate(),
				CertificateBlacklistManager.CERTIFICATE_RENEWED);
		X509Certificate cert = createUserCredentials(idp, user.getUID());
		user.setGridId(subjectToIdentity(cert.getSubjectDN().getName()));
		try {
			this.updateUser(user);

			if (!user.getUserStatus().equals(IFSUserStatus.Active)) {
				publisher.publishCRL();
			}
		} catch (InvalidUserFault iuf) {
			try {
				ca.deleteCredentials(getCredentialsManagerUID(user.getIdPId(),
						user.getUID()));
			} catch (Exception ex) {
				log.error(ex);
			}
			throw iuf;
		} catch (DorianInternalFault gif) {
			try {
				ca.deleteCredentials(getCredentialsManagerUID(user.getIdPId(),
						user.getUID()));
			} catch (Exception ex) {
				log.error(ex);
			}
			throw gif;
		}
		try {
			user
					.setCertificate(new org.cagrid.gaards.dorian.X509Certificate(
							CertUtil.writeCertificate(cert)));
		} catch (IOException ioe) {
			try {
				ca.deleteCredentials(getCredentialsManagerUID(user.getIdPId(),
						user.getUID()));
			} catch (Exception ex) {
				log.error(ex);
			}
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Error renewing credentials.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(ioe);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}
		return user;
	}

	public String getUserSubject(String caSubject, TrustedIdP idp, String uid) {
		return getUserSubject(this.conf.getIdentityAssignmentPolicy(),
				caSubject, idp, uid);
	}

	public static String getUserSubject(String policy, String caSubject,
			TrustedIdP idp, String uid) {
		int caindex = caSubject.lastIndexOf(",");
		String caPreSub = caSubject.substring(0, caindex);
		if (policy.equals(IdentityAssignmentPolicy.ID)) {
			return caPreSub + ",OU=IdP [" + idp.getId() + "],CN=" + uid;
		} else {
			return caPreSub + ",OU=" + idp.getName() + ",CN=" + uid;
		}
	}

	private synchronized X509Certificate createUserCredentials(TrustedIdP idp,
			String uid) throws DorianInternalFault {
		try {
			String caSubject = ca.getCACertificate().getSubjectDN().getName();
			String sub = getUserSubject(caSubject, idp, uid);
			Calendar c = new GregorianCalendar();
			Date start = c.getTime();
			Lifetime lifetime = conf.getIssuedCertificateLifetime();
			Date end = org.cagrid.gaards.dorian.common.Utils
					.getExpiredDate(lifetime);
			if (end.after(ca.getCACertificate().getNotAfter())) {
				end = ca.getCACertificate().getNotAfter();
			}
			String alias = getCredentialsManagerUID(idp.getId(), uid);
			ca.deleteCredentials(alias);
			ca.createCredentials(alias, sub, null, start, end);
			return ca.getCertificate(alias);
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

	public synchronized IFSUser getUser(long idpId, String uid)
			throws DorianInternalFault, InvalidUserFault {
		this.buildDatabase();
		IFSUser user = new IFSUser();
		Connection c = null;
		try {
			c = db.getConnection();
			PreparedStatement s = c.prepareStatement("select * from "
					+ USERS_TABLE + " WHERE IDP_ID= ? AND UID= ?");
			s.setLong(1, idpId);
			s.setString(2, uid);
			ResultSet rs = s.executeQuery();
			if (rs.next()) {
				user.setIdPId(rs.getLong("IDP_ID"));
				user.setUID(rs.getString("UID"));
				user.setGridId(rs.getString("GID"));
				String firstName = rs.getString("FIRST_NAME");
				if ((firstName != null)
						&& (!firstName.equalsIgnoreCase("null"))) {
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
				user.setUserStatus(IFSUserStatus.fromValue(rs
						.getString("STATUS")));
				X509Certificate cert = ca
						.getCertificate(getCredentialsManagerUID(user
								.getIdPId(), user.getUID()));
				user
						.setCertificate(new org.cagrid.gaards.dorian.X509Certificate(
								CertUtil.writeCertificate(cert)));
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

	public synchronized IFSUser getUser(String gridId)
			throws DorianInternalFault, InvalidUserFault {
		this.buildDatabase();
		IFSUser user = new IFSUser();
		Connection c = null;
		try {
			c = db.getConnection();
			PreparedStatement s = c.prepareStatement("select * from "
					+ USERS_TABLE + " WHERE GID= ?");
			s.setString(1, gridId);
			ResultSet rs = s.executeQuery();
			if (rs.next()) {
				user.setIdPId(rs.getLong("IDP_ID"));
				user.setUID(rs.getString("UID"));
				user.setGridId(rs.getString("GID"));
				String firstName = rs.getString("FIRST_NAME");
				if ((firstName != null)
						&& (!firstName.equalsIgnoreCase("null"))) {
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
				user.setUserStatus(IFSUserStatus.fromValue(rs
						.getString("STATUS")));
				X509Certificate cert = ca
						.getCertificate(getCredentialsManagerUID(user
								.getIdPId(), user.getUID()));
				user
						.setCertificate(new org.cagrid.gaards.dorian.X509Certificate(
								CertUtil.writeCertificate(cert)));
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
			fault.setFaultString("Unexpected Error, could not obtain the user "
					+ gridId);
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
		return user;
	}

	public synchronized IFSUser[] getUsers(IFSUserFilter filter)
			throws DorianInternalFault {

		this.buildDatabase();
		Connection c = null;
		List users = new ArrayList();
		try {
			c = db.getConnection();
			PreparedStatement s = null;
			if (filter != null) {
				s = c
						.prepareStatement("select * from  "
								+ USERS_TABLE
								+ " WHERE IDP_ID>= ? AND IDP_ID<= ? AND UID LIKE ? AND GID LIKE ? AND STATUS LIKE ? AND FIRST_NAME LIKE ? AND LAST_NAME LIKE ? AND EMAIL LIKE ?");

				if (filter.getIdPId() > 0) {
					s.setLong(1, filter.getIdPId());
					s.setLong(2, filter.getIdPId());
				} else {
					s.setLong(1, 0);
					s.setLong(2, Long.MAX_VALUE);
				}

				if (filter.getUID() != null) {
					s.setString(3, "%" + filter.getUID() + "%");
				} else {
					s.setString(3, "%");
				}

				if (filter.getGridId() != null) {
					s.setString(4, "%" + filter.getGridId() + "%");
				} else {
					s.setString(4, "%");
				}

				if (filter.getUserStatus() != null) {
					s.setString(5, filter.getUserStatus().getValue());
				} else {
					s.setString(5, "%");
				}

				if (filter.getFirstName() != null) {
					s.setString(6, "%" + filter.getFirstName() + "%");
				} else {
					s.setString(6, "%");
				}

				if (filter.getLastName() != null) {
					s.setString(7, "%" + filter.getLastName() + "%");
				} else {
					s.setString(7, "%");
				}

				if (filter.getEmail() != null) {
					s.setString(8, "%" + filter.getEmail() + "%");
				} else {
					s.setString(8, "%");
				}
			} else {
				s = c.prepareStatement("select * from  " + USERS_TABLE);
			}

			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				IFSUser user = new IFSUser();
				user.setIdPId(rs.getLong("IDP_ID"));
				user.setUID(rs.getString("UID"));
				user.setGridId(rs.getString("GID"));
				String firstName = rs.getString("FIRST_NAME");
				if ((firstName != null)
						&& (!firstName.equalsIgnoreCase("null"))) {
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
				user.setUserStatus(IFSUserStatus.fromValue(rs
						.getString("STATUS")));
				X509Certificate cert = ca
						.getCertificate(getCredentialsManagerUID(user
								.getIdPId(), user.getUID()));
				user
						.setCertificate(new org.cagrid.gaards.dorian.X509Certificate(
								CertUtil.writeCertificate(cert)));
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
			fault
					.setFaultString("Unexpected Error, could not obtain a list of users");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
	}

	public synchronized IFSUser addUser(TrustedIdP idp, IFSUser user)
			throws DorianInternalFault, InvalidUserFault {
		this.buildDatabase();
		if (!determineIfUserExists(user.getIdPId(), user.getUID())) {
			X509Certificate cert = createUserCredentials(idp, user.getUID());
			Connection c = null;
			try {

				// Write method for creating and setting a users credentials
				user
						.setCertificate(new org.cagrid.gaards.dorian.X509Certificate(
								CertUtil.writeCertificate(cert)));
				user
						.setGridId(subjectToIdentity(cert.getSubjectDN()
								.toString()));
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
				c = db.getConnection();
				PreparedStatement s = c
						.prepareStatement("INSERT INTO "
								+ USERS_TABLE
								+ " SET IDP_ID= ?,UID= ?,GID= ?, STATUS=?, FIRST_NAME=?, LAST_NAME= ?, EMAIL=?");
				s.setLong(1, user.getIdPId());
				s.setString(2, user.getUID());
				s.setString(3, user.getGridId());
				s.setString(4, user.getUserStatus().toString());
				s.setString(5, user.getFirstName());
				s.setString(6, user.getLastName());
				s.setString(7, user.getEmail());
				s.execute();
				if (!user.getUserStatus().equals(IFSUserStatus.Active)) {
					publisher.publishCRL();
				}
			} catch (InvalidUserFault iuf) {
				throw iuf;
			} catch (Exception e) {
				try {
					this.removeUser(user.getIdPId(), user.getUID());
				} catch (Exception ex) {

				}

				try {
					this.ca.deleteCredentials(getCredentialsManagerUID(user
							.getIdPId(), user.getUID()));
				} catch (Exception ex) {

				}
				logError(e.getMessage(), e);
				DorianInternalFault fault = new DorianInternalFault();
				fault
						.setFaultString("Error adding the user "
								+ getCredentialsManagerUID(user.getIdPId(),
										user.getUID())
								+ " to the IFS, an unexpected database error occurred.");
				FaultHelper helper = new FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (DorianInternalFault) helper.getFault();
				throw fault;
			} finally {
				db.releaseConnection(c);
			}

		} else {
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Error adding the user, "
					+ getCredentialsManagerUID(user.getIdPId(), user.getUID())
					+ ", the user already exists!!!");
			throw fault;

		}

		return user;
	}

	public synchronized void updateUser(IFSUser u) throws DorianInternalFault,
			InvalidUserFault {
		this.buildDatabase();
		String credId = getCredentialsManagerUID(u.getIdPId(), u.getUID());
		boolean publishCRL = false;
		if (determineIfUserExists(u.getIdPId(), u.getUID())) {

			Connection c = null;
			try {

				IFSUser curr = this.getUser(u.getIdPId(), u.getUID());

				if ((u.getFirstName() != null)
						&& (!u.getFirstName().equals(curr.getFirstName()))) {
					validateSpecifiedField("First Name", u.getFirstName());
					curr.setFirstName(u.getFirstName());
				}

				if ((u.getLastName() != null)
						&& (!u.getLastName().equals(curr.getLastName()))) {
					validateSpecifiedField("Last Name", u.getLastName());
					curr.setLastName(u.getLastName());
				}

				if ((u.getEmail() != null)
						&& (!u.getEmail().equals(curr.getEmail()))) {
					try {
						AddressValidator.validateEmail(u.getEmail());
					} catch (IllegalArgumentException e) {
						InvalidUserFault fault = new InvalidUserFault();
						fault.setFaultString(e.getMessage());
						throw fault;
					}
					curr.setEmail(u.getEmail());
				}

				if ((u.getGridId() != null)
						&& (!u.getGridId().equals(curr.getGridId()))) {
					validateSpecifiedField("Grid Id", u.getGridId());
					curr.setGridId(u.getGridId());
				}

				if ((u.getUserStatus() != null)
						&& (!u.getUserStatus().equals(curr.getUserStatus()))) {
					if (accountCreated(curr.getUserStatus())
							&& !accountCreated(u.getUserStatus())) {
						InvalidUserFault fault = new InvalidUserFault();
						fault
								.setFaultString("Error, cannot change "
										+ credId
										+ "'s status from a post-created account status ("
										+ curr.getUserStatus()
										+ ") to a pre-created account status ("
										+ u.getUserStatus() + ").");
						throw fault;
					}
					if (curr.getUserStatus().equals(IFSUserStatus.Active)) {
						publishCRL = true;
					} else if (u.getUserStatus().equals(IFSUserStatus.Active)) {
						publishCRL = true;
					}
					curr.setUserStatus(u.getUserStatus());
				}

				c = db.getConnection();
				PreparedStatement s = c
						.prepareStatement("UPDATE "
								+ USERS_TABLE
								+ " SET GID= ?, STATUS=?, FIRST_NAME=?, LAST_NAME= ?, EMAIL=? where IDP_ID= ? AND UID= ?");
				s.setString(1, curr.getGridId());
				s.setString(2, curr.getUserStatus().getValue());
				s.setString(3, curr.getFirstName());
				s.setString(4, curr.getLastName());
				s.setString(5, curr.getEmail());
				s.setLong(6, curr.getIdPId());
				s.setString(7, curr.getUID());
				s.execute();
				if (publishCRL) {
					publisher.publishCRL();
				}
			} catch (Exception e) {
				logError(e.getMessage(), e);
				DorianInternalFault fault = new DorianInternalFault();
				fault
						.setFaultString("Error updating the user "
								+ getCredentialsManagerUID(u.getIdPId(), u
										.getUID())
								+ " to the IFS, an unexpected database error occurred.");
				FaultHelper helper = new FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (DorianInternalFault) helper.getFault();
				throw fault;
			} finally {
				db.releaseConnection(c);
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

	public synchronized void removeUser(IFSUser user)
			throws DorianInternalFault, InvalidUserFault {
		this.buildDatabase();
		if (determineIfUserExists(user.getIdPId(), user.getUID())) {
			this.removeUser(user.getIdPId(), user.getUID());
		} else {
			InvalidUserFault fault = new InvalidUserFault();
			fault
					.setFaultString("Could not remove user, the specified user does not exist.");
			throw fault;
		}
	}

	public synchronized void removeUser(long idpId, String uid)
			throws DorianInternalFault {
		this.buildDatabase();
		try {
			String alias = getCredentialsManagerUID(idpId, uid);
			this.blackList.addCertificateToBlackList(ca.getCertificate(alias),
					CertificateBlacklistManager.ACCOUNT_DELETED);
			ca.deleteCredentials(alias);
		} catch (Exception e) {
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Unexpected Error");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}
		Connection c = null;
		try {
			c = db.getConnection();
			PreparedStatement s = c.prepareStatement("delete from "
					+ USERS_TABLE + " WHERE IDP_ID= ? AND UID= ?");
			s.setLong(1, idpId);
			s.setString(2, uid);
			s.execute();
			s.close();
		} catch (Exception e) {
			DorianInternalFault fault = new DorianInternalFault();
			fault
					.setFaultString("Unexpected Database Error - Could not remove user!!!");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
	}

	private void validateSpecifiedField(String type, String name)
			throws InvalidUserFault {
		name = Utils.clean(name);
		if (name == null) {
			throw new IllegalArgumentException("No " + type + " specified.");
		}
		if (name.length() > 255) {
			throw new IllegalArgumentException(
					"The "
							+ type
							+ " specified is too long, it must be less than 255 characters.");
		}
	}

	public Map<String, DisabledUser> getDisabledUsers()
			throws DorianInternalFault {
		Map<String, DisabledUser> users = new HashMap<String, DisabledUser>();
		this.buildDatabase();
		Connection c = null;
		try {
			// First get all the users who's accounts are disabled.
			c = db.getConnection();
			Statement s = c.createStatement();

			StringBuffer sql = new StringBuffer();
			sql.append("select IDP_ID,UID, GID from " + USERS_TABLE
					+ " WHERE STATUS='" + IFSUserStatus.Suspended
					+ "' OR STATUS='" + IFSUserStatus.Pending + "' OR STATUS='"
					+ IFSUserStatus.Rejected + "' OR STATUS='"
					+ IFSUserStatus.Expired + "'");
			ResultSet rs = s.executeQuery(sql.toString());
			while (rs.next()) {
				String id = getCredentialsManagerUID(rs.getLong("IDP_ID"), rs
						.getString("UID"));
				DisabledUser usr = new DisabledUser(rs.getString("GID"), ca
						.getCertificateSerialNumber(id),
						CRLReason.PRIVILEGE_WITHDRAWN);
				if (!users.containsKey(usr.getGridIdentity())) {
					users.put(usr.getGridIdentity(), usr);
				}
			}
			rs.close();
			s.close();

			// Now get all the IdPs who are suspended.
			TrustedIdP[] idp = this.tm.getSuspendedTrustedIdPs();
			if (idp != null) {
				for (int i = 0; i < idp.length; i++) {
					Statement stmt = c.createStatement();
					StringBuffer sb = new StringBuffer();
					sb.append("select IDP_ID,UID,GID from " + USERS_TABLE
							+ " WHERE IDP_ID=" + idp[i].getId());
					ResultSet result = stmt.executeQuery(sb.toString());
					while (result.next()) {
						String id = getCredentialsManagerUID(result
								.getLong("IDP_ID"), result.getString("UID"));
						DisabledUser usr = new DisabledUser(result
								.getString("GID"), ca
								.getCertificateSerialNumber(id),
								CRLReason.PRIVILEGE_WITHDRAWN);
						if (!users.containsKey(usr.getGridIdentity())) {
							users.put(usr.getGridIdentity(), usr);
						}
					}
					stmt.close();
					result.close();
				}
			}
			return users;

		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault
					.setFaultString("Unexpected Error, could not obtain a list of users");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
	}

	public void clearDatabase() throws DorianInternalFault {

		try {
			IFSUser[] users = getUsers(null);
			for (int i = 0; i < users.length; i++) {
				ca.deleteCredentials(getCredentialsManagerUID(users[i]
						.getIdPId(), users[i].getUID()));
			}
		} catch (Exception e) {
			//FaultUtil.printFault(e);
			log.error(e.getMessage(), e);
		}
		try {
			db.update("DROP TABLE IF EXISTS " + USERS_TABLE);
		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("An unexpected database error occurred.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}
		this.tm.clearDatabase();
		this.dbBuilt = false;
	}

	public void buildDatabase() throws DorianInternalFault {
		if (!dbBuilt) {
			try {
				if (!this.db.tableExists(USERS_TABLE)) {
					String users = "CREATE TABLE " + USERS_TABLE + " ("
							+ "IDP_ID INT NOT NULL,"
							+ "UID VARCHAR(255) NOT NULL,"
							+ "FIRST_NAME VARCHAR(255) NOT NULL,"
							+ "LAST_NAME VARCHAR(255) NOT NULL,"
							+ "GID VARCHAR(255) NOT NULL,"
							+ "STATUS VARCHAR(50) NOT NULL,"
							+ "EMAIL VARCHAR(255) NOT NULL, "
							+ "INDEX document_index (UID));";
					db.update(users);
					properties.setCurrentVersion();
					try {

						if (defaults.getDefaultIdP() != null) {
							TrustedIdP idp = tm.addTrustedIdP(defaults
									.getDefaultIdP());
							IFSUser usr = defaults.getDefaultUser();
							if (usr != null) {
								usr.setIdPId(idp.getId());
								this.addUser(idp, usr);
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
						fault
								.setFaultString("Unexpected error initializing the User Manager.");
						FaultHelper helper = new FaultHelper(fault);
						helper.addDescription(Utils.getExceptionMessage(e));
						helper.addFaultCause(e);
						fault = (DorianInternalFault) helper.getFault();
						throw fault;

					}

				}
			} catch (DatabaseException e) {
				logError(e.getMessage(), e);
				DorianInternalFault fault = new DorianInternalFault();
				fault.setFaultString("An unexpected database error occurred.");
				FaultHelper helper = new FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (DorianInternalFault) helper.getFault();
				throw fault;
			}
			this.dbBuilt = true;
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