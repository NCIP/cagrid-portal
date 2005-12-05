package gov.nih.nci.cagrid.gums.client;

import gov.nih.nci.cagrid.gums.IFSUserAccess;
import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.bean.PermissionDeniedFault;
import gov.nih.nci.cagrid.gums.common.FaultHelper;
import gov.nih.nci.cagrid.gums.common.FaultUtil;
import gov.nih.nci.cagrid.gums.common.GUMSFault;
import gov.nih.nci.cagrid.gums.common.IOUtils;
import gov.nih.nci.cagrid.gums.common.ca.CertUtil;
import gov.nih.nci.cagrid.gums.common.ca.KeyUtil;
import gov.nih.nci.cagrid.gums.idp.bean.BasicAuthCredential;
import gov.nih.nci.cagrid.gums.ifs.bean.InvalidAssertionFault;
import gov.nih.nci.cagrid.gums.ifs.bean.InvalidProxyFault;
import gov.nih.nci.cagrid.gums.ifs.bean.ProxyLifetime;
import gov.nih.nci.cagrid.gums.ifs.bean.UserPolicyFault;
import gov.nih.nci.cagrid.gums.wsrf.GUMSPortType;
import gov.nih.nci.cagrid.gums.wsrf.IFSCreateProxy;
import gov.nih.nci.cagrid.security.commstyle.AnonymousSecureConversationWithEncryption;

import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.cert.X509Certificate;

import org.globus.gsi.GlobusCredential;
import org.globus.util.ConfigUtil;
import org.opensaml.SAMLAssertion;

public class IFSUserClient extends GUMSBaseClient implements IFSUserAccess {

	public IFSUserClient(String serviceURI) {
		super(serviceURI);
	}

	public GlobusCredential createProxy(SAMLAssertion saml,
			ProxyLifetime lifetime) throws GUMSFault,
			GUMSInternalFault, InvalidAssertionFault, InvalidProxyFault,
			UserPolicyFault, PermissionDeniedFault {
		GUMSPortType port = null;
		try {
			port = this
					.getPort(new AnonymousSecureConversationWithEncryption());
		} catch (Exception e) {
			GUMSFault fault = new GUMSFault();
			fault.setFaultString(e.getMessage());
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSFault) helper.getFault();
			throw fault;
		}
		

		try {
			KeyPair pair = KeyUtil.generateRSAKeyPair512();
			IFSCreateProxy params = new IFSCreateProxy();
			params.setProxyLifetime(lifetime);
			gov.nih.nci.cagrid.gums.ifs.bean.PublicKey key = new gov.nih.nci.cagrid.gums.ifs.bean.PublicKey(KeyUtil.writePublicKeyToString(pair.getPublic()));
			params.setPublicKey(key);
			gov.nih.nci.cagrid.gums.bean.SAMLAssertion s = new gov.nih.nci.cagrid.gums.bean.SAMLAssertion(IOUtils.samlAssertionToString(saml));
			params.setSAMLAssertion(s);	
			gov.nih.nci.cagrid.gums.ifs.bean.X509Certificate list[] = port
					.createProxy(params).getCertificates();
			X509Certificate[] certs = new X509Certificate[list.length];
			for(int i=0; i<list.length; i++){
				certs[i] = CertUtil.loadCertificateFromString(list[i].getCertificateAsString());
			}
			return new GlobusCredential(pair.getPrivate(),certs);
		} catch (GUMSInternalFault gie) {
			throw gie;
		} catch (InvalidAssertionFault f) {
			throw f;
		} catch (InvalidProxyFault f) {
			throw f;
		} catch (UserPolicyFault f) {
			throw f;
		} catch (PermissionDeniedFault f) {
			throw f;
		}catch (Exception e) {
			FaultUtil.printFault(e);
			GUMSFault fault = new GUMSFault();
			fault.setFaultString(simplifyMessage(IOUtils.getExceptionMessage(e)));
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSFault) helper.getFault();
			throw fault;
		}
	}

	public static void main(String[] args) {
		try{
	    BasicAuthCredential auth = new BasicAuthCredential();
	    auth.setUserId("langella");
	    auth.setPassword("gomets123");
		IdPAuthenticationClient c1 = new IdPAuthenticationClient("http://localhost:8080/wsrf/services/cagrid/gums",auth);
		SAMLAssertion saml = c1.authenticate();
		IFSUserClient c2 = new IFSUserClient("http://localhost:8080/wsrf/services/cagrid/gums");
		ProxyLifetime lifetime = new ProxyLifetime();
		lifetime.setSeconds(500);
		GlobusCredential cred = c2.createProxy(saml,lifetime);
		FileOutputStream fos = new FileOutputStream(ConfigUtil
				.discoverProxyLocation());
		cred.save(fos);
		fos.close();
		}catch (Exception e) {
			//e.printStackTrace();
			FaultUtil.printFault(e);
		}
		

	}

}
