package gov.nih.nci.cagrid.dorian.service.ca;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.dorian.conf.DorianCAConfiguration;
import gov.nih.nci.cagrid.dorian.service.Database;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;

import java.io.ByteArrayInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Enumeration;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;

import au.com.eracom.crypto.provider.ERACOMProvider;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class EracomWrappingCertificateAuthority extends CertificateAuthority {

	public static final String SIGNATURE_ALGORITHM = "MD5WithRSA";

	public static String CREDENTIALS_TABLE = "eracom_wrapped_ca";

	public static String WRAPPER_KEY_ALIAS = "dorian-wrapper-key";

	private Provider provider;
	private KeyStore keyStore;
	private Database db;
	private Key wrapper;
	private boolean isInit = false;

	private boolean dbBuilt = false;

	public EracomWrappingCertificateAuthority(Database db,
			DorianCAConfiguration conf) throws CertificateAuthorityFault {
		super(conf);
		try {
			this.db = db;
			provider = new ERACOMProvider();
			Security.addProvider(provider);
			keyStore = KeyStore.getInstance("CRYPTOKI", provider.getName());
			keyStore.load(null, conf.getCertificateAuthorityPassword()
					.toCharArray());

		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault
					.setFaultString("Error initializing the Dorian Certificate Authority.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}
	}

	private void init() throws CertificateAuthorityFault {
		try {
			if (!isInit) {
				if (keyStore.containsAlias(WRAPPER_KEY_ALIAS)) {
					wrapper = keyStore.getKey(WRAPPER_KEY_ALIAS, null);
				} else {
					KeyGenerator generator1 = KeyGenerator.getInstance("AES",
							provider);
					generator1.init(256, new SecureRandom());
					keyStore.setKeyEntry(WRAPPER_KEY_ALIAS, generator1
							.generateKey(), null, null);
					wrapper = keyStore.getKey(WRAPPER_KEY_ALIAS, null);
				}
				isInit = true;
			}
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault
					.setFaultString("An unexpected error occurred, could not add certificate.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}
	}

	public void addCertificate(String alias, X509Certificate cert)
			throws CertificateAuthorityFault {
		init();
		if (alias.equals(CertificateAuthority.CA_ALIAS)) {
			try {

				keyStore.setCertificateEntry(alias, cert);
			} catch (Exception e) {
				logError(e.getMessage(), e);
				CertificateAuthorityFault fault = new CertificateAuthorityFault();
				fault
						.setFaultString("An unexpected error occurred, could not add certificate.");
				FaultHelper helper = new FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (CertificateAuthorityFault) helper.getFault();
				throw fault;
			}
		} else {
			this.addUserCertificate(alias, cert);
		}

	}

	public void addCredentials(String alias, String password,
			X509Certificate cert, PrivateKey key)
			throws CertificateAuthorityFault {
		init();
		if (alias.equals(CertificateAuthority.CA_ALIAS)) {
			try {
				keyStore.setKeyEntry(alias, key, null,
						new X509Certificate[] { cert });
			} catch (Exception e) {
				logError(e.getMessage(), e);
				CertificateAuthorityFault fault = new CertificateAuthorityFault();
				fault
						.setFaultString("Unexpected Error, could not add credentials.");
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
			this.isInit = false;
			db.update("delete from " + CREDENTIALS_TABLE);

			Enumeration<String> e = keyStore.aliases();
			while (e.hasMoreElements()) {
				keyStore.deleteEntry(e.nextElement());
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
	public void deleteCredentials(String alias)
			throws CertificateAuthorityFault {
		init();
		if (alias.equals(CertificateAuthority.CA_ALIAS)) {
			try {
				keyStore.deleteEntry(alias);
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
	public X509Certificate getCertificate(String alias)
			throws CertificateAuthorityFault {
		init();
		if (alias.equals(CertificateAuthority.CA_ALIAS)) {
			try {
				if (!hasCredentials(alias)) {
					CertificateAuthorityFault fault = new CertificateAuthorityFault();
					fault
							.setFaultString("The requested certificate does not exist.");
					throw fault;
				} else {
					return convert((X509Certificate) keyStore
							.getCertificate(alias));
				}
			} catch (CertificateAuthorityFault f) {
				throw f;
			} catch (Exception e) {
				logError(e.getMessage(), e);
				CertificateAuthorityFault fault = new CertificateAuthorityFault();
				fault
						.setFaultString("Unexpected Error, could not obtain the certificate.");
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
	public long getCertificateSerialNumber(String alias)
			throws CertificateAuthorityFault {
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
	public PrivateKey getPrivateKey(String alias, String password)
			throws CertificateAuthorityFault {
		init();
		if (alias.equals(CertificateAuthority.CA_ALIAS)) {
			try {
				if (!hasCredentials(alias)) {
					CertificateAuthorityFault fault = new CertificateAuthorityFault();
					fault
							.setFaultString("The requested private key does not exist.");
					throw fault;
				} else {
					return (PrivateKey) keyStore.getKey(alias, null);
				}
			} catch (CertificateAuthorityFault f) {
				throw f;
			} catch (Exception e) {
				logError(e.getMessage(), e);
				CertificateAuthorityFault fault = new CertificateAuthorityFault();
				fault
						.setFaultString("Unexpected Error, could not obtain the private key.");
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
	public boolean hasCredentials(String alias)
			throws CertificateAuthorityFault {
		init();
		if (alias.equals(CertificateAuthority.CA_ALIAS)) {
			try {
				return keyStore.containsAlias(alias);
			} catch (Exception e) {
				logError(e.getMessage(), e);
				CertificateAuthorityFault fault = new CertificateAuthorityFault();
				fault
						.setFaultString("An unexpected error occurred, could determin if credentials exist.");
				FaultHelper helper = new FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (CertificateAuthorityFault) helper.getFault();
				throw fault;
			}
		} else {
			return hasUserCredentials(alias);
		}
	}

	public String getCACredentialsProvider() {
		return provider.getName();
	}

	public String getUserCredentialsProvider() {
		return "BC";
	}

	public String getSignatureAlgorithm() {
		return SIGNATURE_ALGORITHM;
	}

	private void addUserCertificate(String alias, X509Certificate cert)
			throws CertificateAuthorityFault {
		this.buildDatabase();
		Connection c = null;
		try {
			if (!hasCredentials(alias)) {
				c = db.getConnection();
				long serial = cert.getSerialNumber().longValue();
				String certStr = CertUtil.writeCertificate(cert);
				PreparedStatement s = c.prepareStatement("INSERT INTO "
						+ CREDENTIALS_TABLE
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
			fault
					.setFaultString("Unexpected Error, could not add certificate to the credentials database.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
	}

	private boolean hasUserCredentials(String alias)
			throws CertificateAuthorityFault {
		this.buildDatabase();
		Connection c = null;
		boolean exists = false;
		try {
			c = db.getConnection();
			PreparedStatement s = c.prepareStatement("select count(*) from "
					+ CREDENTIALS_TABLE + " where ALIAS= ?");
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
			fault
					.setFaultString("Unexpected Database Error, Error determining if the user "
							+ alias + " has credentials.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
		return exists;
	}

	private void deleteUserCredentials(String alias)
			throws CertificateAuthorityFault {
		this.buildDatabase();
		Connection c = null;
		try {
			c = db.getConnection();
			PreparedStatement s = c.prepareStatement("delete from "
					+ CREDENTIALS_TABLE + " where ALIAS= ? ");
			s.setString(1, alias);
			s.execute();
			s.close();
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault
					.setFaultString("Unexpected Database Error, Error removing the credentials for the user "
							+ alias + "!!!");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
	}

	private void addUserCredentials(String alias, X509Certificate cert,
			PrivateKey key) throws CertificateAuthorityFault {
		this.buildDatabase();
		Connection c = null;
		try {
			if (!hasUserCredentials(alias)) {
				long serial = cert.getSerialNumber().longValue();
				byte[] input = KeyUtil.writePrivateKey(key, (String) null)
						.getBytes();
				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding",
						provider);
				cipher.init(Cipher.ENCRYPT_MODE, wrapper);
				byte[] wrappedKey = cipher.doFinal(input);
				byte[] iv = cipher.getIV();
				String certStr = CertUtil.writeCertificate(cert);
				c = db.getConnection();
				PreparedStatement s = c
						.prepareStatement("INSERT INTO "
								+ CREDENTIALS_TABLE
								+ " SET ALIAS= ?, SERIAL_NUMBER= ?, CERTIFICATE= ?, PRIVATE_KEY= ?, IV= ?");
				s.setString(1, alias);
				s.setLong(2, serial);
				s.setString(3, certStr);
				s.setBytes(4, wrappedKey);
				s.setBytes(5, iv);
				s.execute();
				s.close();
			}
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault
					.setFaultString("Unexpected Error, could not add credentials to the credentials database.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
	}

	private PrivateKey getUserPrivateKey(String alias)
			throws CertificateAuthorityFault {
		this.buildDatabase();
		Connection c = null;
		PrivateKey key = null;
		try {
			c = db.getConnection();
			PreparedStatement s = c
					.prepareStatement("select PRIVATE_KEY, IV from "
							+ CREDENTIALS_TABLE + " where ALIAS= ?");
			s.setString(1, alias);
			ResultSet rs = s.executeQuery();
			if (rs.next()) {
				byte[] wrapped = rs.getBytes("PRIVATE_KEY");
				byte[] iv = rs.getBytes("IV");
				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding",
						provider);
				IvParameterSpec dps = new IvParameterSpec(iv);
				cipher.init(Cipher.DECRYPT_MODE, wrapper, dps);
				byte[] output = cipher.doFinal(wrapped);
				key = KeyUtil.loadPrivateKey(new ByteArrayInputStream(output),
						null);
			}
			rs.close();
			s.close();
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault
					.setFaultString("Unexpected error obtaining the private key for the user "
							+ alias + ".");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
		if (key == null) {
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("No PrivateKey exists for the user " + alias
					+ ".");
			throw fault;
		}
		return key;
	}

	private X509Certificate getUserCertificate(String alias)
			throws CertificateAuthorityFault {
		this.buildDatabase();
		Connection c = null;
		X509Certificate cert = null;
		try {
			c = db.getConnection();
			PreparedStatement s = c.prepareStatement("select CERTIFICATE from "
					+ CREDENTIALS_TABLE + " where ALIAS= ?");
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
			fault
					.setFaultString("Unexpected Database Error, Error obtaining the certificate for the user "
							+ alias + ".");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
		if (cert == null) {
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("No Certificate exists for the user " + alias
					+ ".");
			throw fault;
		}
		return cert;
	}

	private long getUserCertificateSerialNumber(String alias)
			throws CertificateAuthorityFault {
		this.buildDatabase();
		Connection c = null;
		long sn = -1;
		try {
			c = db.getConnection();
			PreparedStatement s = c
					.prepareStatement("select SERIAL_NUMBER from "
							+ CREDENTIALS_TABLE + " where ALIAS= ?");
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
			fault.setFaultString("No Certificate exists for the user " + alias
					+ ".");
			throw fault;
		}
		return sn;
	}

	private void buildDatabase() throws CertificateAuthorityFault {
		if (!dbBuilt) {
			try {
				if (!this.db.tableExists(CREDENTIALS_TABLE)) {
					String users = "CREATE TABLE " + CREDENTIALS_TABLE + " ("
							+ "ALIAS VARCHAR(255) NOT NULL PRIMARY KEY,"
							+ " SERIAL_NUMBER BIGINT NOT NULL,"
							+ "CERTIFICATE TEXT NOT NULL,"
							+ "PRIVATE_KEY BLOB," + "IV BLOB,"
							+ "INDEX document_index (ALIAS));";
					db.update(users);
				}
				this.dbBuilt = true;
			} catch (Exception e) {
				logError(e.getMessage(), e);
				CertificateAuthorityFault fault = new CertificateAuthorityFault();
				fault
						.setFaultString("Unexpected error creating the CA database");
				FaultHelper helper = new FaultHelper(fault);
				helper.addFaultCause(e);
				fault = (CertificateAuthorityFault) helper.getFault();
				throw fault;
			}
		}
	}

	private X509Certificate convert(X509Certificate cert) throws Exception {
		String str = CertUtil.writeCertificate(cert);
		return CertUtil.loadCertificate(str);
	}
}