package gov.nih.nci.cagrid.gums.ifs;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.GUMSObject;
import gov.nih.nci.cagrid.gums.common.ca.CertUtil;
import gov.nih.nci.cagrid.gums.ifs.bean.AttributeDescriptor;
import gov.nih.nci.cagrid.gums.ifs.bean.InvalidTrustedIdPFault;
import gov.nih.nci.cagrid.gums.ifs.bean.SAMLAuthenticationMethod;
import gov.nih.nci.cagrid.gums.ifs.bean.TrustedIdP;

import java.io.StringReader;
import java.security.cert.X509Certificate;
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
public class TrustManager extends GUMSObject {

	private Database db;

	private final static String TRUST_MANAGER_TABLE = "TRUST_MANAGER";

	private final static String AUTH_METHODS_TABLE = "TRUST_MANAGER_AUTH_METHODS";

	private boolean dbBuilt = false;

	private IFSConfiguration conf;

	public TrustManager(IFSConfiguration conf, Database db) {
		this.db = db;
		this.conf = conf;
	}

	private void buildDatabase() throws GUMSInternalFault {
		if (!dbBuilt) {
			if (!this.db.tableExists(TRUST_MANAGER_TABLE)) {
				String trust = "CREATE TABLE " + TRUST_MANAGER_TABLE + " ("
						+ "NAME VARCHAR(255) NOT NULL,"
						+ "IDP_SUBJECT VARCHAR(255) NOT NULL,"
						+ "POLICY_CLASS VARCHAR(255) NOT NULL,"
						+ "IDP_CERTIFICATE TEXT NOT NULL,"
						+ "INDEX document_index (NAME));";
				db.update(trust);

				String methods = "CREATE TABLE " + AUTH_METHODS_TABLE + " ("
						+ "NAME VARCHAR(255) NOT NULL,"
						+ "METHOD VARCHAR(255) NOT NULL,"
						+ "INDEX document_index (NAME));";
				db.update(methods);
			}
			dbBuilt = true;
		}
	}

	public void removeTrustedIdP(String name) throws GUMSInternalFault {
		buildDatabase();
		db.update("delete from " + TRUST_MANAGER_TABLE + " WHERE NAME='" + name
				+ "'");
		removeAuthenticationMethodsForTrustedIdP(name);
	}

	private void removeAuthenticationMethodsForTrustedIdP(String name)
			throws GUMSInternalFault {
		buildDatabase();
		db.update("delete from " + AUTH_METHODS_TABLE + " WHERE NAME='" + name
				+ "'");
	}

	private SAMLAuthenticationMethod[] getAuthenticationMethods(
			String trustedIdPName) throws GUMSInternalFault {
		buildDatabase();
		Connection c = null;
		try {
			c = db.getConnectionManager().getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select * from " + AUTH_METHODS_TABLE
					+ " where NAME='" + trustedIdPName + "'");
			List methods = new ArrayList();
			while (rs.next()) {
				SAMLAuthenticationMethod method = SAMLAuthenticationMethod
						.fromString(rs.getString("METHOD"));
				methods.add(method);
			}
			rs.close();
			s.close();

			SAMLAuthenticationMethod[] list = new SAMLAuthenticationMethod[methods
					.size()];
			for (int i = 0; i < methods.size(); i++) {
				list[i] = (SAMLAuthenticationMethod) methods.get(i);
			}
			return list;

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

	}

	public TrustedIdP[] getTrustedIdPs() throws GUMSInternalFault {
		buildDatabase();
		Connection c = null;
		try {
			c = db.getConnectionManager().getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select * from "
					+ TRUST_MANAGER_TABLE);
			List idps = new ArrayList();
			while (rs.next()) {
				TrustedIdP idp = new TrustedIdP();
				idp.setName(rs.getString("NAME"));
				idp.setIdPCertificate(rs.getString("IDP_CERTIFICATE"));
				idp.setPolicyClass("POLICY_CLASS");
				idps.add(idp);
			}
			rs.close();
			s.close();

			TrustedIdP[] list = new TrustedIdP[idps.size()];
			for (int i = 0; i < idps.size(); i++) {
				list[i] = (TrustedIdP) idps.get(i);
				list[i]
						.setAuthenticationMethod(getAuthenticationMethods(list[i]
								.getName()));
			}
			return list;

		} catch (Exception e) {
			GUMSInternalFault fault = new GUMSInternalFault();
			fault
					.setFaultString("Error obtaining a list of trusted IdPs, unexpected database error");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.getConnectionManager().releaseConnection(c);
		}

	}

	public synchronized void addTrustedIdP(TrustedIdP idp)
			throws GUMSInternalFault, InvalidTrustedIdPFault {
		buildDatabase();
		if (!determineTrustedIdPExistsByName(idp.getName())) {
			String name = idp.getName();
			if ((name == null)
					|| (name.trim().length() <= conf.getMinimumIdPNameLength())
					|| (name.trim().length() >= conf.getMaximumIdPNameLength())) {
				InvalidTrustedIdPFault fault = new InvalidTrustedIdPFault();
				fault
						.setFaultString("Invalid IdP name specified, the IdP name must be between "
								+ conf.getMinimumIdPNameLength()
								+ " and "
								+ conf.getMaximumIdPNameLength()
								+ " in length.");
				throw fault;
			}
			name = name.trim();

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
				fault
						.setFaultString("The IdP Certificate specified is expired.");
				throw fault;
			}

			// TODO: Validate the policy class;

			String policyClass = idp.getPolicyClass();

			try {
				db.update("INSERT INTO " + TRUST_MANAGER_TABLE + " SET NAME='"
						+ name + "',IDP_SUBJECT='"
						+ cert.getSubjectDN().toString() + "', POLICY_CLASS='"
						+ policyClass + "',IDP_CERTIFICATE='"
						+ idp.getIdPCertificate() + "'");
				for (int i = 0; i < idp.getAuthenticationMethod().length; i++) {
					this.addAuthenticationMethod(name, idp
							.getAuthenticationMethod(i));
				}

			} catch (Exception e) {
				try {
					this.removeTrustedIdP(name);
				} catch (Exception ex) {
					logError(ex.getMessage(), ex);
				}
				logError(e.getMessage(), e);
				GUMSInternalFault fault = new GUMSInternalFault();
				fault.setFaultString("Error adding the Trusted IdP " + name
						+ ", an unexpected database error occurred.");
				FaultHelper helper = new FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (GUMSInternalFault) helper.getFault();
				throw fault;
			}

		} else {
			InvalidTrustedIdPFault fault = new InvalidTrustedIdPFault();
			fault.setFaultString("Cannot add the Trusted IdP, " + idp.getName()
					+ " because it already exists.");
			throw fault;
		}

	}

	private synchronized void addAuthenticationMethod(String idpName,
			SAMLAuthenticationMethod method) throws GUMSInternalFault {
		db.update("INSERT INTO " + TRUST_MANAGER_TABLE + " SET NAME='"
				+ idpName + "',METHOD='" + method.getValue() + "'");
	}

	public boolean determineTrustedIdPExistsBySubject(String subject)
			throws GUMSInternalFault {
		buildDatabase();
		Connection c = null;
		boolean exists = false;
		try {
			c = db.getConnectionManager().getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select count(*) from "
					+ TRUST_MANAGER_TABLE + " where IDP_SUBJECT='" + subject
					+ "'");
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

	public boolean determineTrustedIdPExistsByName(String name)
			throws GUMSInternalFault {
		buildDatabase();
		Connection c = null;
		boolean exists = false;
		try {
			c = db.getConnectionManager().getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select count(*) from "
					+ TRUST_MANAGER_TABLE + " where NAME='" + name + "'");
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

	public void removeAllTrustedIdPs() throws GUMSInternalFault {
		buildDatabase();
		db.update("delete from " + TRUST_MANAGER_TABLE);
		db.update("delete from " + AUTH_METHODS_TABLE);
	}

	public void destroyTable() throws GUMSInternalFault {
		db.update("DROP TABLE IF EXISTS " + TRUST_MANAGER_TABLE);
		dbBuilt = false;
	}

}