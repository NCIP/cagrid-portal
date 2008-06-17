package org.cagrid.gaards.dorian.ca;

import gov.nih.nci.cagrid.common.FaultHelper;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Enumeration;

import org.cagrid.gaards.pki.CertUtil;
import org.cagrid.tools.database.Database;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class EracomWrappingCertificateAuthority extends BaseEracomCertificateAuthority
	implements
		WrappingCertificateAuthority {

	public static String CREDENTIALS_TABLE = "eracom_wrapped_ca";

	public static String WRAPPER_KEY_ALIAS = "dorian-wrapper-key";

	private boolean dbBuilt = false;

	private Database db;


	public EracomWrappingCertificateAuthority(Database db, EracomCertificateAuthorityProperties properties) throws CertificateAuthorityFault {
		super(properties);
		this.db = db;
	}


	public void addCertificate(String alias, X509Certificate cert) throws CertificateAuthorityFault {
		init();
		if (alias.equals(CertificateAuthority.CA_ALIAS)) {
			try {

				getKeyStore().setCertificateEntry(alias, cert);
			} catch (Exception e) {
				logError(e.getMessage(), e);
				CertificateAuthorityFault fault = new CertificateAuthorityFault();
				fault.setFaultString("An unexpected error occurred, could not add certificate.");
				FaultHelper helper = new FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (CertificateAuthorityFault) helper.getFault();
				throw fault;
			}
		} else {
			this.addUserCertificate(alias, cert);
		}

	}


	public void addCredentials(String alias, String password, X509Certificate cert, PrivateKey key)
		throws CertificateAuthorityFault {
		init();
		if (alias.equals(CertificateAuthority.CA_ALIAS)) {
			try {
				getKeyStore().setKeyEntry(alias, key, null, new X509Certificate[]{cert});
			} catch (Exception e) {
				logError(e.getMessage(), e);
				CertificateAuthorityFault fault = new CertificateAuthorityFault();
				fault.setFaultString("Unexpected Error, could not add credentials.");
				FaultHelper helper = new FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (CertificateAuthorityFault) helper.getFault();
				throw fault;
			}
		} else {
			this.addUserCredentials(alias, cert, key);
		}

	}


	@Override
	protected void clear() throws CertificateAuthorityFault {
		buildDatabase();
		try {
			setInitialized(false);
			db.update("delete from " + CREDENTIALS_TABLE);

			Enumeration<String> e = getKeyStore().aliases();
			while (e.hasMoreElements()) {
				getKeyStore().deleteEntry(e.nextElement());
			}
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Unexpected error clearing the CA.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}
	}


	@Override
	public void deleteCredentials(String alias) throws CertificateAuthorityFault {
		init();
		if (alias.equals(CertificateAuthority.CA_ALIAS)) {
			try {
				getKeyStore().deleteEntry(alias);
			} catch (Exception e) {
				logError(e.getMessage(), e);
				CertificateAuthorityFault fault = new CertificateAuthorityFault();
				fault.setFaultString("Could not delete CA credentials.");
				FaultHelper helper = new FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (CertificateAuthorityFault) helper.getFault();
				throw fault;
			}
		} else {
			this.deleteUserCredentials(alias);
		}

	}


	@Override
	public X509Certificate getCertificate(String alias) throws CertificateAuthorityFault {
		init();
		if (alias.equals(CertificateAuthority.CA_ALIAS)) {
			try {
				if (!hasCredentials(alias)) {
					CertificateAuthorityFault fault = new CertificateAuthorityFault();
					fault.setFaultString("The requested certificate does not exist.");
					throw fault;
				} else {
					return convert((X509Certificate) getKeyStore().getCertificate(alias));
				}
			} catch (CertificateAuthorityFault f) {
				throw f;
			} catch (Exception e) {
				logError(e.getMessage(), e);
				CertificateAuthorityFault fault = new CertificateAuthorityFault();
				fault.setFaultString("Unexpected Error, could not obtain the certificate.");
				FaultHelper helper = new FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (CertificateAuthorityFault) helper.getFault();
				throw fault;
			}
		} else {
			return this.getUserCertificate(alias);
		}
	}


	@Override
	public long getCertificateSerialNumber(String alias) throws CertificateAuthorityFault {
		init();
		if (alias.equals(CertificateAuthority.CA_ALIAS)) {
			try {
				X509Certificate cert = getCertificate(alias);
				return cert.getSerialNumber().longValue();
			} catch (Exception e) {
				logError(e.getMessage(), e);
				CertificateAuthorityFault fault = new CertificateAuthorityFault();
				fault
					.setFaultString("An unexpected error occurred, could not the serial number of the CA certificate.");
				FaultHelper helper = new FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (CertificateAuthorityFault) helper.getFault();
				throw fault;
			}
		} else {
			return getUserCertificateSerialNumber(alias);
		}
	}


	@Override
	public PrivateKey getPrivateKey(String alias, String password) throws CertificateAuthorityFault {
		init();
		if (alias.equals(CertificateAuthority.CA_ALIAS)) {
			try {
				if (!hasCredentials(alias)) {
					CertificateAuthorityFault fault = new CertificateAuthorityFault();
					fault.setFaultString("The requested private key does not exist.");
					throw fault;
				} else {
					return (PrivateKey) getKeyStore().getKey(alias, null);
				}
			} catch (CertificateAuthorityFault f) {
				throw f;
			} catch (Exception e) {
				logError(e.getMessage(), e);
				CertificateAuthorityFault fault = new CertificateAuthorityFault();
				fault.setFaultString("Unexpected Error, could not obtain the private key.");
				FaultHelper helper = new FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (CertificateAuthorityFault) helper.getFault();
				throw fault;
			}
		} else {
			return this.getUserPrivateKey(alias);
		}
	}


	@Override
	public boolean hasCredentials(String alias) throws CertificateAuthorityFault {
		init();
		if (alias.equals(CertificateAuthority.CA_ALIAS)) {
			try {
				return getKeyStore().containsAlias(alias);
			} catch (Exception e) {
				logError(e.getMessage(), e);
				CertificateAuthorityFault fault = new CertificateAuthorityFault();
				fault.setFaultString("An unexpected error occurred, could determin if credentials exist.");
				FaultHelper helper = new FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (CertificateAuthorityFault) helper.getFault();
				throw fault;
			}
		} else {
			return hasUserCredentials(alias);
		}
	}


	public String getUserCredentialsProvider() {
		return "BC";
	}


	private void addUserCertificate(String alias, X509Certificate cert) throws CertificateAuthorityFault {
		this.buildDatabase();
		Connection c = null;
		try {
			if (!hasCredentials(alias)) {
				c = db.getConnection();
				long serial = cert.getSerialNumber().longValue();
				String certStr = CertUtil.writeCertificate(cert);
				PreparedStatement s = c.prepareStatement("INSERT INTO " + CREDENTIALS_TABLE
					+ " SET ALIAS= ?, SERIAL_NUMBER= ?, CERTIFICATE= ?");
				s.setString(1, alias);
				s.setLong(2, serial);
				s.setString(3, certStr);
				s.execute();
				s.close();
			}
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Unexpected Error, could not add certificate to the credentials database.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
	}


	private boolean hasUserCredentials(String alias) throws CertificateAuthorityFault {
		this.buildDatabase();
		Connection c = null;
		boolean exists = false;
		try {
			c = db.getConnection();
			PreparedStatement s = c.prepareStatement("select count(*) from " + CREDENTIALS_TABLE + " where ALIAS= ?");
			s.setString(1, alias);
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
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Unexpected Database Error, Error determining if the user " + alias
				+ " has credentials.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
		return exists;
	}


	private void deleteUserCredentials(String alias) throws CertificateAuthorityFault {
		this.buildDatabase();
		Connection c = null;
		try {
			c = db.getConnection();
			PreparedStatement s = c.prepareStatement("delete from " + CREDENTIALS_TABLE + " where ALIAS= ? ");
			s.setString(1, alias);
			s.execute();
			s.close();
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Unexpected Database Error, Error removing the credentials for the user " + alias
				+ "!!!");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
	}


	private void addUserCredentials(String alias, X509Certificate cert, PrivateKey key)
		throws CertificateAuthorityFault {
		this.buildDatabase();
		Connection c = null;
		try {
			if (!hasUserCredentials(alias)) {
				long serial = cert.getSerialNumber().longValue();
				WrappedKey wk = wrap(key);
				String certStr = CertUtil.writeCertificate(cert);
				c = db.getConnection();
				PreparedStatement s = c.prepareStatement("INSERT INTO " + CREDENTIALS_TABLE
					+ " SET ALIAS= ?, SERIAL_NUMBER= ?, CERTIFICATE= ?, PRIVATE_KEY= ?, IV= ?");
				s.setString(1, alias);
				s.setLong(2, serial);
				s.setString(3, certStr);
				s.setBytes(4, wk.getWrappedKeyData());
				s.setBytes(5, wk.getIV());
				s.execute();
				s.close();
			}
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Unexpected Error, could not add credentials to the credentials database.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
	}


	private PrivateKey getUserPrivateKey(String alias) throws CertificateAuthorityFault {
		this.buildDatabase();
		Connection c = null;
		PrivateKey key = null;
		try {
			c = db.getConnection();
			PreparedStatement s = c.prepareStatement("select PRIVATE_KEY, IV from " + CREDENTIALS_TABLE
				+ " where ALIAS= ?");
			s.setString(1, alias);
			ResultSet rs = s.executeQuery();
			if (rs.next()) {
				byte[] wrapped = rs.getBytes("PRIVATE_KEY");
				byte[] iv = rs.getBytes("IV");
				WrappedKey wk = new WrappedKey(wrapped, iv);
				key = unwrap(wk);
			}
			rs.close();
			s.close();
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Unexpected error obtaining the private key for the user " + alias + ".");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
		if (key == null) {
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("No PrivateKey exists for the user " + alias + ".");
			throw fault;
		}
		return key;
	}


	private X509Certificate getUserCertificate(String alias) throws CertificateAuthorityFault {
		this.buildDatabase();
		Connection c = null;
		X509Certificate cert = null;
		try {
			c = db.getConnection();
			PreparedStatement s = c
				.prepareStatement("select CERTIFICATE from " + CREDENTIALS_TABLE + " where ALIAS= ?");
			s.setString(1, alias);
			ResultSet rs = s.executeQuery();
			if (rs.next()) {
				String certStr = rs.getString("CERTIFICATE");
				cert = CertUtil.loadCertificate(certStr);
			}
			rs.close();
			s.close();
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Unexpected Database Error, Error obtaining the certificate for the user " + alias
				+ ".");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
		if (cert == null) {
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("No Certificate exists for the user " + alias + ".");
			throw fault;
		}
		return cert;
	}


	private long getUserCertificateSerialNumber(String alias) throws CertificateAuthorityFault {
		this.buildDatabase();
		Connection c = null;
		long sn = -1;
		try {
			c = db.getConnection();
			PreparedStatement s = c.prepareStatement("select SERIAL_NUMBER from " + CREDENTIALS_TABLE
				+ " where ALIAS= ?");
			s.setString(1, alias);
			ResultSet rs = s.executeQuery();
			if (rs.next()) {
				sn = rs.getLong("SERIAL_NUMBER");
			}
			rs.close();
			s.close();
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault
				.setFaultString("Unexpected Database Error, Error obtaining the certificate serial number for the user "
					+ alias + ".");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
		if (sn == -1) {
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("No Certificate exists for the user " + alias + ".");
			throw fault;
		}
		return sn;
	}


	private void buildDatabase() throws CertificateAuthorityFault {
		if (!dbBuilt) {
			try {
				if (!this.db.tableExists(CREDENTIALS_TABLE)) {
					String users = "CREATE TABLE " + CREDENTIALS_TABLE + " ("
						+ "ALIAS VARCHAR(255) NOT NULL PRIMARY KEY," + " SERIAL_NUMBER BIGINT NOT NULL,"
						+ "CERTIFICATE TEXT NOT NULL," + "PRIVATE_KEY BLOB," + "IV BLOB,"
						+ "INDEX document_index (ALIAS));";
					db.update(users);
				}
				this.dbBuilt = true;
			} catch (Exception e) {
				logError(e.getMessage(), e);
				CertificateAuthorityFault fault = new CertificateAuthorityFault();
				fault.setFaultString("Unexpected error creating the CA database");
				FaultHelper helper = new FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (CertificateAuthorityFault) helper.getFault();
				throw fault;
			}
		}
	}
}