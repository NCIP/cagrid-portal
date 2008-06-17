package org.cagrid.gaards.dorian.ca;

import gov.nih.nci.cagrid.common.FaultHelper;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Enumeration;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class EracomCertificateAuthority extends BaseEracomCertificateAuthority implements WrappingCertificateAuthority {

	public EracomCertificateAuthority(EracomCertificateAuthorityProperties properties) throws CertificateAuthorityFault {
		super(properties);
	}


	public String getUserCredentialsProvider() {
		return getProvider().getName();
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

	}


	public void addCertificate(String alias, X509Certificate cert) throws CertificateAuthorityFault {
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

	}


	public void deleteCredentials(String alias) throws CertificateAuthorityFault {
		try {
			getKeyStore().deleteEntry(alias);
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
			Enumeration<String> e = getKeyStore().aliases();
			while (e.hasMoreElements()) {
				getKeyStore().deleteEntry(e.nextElement());
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
	}


	public PrivateKey getPrivateKey(String alias, String password) throws CertificateAuthorityFault {

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

	}


	public X509Certificate getCertificate(String alias) throws CertificateAuthorityFault {
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

	}


}