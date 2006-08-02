package gov.nih.nci.cagrid.dorian.service.ifs;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.dorian.common.Database;
import gov.nih.nci.cagrid.dorian.common.LoggingObject;
import gov.nih.nci.cagrid.dorian.ifs.bean.InvalidPasswordFault;
import gov.nih.nci.cagrid.dorian.stubs.DorianInternalFault;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;

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
public class CredentialsManager extends LoggingObject {

	public static String CREDENTIALS_TABLE = "credentials";

	private Database db;

	private boolean dbBuilt = false;


	public CredentialsManager(Database db) {
		this.db = db;
	}


	public boolean hasCredentials(String username) throws DorianInternalFault {
		this.buildDatabase();
		Connection c = null;
		boolean exists = false;
		try {
			c = db.getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select count(*) from " + CREDENTIALS_TABLE + " where username='" + username
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
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Unexpected Database Error, Error determining if the user " + username
				+ " has credentials.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
		return exists;
	}


	public void deleteCredentials(String username) throws DorianInternalFault {
		this.buildDatabase();
		db.update("delete from " + CREDENTIALS_TABLE + " where username='" + username + "'");
	}


	public void addCredentials(String username, String password, X509Certificate cert, PrivateKey key)
		throws DorianInternalFault {
		this.buildDatabase();
		try {

			if (!hasCredentials(username)) {
				long serial = cert.getSerialNumber().longValue();
				String keyStr = KeyUtil.writePrivateKey(key, password);
				String certStr = CertUtil.writeCertificate(cert);
				db.update("INSERT INTO " + CREDENTIALS_TABLE + " VALUES('" + username + "'," + serial + ",'" + certStr
					+ "','" + keyStr + "')");
			}
		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Unexpected Error, could not add credentials to the credentials database.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}
	}


	public PrivateKey getPrivateKey(String username, String password) throws DorianInternalFault, InvalidPasswordFault {
		this.buildDatabase();
		Connection c = null;
		PrivateKey key = null;
		String keyStr = null;
		try {
			c = db.getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select PRIVATE_KEY from " + CREDENTIALS_TABLE + " where username='"
				+ username + "'");
			if (rs.next()) {
				keyStr = rs.getString("PRIVATE_KEY");
			}
			rs.close();
			s.close();
		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Unexpected Database Error, Error obtaining the private key for the user " + username
				+ ".");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
		if (keyStr == null || keyStr.trim().equals("")) {
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("No PrivateKey exists for the user " + username + ".");
			throw fault;
		}
		try {
			key = KeyUtil.loadPrivateKey(new ByteArrayInputStream(keyStr.getBytes()), password);
		} catch (Exception e) {
			InvalidPasswordFault fault = new InvalidPasswordFault();
			fault.setFaultString("Invalid Password Specified.");
			throw fault;
		}
		return key;
	}


	public X509Certificate getCertificate(String username) throws DorianInternalFault {
		this.buildDatabase();
		Connection c = null;
		X509Certificate cert = null;
		try {
			c = db.getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select CERTIFICATE from " + CREDENTIALS_TABLE + " where username='"
				+ username + "'");
			if (rs.next()) {
				String certStr = rs.getString("CERTIFICATE");
				cert = CertUtil.loadCertificate(certStr);
			}
			rs.close();
			s.close();
		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("Unexpected Database Error, Error obtaining the certificate for the user " + username
				+ ".");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
		if (cert == null) {
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("No Certificate exists for the user " + username + ".");
			throw fault;
		}
		return cert;
	}


	public long getCertificateSerialNumber(String username) throws DorianInternalFault {
		this.buildDatabase();
		Connection c = null;
		long sn = -1;
		try {
			c = db.getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select SERIAL_NUMBER from " + CREDENTIALS_TABLE + " where username='"
				+ username + "'");
			if (rs.next()) {
				sn = rs.getLong("SERIAL_NUMBER");
			}
			rs.close();
			s.close();
		} catch (Exception e) {
			logError(e.getMessage(), e);
			DorianInternalFault fault = new DorianInternalFault();
			fault
				.setFaultString("Unexpected Database Error, Error obtaining the certificate serial number for the user "
					+ username + ".");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
		if (sn == -1) {
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("No Certificate exists for the user " + username + ".");
			throw fault;
		}
		return sn;
	}


	private void buildDatabase() throws DorianInternalFault {
		if (!dbBuilt) {
			if (!this.db.tableExists(CREDENTIALS_TABLE)) {
				String users = "CREATE TABLE " + CREDENTIALS_TABLE + " ("
					+ "USERNAME VARCHAR(255) NOT NULL PRIMARY KEY," + " SERIAL_NUMBER BIGINT NOT NULL,"
					+ "CERTIFICATE TEXT NOT NULL," + "PRIVATE_KEY TEXT NOT NULL," + "INDEX document_index (USERNAME));";
				db.update(users);
			}
			this.dbBuilt = true;
		}
	}


	public void clearDatabase() throws DorianInternalFault {
		buildDatabase();
		db.update("delete from " + CREDENTIALS_TABLE);
	}

}