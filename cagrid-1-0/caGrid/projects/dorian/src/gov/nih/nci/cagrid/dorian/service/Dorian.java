package gov.nih.nci.cagrid.dorian.service;

import gov.nih.nci.cagrid.common.FaultHelper;
import gov.nih.nci.cagrid.dorian.ca.CertificateAuthority;
import gov.nih.nci.cagrid.dorian.ca.DorianCertificateAuthority;
import gov.nih.nci.cagrid.dorian.ca.DorianCertificateAuthorityConf;
import gov.nih.nci.cagrid.dorian.common.Database;
import gov.nih.nci.cagrid.dorian.idp.bean.Application;
import gov.nih.nci.cagrid.dorian.idp.bean.BasicAuthCredential;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUser;
import gov.nih.nci.cagrid.dorian.idp.bean.IdPUserFilter;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUser;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserFilter;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserPolicy;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserRole;
import gov.nih.nci.cagrid.dorian.ifs.bean.IFSUserStatus;
import gov.nih.nci.cagrid.dorian.ifs.bean.ProxyLifetime;
import gov.nih.nci.cagrid.dorian.ifs.bean.SAMLAuthenticationMethod;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdP;
import gov.nih.nci.cagrid.dorian.ifs.bean.TrustedIdPStatus;
import gov.nih.nci.cagrid.dorian.service.idp.IdPConfiguration;
import gov.nih.nci.cagrid.dorian.service.idp.IdentityProvider;
import gov.nih.nci.cagrid.dorian.service.ifs.AutoApprovalAutoRenewalPolicy;
import gov.nih.nci.cagrid.dorian.service.ifs.IFS;
import gov.nih.nci.cagrid.dorian.service.ifs.IFSConfiguration;
import gov.nih.nci.cagrid.dorian.stubs.DorianInternalFault;
import gov.nih.nci.cagrid.dorian.stubs.InvalidAssertionFault;
import gov.nih.nci.cagrid.dorian.stubs.InvalidProxyFault;
import gov.nih.nci.cagrid.dorian.stubs.InvalidTrustedIdPFault;
import gov.nih.nci.cagrid.dorian.stubs.InvalidUserFault;
import gov.nih.nci.cagrid.dorian.stubs.InvalidUserPropertyFault;
import gov.nih.nci.cagrid.dorian.stubs.NoSuchUserFault;
import gov.nih.nci.cagrid.dorian.stubs.PermissionDeniedFault;
import gov.nih.nci.cagrid.dorian.stubs.UserPolicyFault;
import gov.nih.nci.cagrid.gridca.common.CertUtil;
import gov.nih.nci.cagrid.opensaml.SAMLAssertion;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import org.projectmobius.common.MobiusConfigurator;
import org.projectmobius.common.MobiusResourceManager;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class Dorian extends MobiusResourceManager {

	private Database db;

	public static final String IDP_ADMIN_USER_ID = "dorian";

	public static final String IDP_ADMIN_PASSWORD = "password";

	private CertificateAuthority ca;

	private IdentityProvider identityProvider;

	private IFS ifs;

	private IFSConfiguration ifsConfiguration;


	public Dorian(String confFile, String serviceId) throws DorianInternalFault {
		this(getFileInputStream(confFile), serviceId);
	}


	public Dorian(InputStream in, String serviceId) throws DorianInternalFault {
		try {
			MobiusConfigurator.parseMobiusConfiguration(in, this);

			IdentityProvider.ADMIN_USER_ID = IDP_ADMIN_USER_ID;
			IdentityProvider.ADMIN_PASSWORD = IDP_ADMIN_PASSWORD;

			this.db = new Database(getConfiguration().getConnectionManager(), getConfiguration().getDorianInternalId());
			this.db.createDatabaseIfNeeded();
			DorianCertificateAuthorityConf caconf = (DorianCertificateAuthorityConf) getResource(DorianCertificateAuthorityConf.RESOURCE);
			this.ca = new DorianCertificateAuthority(db, caconf);

			IdPConfiguration idpConf = (IdPConfiguration) getResource(IdPConfiguration.RESOURCE);
			this.identityProvider = new IdentityProvider(idpConf, db, ca);

			TrustedIdP idp = new TrustedIdP();
			idp.setName(serviceId);
			SAMLAuthenticationMethod[] methods = new SAMLAuthenticationMethod[1];
			methods[0] = SAMLAuthenticationMethod.fromString("urn:oasis:names:tc:SAML:1.0:am:password");
			idp.setAuthenticationMethod(methods);
			idp.setUserPolicyClass(AutoApprovalAutoRenewalPolicy.class.getName());
			idp.setIdPCertificate(CertUtil.writeCertificate(this.identityProvider.getIdPCertificate()));
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
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("An unexpected error occurred in configuring the service.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}
	}


	private static FileInputStream getFileInputStream(String file) throws DorianInternalFault {
		try {
			return new FileInputStream(new File(file));
		} catch (Exception e) {
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("An unexpected error occurred in configuring the service.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}
	}


	public DorianConfiguration getConfiguration() {
		return (DorianConfiguration) this.getResource(DorianConfiguration.RESOURCE);
	}


	public Database getDatabase() {
		return this.db;
	}


	public X509Certificate getCACertificate() throws DorianInternalFault {
		try {
			return this.ca.getCACertificate();
		} catch (Exception e) {
			DorianInternalFault fault = new DorianInternalFault();
			fault.setFaultString("An unexpected error occurred, in obtaining the CA certificate.");
			FaultHelper helper = new FaultHelper(fault);
			helper.addFaultCause(e);
			fault = (DorianInternalFault) helper.getFault();
			throw fault;
		}
	}


	public X509Certificate getIdPCertificate() throws DorianInternalFault {
		return identityProvider.getIdPCertificate();
	}


	public IdPUser[] findIdPUsers(String gridIdentity, IdPUserFilter filter) throws DorianInternalFault,
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


	public void updateIdPUser(String gridIdentity, IdPUser u) throws DorianInternalFault, PermissionDeniedFault,
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


	public void removeIdPUser(String gridIdentity, String userId) throws DorianInternalFault, PermissionDeniedFault {
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


	public SAMLAssertion authenticate(BasicAuthCredential credential) throws DorianInternalFault, PermissionDeniedFault {
		return this.identityProvider.authenticate(credential);
	}


	public String registerWithIdP(Application a) throws DorianInternalFault, InvalidUserPropertyFault {
		return this.identityProvider.register(a);
	}


	/** *************** IFS FUNCTIONS ********************** */

	public IFSUserPolicy[] getIFSUserPolicies(String callerGridIdentity) throws DorianInternalFault, PermissionDeniedFault {
		return ifs.getUserPolicies(callerGridIdentity);
	}


	public X509Certificate[] createProxy(SAMLAssertion saml, PublicKey publicKey, ProxyLifetime lifetime)
		throws DorianInternalFault, InvalidAssertionFault, InvalidProxyFault, UserPolicyFault, PermissionDeniedFault {
		return this.ifs.createProxy(saml, publicKey, lifetime);
	}


	public TrustedIdP[] getTrustedIdPs(String callerGridIdentity) throws DorianInternalFault, PermissionDeniedFault {
		return ifs.getTrustedIdPs(callerGridIdentity);
	}


	public TrustedIdP addTrustedIdP(String callerGridIdentity, TrustedIdP idp) throws DorianInternalFault,
		InvalidTrustedIdPFault, PermissionDeniedFault {
		return ifs.addTrustedIdP(callerGridIdentity, idp);
	}


	public void updateTrustedIdP(String callerGridIdentity, TrustedIdP idp) throws DorianInternalFault,
		InvalidTrustedIdPFault, PermissionDeniedFault {
		ifs.updateTrustedIdP(callerGridIdentity, idp);
	}


	public void removeTrustedIdP(String callerGridIdentity, TrustedIdP idp) throws DorianInternalFault,
		InvalidTrustedIdPFault, PermissionDeniedFault {
		ifs.removeTrustedIdP(callerGridIdentity, idp.getId());
	}


	public IFSUser[] findIFSUsers(String callerGridIdentity, IFSUserFilter filter) throws DorianInternalFault,
		PermissionDeniedFault {
		return ifs.findUsers(callerGridIdentity, filter);
	}


	public void updateIFSUser(String callerGridIdentity, IFSUser usr) throws DorianInternalFault, InvalidUserFault,
		PermissionDeniedFault {
		ifs.updateUser(callerGridIdentity, usr);
	}


	public void removeIFSUser(String callerGridIdentity, IFSUser usr) throws DorianInternalFault, InvalidUserFault,
		PermissionDeniedFault {
		ifs.removeUser(callerGridIdentity, usr);
	}


	public IFSUser renewIFSUserCredentials(String callerGridIdentity, IFSUser usr) throws DorianInternalFault,
		InvalidUserFault, PermissionDeniedFault {
		return ifs.renewUserCredentials(callerGridIdentity, usr);
	}

}
