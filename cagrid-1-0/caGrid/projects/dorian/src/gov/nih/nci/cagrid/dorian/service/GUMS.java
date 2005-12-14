package gov.nih.nci.cagrid.gums.service;

import gov.nih.nci.cagrid.gums.bean.GUMSInternalFault;
import gov.nih.nci.cagrid.gums.bean.PermissionDeniedFault;
import gov.nih.nci.cagrid.gums.ca.CertificateAuthority;
import gov.nih.nci.cagrid.gums.ca.GUMSCertificateAuthority;
import gov.nih.nci.cagrid.gums.ca.GUMSCertificateAuthorityConf;
import gov.nih.nci.cagrid.gums.common.Database;
import gov.nih.nci.cagrid.gums.common.FaultHelper;
import gov.nih.nci.cagrid.gums.common.ca.CertUtil;
import gov.nih.nci.cagrid.gums.idp.IdPConfiguration;
import gov.nih.nci.cagrid.gums.idp.IdentityProvider;
import gov.nih.nci.cagrid.gums.idp.bean.Application;
import gov.nih.nci.cagrid.gums.idp.bean.BasicAuthCredential;
import gov.nih.nci.cagrid.gums.idp.bean.IdPUser;
import gov.nih.nci.cagrid.gums.idp.bean.IdPUserFilter;
import gov.nih.nci.cagrid.gums.idp.bean.InvalidUserPropertyFault;
import gov.nih.nci.cagrid.gums.idp.bean.NoSuchUserFault;
import gov.nih.nci.cagrid.gums.ifs.AutoApprovalAutoRenewalPolicy;
import gov.nih.nci.cagrid.gums.ifs.IFS;
import gov.nih.nci.cagrid.gums.ifs.IFSConfiguration;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUser;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUserFilter;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUserPolicy;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUserRole;
import gov.nih.nci.cagrid.gums.ifs.bean.IFSUserStatus;
import gov.nih.nci.cagrid.gums.ifs.bean.InvalidAssertionFault;
import gov.nih.nci.cagrid.gums.ifs.bean.InvalidProxyFault;
import gov.nih.nci.cagrid.gums.ifs.bean.InvalidTrustedIdPFault;
import gov.nih.nci.cagrid.gums.ifs.bean.InvalidUserFault;
import gov.nih.nci.cagrid.gums.ifs.bean.ProxyLifetime;
import gov.nih.nci.cagrid.gums.ifs.bean.SAMLAuthenticationMethod;
import gov.nih.nci.cagrid.gums.ifs.bean.TrustedIdP;
import gov.nih.nci.cagrid.gums.ifs.bean.TrustedIdPStatus;
import gov.nih.nci.cagrid.gums.ifs.bean.UserPolicyFault;

import java.security.PublicKey;
import java.security.cert.X509Certificate;

import org.opensaml.SAMLAssertion;
import org.projectmobius.common.MobiusConfigurator;
import org.projectmobius.common.MobiusResourceManager;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class GUMS extends MobiusResourceManager {

	private Database db;

	public static final String IDP_ADMIN_USER_ID = "gums";

	public static final String IDP_ADMIN_PASSWORD = "password";

	private String serviceId;

	private CertificateAuthority ca;

	private IdentityProvider identityProvider;

	private IFS ifs;

	private IFSConfiguration ifsConfiguration;


	public GUMS(String confFile, String serviceId) throws GUMSInternalFault {
		try {
			this.serviceId = serviceId;
			MobiusConfigurator.parseMobiusConfiguration(confFile, this);

			IdentityProvider.ADMIN_USER_ID = IDP_ADMIN_USER_ID;
			IdentityProvider.ADMIN_PASSWORD = IDP_ADMIN_PASSWORD;

			this.db = new Database(getGUMSConfiguration().getConnectionManager(), getGUMSConfiguration()
				.getGUMSInternalId());
			this.db.createDatabaseIfNeeded();
			GUMSCertificateAuthorityConf caconf = (GUMSCertificateAuthorityConf) getResource(GUMSCertificateAuthorityConf.RESOURCE);
			this.ca = new GUMSCertificateAuthority(db, caconf);

			IdPConfiguration idpConf = (IdPConfiguration) getResource(IdPConfiguration.RESOURCE);
			this.identityProvider = new IdentityProvider(idpConf, db, ca);

			TrustedIdP idp = new TrustedIdP();
			idp.setName(serviceId);
			SAMLAuthenticationMethod[] methods = new SAMLAuthenticationMethod[1];
			methods[0] = SAMLAuthenticationMethod.fromString("urn:oasis:names:tc:SAML:1.0:am:password");
			idp.setAuthenticationMethod(methods);
			idp.setUserPolicyClass(AutoApprovalAutoRenewalPolicy.class.getName());
			idp.setIdPCertificate(CertUtil.writeCertificateToString(this.identityProvider.getIdPCertificate()));
			idp.setStatus(TrustedIdPStatus.Active);
			IFSUser usr = new IFSUser();
			usr.setUID(IDP_ADMIN_USER_ID);
			// usr.setEmail(idpUser.getEmail());
			usr.setUserStatus(IFSUserStatus.Active);
			usr.setUserRole(IFSUserRole.Administrator);

			ifsConfiguration = (IFSConfiguration) getResource(IFSConfiguration.RESOURCE);
			ifsConfiguration.setInitalTrustedIdP(idp);
			ifsConfiguration.setInitialUser(usr);
			this.ifs = new IFS(ifsConfiguration, db, ca);

		} catch (Exception e) {
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("An unexpected error occurred in configuring the service.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		}
	}


	public GUMSConfiguration getGUMSConfiguration() {
		return (GUMSConfiguration) this.getResource(GUMSConfiguration.RESOURCE);
	}


	public Database getDatabase() {
		return this.db;
	}


	public X509Certificate getCACertificate() throws GUMSInternalFault {
		try {
			return this.ca.getCACertificate();
		} catch (Exception e) {
			GUMSInternalFault fault = new GUMSInternalFault();
			fault.setFaultString("An unexpected error occurred, in obtaining the CA certificate.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (GUMSInternalFault) helper.getFault();
			throw fault;
		}
	}


	public X509Certificate getIdPCertificate() throws GUMSInternalFault {
		return identityProvider.getIdPCertificate();
	}


	public IdPUser[] findIdPUsers(String gridIdentity, IdPUserFilter filter) throws GUMSInternalFault,
		PermissionDeniedFault {
		String uid = null;
		try {
			uid = ifs.getUserIdVerifyTrustedIdP(identityProvider.getIdPCertificate(), gridIdentity);
		} catch (Exception e) {
			PermissionDeniedFault fault = new PermissionDeniedFault();
			fault.setFaultString("Invalid IdP User.");
			throw fault;
		}
		return this.identityProvider.findUsers(uid, filter);
	}


	public void updateIdPUser(String gridIdentity, IdPUser u) throws GUMSInternalFault, PermissionDeniedFault,
		NoSuchUserFault, InvalidUserPropertyFault {
		String uid = null;
		try {
			uid = ifs.getUserIdVerifyTrustedIdP(identityProvider.getIdPCertificate(), gridIdentity);
		} catch (Exception e) {
			PermissionDeniedFault fault = new PermissionDeniedFault();
			fault.setFaultString("Invalid IdP User.");
			throw fault;
		}
		this.identityProvider.updateUser(uid, u);
	}


	public void removeIdPUser(String gridIdentity, String userId) throws GUMSInternalFault, PermissionDeniedFault {
		String uid = null;
		try {
			uid = ifs.getUserIdVerifyTrustedIdP(identityProvider.getIdPCertificate(), gridIdentity);
		} catch (Exception e) {
			PermissionDeniedFault fault = new PermissionDeniedFault();
			fault.setFaultString("Invalid IdP User.");
			throw fault;
		}
		this.identityProvider.removeUser(uid, userId);

	}


	public SAMLAssertion authenticate(BasicAuthCredential credential) throws GUMSInternalFault, PermissionDeniedFault {
		return this.identityProvider.authenticate(credential);
	}


	public String registerWithIdP(Application a) throws GUMSInternalFault, InvalidUserPropertyFault {
		return this.identityProvider.register(a);
	}


	/** *************** IFS FUNCTIONS ********************** */

	public IFSUserPolicy[] getIFSUserPolicies(String callerGridIdentity) throws GUMSInternalFault, InvalidUserFault,
		PermissionDeniedFault {
		return ifs.getUserPolicies(callerGridIdentity);
	}


	public X509Certificate[] createProxy(SAMLAssertion saml, PublicKey publicKey, ProxyLifetime lifetime)
		throws GUMSInternalFault, InvalidAssertionFault, InvalidProxyFault, UserPolicyFault, PermissionDeniedFault {
		return this.ifs.createProxy(saml, publicKey, lifetime);
	}


	public TrustedIdP[] getTrustedIdPs(String callerGridIdentity) throws GUMSInternalFault, InvalidUserFault,
		PermissionDeniedFault {
		return ifs.getTrustedIdPs(callerGridIdentity);
	}


	public TrustedIdP addTrustedIdP(String callerGridIdentity, TrustedIdP idp) throws GUMSInternalFault,
		InvalidTrustedIdPFault, InvalidUserFault, PermissionDeniedFault {
		return ifs.addTrustedIdP(callerGridIdentity, idp);
	}


	public void updatedTrustedIdP(String callerGridIdentity, TrustedIdP idp) throws GUMSInternalFault,
		InvalidTrustedIdPFault, InvalidUserFault, PermissionDeniedFault {
		ifs.updatedTrustedIdP(callerGridIdentity, idp);
	}


	public void removeTrustedIdP(String callerGridIdentity, TrustedIdP idp) throws GUMSInternalFault,
		InvalidTrustedIdPFault, InvalidUserFault, PermissionDeniedFault {
		ifs.removeTrustedIdP(callerGridIdentity, idp.getId());
	}


	public IFSUser[] findIFSUsers(String callerGridIdentity, IFSUserFilter filter) throws GUMSInternalFault,
		InvalidUserFault, PermissionDeniedFault {
		return ifs.findUsers(callerGridIdentity, filter);
	}


	public void updateIFSUser(String callerGridIdentity, IFSUser usr) throws GUMSInternalFault, InvalidUserFault,
		PermissionDeniedFault {
		ifs.updateUser(callerGridIdentity, usr);
	}


	public void removeIFSUser(String callerGridIdentity, IFSUser usr) throws GUMSInternalFault, InvalidUserFault,
		PermissionDeniedFault {
		ifs.removeUser(callerGridIdentity, usr);
	}


	public IFSUser renewIFSUserCredentials(String callerGridIdentity, IFSUser usr) throws GUMSInternalFault,
		InvalidUserFault, PermissionDeniedFault {
		return ifs.renewUserCredentials(callerGridIdentity, usr);
	}

}
