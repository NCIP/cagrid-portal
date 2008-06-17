package org.cagrid.gaards.dorian.federation;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;
import gov.nih.nci.cagrid.opensaml.SAMLException;

import java.io.StringReader;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.cagrid.gaards.dorian.common.LoggingObject;
import org.cagrid.gaards.dorian.federation.IFSUserPolicy;
import org.cagrid.gaards.dorian.federation.SAMLAttributeDescriptor;
import org.cagrid.gaards.dorian.federation.SAMLAuthenticationMethod;
import org.cagrid.gaards.dorian.federation.TrustedIdP;
import org.cagrid.gaards.dorian.federation.TrustedIdPStatus;
import org.cagrid.gaards.dorian.stubs.types.DorianInternalFault;
import org.cagrid.gaards.dorian.stubs.types.InvalidAssertionFault;
import org.cagrid.gaards.dorian.stubs.types.InvalidTrustedIdPFault;
import org.cagrid.gaards.pki.CertUtil;
import org.cagrid.tools.database.Database;




/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TrustedIdPManager extends LoggingObject {

	private Database db;

	private final static String TRUST_MANAGER_TABLE = "trust_manager";

	private final static String AUTH_METHODS_TABLE = "trust_manager_auth_methods";

	private boolean dbBuilt = false;

	private IdentityFederationProperties conf;

	private IFSUserPolicy[] accountPolicies;


	public TrustedIdPManager(IdentityFederationProperties conf, Database db) throws DorianInternalFault {
		this.db = db;
		this.conf = conf;
		List<AccountPolicy> policies = conf.getAccountPolicies();
		this.accountPolicies = new IFSUserPolicy[policies.size()];
		for (int i = 0; i < policies.size(); i++) {
			AccountPolicy p = policies.get(i);
			accountPolicies[i] = new IFSUserPolicy();
			accountPolicies[i].setName(p.getDisplayName());
			accountPolicies[i].setClassName(p.getClass().getName());
		}
	}


	public IFSUserPolicy[] getAccountPolicies() {
		return accountPolicies;
	}


	public void clearDatabase() throws DorianInternalFault {
		buildDatabase();
		try {
			db.update("DROP TABLE IF EXISTS " + TRUST_MANAGER_TABLE);
			db.update("DROP TABLE IF EXISTS " + AUTH_METHODS_TABLE);
			dbBuilt = false;
		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("An unexpected database error occurred.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}
	}


	private void buildDatabase() throws DorianInternalFault {
		if (!dbBuilt) {
			try {
				if (!this.db.tableExists(TRUST_MANAGER_TABLE)) {
					String trust = "CREATE TABLE " + TRUST_MANAGER_TABLE + " ("
						+ "ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY," + "NAME VARCHAR(255) NOT NULL,"
						+ "IDP_SUBJECT VARCHAR(255) NOT NULL," + "STATUS VARCHAR(50) NOT NULL,"
						+ "POLICY_CLASS VARCHAR(255) NOT NULL," + "IDP_CERTIFICATE TEXT NOT NULL,"
						+ "USER_ID_ATT_NS TEXT NOT NULL," + "USER_ID_ATT_NAME TEXT NOT NULL,"
						+ "FIRST_NAME_ATT_NS TEXT NOT NULL," + "FIRST_NAME_ATT_NAME TEXT NOT NULL,"
						+ "LAST_NAME_ATT_NS TEXT NOT NULL," + "LAST_NAME_ATT_NAME TEXT NOT NULL,"
						+ "EMAIL_ATT_NS TEXT NOT NULL," + "EMAIL_ATT_NAME TEXT NOT NULL,"
						+ "INDEX document_index (NAME));";
					db.update(trust);

					String methods = "CREATE TABLE " + AUTH_METHODS_TABLE + " ("
						+ "ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY," + "IDP_ID INT NOT NULL,"
						+ "METHOD VARCHAR(255) NOT NULL," + "INDEX document_index (ID));";
					db.update(methods);
				}

			} catch (Exception e) {
				logError(e.getMessage(), e);
				DorianInternalFault fault = new DorianInternalFault();
				fault.setFaultString("An unexpected database error occurred.");
				FaultHelper helper = new FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (DorianInternalFault) helper.getFault();
				throw fault;
			}
			dbBuilt = true;
		}
	}


	public synchronized void removeTrustedIdP(long id) throws DorianInternalFault {
		buildDatabase();
		Connection c = null;
		try {
			c = db.getConnection();
			PreparedStatement s = c.prepareStatement("delete from " + TRUST_MANAGER_TABLE + " WHERE ID= ?");
			s.setLong(1, id);
			s.execute();
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
		removeAuthenticationMethodsForTrustedIdP(id);
	}


	private void removeAuthenticationMethodsForTrustedIdP(long id) throws DorianInternalFault {
		buildDatabase();
		Connection c = null;
		try {
			c = db.getConnection();
			PreparedStatement s = c.prepareStatement("delete from " + AUTH_METHODS_TABLE + " WHERE IDP_ID= ?");
			s.setLong(1, id);
			s.execute();
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
	}


	public synchronized SAMLAuthenticationMethod[] getAuthenticationMethods(long id) throws DorianInternalFault {
		buildDatabase();
		Connection c = null;
		try {
			c = db.getConnection();
			PreparedStatement s = c.prepareStatement("select * from " + AUTH_METHODS_TABLE
				+ " where IDP_ID= ? ORDER BY ID");
			s.setLong(1, id);
			ResultSet rs = s.executeQuery();
			List methods = new ArrayList();
			while (rs.next()) {
				SAMLAuthenticationMethod method = SAMLAuthenticationMethod.fromString(rs.getString("METHOD"));
				methods.add(method);
			}
			rs.close();
			s.close();
			if (methods.size() > 0) {
				SAMLAuthenticationMethod[] list = new SAMLAuthenticationMethod[methods.size()];
				for (int i = 0; i < methods.size(); i++) {
					list[i] = (SAMLAuthenticationMethod) methods.get(i);
				}
				return list;
			} else {
				return null;
			}

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

	}


	public synchronized void updateIdP(TrustedIdP idp) throws DorianInternalFault, InvalidTrustedIdPFault {

		TrustedIdP curr = this.getTrustedIdPById(idp.getId());
		boolean needsUpdate = false;
		String name = curr.getName();
		if ((Utils.clean(idp.getName()) != null) && (!idp.getName().equals(curr.getName()))) {
			InvalidTrustedIdPFault fault = new InvalidTrustedIdPFault();
			fault.setFaultString("The name of a TrustedIdP cannot be changed.");
			throw fault;
		}
		String policy = curr.getUserPolicyClass();

		if ((Utils.clean(idp.getUserPolicyClass()) != null)
			&& (!idp.getUserPolicyClass().equals(curr.getUserPolicyClass()))) {
			needsUpdate = true;
			policy = validateAndGetPolicy(idp.getUserPolicyClass()).getClassName();
		}
		String status = curr.getStatus().getValue();
		if ((idp.getStatus() != null) && (!idp.getStatus().equals(curr.getStatus()))) {
			needsUpdate = true;
			status = idp.getStatus().getValue();
		}
		X509Certificate currcert = validateAndGetCertificate(curr);
		String certSubject = currcert.getSubjectDN().getName();
		String certEncoded = curr.getIdPCertificate();
		if ((Utils.clean(idp.getIdPCertificate()) != null)
			&& (!idp.getIdPCertificate().equals(curr.getIdPCertificate()))) {
			if (!isCertificateUnique(idp.getIdPCertificate())) {
				InvalidTrustedIdPFault fault = new InvalidTrustedIdPFault();
				fault.setFaultString("Cannot update the Trusted IdP, " + idp.getName()
					+ ", it does not contain a unique certificate.");
				throw fault;
			}

			X509Certificate cert = validateAndGetCertificate(idp);
			certSubject = cert.getSubjectDN().getName();
			certEncoded = idp.getIdPCertificate();
			needsUpdate = true;
		}

		String uidNS = curr.getUserIdAttributeDescriptor().getNamespaceURI();
		String uidName = curr.getUserIdAttributeDescriptor().getName();
		if ((idp.getUserIdAttributeDescriptor() != null)
			&& (!idp.getUserIdAttributeDescriptor().equals(curr.getUserIdAttributeDescriptor()))) {
			verifyUserIdAttributeDescriptor(idp.getUserIdAttributeDescriptor());
			uidNS = idp.getUserIdAttributeDescriptor().getNamespaceURI();
			needsUpdate = true;
			uidName = idp.getUserIdAttributeDescriptor().getName();

		}

		String firstNS = curr.getFirstNameAttributeDescriptor().getNamespaceURI();
		String firstName = curr.getFirstNameAttributeDescriptor().getName();

		if ((idp.getFirstNameAttributeDescriptor() != null)
			&& (!idp.getFirstNameAttributeDescriptor().equals(curr.getFirstNameAttributeDescriptor()))) {
			verifyFirstNameAttributeDescriptor(idp.getFirstNameAttributeDescriptor());
			firstNS = idp.getFirstNameAttributeDescriptor().getNamespaceURI();
			needsUpdate = true;
			firstName = idp.getFirstNameAttributeDescriptor().getName();

		}
		String lastNS = curr.getLastNameAttributeDescriptor().getNamespaceURI();
		String lastName = curr.getLastNameAttributeDescriptor().getName();
		if ((idp.getLastNameAttributeDescriptor() != null)
			&& (!idp.getLastNameAttributeDescriptor().equals(curr.getLastNameAttributeDescriptor()))) {
			verifyFirstNameAttributeDescriptor(idp.getFirstNameAttributeDescriptor());
			lastNS = idp.getLastNameAttributeDescriptor().getNamespaceURI();
			needsUpdate = true;
			lastName = idp.getLastNameAttributeDescriptor().getName();
		}

		String emailNS = curr.getEmailAttributeDescriptor().getNamespaceURI();
		String emailName = curr.getEmailAttributeDescriptor().getName();
		if ((idp.getEmailAttributeDescriptor() != null)
			&& (!idp.getEmailAttributeDescriptor().equals(curr.getEmailAttributeDescriptor()))) {
			verifyEmailAttributeDescriptor(idp.getEmailAttributeDescriptor());
			emailNS = idp.getEmailAttributeDescriptor().getNamespaceURI();
			needsUpdate = true;
			emailName = idp.getEmailAttributeDescriptor().getName();
		}

		Connection c = null;
		try {

			if (needsUpdate) {
				c = db.getConnection();
				PreparedStatement s = c
					.prepareStatement("UPDATE "
						+ TRUST_MANAGER_TABLE
						+ " SET NAME= ?, IDP_SUBJECT= ?, STATUS= ?, POLICY_CLASS= ?, IDP_CERTIFICATE= ?, USER_ID_ATT_NS = ?, USER_ID_ATT_NAME = ?, FIRST_NAME_ATT_NS = ?, FIRST_NAME_ATT_NAME = ?, LAST_NAME_ATT_NS = ?, LAST_NAME_ATT_NAME = ?, EMAIL_ATT_NS = ?, EMAIL_ATT_NAME = ? WHERE ID= ?");

				s.setString(1, name);
				s.setString(2, certSubject);
				s.setString(3, status);
				s.setString(4, policy);
				s.setString(5, certEncoded);
				s.setString(6, uidNS);
				s.setString(7, uidName);
				s.setString(8, firstNS);
				s.setString(9, firstName);
				s.setString(10, lastNS);
				s.setString(11, lastName);
				s.setString(12, emailNS);
				s.setString(13, emailName);
				s.setLong(14, curr.getId());
				s.execute();
				s.close();
			}

			if (!Arrays.equals(curr.getAuthenticationMethod(), idp.getAuthenticationMethod())) {
				removeAuthenticationMethodsForTrustedIdP(idp.getId());
				if (idp.getAuthenticationMethod() != null) {
					for (int i = 0; i < idp.getAuthenticationMethod().length; i++) {
						this.addAuthenticationMethod(idp.getId(), idp.getAuthenticationMethod(i));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Error updating the Trusted IdP " + idp.getName()
				+ ", an unexpected database error occurred.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
	}


	public TrustedIdP getTrustedIdP(SAMLAssertion saml) throws DorianInternalFault, InvalidAssertionFault {
		TrustedIdP[] idps = getTrustedIdPs();
		for (int i = 0; i < idps.length; i++) {
			try {
				X509Certificate cert = CertUtil.loadCertificate(idps[i].getIdPCertificate());
				saml.verify(cert);
				return idps[i];
			} catch (SAMLException se) {

			} catch (Exception e) {
				logError(e.getMessage(), e);
			}
		}
		InvalidAssertionFault fault = new InvalidAssertionFault();
		fault.setFaultString("The assertion specified, is not signed by a trusted IdP and therefore is not trusted.");
		throw fault;
	}


	public synchronized TrustedIdP[] getTrustedIdPs() throws DorianInternalFault {
		buildDatabase();
		Connection c = null;
		try {
			c = db.getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select * from " + TRUST_MANAGER_TABLE);
			List idps = new ArrayList();
			while (rs.next()) {
				TrustedIdP idp = new TrustedIdP();
				idp.setId(rs.getLong("ID"));
				idp.setName(rs.getString("NAME"));
				idp.setStatus(TrustedIdPStatus.fromValue(rs.getString("STATUS")));
				idp.setIdPCertificate(rs.getString("IDP_CERTIFICATE"));
				idp.setUserPolicyClass(rs.getString("POLICY_CLASS"));
				SAMLAttributeDescriptor uid = new SAMLAttributeDescriptor();
				uid.setNamespaceURI(rs.getString("USER_ID_ATT_NS"));
				uid.setName(rs.getString("USER_ID_ATT_NAME"));
				idp.setUserIdAttributeDescriptor(uid);

				SAMLAttributeDescriptor firstName = new SAMLAttributeDescriptor();
				firstName.setNamespaceURI(rs.getString("FIRST_NAME_ATT_NS"));
				firstName.setName(rs.getString("FIRST_NAME_ATT_NAME"));
				idp.setFirstNameAttributeDescriptor(firstName);

				SAMLAttributeDescriptor lastName = new SAMLAttributeDescriptor();
				lastName.setNamespaceURI(rs.getString("LAST_NAME_ATT_NS"));
				lastName.setName(rs.getString("LAST_NAME_ATT_NAME"));
				idp.setLastNameAttributeDescriptor(lastName);

				SAMLAttributeDescriptor email = new SAMLAttributeDescriptor();
				email.setNamespaceURI(rs.getString("EMAIL_ATT_NS"));
				email.setName(rs.getString("EMAIL_ATT_NAME"));
				idp.setEmailAttributeDescriptor(email);
				idps.add(idp);
			}
			rs.close();
			s.close();

			TrustedIdP[] list = new TrustedIdP[idps.size()];
			for (int i = 0; i < idps.size(); i++) {
				list[i] = (TrustedIdP) idps.get(i);
				list[i].setAuthenticationMethod(getAuthenticationMethods(list[i].getId()));
			}
			return list;

		} catch (Exception e) {
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Error obtaining a list of trusted IdPs, unexpected database error");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}

	}


	public synchronized TrustedIdP[] getSuspendedTrustedIdPs() throws DorianInternalFault {
		buildDatabase();
		Connection c = null;
		try {
			c = db.getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select * from " + TRUST_MANAGER_TABLE + " where STATUS='"
				+ TrustedIdPStatus.Suspended + "'");
			List idps = new ArrayList();
			while (rs.next()) {
				TrustedIdP idp = new TrustedIdP();
				idp.setId(rs.getLong("ID"));
				idp.setName(rs.getString("NAME"));
				idp.setStatus(TrustedIdPStatus.fromValue(rs.getString("STATUS")));
				idp.setIdPCertificate(rs.getString("IDP_CERTIFICATE"));
				idp.setUserPolicyClass(rs.getString("POLICY_CLASS"));
				idps.add(idp);
			}
			rs.close();
			s.close();

			TrustedIdP[] list = new TrustedIdP[idps.size()];
			for (int i = 0; i < idps.size(); i++) {
				list[i] = (TrustedIdP) idps.get(i);
				list[i].setAuthenticationMethod(getAuthenticationMethods(list[i].getId()));
			}
			return list;

		} catch (Exception e) {
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Error obtaining a list of trusted IdPs, unexpected database error");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}

	}


	public synchronized TrustedIdP getTrustedIdPById(long id) throws DorianInternalFault, InvalidTrustedIdPFault {
		buildDatabase();
		Connection c = null;

		try {
			c = db.getConnection();
			PreparedStatement s = c.prepareStatement("select * from " + TRUST_MANAGER_TABLE + " WHERE ID= ?");
			s.setLong(1, id);
			ResultSet rs = s.executeQuery();
			TrustedIdP idp = null;
			if (rs.next()) {
				idp = new TrustedIdP();
				idp.setId(rs.getLong("ID"));
				idp.setName(rs.getString("NAME"));
				idp.setStatus(TrustedIdPStatus.fromValue(rs.getString("STATUS")));
				idp.setIdPCertificate(rs.getString("IDP_CERTIFICATE"));
				idp.setUserPolicyClass(rs.getString("POLICY_CLASS"));
				SAMLAttributeDescriptor uid = new SAMLAttributeDescriptor();
				uid.setNamespaceURI(rs.getString("USER_ID_ATT_NS"));
				uid.setName(rs.getString("USER_ID_ATT_NAME"));
				idp.setUserIdAttributeDescriptor(uid);

				SAMLAttributeDescriptor firstName = new SAMLAttributeDescriptor();
				firstName.setNamespaceURI(rs.getString("FIRST_NAME_ATT_NS"));
				firstName.setName(rs.getString("FIRST_NAME_ATT_NAME"));
				idp.setFirstNameAttributeDescriptor(firstName);

				SAMLAttributeDescriptor lastName = new SAMLAttributeDescriptor();
				lastName.setNamespaceURI(rs.getString("LAST_NAME_ATT_NS"));
				lastName.setName(rs.getString("LAST_NAME_ATT_NAME"));
				idp.setLastNameAttributeDescriptor(lastName);

				SAMLAttributeDescriptor email = new SAMLAttributeDescriptor();
				email.setNamespaceURI(rs.getString("EMAIL_ATT_NS"));
				email.setName(rs.getString("EMAIL_ATT_NAME"));
				idp.setEmailAttributeDescriptor(email);
			} else {
				InvalidTrustedIdPFault fault = new InvalidTrustedIdPFault();
				fault.setFaultString("The Trusted IdP " + id + " does not exist.");
				throw fault;
			}
			rs.close();
			s.close();
			idp.setAuthenticationMethod(getAuthenticationMethods(idp.getId()));
			return idp;
		} catch (InvalidTrustedIdPFault f) {
			throw f;
		} catch (Exception e) {
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Error obtaining the Trusted IdP " + id + ", unexpected database error");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
	}


	public synchronized TrustedIdP getTrustedIdPByName(String name) throws DorianInternalFault, InvalidTrustedIdPFault {
		buildDatabase();
		Connection c = null;

		try {
			c = db.getConnection();
			PreparedStatement s = c.prepareStatement("select * from " + TRUST_MANAGER_TABLE + " WHERE NAME= ?");
			s.setString(1, name);
			ResultSet rs = s.executeQuery();
			TrustedIdP idp = null;
			if (rs.next()) {
				idp = new TrustedIdP();
				idp.setId(rs.getLong("ID"));
				idp.setName(rs.getString("NAME"));
				idp.setStatus(TrustedIdPStatus.fromValue(rs.getString("STATUS")));
				idp.setIdPCertificate(rs.getString("IDP_CERTIFICATE"));
				idp.setUserPolicyClass(rs.getString("POLICY_CLASS"));
				SAMLAttributeDescriptor uid = new SAMLAttributeDescriptor();
				uid.setNamespaceURI(rs.getString("USER_ID_ATT_NS"));
				uid.setName(rs.getString("USER_ID_ATT_NAME"));
				idp.setUserIdAttributeDescriptor(uid);

				SAMLAttributeDescriptor firstName = new SAMLAttributeDescriptor();
				firstName.setNamespaceURI(rs.getString("FIRST_NAME_ATT_NS"));
				firstName.setName(rs.getString("FIRST_NAME_ATT_NAME"));
				idp.setFirstNameAttributeDescriptor(firstName);

				SAMLAttributeDescriptor lastName = new SAMLAttributeDescriptor();
				lastName.setNamespaceURI(rs.getString("LAST_NAME_ATT_NS"));
				lastName.setName(rs.getString("LAST_NAME_ATT_NAME"));
				idp.setLastNameAttributeDescriptor(lastName);

				SAMLAttributeDescriptor email = new SAMLAttributeDescriptor();
				email.setNamespaceURI(rs.getString("EMAIL_ATT_NS"));
				email.setName(rs.getString("EMAIL_ATT_NAME"));
				idp.setEmailAttributeDescriptor(email);
			} else {
				InvalidTrustedIdPFault fault = new InvalidTrustedIdPFault();
				fault.setFaultString("The Trusted IdP " + name + " does not exist.");
				throw fault;
			}
			rs.close();
			s.close();
			idp.setAuthenticationMethod(getAuthenticationMethods(idp.getId()));
			return idp;
		} catch (InvalidTrustedIdPFault f) {
			throw f;
		} catch (Exception e) {
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Error obtaining the Trusted IdP " + name + ", unexpected database error");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
	}


	public synchronized TrustedIdP getTrustedIdPByDN(String dn) throws DorianInternalFault, InvalidTrustedIdPFault {
		buildDatabase();
		Connection c = null;

		try {
			c = db.getConnection();
			PreparedStatement s = c.prepareStatement("select * from " + TRUST_MANAGER_TABLE + " WHERE IDP_SUBJECT= ?");
			s.setString(1, dn);
			ResultSet rs = s.executeQuery();
			TrustedIdP idp = null;
			if (rs.next()) {
				idp = new TrustedIdP();
				idp.setId(rs.getLong("ID"));
				idp.setName(rs.getString("NAME"));
				idp.setStatus(TrustedIdPStatus.fromValue(rs.getString("STATUS")));
				idp.setIdPCertificate(rs.getString("IDP_CERTIFICATE"));
				idp.setUserPolicyClass(rs.getString("POLICY_CLASS"));

				SAMLAttributeDescriptor uid = new SAMLAttributeDescriptor();
				uid.setNamespaceURI(rs.getString("USER_ID_ATT_NS"));
				uid.setName(rs.getString("USER_ID_ATT_NAME"));
				idp.setUserIdAttributeDescriptor(uid);

				SAMLAttributeDescriptor firstName = new SAMLAttributeDescriptor();
				firstName.setNamespaceURI(rs.getString("FIRST_NAME_ATT_NS"));
				firstName.setName(rs.getString("FIRST_NAME_ATT_NAME"));
				idp.setFirstNameAttributeDescriptor(firstName);

				SAMLAttributeDescriptor lastName = new SAMLAttributeDescriptor();
				lastName.setNamespaceURI(rs.getString("LAST_NAME_ATT_NS"));
				lastName.setName(rs.getString("LAST_NAME_ATT_NAME"));
				idp.setLastNameAttributeDescriptor(lastName);

				SAMLAttributeDescriptor email = new SAMLAttributeDescriptor();
				email.setNamespaceURI(rs.getString("EMAIL_ATT_NS"));
				email.setName(rs.getString("EMAIL_ATT_NAME"));
				idp.setEmailAttributeDescriptor(email);

			} else {
				InvalidTrustedIdPFault fault = new InvalidTrustedIdPFault();
				fault.setFaultString("The Trusted IdP " + dn + " does not exist.");
				throw fault;
			}
			rs.close();
			s.close();
			idp.setAuthenticationMethod(getAuthenticationMethods(idp.getId()));
			return idp;
		} catch (InvalidTrustedIdPFault f) {
			throw f;
		} catch (Exception e) {
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Error obtaining the Trusted IdP " + dn + ", unexpected database error");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
	}


	private String validateAndGetName(TrustedIdP idp) throws DorianInternalFault, InvalidTrustedIdPFault {
		String name = idp.getName();
		if ((name == null) || (name.trim().length() < conf.getMinIdPNameLength())
			|| (name.trim().length() > conf.getMaxIdPNameLength())) {
			InvalidTrustedIdPFault fault = new InvalidTrustedIdPFault();
			fault.setFaultString("Invalid IdP name specified, the IdP name must be between "
				+ conf.getMinIdPNameLength() + " and "
				+ conf.getMaxIdPNameLength() + " in length.");
			throw fault;
		}

		return name.trim();
	}


	private IFSUserPolicy validateAndGetPolicy(String className) throws DorianInternalFault, InvalidTrustedIdPFault {
		for (int i = 0; i < accountPolicies.length; i++) {
			if (accountPolicies[i].getClassName().equals(className)) {
				return accountPolicies[i];
			}
		}
		InvalidTrustedIdPFault fault = new InvalidTrustedIdPFault();
		fault.setFaultString("Invalid User Policy Class Specified.");
		throw fault;
	}


	private X509Certificate validateAndGetCertificate(TrustedIdP idp) throws DorianInternalFault,
		InvalidTrustedIdPFault {

		if (idp.getIdPCertificate() == null) {
			InvalidTrustedIdPFault fault = new InvalidTrustedIdPFault();
			fault.setFaultString("Invalid Trusted IdP, no IdP certificate specified.");
			throw fault;
		}
		StringReader reader = new StringReader(idp.getIdPCertificate());
		X509Certificate cert = null;
		try {
			cert = CertUtil.loadCertificate(reader);
		} catch (Exception e) {
			logError(e.getMessage(), e);
			InvalidTrustedIdPFault fault = new InvalidTrustedIdPFault();
			fault.setFaultString("Invalid IdP Certificate specified.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (InvalidTrustedIdPFault) helper.getFault();
			throw fault;
		}

		if (CertUtil.isExpired(cert)) {
			InvalidTrustedIdPFault fault = new InvalidTrustedIdPFault();
			fault.setFaultString("The IdP Certificate specified is expired.");
			throw fault;
		}

		return cert;

	}


	private boolean isCertificateUnique(String certAsString) throws DorianInternalFault {
		buildDatabase();
		Connection c = null;
		boolean exists = true;
		try {
			c = db.getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select count(*) from " + TRUST_MANAGER_TABLE + " where IDP_CERTIFICATE='"
				+ certAsString + "'");
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count > 0) {
					exists = false;
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


	private void verifyUserIdAttributeDescriptor(SAMLAttributeDescriptor des) throws InvalidTrustedIdPFault {
		verifySAMLAttributeDescriptor(des, "User Id");
	}


	private void verifyFirstNameAttributeDescriptor(SAMLAttributeDescriptor des) throws InvalidTrustedIdPFault {
		verifySAMLAttributeDescriptor(des, "First Name");
	}


	private void verifyLastNameAttributeDescriptor(SAMLAttributeDescriptor des) throws InvalidTrustedIdPFault {
		verifySAMLAttributeDescriptor(des, "Last Name");
	}


	private void verifyEmailAttributeDescriptor(SAMLAttributeDescriptor des) throws InvalidTrustedIdPFault {
		verifySAMLAttributeDescriptor(des, "Email");
	}


	private void verifySAMLAttributeDescriptor(SAMLAttributeDescriptor des, String name) throws InvalidTrustedIdPFault {
		if ((des == null) || (Utils.clean(des.getNamespaceURI()) == null) || (Utils.clean(des.getName()) == null)) {
			InvalidTrustedIdPFault fault = new InvalidTrustedIdPFault();
			fault.setFaultString("Cannot add the Trusted IdP, it does not contain a valid " + name
				+ " Attribute Descriptor");
			throw fault;
		}
	}


	public synchronized TrustedIdP addTrustedIdP(TrustedIdP idp) throws DorianInternalFault, InvalidTrustedIdPFault {
		buildDatabase();
		if (!determineTrustedIdPExistsByName(idp.getName())) {
			String name = validateAndGetName(idp);
			X509Certificate cert = validateAndGetCertificate(idp);
			String policyClass = validateAndGetPolicy(idp.getUserPolicyClass()).getClassName();
			verifyUserIdAttributeDescriptor(idp.getUserIdAttributeDescriptor());
			verifyFirstNameAttributeDescriptor(idp.getFirstNameAttributeDescriptor());
			verifyLastNameAttributeDescriptor(idp.getLastNameAttributeDescriptor());
			verifyEmailAttributeDescriptor(idp.getEmailAttributeDescriptor());

			if (!isCertificateUnique(idp.getIdPCertificate())) {
				InvalidTrustedIdPFault fault = new InvalidTrustedIdPFault();
				fault.setFaultString("Cannot add the Trusted IdP, " + idp.getName()
					+ ", it does not contain a unique certificate.");
				throw fault;
			}
			Connection c = null;
			try {
				c = db.getConnection();
				PreparedStatement s = c
					.prepareStatement("INSERT INTO "
						+ TRUST_MANAGER_TABLE
						+ " SET NAME= ?, IDP_SUBJECT= ?, STATUS= ?, POLICY_CLASS= ?, IDP_CERTIFICATE= ?, USER_ID_ATT_NS = ?, USER_ID_ATT_NAME = ?, FIRST_NAME_ATT_NS = ?, FIRST_NAME_ATT_NAME = ?, LAST_NAME_ATT_NS = ?, LAST_NAME_ATT_NAME = ?, EMAIL_ATT_NS = ?, EMAIL_ATT_NAME = ?");

				s.setString(1, name);
				s.setString(2, cert.getSubjectDN().toString());
				s.setString(3, idp.getStatus().getValue());
				s.setString(4, policyClass);
				s.setString(5, idp.getIdPCertificate());
				s.setString(6, idp.getUserIdAttributeDescriptor().getNamespaceURI());
				s.setString(7, idp.getUserIdAttributeDescriptor().getName());
				s.setString(8, idp.getFirstNameAttributeDescriptor().getNamespaceURI());
				s.setString(9, idp.getFirstNameAttributeDescriptor().getName());
				s.setString(10, idp.getLastNameAttributeDescriptor().getNamespaceURI());
				s.setString(11, idp.getLastNameAttributeDescriptor().getName());
				s.setString(12, idp.getEmailAttributeDescriptor().getNamespaceURI());
				s.setString(13, idp.getEmailAttributeDescriptor().getName());
				s.execute();
				idp.setId(db.getLastAutoId(c));
				s.close();
				if (idp.getAuthenticationMethod() != null) {
					for (int i = 0; i < idp.getAuthenticationMethod().length; i++) {
						this.addAuthenticationMethod(idp.getId(), idp.getAuthenticationMethod(i));
					}
				}
			} catch (Exception e) {
				try {
					this.removeTrustedIdP(idp.getId());
				} catch (Exception ex) {
					logError(ex.getMessage(), ex);
				}
				logError(e.getMessage(), e);
				DorianInternalFault fault = new DorianInternalFault();
				fault.setFaultString("Error adding the Trusted IdP " + name
					+ ", an unexpected database error occurred.");
				FaultHelper helper = new FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (DorianInternalFault) helper.getFault();
				throw fault;
			} finally {
				if (c != null) {
					db.releaseConnection(c);
				}
			}

		} else {
			InvalidTrustedIdPFault fault = new InvalidTrustedIdPFault();
			fault.setFaultString("Cannot not add IdP, an IdP with the name " + idp.getName() + " already exists.");
			throw fault;
		}
		return idp;

	}


	private synchronized void addAuthenticationMethod(long id, SAMLAuthenticationMethod method)
		throws DorianInternalFault {
		Connection c = null;
		try {
			c = db.getConnection();
			PreparedStatement s = c.prepareStatement("INSERT INTO " + AUTH_METHODS_TABLE + " SET IDP_ID= ?,METHOD= ?");

			s.setLong(1, id);
			s.setString(2, method.getValue());
			s.execute();
			s.close();
		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Error adding the authentication method " + method.getValue()
				+ " for the  Trusted IdP " + id + ", an unexpected database error occurred.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			if (c != null) {
				db.releaseConnection(c);
			}
		}
	}


	public synchronized boolean determineTrustedIdPExistsByDN(String subject) throws DorianInternalFault {
		buildDatabase();
		Connection c = null;
		boolean exists = false;
		try {
			c = db.getConnection();
			PreparedStatement s = c.prepareStatement("select count(*) from " + TRUST_MANAGER_TABLE
				+ " where IDP_SUBJECT= ?");
			s.setString(1, subject);
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


	public synchronized boolean determineTrustedIdPExistsByName(String name) throws DorianInternalFault {
		buildDatabase();
		Connection c = null;
		boolean exists = false;
		try {
			c = db.getConnection();
			PreparedStatement s = c.prepareStatement("select count(*) from " + TRUST_MANAGER_TABLE + " where NAME= ?");
			s.setString(1, name);
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


	public synchronized void removeAllTrustedIdPs() throws DorianInternalFault {
		buildDatabase();
		try {
			db.update("delete from " + TRUST_MANAGER_TABLE);
			db.update("delete from " + AUTH_METHODS_TABLE);
		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("An unexpected database error occurred.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}
	}

}