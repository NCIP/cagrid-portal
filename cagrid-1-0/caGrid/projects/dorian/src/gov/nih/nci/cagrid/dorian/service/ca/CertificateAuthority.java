package gov.nih.nci.cagrid.dorian.service.ca;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.dorian.common.LoggingObject;
import gov.nih.nci.cagrid.dorian.common.Utils;
import gov.nih.nci.cagrid.dorian.conf.CredentialLifetime;
import gov.nih.nci.cagrid.dorian.conf.DorianCAConfiguration;
import gov.nih.nci.cagrid.gridca.common.CRLEntry;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
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
public abstract class CertificateAuthority extends LoggingObject {

	public static final String CA_ALIAS = "dorianca";

	private boolean initialized = false;

	private DorianCAConfiguration conf;


	public CertificateAuthority(DorianCAConfiguration conf) {
		this.conf = conf;
	}


	public abstract String getProvider();


	public abstract String getSignatureAlgorithm();


	protected abstract void addCredentials(String alias, String password, X509Certificate cert, PrivateKey key)
		throws CertificateAuthorityFault;


	protected abstract void deleteCredentials(String alias) throws CertificateAuthorityFault;


	public abstract boolean hasCredentials(String alias) throws CertificateAuthorityFault;


	public abstract PrivateKey getPrivateKey(String alias, String password) throws CertificateAuthorityFault;


	public abstract X509Certificate getCertificate(String alias) throws CertificateAuthorityFault;


	protected abstract void clear() throws CertificateAuthorityFault;


	public void clearCertificateAuthority() throws CertificateAuthorityFault {
		clear();
		this.initialized = false;
	}


	private void init() throws CertificateAuthorityFault {
		try {
			if (!initialized) {
				if (!hasCredentials(CA_ALIAS)) {
					if (conf.getAutoCreate() != null) {
						CredentialLifetime lifetime = conf.getAutoCreate().getLifetime();
						this.createCertifcateAuthorityCredentials(conf.getAutoCreate().getCASubject(), Utils
							.getExpiredDate(lifetime));
					}
				}
				initialized = true;
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


	private void createCertifcateAuthorityCredentials(String dn, Date expirationDate) throws CertificateAuthorityFault,
		NoCACredentialsFault {
		try {
			KeyPair pair = KeyUtil.generateRSAKeyPair2048(getProvider());
			X509Certificate cacert = CertUtil.generateCACertificate(getProvider(), new X509Name(dn), new Date(),
				expirationDate, pair, getSignatureAlgorithm());
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


	private void deleteCACredentials() throws CertificateAuthorityFault {
		try {
			deleteCredentials(CA_ALIAS);
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


	public void setCACredentials(X509Certificate cert, PrivateKey key) throws CertificateAuthorityFault {
		try {
			this.deleteCACredentials();
			addCredentials(CA_ALIAS, conf.getCertificateAuthorityPassword(), cert, key);
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


	public PrivateKey getCAPrivateKey() throws CertificateAuthorityFault, NoCACredentialsFault {
		init();
		try {
			if (!hasCredentials(CA_ALIAS)) {
				NoCACredentialsFault fault = new NoCACredentialsFault();
				fault.setFaultString("No Private Key exists for the CA.");
				throw fault;
			} else {
				return getPrivateKey(CA_ALIAS, conf.getCertificateAuthorityPassword());
			}
		} catch (NoCACredentialsFault f) {
			throw f;
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Unexpected Error, Error obtaining the CA private key.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}

	}


	public java.security.cert.X509Certificate getCACertificate() throws CertificateAuthorityFault, NoCACredentialsFault {
		return getCACertificate(true);
	}


	private java.security.cert.X509Certificate getCACertificate(boolean errorOnExpiredCredentials)
		throws CertificateAuthorityFault, NoCACredentialsFault {
		X509Certificate cert = null;
		init();
		try {
			if (!hasCredentials(CA_ALIAS)) {
				NoCACredentialsFault fault = new NoCACredentialsFault();
				fault.setFaultString("No certificate exists for the CA.");
				throw fault;
			} else {
				cert = getCertificate(CA_ALIAS);
			}
		} catch (NoCACredentialsFault f) {
			throw f;
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Unexpected Error, Error obtaining the CA private key.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}

		if (errorOnExpiredCredentials) {
			Date now = new Date();
			if (now.before(cert.getNotBefore()) || (now.after(cert.getNotAfter()))) {
				if (conf.getAutoRenewal() != null) {
					CredentialLifetime lifetime = conf.getAutoRenewal();
					return renewCertifcateAuthorityCredentials(Utils.getExpiredDate(lifetime));

				} else {
					NoCACredentialsFault fault = new NoCACredentialsFault();
					fault.setFaultString("The CA certificate had expired or is not valid at this time.");
					throw fault;
				}
			}
		}
		return cert;
	}


	public void createCredentials(String alias, String cn, String password, Date start, Date expiration)
		throws CertificateAuthorityFault, NoCACredentialsFault {
		init();
		X509Certificate cacert = getCACertificate();

		Date caDate = cacert.getNotAfter();
		if (start.after(caDate)) {
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Certificate start date is after the CA certificates expiration date.");
			throw fault;
		}
		if (expiration.after(caDate)) {
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Certificate expiration date is after the CA certificates expiration date.");
			throw fault;
		}

		try {

			if (hasCredentials(alias)) {
				CertificateAuthorityFault fault = new CertificateAuthorityFault();
				fault.setFaultString("Credentials already exist for " + alias);
				throw fault;
			}

			String caSubject = cacert.getSubjectDN().getName();
			int caindex = caSubject.lastIndexOf(",");
			String caPreSub = caSubject.substring(0, caindex);

			String subject = caPreSub + ",CN=" + cn;
			KeyPair pair = KeyUtil.generateRSAKeyPair1024(getProvider());
			X509Certificate cert = CertUtil.generateCertificate(getProvider(), new X509Name(subject), start,
				expiration, pair.getPublic(), cacert, getCAPrivateKey(), getSignatureAlgorithm());
			addCredentials(alias, password, cert, pair.getPrivate());
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Unexpected Error, could not create credentials.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}
	}


	public X509Certificate renewCertifcateAuthorityCredentials(Date expirationDate) throws CertificateAuthorityFault,
		NoCACredentialsFault {
		init();
		try {
			X509Certificate oldcert = getCACertificate(false);
			KeyPair pair = KeyUtil.generateRSAKeyPair2048(getProvider());
			X509Certificate cacert = CertUtil.generateCACertificate(getProvider(), new X509Name(oldcert.getSubjectDN()
				.getName()), new Date(), expirationDate, pair, getSignatureAlgorithm());
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


	public X509Certificate requestCertificate(PKCS10CertificationRequest request, Date startDate, Date expirationDate)
		throws CertificateAuthorityFault, NoCACredentialsFault {
		init();
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
			return CertUtil.signCertificateRequest(getProvider(), request, startDate, expirationDate, cacert, cakey,
				getSignatureAlgorithm());
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


	public X509CRL getCRL(CRLEntry[] entries) throws CertificateAuthorityFault, NoCACredentialsFault {
		try {
			init();
			return CertUtil.createCRL(getProvider(), getCACertificate(), getCAPrivateKey(), entries, getCACertificate()
				.getNotAfter(), getSignatureAlgorithm());
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
