package gov.nih.nci.cagrid.dorian.ifs;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.dorian.bean.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.common.Database;
import gov.nih.nci.cagrid.dorian.common.DorianObject;
import gov.nih.nci.cagrid.dorian.common.ca.CertUtil;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserPolicy;
import gov.nih.nci.cagrid.dorian.ifs.bean.InvalidAssertionFault;
import gov.nih.nci.cagrid.dorian.ifs.bean.InvalidTrustedIdPFault;
import gov.nih.nci.cagrid.dorian.ifs.bean.SAMLAuthenticationMethod;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdPStatus;

import java.io.StringReader;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.opensaml.SAMLAssertion;
import org.opensaml.SAMLException;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TrustManager extends DorianObject {
	

	private Database db;

	private final static String TRUST_MANAGER_TABLE = "TRUST_MANAGER";

	private final static String AUTH_METHODS_TABLE = "TRUST_MANAGER_AUTH_METHODS";

	private boolean dbBuilt = false;

	private IFSConfiguration conf;


	public TrustManager(IFSConfiguration conf, Database db) {
		this.db = db;
		this.conf = conf;
	}


	private void buildDatabase() throws DorianInternalFault {
		if (!dbBuilt) {
			if (!this.db.tableExists(TRUST_MANAGER_TABLE)) {
				String trust = "CREATE TABLE " + TRUST_MANAGER_TABLE + " ("
					+ "ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY," + "NAME VARCHAR(255) NOT NULL,"
					+ "IDP_SUBJECT VARCHAR(255) NOT NULL," + "STATUS VARCHAR(50) NOT NULL,"
					+ "POLICY_CLASS VARCHAR(255) NOT NULL," + "IDP_CERTIFICATE TEXT NOT NULL,"
					+ "INDEX document_index (NAME));";
				db.update(trust);

				String methods = "CREATE TABLE " + AUTH_METHODS_TABLE + " (" + "ID INT NOT NULL,"
					+ "METHOD VARCHAR(255) NOT NULL," + "INDEX document_index (ID));";
				db.update(methods);
			}
			dbBuilt = true;
		}
	}


	public synchronized void removeTrustedIdP(long id) throws DorianInternalFault {
		buildDatabase();
		db.update("delete from " + TRUST_MANAGER_TABLE + " WHERE ID=" + id);
		removeAuthenticationMethodsForTrustedIdP(id);
	}


	private void removeAuthenticationMethodsForTrustedIdP(long id) throws DorianInternalFault {
		buildDatabase();
		db.update("delete from " + AUTH_METHODS_TABLE + " WHERE ID=" + id);
	}


	public synchronized SAMLAuthenticationMethod[] getAuthenticationMethods(long id) throws DorianInternalFault {
		buildDatabase();
		Connection c = null;
		try {
			c = db.getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select * from " + AUTH_METHODS_TABLE + " where ID=" + id);
			List methods = new ArrayList();
			while (rs.next()) {
				SAMLAuthenticationMethod method = SAMLAuthenticationMethod.fromString(rs.getString("METHOD"));
				methods.add(method);
			}
			rs.close();
			s.close();

			SAMLAuthenticationMethod[] list = new SAMLAuthenticationMethod[methods.size()];
			for (int i = 0; i < methods.size(); i++) {
				list[i] = (SAMLAuthenticationMethod) methods.get(i);
			}
			return list;

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


	private String clean(String s) {
		if ((s == null) || (s.trim().length() == 0)) {
			return null;
		} else {
			return s;
		}
	}


	private void buildUpdate(boolean needsUpdate, StringBuffer sql, String field, String value) {
		if (needsUpdate) {
			sql.append(",").append(field).append("='").append(value).append("'");
		} else {
			sql.append("UPDATE " + TRUST_MANAGER_TABLE + " SET ");
			sql.append(field).append("='").append(value).append("'");
		}

	}


	public synchronized void updateIdP(TrustedIdP idp) throws DorianInternalFault, InvalidTrustedIdPFault {

		TrustedIdP curr = this.getTrustedIdPById(idp.getId());
		StringBuffer sql = new StringBuffer();
		boolean needsUpdate = false;

		if ((clean(idp.getName()) != null) && (!idp.getName().equals(curr.getName()))) {
			if (determineTrustedIdPExistsByName(idp.getName())) {
				InvalidTrustedIdPFault fault = new InvalidTrustedIdPFault();
				fault.setFaultString("The name of Trusted IdP " + curr.getName() + " cannot be changed to "
					+ idp.getName() + " and IdP with that name already exists");
				throw fault;
			}
			buildUpdate(needsUpdate, sql, "NAME", validateAndGetName(idp));
			needsUpdate = true;
		}

		if ((clean(idp.getUserPolicyClass()) != null) && (!idp.getUserPolicyClass().equals(curr.getUserPolicyClass()))) {
			buildUpdate(needsUpdate, sql, "POLICY_CLASS", validateAndGetPolicy(idp.getUserPolicyClass()).getClassName());
			needsUpdate = true;
		}

		if ((idp.getStatus() != null) && (!idp.getStatus().equals(curr.getStatus()))) {
			buildUpdate(needsUpdate, sql, "STATUS", idp.getStatus().getValue());
			needsUpdate = true;
		}

		if ((clean(idp.getIdPCertificate()) != null) && (!idp.getIdPCertificate().equals(curr.getIdPCertificate()))) {
			if (!isCertificateUnique(idp.getIdPCertificate())) {
				InvalidTrustedIdPFault fault = new InvalidTrustedIdPFault();
				fault.setFaultString("Cannot update the Trusted IdP, " + idp.getName()
					+ ", it does not contain a unique certificate.");
				throw fault;
			}
			X509Certificate cert = validateAndGetCertificate(idp);
			buildUpdate(needsUpdate, sql, "IDP_SUBJECT", cert.getSubjectDN().getName());
			needsUpdate = true;
			buildUpdate(needsUpdate, sql, "IDP_CERTIFICATE", idp.getIdPCertificate());
		}

		try {
			if (!idp.equals(curr)) {
				if (needsUpdate) {
					sql.append(" WHERE ID=" + idp.getId());
					db.update(sql.toString());
				}
				removeAuthenticationMethodsForTrustedIdP(idp.getId());
				for (int i = 0; i < idp.getAuthenticationMethod().length; i++) {
					this.addAuthenticationMethod(idp.getId(), idp.getAuthenticationMethod(i));
				}
			}
		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Error updating the Trusted IdP " + idp.getName()
				+ ", an unexpected database error occurred.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}
	}


	public TrustedIdP getTrustedIdP(SAMLAssertion saml) throws DorianInternalFault, InvalidAssertionFault {
		TrustedIdP[] idps = getTrustedIdPs();
		for (int i = 0; i < idps.length; i++) {
			try {
				X509Certificate cert = CertUtil.loadCertificateFromString(idps[i].getIdPCertificate());
				saml.verify(cert, false);
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
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select * from " + TRUST_MANAGER_TABLE + " WHERE ID=" + id);
			TrustedIdP idp = null;
			if (rs.next()) {
				idp = new TrustedIdP();
				idp.setId(rs.getLong("ID"));
				idp.setName(rs.getString("NAME"));
				idp.setStatus(TrustedIdPStatus.fromValue(rs.getString("STATUS")));
				idp.setIdPCertificate(rs.getString("IDP_CERTIFICATE"));
				idp.setUserPolicyClass(rs.getString("POLICY_CLASS"));
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
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select * from " + TRUST_MANAGER_TABLE + " WHERE NAME='" + name + "'");
			TrustedIdP idp = null;
			if (rs.next()) {
				idp = new TrustedIdP();
				idp.setId(rs.getLong("ID"));
				idp.setName(rs.getString("NAME"));
				idp.setStatus(TrustedIdPStatus.fromValue(rs.getString("STATUS")));
				idp.setIdPCertificate(rs.getString("IDP_CERTIFICATE"));
				idp.setUserPolicyClass(rs.getString("POLICY_CLASS"));
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
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select * from " + TRUST_MANAGER_TABLE + " WHERE IDP_SUBJECT='" + dn + "'");
			TrustedIdP idp = null;
			if (rs.next()) {
				idp = new TrustedIdP();
				idp.setId(rs.getLong("ID"));
				idp.setName(rs.getString("NAME"));
				idp.setStatus(TrustedIdPStatus.fromValue(rs.getString("STATUS")));
				idp.setIdPCertificate(rs.getString("IDP_CERTIFICATE"));
				idp.setUserPolicyClass(rs.getString("POLICY_CLASS"));
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
		if ((name == null) || (name.trim().length() < conf.getMinimumIdPNameLength())
			|| (name.trim().length() > conf.getMaximumIdPNameLength())) {
			InvalidTrustedIdPFault fault = new InvalidTrustedIdPFault();
			fault.setFaultString("Invalid IdP name specified, the IdP name must be between "
				+ conf.getMinimumIdPNameLength() + " and " + conf.getMaximumIdPNameLength() + " in length.");
			throw fault;
		}
		return name.trim();
	}


	private IFSUserPolicy validateAndGetPolicy(String className) throws DorianInternalFault, InvalidTrustedIdPFault {
		IFSUserPolicy[] policies = conf.getUserPolicies();
		for (int i = 0; i < policies.length; i++) {
			if (policies[i].getClassName().equals(className)) {
				return policies[i];
			}
		}
		InvalidTrustedIdPFault fault = new InvalidTrustedIdPFault();
		fault.setFaultString("Invalid User Policy Class Specified.");
		throw fault;
	}


	private X509Certificate validateAndGetCertificate(TrustedIdP idp) throws DorianInternalFault, InvalidTrustedIdPFault {
  
		if(idp.getIdPCertificate() == null){
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


	public synchronized TrustedIdP addTrustedIdP(TrustedIdP idp) throws DorianInternalFault, InvalidTrustedIdPFault {
		buildDatabase();
		if (!determineTrustedIdPExistsByName(idp.getName())) {
			String name = validateAndGetName(idp);
			X509Certificate cert = validateAndGetCertificate(idp);
			String policyClass = validateAndGetPolicy(idp.getUserPolicyClass()).getClassName();

			if (!isCertificateUnique(idp.getIdPCertificate())) {
				InvalidTrustedIdPFault fault = new InvalidTrustedIdPFault();
				fault.setFaultString("Cannot add the Trusted IdP, " + idp.getName()
					+ ", it does not contain a unique certificate.");
				throw fault;
			}

			try {
				long id = db.insertGetId("INSERT INTO " + TRUST_MANAGER_TABLE + " SET NAME='" + name
					+ "',IDP_SUBJECT='" + cert.getSubjectDN().toString() + "', STATUS='" + idp.getStatus().getValue()
					+ "', POLICY_CLASS='" + policyClass + "',IDP_CERTIFICATE='" + idp.getIdPCertificate() + "'");
				idp.setId(id);
				for (int i = 0; i < idp.getAuthenticationMethod().length; i++) {
					this.addAuthenticationMethod(idp.getId(), idp.getAuthenticationMethod(i));
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
			}

		} else {
			InvalidTrustedIdPFault fault = new InvalidTrustedIdPFault();
			fault.setFaultString("Cannot add the Trusted IdP, " + idp.getName() + " because it already exists.");
			throw fault;
		}
		return idp;

	}


	private synchronized void addAuthenticationMethod(long id, SAMLAuthenticationMethod method)
		throws DorianInternalFault {
		db.update("INSERT INTO " + AUTH_METHODS_TABLE + " SET ID=" + id + ",METHOD='" + method.getValue() + "'");
	}


	public synchronized boolean determineTrustedIdPExistsByDN(String subject) throws DorianInternalFault {
		buildDatabase();
		Connection c = null;
		boolean exists = false;
		try {
			c = db.getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select count(*) from " + TRUST_MANAGER_TABLE + " where IDP_SUBJECT='"
				+ subject + "'");
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
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select count(*) from " + TRUST_MANAGER_TABLE + " where NAME='" + name + "'");
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
		db.update("delete from " + TRUST_MANAGER_TABLE);
		db.update("delete from " + AUTH_METHODS_TABLE);
	}


	public void destroyTable() throws DorianInternalFault {
		db.update("DROP TABLE IF EXISTS " + TRUST_MANAGER_TABLE);
		dbBuilt = false;
	}

}