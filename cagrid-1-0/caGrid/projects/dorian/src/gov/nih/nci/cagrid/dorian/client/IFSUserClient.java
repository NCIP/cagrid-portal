package gov.nih.nci.cagrid.dorian.client;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.security.commstyle.CommunicationStyle;
import gov.nih.nci.cagrid.dorian.IFSUserAccess;
import gov.nih.nci.cagrid.dorian.bean.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.bean.PermissionDeniedFault;
import gov.nih.nci.cagrid.dorian.common.DorianFault;
import gov.nih.nci.cagrid.dorian.common.SAMLUtils;
import gov.nih.nci.cagrid.dorian.ifs.bean.InvalidAssertionFault;
import gov.nih.nci.cagrid.dorian.ifs.bean.InvalidProxyFault;
import gov.nih.nci.cagrid.dorian.ifs.bean.ProxyLifetime;
import gov.nih.nci.cagrid.dorian.ifs.bean.UserPolicyFault;
import gov.nih.nci.cagrid.dorian.wsrf.DorianPortType;
import gov.nih.nci.cagrid.dorian.wsrf.IFSCreateProxy;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.security.KeyPair;
import java.security.cert.X509Certificate;

import org.globus.gsi.GlobusCredential;


public class IFSUserClient extends DorianBaseClient implements IFSUserAccess {

	private CommunicationStyle style;
	public IFSUserClient(String serviceURI,CommunicationStyle style) {
		super(serviceURI);
		this.style = style;
	}

	public GlobusCredential createProxy(SAMLAssertion saml,
			ProxyLifetime lifetime) throws DorianFault,
			DorianInternalFault, InvalidAssertionFault, InvalidProxyFault,
			UserPolicyFault, PermissionDeniedFault {
		DorianPortType port = null;
		try {
			port = this
					.getPort(style);
		} catch (Exception e) {
			DorianFault fault = new DorianFault();
			fault.setFaultString(e.getMessage());
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianFault) helper.getFault();
			throw fault;
		}
		

		try {
			KeyPair pair = KeyUtil.generateRSAKeyPair512();
			IFSCreateProxy params = new IFSCreateProxy();
			params.setProxyLifetime(lifetime);
			gov.nih.nci.cagrid.dorian.ifs.bean.PublicKey key = new gov.nih.nci.cagrid.dorian.ifs.bean.PublicKey(KeyUtil.writePublicKey(pair.getPublic()));
			params.setPublicKey(key);
			gov.nih.nci.cagrid.dorian.bean.SAMLAssertion s = new gov.nih.nci.cagrid.dorian.bean.SAMLAssertion(SAMLUtils.samlAssertionToString(saml));
			params.setSAMLAssertion(s);	
			gov.nih.nci.cagrid.dorian.ifs.bean.X509Certificate list[] = port
					.createProxy(params).getCertificates();
			X509Certificate[] certs = new X509Certificate[list.length];
			for(int i=0; i<list.length; i++){
				certs[i] = CertUtil.loadCertificate(list[i].getCertificateAsString());
			}
			return new GlobusCredential(pair.getPrivate(),certs);
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
		}catch (Exception e) {
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
