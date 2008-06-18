package org.cagrid.gaards.dorian.ca;

import gov.nih.nci.cagrid.common.FaultHelper;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

import org.bouncycastle.asn1.x509.X509Name;
import org.cagrid.gaards.dorian.common.Lifetime;
import org.cagrid.gaards.dorian.common.LoggingObject;
import org.cagrid.gaards.dorian.federation.ProxyLifetime;
import org.cagrid.gaards.dorian.service.util.Utils;
import org.cagrid.gaards.pki.CRLEntry;
import org.cagrid.gaards.pki.CertUtil;
import org.cagrid.gaards.pki.KeyUtil;
import org.cagrid.gaards.pki.ProxyCreator;

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

	private CertificateAuthorityProperties properties;

	public CertificateAuthority(CertificateAuthorityProperties properties) {
		this.properties = properties;
	}

	public abstract String getUserCredentialsProvider();

	public abstract String getCACredentialsProvider();

	public abstract String getSignatureAlgorithm();

	public abstract void addCredentials(String alias, String password,
			X509Certificate cert, PrivateKey key)
			throws CertificateAuthorityFault;

	public abstract void deleteCredentials(String alias)
			throws CertificateAuthorityFault;

	public abstract void addCertificate(String alias, X509Certificate cert)
			throws CertificateAuthorityFault;

	public abstract boolean hasCredentials(String alias)
			throws CertificateAuthorityFault;

	public abstract PrivateKey getPrivateKey(String alias, String password)
			throws CertificateAuthorityFault;

	public abstract X509Certificate getCertificate(String alias)
			throws CertificateAuthorityFault;

	public abstract long getCertificateSerialNumber(String alias)
			throws CertificateAuthorityFault;

	protected abstract void clear() throws CertificateAuthorityFault;

	public void clearCertificateAuthority() throws CertificateAuthorityFault {
		clear();
		this.initialized = false;
	}

	private synchronized void init() throws CertificateAuthorityFault {
		try {
			if (!initialized) {
				if (!hasCredentials(CA_ALIAS)) {
					if (properties.isAutoCreateCAEnabled()) {
						Lifetime lifetime = properties.getCreationPolicy()
								.getLifetime();
						this.createCertifcateAuthorityCredentials(properties
								.getCreationPolicy().getSubject(), Utils
								.getExpiredDate(lifetime), properties
								.getCreationPolicy().getKeySize());
					}
				}
				initialized = true;
			}
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault
					.setFaultString("Unexpected Error, could not initialize the Dorian Certificate Authority.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}
	}

	private void createCertifcateAuthorityCredentials(String dn,
			Date expirationDate, int keySize) throws CertificateAuthorityFault,
			NoCACredentialsFault {
		try {
			KeyPair pair = KeyUtil.generateRSAKeyPair(
					getCACredentialsProvider(), keySize);
			X509Certificate cacert = CertUtil.generateCACertificate(
					getCACredentialsProvider(), new X509Name(dn), new Date(),
					expirationDate, pair, getSignatureAlgorithm());
			deleteCACredentials();
			this.setCACredentials(cacert, pair.getPrivate());
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault
					.setFaultString("Unexpected Error, could not create the CA credentials.");
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
			fault
					.setFaultString("Unexpected Error, could not remove the CA credentials from the database.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}
	}

	public void setCACredentials(X509Certificate cert, PrivateKey key)
			throws CertificateAuthorityFault {
		try {
			this.deleteCACredentials();
			addCredentials(CA_ALIAS, properties
					.getCertificateAuthorityPassword(), cert, key);
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault
					.setFaultString("Unexpected Error, could not add the CA credentials to the CA database.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}
	}

	public PrivateKey getCAPrivateKey() throws CertificateAuthorityFault,
			NoCACredentialsFault {
		init();
		try {
			if (!hasCredentials(CA_ALIAS)) {
				NoCACredentialsFault fault = new NoCACredentialsFault();
				fault.setFaultString("No Private Key exists for the CA.");
				throw fault;
			} else {
				return getPrivateKey(CA_ALIAS, properties
						.getCertificateAuthorityPassword());
			}
		} catch (NoCACredentialsFault f) {
			throw f;
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault
					.setFaultString("Unexpected Error, Error obtaining the CA private key.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}

	}

	public java.security.cert.X509Certificate getCACertificate()
			throws CertificateAuthorityFault, NoCACredentialsFault {
		return getCACertificate(true);
	}

	private java.security.cert.X509Certificate getCACertificate(
			boolean errorOnExpiredCredentials)
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
			fault
					.setFaultString("Unexpected Error, Error obtaining the CA private key.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}

		if (errorOnExpiredCredentials) {
			Date now = new Date();
			if (now.before(cert.getNotBefore())
					|| (now.after(cert.getNotAfter()))) {
				if (properties.isAutoRenewCAEnabled()) {
					Lifetime lifetime = properties.getRenewalLifetime();
					return renewCertifcateAuthorityCredentials(Utils
							.getExpiredDate(lifetime));

				} else {
					NoCACredentialsFault fault = new NoCACredentialsFault();
					fault
							.setFaultString("The CA certificate had expired or is not valid at this time.");
					throw fault;
				}
			}
		}
		return cert;
	}

	public synchronized void createCredentials(String alias, String subject,
			String password, Date start, Date expiration)
			throws CertificateAuthorityFault, NoCACredentialsFault {
		init();
		X509Certificate cacert = getCACertificate();

		Date caDate = cacert.getNotAfter();
		if (start.after(caDate)) {
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault
					.setFaultString("Certificate start date is after the CA certificates expiration date.");
			throw fault;
		}
		if (expiration.after(caDate)) {
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault
					.setFaultString("Certificate expiration date is after the CA certificates expiration date.");
			throw fault;
		}

		try {

			if (hasCredentials(alias)) {
				CertificateAuthorityFault fault = new CertificateAuthorityFault();
				fault.setFaultString("Credentials already exist for " + alias);
				throw fault;
			}

			// VALIDATE DN
			String caSubject = cacert.getSubjectDN().getName();
			int caindex = caSubject.lastIndexOf(",");
			String caPreSub = caSubject.substring(0, caindex);

			if (!subject.startsWith(caPreSub)) {
				CertificateAuthorityFault fault = new CertificateAuthorityFault();
				fault
						.setFaultString("Invalid certificate subject, the subject must start with, "
								+ caPreSub);
				throw fault;
			}
			KeyPair pair = KeyUtil.generateRSAKeyPair(
					getUserCredentialsProvider(), properties
							.getIssuedCertificateKeySize());
			X509Certificate cert = CertUtil.generateCertificate(
					getCACredentialsProvider(), new X509Name(subject), start,
					expiration, pair.getPublic(), cacert, getCAPrivateKey(),
					getSignatureAlgorithm(), properties.getPolicyOID());
			addCredentials(alias, password, cert, pair.getPrivate());
		} catch (CertificateAuthorityFault f) {
			throw f;
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault
					.setFaultString("Unexpected Error, could not create credentials.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}
	}

	public synchronized X509Certificate signCertificate(String alias,
			String subject, PublicKey publicKey, Date start, Date expiration)
			throws CertificateAuthorityFault, NoCACredentialsFault {
		init();
		X509Certificate cacert = getCACertificate();

		Date caDate = cacert.getNotAfter();
		if (start.after(caDate)) {
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault
					.setFaultString("Certificate start date is after the CA certificates expiration date.");
			throw fault;
		}
		if (expiration.after(caDate)) {
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault
					.setFaultString("Certificate expiration date is after the CA certificates expiration date.");
			throw fault;
		}

		try {

			if (hasCredentials(alias)) {
				CertificateAuthorityFault fault = new CertificateAuthorityFault();
				fault.setFaultString("Credentials already exist for " + alias);
				throw fault;
			}

			// VALIDATE DN
			String caSubject = cacert.getSubjectDN().getName();
			int caindex = caSubject.lastIndexOf(",");
			String caPreSub = caSubject.substring(0, caindex);

			if (!subject.startsWith(caPreSub)) {
				CertificateAuthorityFault fault = new CertificateAuthorityFault();
				fault
						.setFaultString("Invalid certificate subject, the subject must start with, "
								+ caPreSub);
				throw fault;
			}
			X509Certificate cert = CertUtil.generateCertificate(
					getCACredentialsProvider(), new X509Name(subject), start,
					expiration, publicKey, cacert, getCAPrivateKey(),
					getSignatureAlgorithm(), properties.getPolicyOID());
			addCertificate(alias, cert);
			return cert;
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault
					.setFaultString("Unexpected Error, could not sign certificate.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}
	}

	public synchronized X509Certificate signHostCertificate(String alias,
			String host, PublicKey publicKey, Date start, Date expiration)
			throws CertificateAuthorityFault, NoCACredentialsFault {
		init();
		X509Certificate cacert = getCACertificate();
		try {
			String subject = Utils.getHostCertificateSubject(cacert, host);
			return signCertificate(alias, subject, publicKey, start, expiration);
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault
					.setFaultString("Unexpected Error, could not sign host certificate.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}
	}

	public synchronized X509Certificate renewCertifcateAuthorityCredentials(
			Date expirationDate) throws CertificateAuthorityFault,
			NoCACredentialsFault {
		init();
		try {
			X509Certificate oldcert = getCACertificate(false);
			int size = ((RSAPublicKey) oldcert.getPublicKey()).getModulus()
					.bitLength();
			KeyPair pair = KeyUtil.generateRSAKeyPair(
					getCACredentialsProvider(), size);
			X509Certificate cacert = CertUtil.generateCACertificate(
					getCACredentialsProvider(), new X509Name(oldcert
							.getSubjectDN().getName()), new Date(),
					expirationDate, pair, getSignatureAlgorithm());
			deleteCACredentials();
			this.setCACredentials(cacert, pair.getPrivate());
			return cacert;
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault
					.setFaultString("Unexpected Error, could renew the CA credentials.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}
	}

	public X509CRL getCRL(CRLEntry[] entries) throws CertificateAuthorityFault,
			NoCACredentialsFault {
		try {
			init();
			return CertUtil.createCRL(getCACredentialsProvider(),
					getCACertificate(), getCAPrivateKey(), entries,
					getCACertificate().getNotAfter(), getSignatureAlgorithm());
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

	public X509Certificate[] createImpersonationProxyCertificate(String alias,
			String password, PublicKey proxyPublicKey, ProxyLifetime lifetime,
			int delegationPathLength) throws CertificateAuthorityFault {
		try {
			X509Certificate[] certs = new X509Certificate[] { getCertificate(alias) };
			PrivateKey key = getPrivateKey(alias, password);
			return ProxyCreator.createImpersonationProxyCertificate(
					getUserCredentialsProvider(), certs, key, proxyPublicKey,
					lifetime.getHours(), lifetime.getMinutes(), lifetime
							.getSeconds(), delegationPathLength,
					getSignatureAlgorithm());
		} catch (Exception e) {
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Unexpected Error, could not create proxy, "
					+ e.getMessage());
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}
	}

	public CertificateAuthorityProperties getProperties() {
		return properties;
	}
}
