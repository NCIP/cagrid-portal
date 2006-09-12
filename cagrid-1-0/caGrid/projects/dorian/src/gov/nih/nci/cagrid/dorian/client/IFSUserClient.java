package gov.nih.nci.cagrid.dorian.client;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.dorian.common.DorianFault;
import gov.nih.nci.cagrid.dorian.common.SAMLUtils;
import gov.nih.nci.cagrid.dorian.ifs.bean.ProxyLifetime;
import gov.nih.nci.cagrid.dorian.stubs.types.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.stubs.types.InvalidAssertionFault;
import gov.nih.nci.cagrid.dorian.stubs.types.InvalidProxyFault;
import gov.nih.nci.cagrid.dorian.stubs.types.PermissionDeniedFault;
import gov.nih.nci.cagrid.dorian.stubs.types.UserPolicyFault;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.rmi.RemoteException;
import java.security.KeyPair;
import java.security.cert.X509Certificate;

import org.apache.axis.types.URI.MalformedURIException;
import org.globus.gsi.GlobusCredential;


public class IFSUserClient{

	private DorianClient client;


	public IFSUserClient(String serviceURI) throws MalformedURIException, RemoteException {
		client = new DorianClient(serviceURI);
	}


	public GlobusCredential createProxy(SAMLAssertion saml, ProxyLifetime lifetime) throws DorianFault,
		DorianInternalFault, InvalidAssertionFault, InvalidProxyFault, UserPolicyFault, PermissionDeniedFault {

		try {
			KeyPair pair = KeyUtil.generateRSAKeyPair512();

			gov.nih.nci.cagrid.dorian.ifs.bean.PublicKey key = new gov.nih.nci.cagrid.dorian.ifs.bean.PublicKey(KeyUtil
				.writePublicKey(pair.getPublic()));
			gov.nih.nci.cagrid.dorian.bean.SAMLAssertion s = new gov.nih.nci.cagrid.dorian.bean.SAMLAssertion(SAMLUtils
				.samlAssertionToString(saml));
			gov.nih.nci.cagrid.dorian.bean.X509Certificate list[] = client.createProxy(s, key, lifetime);
			X509Certificate[] certs = new X509Certificate[list.length];
			for (int i = 0; i < list.length; i++) {
				certs[i] = CertUtil.loadCertificate(list[i].getCertificateAsString());
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

}
