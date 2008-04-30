package gov.nih.nci.cagrid.dorian.service.ca;

import gov.nih.nci.cagrid.common.FaultHelper;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import org.cagrid.gaards.pki.SecurityUtil;
import org.cagrid.tools.database.Database;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class DBCertificateAuthority extends CertificateAuthority {

	public static final String SIGNATURE_ALGORITHM = "SHA1WithRSAEncryption";

	private CredentialsManager manager;

	public DBCertificateAuthority(Database db, CertificateAuthorityProperties properties) {
		super(properties);
		SecurityUtil.init();
		this.manager = new CredentialsManager(db);
	}

	public String getCACredentialsProvider() {
		return getProvider();
	}

	public String getUserCredentialsProvider() {
		return getProvider();
	}

	public String getProvider() {
		return "BC";
	}

	public String getSignatureAlgorithm() {
		return SIGNATURE_ALGORITHM;
	}

	public long getCertificateSerialNumber(String alias)
			throws CertificateAuthorityFault {
		try {
			return manager.getCertificateSerialNumber(alias);
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault
					.setFaultString("An unexpected error occurred, could not the serial number of the requested certificate.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}
	}

	public void addCredentials(String alias, String password,
			X509Certificate cert, PrivateKey key)
			throws CertificateAuthorityFault {
		try {

			if (manager.hasCredentials(alias)) {
				CertificateAuthorityFault fault = new CertificateAuthorityFault();
				fault.setFaultString("Credentials already exist for " + alias);
				throw fault;
			}
			manager.addCredentials(alias, password, cert, key);
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault
					.setFaultString("An unexpected error occurred, could not add credentials.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}

	}

	public void addCertificate(String alias, X509Certificate cert)
			throws CertificateAuthorityFault {
		try {

			if (manager.hasCredentials(alias)) {
				CertificateAuthorityFault fault = new CertificateAuthorityFault();
				fault.setFaultString("Credentials already exist for " + alias);
				throw fault;
			}
			manager.addCertificate(alias, cert);
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

	public void deleteCredentials(String alias)
			throws CertificateAuthorityFault {
		try {
			manager.deleteCredentials(alias);
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault
					.setFaultString("An unexpected error occurred, could not delete credentials.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}

	}

	public boolean hasCredentials(String alias)
			throws CertificateAuthorityFault {
		try {
			return manager.hasCredentials(alias);
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
	}

	public PrivateKey getPrivateKey(String alias, String password)
			throws CertificateAuthorityFault {

		try {
			if (!hasCredentials(alias)) {
				CertificateAuthorityFault fault = new CertificateAuthorityFault();
				fault
						.setFaultString("The requested private key does not exist.");
				throw fault;
			} else {
				return manager.getPrivateKey(alias, password);
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

	}

	public X509Certificate getCertificate(String alias)
			throws CertificateAuthorityFault {
		try {
			if (!hasCredentials(alias)) {
				CertificateAuthorityFault fault = new CertificateAuthorityFault();
				fault
						.setFaultString("The requested certificate does not exist.");
				throw fault;
			} else {
				return manager.getCertificate(alias);
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

	}

	///////////////////////////////////////////////////////////////////////////////////////////////

	public void clear() throws CertificateAuthorityFault {
		try {
			manager.clearDatabase();
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault
					.setFaultString("Unexpected Error, could not destroy Dorian Certificate Authority.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}
	}

}