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
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PKCS10CertificationRequest;

import au.com.eracom.crypto.provider.ERACOMProvider;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class SafeNetCertificateAuthority extends LoggingObject implements
		CertificateAuthority {

	private static final String CA_ALIAS = "dorianca";
	public static final String SIGNATURE_ALGORITHM = "MD5WithRSA";
	private DorianCAConfiguration conf;
	private Provider provider;
	private KeyStore keyStore;

	public SafeNetCertificateAuthority(DorianCAConfiguration conf)
			throws CertificateAuthorityFault {
		try {
			this.conf = conf;
			provider = new ERACOMProvider();
			Security.addProvider(provider);
			keyStore = KeyStore.getInstance("CRYPTOKI", provider.getName());
			// TODO: Determine which password this is.
			keyStore.load(null, conf.getCertificateAuthorityPassword()
					.toCharArray());
			if (!keyStore.containsAlias(CA_ALIAS)) {
				if (conf.getAutoCreate() != null) {
					CredentialLifetime lifetime = conf.getAutoCreate()
							.getLifetime();
					this.createCertifcateAuthorityCredentials(conf
							.getAutoCreate().getCASubject(), Utils
							.getExpiredDate(lifetime));
				}
			}
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

	private void deleteCACredentials() throws CertificateAuthorityFault {
		try {
			if (keyStore.containsAlias(CA_ALIAS)) {
				keyStore.deleteEntry(CA_ALIAS);
			}
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault
					.setFaultString("Unexpected Error, could not remove the CA credentials from the database.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		} finally {
		}
	}

	private void createCertifcateAuthorityCredentials(String dn,
			Date expirationDate) throws CertificateAuthorityFault,
			NoCACredentialsFault {
		try {
			KeyPair pair = KeyUtil.generateRSAKeyPair2048(provider.getName());
			X509Certificate cacert = CertUtil.generateCACertificate(provider
					.getName(), new X509Name(dn), new Date(), expirationDate,
					pair,SIGNATURE_ALGORITHM);
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

	public void setCACredentials(X509Certificate cert, PrivateKey key)
			throws CertificateAuthorityFault {
		try {
			this.deleteCACredentials();
			keyStore.setKeyEntry(CA_ALIAS, key, null,
					new X509Certificate[] { cert });
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

	public X509Certificate renewCertifcateAuthorityCredentials(
			Date expirationDate) throws CertificateAuthorityFault,
			NoCACredentialsFault {
		try {
			X509Certificate oldcert = getCACertificate(false);
			KeyPair pair = KeyUtil.generateRSAKeyPair2048(provider.getName());
			X509Certificate cacert = CertUtil.generateCACertificate(provider
					.getName(), new X509Name(oldcert.getSubjectDN().getName()),
					new Date(), expirationDate, pair,SIGNATURE_ALGORITHM);
			deleteCACredentials();
			this.setCACredentials(cacert, pair.getPrivate());
			return convert(cacert);
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

	public PrivateKey getCAPrivateKey() throws CertificateAuthorityFault,
			NoCACredentialsFault {
		try {
			if (!keyStore.containsAlias(CA_ALIAS)) {
				NoCACredentialsFault fault = new NoCACredentialsFault();
				fault.setFaultString("No Private Key exists for the CA.");
				throw fault;
			} else {
				return (PrivateKey) keyStore.getKey(CA_ALIAS, null);
			}
		} catch (NoCACredentialsFault f) {
			throw f;
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Error obtaining the CA private key.");
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

		try {
			if (!keyStore.containsAlias(CA_ALIAS)) {
				NoCACredentialsFault fault = new NoCACredentialsFault();
				fault.setFaultString("No Private Key exists for the CA.");
				throw fault;
			} else {
				cert = convert((X509Certificate) keyStore
						.getCertificate(CA_ALIAS));
			}
		} catch (NoCACredentialsFault f) {
			throw f;
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Error obtaining the CA certificate.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}
		if (errorOnExpiredCredentials) {
			Date now = new Date();
			if (now.before(cert.getNotBefore())
					|| (now.after(cert.getNotAfter()))) {
				if (conf.getAutoRenewal() != null) {
					CredentialLifetime lifetime = conf.getAutoRenewal();
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

	public void clearDatabase() throws CertificateAuthorityFault {
		try {
			keyStore.deleteEntry(CA_ALIAS);
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

	public X509Certificate requestCertificate(
			PKCS10CertificationRequest request, Date startDate,
			Date expirationDate) throws CertificateAuthorityFault,
			NoCACredentialsFault {

		PrivateKey cakey = getCAPrivateKey();
		X509Certificate cacert = getCACertificate();
		Date caDate = cacert.getNotAfter();
		if (startDate.after(caDate)) {
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault
					.setFaultString("Certificate start date is after the CA certificates expiration date.");
			throw fault;
		}
		if (expirationDate.after(caDate)) {
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault
					.setFaultString("Certificate expiration date is after the CA certificates expiration date.");
			throw fault;
		}

		// VALIDATE DN
		String caSubject = cacert.getSubjectDN().getName();
		int caindex = caSubject.lastIndexOf(",");
		String caPreSub = caSubject.substring(0, caindex);

		String userSubject = request.getCertificationRequestInfo().getSubject()
				.toString();

		if (!userSubject.startsWith(caPreSub)) {
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault
					.setFaultString("Invalid certificate subject, the subject must start with, "
							+ caPreSub);
			throw fault;
		}
		try {
			return convert(CertUtil.signCertificateRequest(provider.getName(),
					request, startDate, expirationDate, cacert, cakey,SIGNATURE_ALGORITHM));
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault
					.setFaultString("Unexpected Error, could not sign certifcate request.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}
	}

	public X509CRL getCRL(CRLEntry[] entries) throws CertificateAuthorityFault,
			NoCACredentialsFault {
		try {
			return CertUtil.createCRL(provider.getName(), getCACertificate(),
					getCAPrivateKey(), entries, getCACertificate()
							.getNotAfter(),SIGNATURE_ALGORITHM);
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

	private X509Certificate convert(X509Certificate cert) throws Exception {
		String str = CertUtil.writeCertificate(cert);
		return CertUtil.loadCertificate(str);
	}

}