package gov.nih.nci.cagrid.dorian.ca;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.dorian.common.Database;
import gov.nih.nci.cagrid.dorian.common.LoggingObject;
import gov.nih.nci.cagrid.gridca.common.CRLEntry;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PKCS10CertificationRequest;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class DorianCertificateAuthority extends LoggingObject implements CertificateAuthority {

	public static String CA_TABLE = "certificate_authority";

	private Database db;

	private final static String CA_USER = "CA";

	private boolean dbBuilt = false;

	private DorianCertificateAuthorityConf conf;


	public DorianCertificateAuthority(Database db, DorianCertificateAuthorityConf conf) {
		this.db = db;
		this.conf = conf;
	}


	public void setCACredentials(X509Certificate cert, PrivateKey key) throws CertificateAuthorityFault {
		this.buildDatabase();
		try {
			this.deleteCACredentials();
			String keyStr = KeyUtil.writePrivateKey(key, conf.getCaPassword());
			String certStr = CertUtil.writeCertificate(cert);
			db.update("INSERT INTO " + CA_TABLE + " VALUES('" + CA_USER + "','" + certStr + "','" + keyStr + "')");
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Unexpected Error, could not add the CA credentials to the CA database.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}
	}


	private void buildDatabase() throws CertificateAuthorityFault {
		try {
			if (!dbBuilt) {
				db.createDatabaseIfNeeded();
				if (!this.db.tableExists(CA_TABLE)) {
					String users = "CREATE TABLE " + CA_TABLE + " (" + "ID VARCHAR(255) NOT NULL PRIMARY KEY,"
						+ "CERTIFICATE TEXT NOT NULL," + "PRIVATE_KEY TEXT NOT NULL," + "INDEX document_index (ID));";
					db.update(users);
					this.dbBuilt = true;
					if (conf.isAutoCreate()) {
						this
							.createCertifcateAuthorityCredentials(conf.getAutoCreateSubject(), conf.getAutoCreateDate());
					}
				}
				
			}
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Unexpected Error, could not initialize the Dorian Certificate Authority.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}
	}


	public void clearDatabase() throws CertificateAuthorityFault {
		try {
			buildDatabase();
			db.update("delete from " + CA_TABLE);
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Unexpected Error, could not destroy Dorian Certificate Authority.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}
	}


	public PrivateKey getCAPrivateKey() throws CertificateAuthorityFault, NoCACredentialsFault {
		this.buildDatabase();
		Connection c = null;
		PrivateKey key = null;
		String keyStr = null;
		try {
			c = db.getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select PRIVATE_KEY from " + CA_TABLE + " where ID='" + CA_USER + "'");
			if (rs.next()) {
				keyStr = rs.getString("PRIVATE_KEY");
			}
			rs.close();
			s.close();
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Unexpected Database Error, Error obtaining the CA private key.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
		if (keyStr == null) {
			NoCACredentialsFault fault = new NoCACredentialsFault();
			fault.setFaultString("No Private Key exists for the CA.");
			throw fault;
		}
		try {
			key = KeyUtil.loadPrivateKey(new ByteArrayInputStream(keyStr.getBytes()), conf.getCaPassword());
		} catch (javax.crypto.BadPaddingException e) {
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Invalid Password Specified for CA private key.");
			throw fault;
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Unexpected Error, Error loading the private key for the CA.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}
		return key;
	}


	public java.security.cert.X509Certificate getCACertificate() throws CertificateAuthorityFault, NoCACredentialsFault {
		return getCACertificate(true);
	}


	private java.security.cert.X509Certificate getCACertificate(boolean errorOnExpiredCredentials)
		throws CertificateAuthorityFault, NoCACredentialsFault {
		this.buildDatabase();
		Connection c = null;
		X509Certificate cert = null;
		String certStr = null;
		try {
			c = db.getConnection();
			Statement s = c.createStatement();
			ResultSet rs = s.executeQuery("select CERTIFICATE from " + CA_TABLE + " where ID='" + CA_USER + "'");
			if (rs.next()) {
				certStr = rs.getString("CERTIFICATE");

			}
			rs.close();
			s.close();
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Unexpected Database Error, Could not obtain the CA certificate.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		} finally {
			db.releaseConnection(c);
		}
		if (certStr == null) {
			NoCACredentialsFault fault = new NoCACredentialsFault();
			fault.setFaultString("No certificate exists for the CA.");
			throw fault;
		}
		try {
			StringReader reader = new StringReader(certStr);
			cert = CertUtil.loadCertificate(reader);

		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Unexpected Error, Error loading the certificate for the CA.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}
		if (errorOnExpiredCredentials) {
			Date now = new Date();
			if (now.before(cert.getNotBefore()) || (now.after(cert.getNotAfter()))) {
				if (conf.isAutoRenewal()) {
					return renewCertifcateAuthorityCredentials(conf.getRenewalDate());

				} else {
					NoCACredentialsFault fault = new NoCACredentialsFault();
					fault.setFaultString("The CA certificate had expired or is not valid at this time.");
					throw fault;
				}
			}
		}
		return cert;
	}


	public X509Certificate renewCertifcateAuthorityCredentials(Date expirationDate) throws CertificateAuthorityFault,
		NoCACredentialsFault {
		try {
			X509Certificate oldcert = getCACertificate(false);
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			X509Certificate cacert = CertUtil.generateCACertificate(new X509Name(oldcert.getSubjectDN().getName()),
				new Date(), expirationDate, pair);
			deleteCACredentials();
			this.setCACredentials(cacert, pair.getPrivate());
			return cacert;
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Unexpected Error, could renew the CA credentials.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}
	}


	private void createCertifcateAuthorityCredentials(String dn, Date expirationDate) throws CertificateAuthorityFault,
		NoCACredentialsFault {
		try {
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			X509Certificate cacert = CertUtil.generateCACertificate(new X509Name(dn), new Date(), expirationDate, pair);
			deleteCACredentials();
			this.setCACredentials(cacert, pair.getPrivate());
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Unexpected Error, could not create the CA credentials.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}
	}


	public X509Certificate requestCertificate(PKCS10CertificationRequest request, Date startDate, Date expirationDate)
		throws CertificateAuthorityFault, NoCACredentialsFault {

		PrivateKey cakey = getCAPrivateKey();
		X509Certificate cacert = getCACertificate();

		Date caDate = cacert.getNotAfter();
		if (startDate.after(caDate)) {
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Certificate start date is after the CA certificates expiration date.");
			throw fault;
		}
		if (expirationDate.after(caDate)) {
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Certificate expiration date is after the CA certificates expiration date.");
			throw fault;
		}

		// VALIDATE DN
		String caSubject = cacert.getSubjectDN().getName();
		int caindex = caSubject.lastIndexOf(",");
		String caPreSub = caSubject.substring(0, caindex);

		String userSubject = request.getCertificationRequestInfo().getSubject().toString();

		if (!userSubject.startsWith(caPreSub)) {
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Invalid certificate subject, the subject must start with, " + caPreSub);
			throw fault;
		}
		try {
			return CertUtil.signCertificateRequest(request, startDate, expirationDate, cacert, cakey);
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Unexpected Error, could not sign certifcate request.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}
	}


	private void deleteCACredentials() throws CertificateAuthorityFault {
		this.buildDatabase();
		try {
			db.update("delete from " + CA_TABLE + " where ID='" + CA_USER + "'");
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Unexpected Error, could not remove the CA credentials from the database.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}
	}


	public X509CRL getCRL(CRLEntry[] entries) throws CertificateAuthorityFault, NoCACredentialsFault {
		try {
			return CertUtil.createCRL(getCACertificate(), getCAPrivateKey(), entries, getCACertificate().getNotAfter());
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Unexpected Error, could not create the CRL.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}
	}

}