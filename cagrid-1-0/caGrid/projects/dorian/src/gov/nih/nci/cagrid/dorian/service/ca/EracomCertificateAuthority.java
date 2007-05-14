package gov.nih.nci.cagrid.dorian.service.ca;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.dorian.conf.DorianCAConfiguration;
import gov.nih.nci.cagrid.gridca.common.CertUtil;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import au.com.eracom.crypto.provider.ERACOMProvider;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class EracomCertificateAuthority extends CertificateAuthority {

	public static final String SIGNATURE_ALGORITHM = "MD5WithRSA";
	private Provider provider;
	private KeyStore keyStore;


	public EracomCertificateAuthority(DorianCAConfiguration conf) throws CertificateAuthorityFault {
		super(conf);
		try {

			provider = new ERACOMProvider();
			Security.addProvider(provider);
			keyStore = KeyStore.getInstance("CRYPTOKI", provider.getName());
			// TODO: Determine which password this is.
			keyStore.load(null, conf.getCertificateAuthorityPassword().toCharArray());
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Error initializing the Dorian Certificate Authority.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}

	}


	public long getCertificateSerialNumber(String alias) throws CertificateAuthorityFault {
		try {
			X509Certificate cert = getCertificate(alias);
			return cert.getSerialNumber().longValue();
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


	public void addCredentials(String alias, String password, X509Certificate cert, PrivateKey key)
		throws CertificateAuthorityFault {
		try {
			keyStore.setKeyEntry(alias, key, null, new X509Certificate[]{cert});
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Unexpected Error, could not add credentials.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}

	}


	public void addCertificate(String alias, X509Certificate cert) throws CertificateAuthorityFault {
		try {

			keyStore.setCertificateEntry(alias, cert);
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("An unexpected error occurred, could not add certificate.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}

	}


	public void deleteCredentials(String alias) throws CertificateAuthorityFault {
		try {
			keyStore.deleteEntry(alias);
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("Unexpected Error, could not add credentials.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}

	}


	protected void clear() throws CertificateAuthorityFault {
		try {
			Enumeration<String> e = keyStore.aliases();
			while (e.hasMoreElements()) {
				keyStore.deleteEntry(e.nextElement());
			}
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


	public boolean hasCredentials(String alias) throws CertificateAuthorityFault {
		try {
			return keyStore.containsAlias(alias);
		} catch (Exception e) {
			logError(e.getMessage(), e);
			CertificateAuthorityFault fault = new CertificateAuthorityFault();
			fault.setFaultString("An unexpected error occurred, could determin if credentials exist.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (CertificateAuthorityFault) helper.getFault();
			throw fault;
		}
	}


	public String getProvider() {
		return provider.getName();
	}


	public String getSignatureAlgorithm() {
		return SIGNATURE_ALGORITHM;
	}


	public PrivateKey getPrivateKey(String alias, String password) throws CertificateAuthorityFault {

		try {
			if (!hasCredentials(alias)) {
				CertificateAuthorityFault fault = new CertificateAuthorityFault();
				fault.setFaultString("The requested private key does not exist.");
				throw fault;
			} else {
				return (PrivateKey) keyStore.getKey(alias, null);
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

	}


	public X509Certificate getCertificate(String alias) throws CertificateAuthorityFault {
		try {
			if (!hasCredentials(alias)) {
				CertificateAuthorityFault fault = new CertificateAuthorityFault();
				fault.setFaultString("The requested certificate does not exist.");
				throw fault;
			} else {
				return convert((X509Certificate) keyStore.getCertificate(alias));
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

	}


	private X509Certificate convert(X509Certificate cert) throws Exception {
		String str = CertUtil.writeCertificate(cert);
		return CertUtil.loadCertificate(str);
	}

}