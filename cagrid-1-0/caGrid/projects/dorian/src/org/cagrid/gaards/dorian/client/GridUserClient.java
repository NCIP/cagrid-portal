package org.cagrid.gaards.dorian.client;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.rmi.RemoteException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.List;

import org.apache.axis.types.URI.MalformedURIException;
import org.cagrid.gaards.dorian.common.DorianFault;
import org.cagrid.gaards.dorian.federation.DelegationPathLength;
import org.cagrid.gaards.dorian.federation.HostCertificateRecord;
import org.cagrid.gaards.dorian.federation.HostCertificateRequest;
import org.cagrid.gaards.dorian.federation.ProxyLifetime;
import org.cagrid.gaards.dorian.stubs.types.DorianInternalFault;
import org.cagrid.gaards.dorian.stubs.types.InvalidAssertionFault;
import org.cagrid.gaards.dorian.stubs.types.InvalidHostCertificateFault;
import org.cagrid.gaards.dorian.stubs.types.InvalidHostCertificateRequestFault;
import org.cagrid.gaards.dorian.stubs.types.InvalidProxyFault;
import org.cagrid.gaards.dorian.stubs.types.PermissionDeniedFault;
import org.cagrid.gaards.dorian.stubs.types.UserPolicyFault;
import org.cagrid.gaards.pki.CertUtil;
import org.cagrid.gaards.pki.KeyUtil;
import org.cagrid.gaards.saml.encoding.SAMLUtils;
import org.globus.gsi.GlobusCredential;

public class GridUserClient {

	private DorianClient client;

	public GridUserClient(String serviceURI) throws MalformedURIException,
			RemoteException {
		client = new DorianClient(serviceURI);
	}

	public GridUserClient(String serviceURI, GlobusCredential cred)
			throws MalformedURIException, RemoteException {
		client = new DorianClient(serviceURI, cred);
	}

	/**
	 * Allow a user to request a short term Grid credential from Dorian, which
	 * they may user to authenticate to Grid service.
	 * 
	 * @param saml
	 *            A signed SAML assertion from an identity provider trusted by
	 *            Dorian.
	 * @param lifetime
	 *            The lifetime of the Grid credential.
	 * @param delegationPathLength
	 *            The delegation path length of the Grid credential.
	 * @return The short term Grid credential.
	 * @throws DorianFault
	 * @throws DorianInternalFault
	 * @throws InvalidAssertionFault
	 * @throws InvalidProxyFault
	 * @throws UserPolicyFault
	 * @throws PermissionDeniedFault
	 */
	public GlobusCredential createProxy(SAMLAssertion saml,
			ProxyLifetime lifetime, int delegationPathLength)
			throws DorianFault, DorianInternalFault, InvalidAssertionFault,
			InvalidProxyFault, UserPolicyFault, PermissionDeniedFault {

		try {
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();

			org.cagrid.gaards.dorian.federation.PublicKey key = new org.cagrid.gaards.dorian.federation.PublicKey(
					KeyUtil.writePublicKey(pair.getPublic()));
			org.cagrid.gaards.dorian.SAMLAssertion s = new org.cagrid.gaards.dorian.SAMLAssertion(
					SAMLUtils.samlAssertionToString(saml));
			org.cagrid.gaards.dorian.X509Certificate list[] = client
					.createProxy(s, key, lifetime, new DelegationPathLength(
							delegationPathLength));
			X509Certificate[] certs = new X509Certificate[list.length];
			for (int i = 0; i < list.length; i++) {
				certs[i] = CertUtil.loadCertificate(list[i]
						.getCertificateAsString());
			}
			return new GlobusCredential(pair.getPrivate(), certs);
		} catch (DorianInternalFault gie) {
			throw gie;
		} catch (InvalidAssertionFault f) {
			throw f;
		} catch (InvalidProxyFault f) {
			throw f;
		} catch (UserPolicyFault f) {
			throw f;
		} catch (PermissionDeniedFault f) {
			throw f;
		} catch (Exception e) {
			FaultUtil.printFault(e);
			DorianFault fault = new DorianFault();
			fault.setFaultString(Utils.getExceptionMessage(e));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianFault) helper.getFault();
			throw fault;
		}
	}

	/**
	 * This method allow a user to request a host certificate.
	 * 
	 * @param hostname
	 *            The host name of the host.
	 * @param publicKey
	 *            The public key to use for the host ceriticate.
	 * @return The host certificate record for the host certificate, if the host
	 *         certificate was immediately approved, the signed host certificate
	 *         will be contained in the record. Otherwise you will have to wait
	 *         for the host certificate to be approved, once approved the
	 *         getOwnedHostCertificates method can be used to obtain the signed
	 *         host certificate.
	 * @throws DorianFault
	 * @throws DorianInternalFault
	 * @throws InvalidHostCertificateRequestFault
	 * @throws InvalidHostCertificateFault
	 * @throws PermissionDeniedFault
	 */
	public HostCertificateRecord requestHostCertificate(String hostname,
			PublicKey publicKey) throws DorianFault, DorianInternalFault,
			InvalidHostCertificateRequestFault, InvalidHostCertificateFault,
			PermissionDeniedFault {
		try {
			HostCertificateRequest req = new HostCertificateRequest();
			req.setHostname(hostname);
			org.cagrid.gaards.dorian.federation.PublicKey key = new org.cagrid.gaards.dorian.federation.PublicKey();
			key.setKeyAsString(KeyUtil.writePublicKey(publicKey));
			req.setPublicKey(key);
			return client.requestHostCertificate(req);
		} catch (DorianInternalFault gie) {
			throw gie;
		} catch (InvalidHostCertificateRequestFault f) {
			throw f;
		} catch (InvalidHostCertificateFault f) {
			throw f;
		} catch (PermissionDeniedFault f) {
			throw f;
		} catch (Exception e) {
			FaultUtil.printFault(e);
			DorianFault fault = new DorianFault();
			fault.setFaultString(Utils.getExceptionMessage(e));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianFault) helper.getFault();
			throw fault;
		}
	}

	/**
	 * This method returns a list for all the host certificates owned by the
	 * user.
	 * 
	 * @return The list of host certificates owned by the user.
	 * @throws DorianFault
	 * @throws DorianInternalFault
	 * @throws PermissionDeniedFault
	 */
	public List<HostCertificateRecord> getOwnedHostCertificates()
			throws DorianFault, DorianInternalFault, PermissionDeniedFault {
		try {
			List<HostCertificateRecord> list = Utils.asList(client
					.getOwnedHostCertificates());
			return list;
		} catch (DorianInternalFault gie) {
			throw gie;
		} catch (PermissionDeniedFault f) {
			throw f;
		} catch (Exception e) {
			FaultUtil.printFault(e);
			DorianFault fault = new DorianFault();
			fault.setFaultString(Utils.getExceptionMessage(e));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianFault) helper.getFault();
			throw fault;
		}

	}
	
	/**
	 * This method obtains Dorian's CA certificate.
	 * @return This method obtains Dorian's CA certificate.
	 * @throws DorianFault
	 * @throws DorianInternalFault
	 */

	public X509Certificate getCACertificate() throws DorianFault, DorianInternalFault {
		try {
			return CertUtil.loadCertificate(client.getCACertificate().getCertificateAsString());
		} catch (DorianInternalFault gie) {
			throw gie;
		} catch (Exception e) {
			FaultUtil.printFault(e);
			DorianFault fault = new DorianFault();
			fault.setFaultString(Utils.getExceptionMessage(e));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianFault) helper.getFault();
			throw fault;
		}
	}
}