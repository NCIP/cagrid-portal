package gov.nih.nci.cagrid.gums.ifs;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.FaultHelper;
import gov.nih.nci.cagrid.gums.common.GUMSObject;
import gov.nih.nci.cagrid.gums.common.ca.CertUtil;
import gov.nih.nci.cagrid.gums.common.ca.KeyUtil;
import gov.nih.nci.cagrid.gums.ifs.bean.InvalidPasswordFault;

import java.io.ByteArrayInputStream;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class CredentialsManager extends GUMSObject {

	public static String CREDENTIALS_TABLE = "CREDENTIALS";

	private Database db;

	private boolean dbBuilt = false;

	public CredentialsManager(Database db) {
		this.db = db;
	}

	public boolean hasCredentials(String username) throws GUMSInternalFault {
		this.buildDatabase();
		Connection c = null;
		boolean exists = false;
		try {
			c = db.getConnectionManager().getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select count(*) from "
					+ CREDENTIALS_TABLE + " where username='" + username + "'");
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
			fault
					.setFaultString("Unexpected Database Error, Error determining if the user "
							+ username + " has credentials.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.getConnectionManager().releaseConnection(c);
		}
		return exists;
	}

	public void deleteCredentials(String username) throws GUMSInternalFault {
		this.buildDatabase();
		db.update("delete from " + CREDENTIALS_TABLE + " where username='"
				+ username + "'");
	}

	public void addCredentials(String username, String password,
			X509Certificate cert, PrivateKey key) throws GUMSInternalFault {
		this.buildDatabase();
		try {

			if (!hasCredentials(username)) {
				String keyStr = KeyUtil.writePrivateKeyToString(key, password);
				String certStr = CertUtil.writeCertificateToString(cert);
				db.update("INSERT INTO " + CREDENTIALS_TABLE + " VALUES('"
						+ username + "','" + certStr + "','" + keyStr + "')");
			}
		} catch (Exception e) {
			logError(e.getMessage(), e);
			GUMSInternalFault fault = new GUMSInternalFault();
			fault
					.setFaultString("Unexpected Error, could not add credentials to the credentials database.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		}
	}

	public PrivateKey getPrivateKey(String username, String password)
			throws GUMSInternalFault, InvalidPasswordFault {
		this.buildDatabase();
		Connection c = null;
		PrivateKey key = null;
		String keyStr = null;
		try {
			c = db.getConnectionManager().getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select PRIVATE_KEY from "
					+ CREDENTIALS_TABLE + " where username='" + username + "'");
			if (rs.next()) {
				keyStr = rs.getString("PRIVATE_KEY");
			}
			rs.close();
			s.close();
		} catch (Exception e) {
			logError(e.getMessage(), e);
			GUMSInternalFault fault = new GUMSInternalFault();
			fault
					.setFaultString("Unexpected Database Error, Error obtaining the private key for the user "
							+ username + ".");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.getConnectionManager().releaseConnection(c);
		}
		if (keyStr == null) {
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("No PrivateKey exists for the user "
					+ username + ".");
			throw fault;
		}
		try {
			key = KeyUtil.loadPrivateKey(new ByteArrayInputStream(keyStr
					.getBytes()), password);
		} catch (javax.crypto.BadPaddingException e) {
			InvalidPasswordFault fault = new InvalidPasswordFault();
			fault.setFaultString("Invalid Password Specified.");
			throw fault;
		} catch (Exception e) {
			logError(e.getMessage(), e);
			GUMSInternalFault fault = new GUMSInternalFault();
			fault
					.setFaultString("Unexpected Database Error, Error obtaining the private key for the user "
							+ username + ".");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		}
		return key;
	}

	public X509Certificate getCertificate(String username)
			throws GUMSInternalFault {
		this.buildDatabase();
		Connection c = null;
		X509Certificate cert = null;
		try {
			c = db.getConnectionManager().getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select CERTIFICATE from "
					+ CREDENTIALS_TABLE + " where username='" + username + "'");
			if (rs.next()) {
				String certStr = rs.getString("CERTIFICATE");
				cert = CertUtil.loadCertificateFromString(certStr);
			}
			rs.close();
			s.close();
		} catch (Exception e) {
			logError(e.getMessage(), e);
			GUMSInternalFault fault = new GUMSInternalFault();
			fault
					.setFaultString("Unexpected Database Error, Error obtaining the certificate for the user "+username+".");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.getConnectionManager().releaseConnection(c);
		}
		if (cert == null) {
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("No Certificate exists for the user "
					+ username + ".");
			throw fault;
		}
		return cert;
	}

	private void buildDatabase() throws GUMSInternalFault {
		if (!dbBuilt) {
			if (!this.db.tableExists(CREDENTIALS_TABLE)) {
				String users = "CREATE TABLE " + CREDENTIALS_TABLE + " ("
						+ "USERNAME VARCHAR(255) NOT NULL PRIMARY KEY,"
						+ "CERTIFICATE TEXT NOT NULL,"
						+ "PRIVATE_KEY TEXT NOT NULL,"
						+ "INDEX document_index (USERNAME));";
				db.update(users);
			}
			this.dbBuilt = true;
		}
	}

	public void destroyTable() throws GUMSInternalFault {
		db.update("DROP TABLE IF EXISTS " + CREDENTIALS_TABLE);
		dbBuilt = false;
	}

}